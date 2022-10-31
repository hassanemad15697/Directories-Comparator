package Main;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;

public class Compare {
    File dir1 = new File("C:\\Users\\hassan.askar\\Desktop\\EDAPI-2063\\6.2022.09\\edapi");
    File dir2 = new File("C:\\Users\\hassan.askar\\Desktop\\EDAPI-2063\\devlop\\edapi");
    public static void main(String ...args)
    {
        Compare compare = new Compare();
        try
        {
            compare.getDiff(compare.dir1,compare.dir2);
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public void getDiff(File dirA, File dirB) throws IOException
    {
        File[] fileList1 = dirA.listFiles();
        File[] fileList2 = dirB.listFiles();
        Arrays.sort(fileList1);
        Arrays.sort(fileList2);
        HashMap<String, File> map1;
        if(fileList1.length < fileList2.length)
        {
            map1 = new HashMap<String, File>();
            for(int i=0;i<fileList1.length;i++)
            {
                map1.put(fileList1[i].getName(),fileList1[i]);
            }

            compareNow(fileList2, map1);
        }
        else
        {
            map1 = new HashMap<String, File>();
            for(int i=0;i<fileList2.length;i++)
            {
                map1.put(fileList2[i].getName(),fileList2[i]);
            }
            compareNow(fileList1, map1);
        }
    }

    public void compareNow(File[] fileArr, HashMap<String, File> map) throws IOException
    {
        for(int i=0;i<fileArr.length;i++)
        {
            String fName = fileArr[i].getName();
            File fComp = map.get(fName);
            map.remove(fName);
            if(fComp!=null)
            {
                if(fComp.isDirectory())
                {
                    getDiff(fileArr[i], fComp);
                }
                else
                {
                    String cSum1 = checksum(fileArr[i]);
                    String cSum2 = checksum(fComp);
                    if(!cSum1.equals(cSum2))
                    {
                        if(fComp.toString().contains(".java")) {
                            System.out.println("________________________________________________________");
                            System.out.println(fileArr[i].getName()+"\t\t"+ "different");
                            getDiffContent(fileArr[i].toString(), fComp.toString());
                            System.out.println("________________________________________________________");
                        }else{
                            System.out.println(fileArr[i].getName()+"\t\t"+ "different");
                        }
                    }
                    else
                    {
                        System.out.println(fileArr[i].getName()+"\t\t"+"identical");
                    }
                }
            }
            else
            {
                if(fileArr[i].isDirectory())
                {
                    traverseDirectory(fileArr[i]);
                }
                else
                {
                    System.out.println(fileArr[i].getName()+"\t\t"+"only in "+fileArr[i].getParent());
                }
            }
        }
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        while(it.hasNext())
        {
            String n = it.next();
            File fileFrmMap = map.get(n);
            map.remove(n);
            if(fileFrmMap.isDirectory())
            {
                traverseDirectory(fileFrmMap);
            }
            else
            {
                System.out.println(fileFrmMap.getName() +"\t\t"+"only in "+ fileFrmMap.getParent());
            }
        }
    }

    public void traverseDirectory(File dir)
    {
        File[] list = dir.listFiles();
        for(int k=0;k<list.length;k++)
        {
            if(list[k].isDirectory())
            {
                traverseDirectory(list[k]);
            }
            else
            {
                System.out.println(list[k].getName() +"\t\t"+"only in "+ list[k].getParent());
            }
        }
    }

    public String checksum(File file)
    {
        try
        {
            InputStream fin = new FileInputStream(file);
            java.security.MessageDigest md5er = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int read;
            do
            {
                read = fin.read(buffer);
                if (read > 0)
                    md5er.update(buffer, 0, read);
            } while (read != -1);
            fin.close();
            byte[] digest = md5er.digest();
            if (digest == null)
                return null;
            String strDigest = "0x";
            for (int i = 0; i < digest.length; i++)
            {
                strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
            }
            return strDigest;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public void getDiffContent(String filename , String filename2) throws IOException {
        HashSet <String> al = new HashSet<String>();
        HashSet <String> al1 = new HashSet<String>();
        HashSet <String> diff1 = new HashSet<String>();
        HashSet <String> diff2 = new HashSet<String>();
        String str = null;
        String str2 = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            while ((str = in.readLine()) != null) {
                al.add(str);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename2));
            while ((str2 = in.readLine()) != null) {
                al1.add(str2);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String str3 : al) {
            if (!al1.contains(str3)) {
                diff1.add(str3);
            }
        }
        for (String str5 : al1) {
            if (!al.contains(str5)) {
                diff2.add(str5);
            }
        }
        System.out.println("**************************Removed Lines**************************");
        for (String str4 : diff1) {
            System.out.println(str4);
        }
        System.out.println("**************************Added Lines**************************");
        for (String str4 : diff2) {
            System.out.println(str4);
        }
    }
}
