import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueString() throws ParseException
    {
        String str = "TestString";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals("TestString", obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidString() throws ParseException
    {
        String str = "TestString123";
        Object obj = TypeHandler.createValue(str, int.class);
        fail("Should throw ParseException");
    }

    @Test
    public void testCreateValueObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createValue(classname, Date.class);
        assertInstanceOf(Date.class, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.List";
        Object obj = TypeHandler.createValue(classname, Date.class);
        fail("Should throw ParseException");
    }

    @Test
    public void testCreateValueNumber() throws ParseException
    {
        String str = "123";
        Number number = TypeHandler.createValue(str, int.class);
        assertEquals((int)123, number.intValue());

        str = "456.78";
        number = TypeHandler.createValue(str, Double.class);
        assertEquals(456.78, number.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidNumber() throws ParseException
    {
        String str = "test";
        Number number = TypeHandler.createValue(str, int.class);
        fail("Should throw ParseException");
    }

    @Test
    public void testCreateValueClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<?> clazz = TypeHandler.createValue(classname, Date.class);
        assertEquals(Date.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.List";
        Class<?> clazz = TypeHandler.createValue(classname, Date.class);
        fail("Should throw ParseException");
    }

    @Test
    public void testCreateValueDate() throws ParseException
    {
        String str = "2023-03-15";
        Date date = TypeHandler.createValue(str, Date.class);
        assertEquals("Wed Mar 15 00:00:00 CET 2023", date.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueInvalidDate() throws ParseException
    {
        String str = "TestDate";
        Date date = TypeHandler.createValue(str, Date.class);
        fail("Should throw UnsupportedOperationException");
    }

    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        String str = "http://example.com";
        URL url = TypeHandler.createValue(str, URL.class);
        assertEquals("http://example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidURL() throws ParseException, MalformedURLException
    {
        String str = "TestURL";
        URL url = TypeHandler.createValue(str, URL.class);
        fail("Should throw ParseException");
    }

    @Test
    public void testCreateValueFile() throws ParseException, FileNotFoundException
    {
        String str = "path/to/file.txt";
        File file = TypeHandler.createValue(str, File.class);
        assertTrue(file.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueInvalidFile() throws ParseException, FileNotFoundException
    {
        String str = "TestFile";
        File file = TypeHandler.createValue(str, File.class);
        fail("Should throw FileNotFoundException");
    }

    @Test
    public void testCreateValueExistingFile() throws ParseException, FileNotFoundException
    {
        String str = "path/to/existing/file.txt";
        File file = TypeHandler.createValue(str, File.class);
        assertTrue(file.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueInvalidExistingFile() throws ParseException, FileNotFoundException
    {
        String str = "TestFile";
        File file = TypeHandler.createValue(str, File.class);
        fail("Should throw FileNotFoundException");
    }

    @Test
    public void testCreateValueFiles() throws ParseException, IOException
    {
        String str = "path/to/files/*.txt";
        File[] files = TypeHandler.createFiles(str);
        assertTrue(files.length > 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueInvalidFiles() throws ParseException, IOException
    {
        String str = "TestFiles";
        File[] files = TypeHandler.createFiles(str);
        fail("Should throw UnsupportedOperationException");
    }
}