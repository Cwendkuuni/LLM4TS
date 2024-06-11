import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

/**
 * TypeHandlerTest
 */
public class TypeHandlerTest
{

    @Test
    public void testCreateValueWithStringObject() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueWithStringClass() throws ParseException
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueWithInvalidPatternOptionBuilder() throws ParseException
    {
        String str = "test";
        Class<?> clazz = Integer.class;
        try
        {
            Object obj = TypeHandler.createValue(str, clazz);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateObject() throws ParseException
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createObject(classname);
        assertTrue(obj instanceof String);
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123, number.intValue());

        str = "123.45";
        number = TypeHandler.createNumber(str);
        assertEquals(123.45, number.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalid() throws ParseException
    {
        String str = "invalid";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(String.class, clazz);
    }

    @Test (expected = ParseException.class)
    public void testCreateClassInvalid() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() throws ParseException
    {
        String str = "2022-03-01";
        Date date = TypeHandler.createDate(str);
        assertEquals(new Date(2022, 2, 1), date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateUnsupported() throws ParseException
    {
        String str = "2022-03-01 12:30:00";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals(new URL("http://www.example.com"), url);
    }

    @Test (expected = ParseException.class)
    public void testCreateURLInvalid() throws ParseException, MalformedURLException
    {
        String str = "invalid://example.com";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() throws ParseException, FileNotFoundException, IOException
    {
        String str = "path/to/file.txt";
        File file = TypeHandler.createFile(str);
        assertTrue(new File(str).exists());
        assertTrue(file.exists());
    }

    @Test (expected = ParseException.class)
    public void testCreateFileInvalidPath() throws ParseException, FileNotFoundException, IOException
    {
        String str = "invalid_path/to/file.txt";
        TypeHandler.createFile(str);
    }

    @Test
    public void testCreateFileInputStream() throws ParseException, FileNotFoundException, IOException
    {
        String str = "path/to/file.txt";
        File file = new File(str);
        FileInputStream fileInputStream = TypeHandler.createFileInputStream(str);
        assertTrue(file.exists());
        assertNotNull(fileInputStream);
    }

    @Test (expected = ParseException.class)
    public void testCreateFileInputStreamInvalid() throws ParseException, FileNotFoundException, IOException
    {
        String str = "invalid_path/to/file.txt";
        TypeHandler.createFileInputStream(str);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() throws ParseException
    {
        String str = "path/to/file1.txt,path/to/file2.txt";
        File[] files = TypeHandler.createFiles(str);
        fail("Not yet implemented");
    }
}