import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_StringObject() throws ParseException
    {
        String str = "test";
        Object obj = new Object();
        Object value = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(str, value);
    }

    @Test
    public void testCreateValue_StringString() throws ParseException
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object value = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, value);
    }

    @Test
    public void testCreateValue_StringClassObject_Number() throws ParseException
    {
        String str = "123";
        Class<?> clazz = Long.class;
        Object value = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123L, value);
    }

    @Test
    public void testCreateValue_StringClassObject_Date() throws ParseException, Exception
    {
        String str = "2023-03-25";
        Class<?> clazz = Date.class;
        Object value = TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(value);
        Date date = (Date) value;
        Assert.assertEquals("Sat Mar 25 00:00:00 EDT 2023", date.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringClassObject_UnknownType() throws ParseException
    {
        String str = "test";
        Class<?> clazz = int.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String classname = "java.util.Date";
        Object value = TypeHandler.createObject(classname);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof Date);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFound() throws ParseException
    {
        String classname = "UnknownClass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_CannotCreateInstance() throws ParseException, InstantiationException
    {
        String classname = "java.io.FileOutputStream";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        Assert.assertEquals(123L, number.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_NotANumber() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String urlString = "http://www.example.com";
        URL url = TypeHandler.createURL(urlString);
        Assert.assertNotNull(url);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL() throws ParseException, MalformedURLException
    {
        String urlString = "www.example.com";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile()
    {
        String filePath = "test.txt";
        File file = TypeHandler.createFile(filePath);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStream_FileNotFound() throws FileNotFoundException
    {
        String filePath = "nonexistentfile.txt";
        File file = new File(filePath);
        FileInputStream inputStream = TypeHandler.createFileInputStream(filePath);
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_InvalidFile() throws ParseException
    {
        String filePath = "nonexistent:/file.txt";
        TypeHandler.createFile(filePath);
    }

    @Test(expected = ParseException.class)
    public void testCreateFiles_InvalidFilePaths() throws ParseException
    {
        String paths = "nonexistentfile1.txt:nonexistentfile2.txt";
        TypeHandler.createFiles(paths);
    }

    @Test
    public void testCreateFiles_ValidFilePaths() throws IOException
    {
        String paths = "test1.txt:test2.txt";
        File[] files = TypeHandler.createFiles(paths);
        Assert.assertNotNull(files);
        Assert.assertEquals(2, files.length);
        for (File file : files)
        {
            Assert.assertTrue(file.exists());
        }
    }
}