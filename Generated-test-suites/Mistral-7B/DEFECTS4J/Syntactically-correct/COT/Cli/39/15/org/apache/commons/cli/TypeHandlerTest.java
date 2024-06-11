import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

/**
 * TypeHandlerTest.java
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String type
     */
    @Test
    public void testCreateValueString()
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    /**
     * Test createValue method with Object type
     */
    @Test
    public void testCreateValueObject()
    {
        try
        {
            Object obj = TypeHandler.createValue("java.lang.String", Object.class);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test createValue method with Number type
     */
    @Test
    public void testCreateValueNumber()
    {
        String str = "123";
        Number number = TypeHandler.createValue(str, Number.class);
        assertEquals(123, number.intValue());

        str = "456.78";
        number = TypeHandler.createValue(str, Double.class);
        assertEquals(456.78, number.doubleValue(), 0.0);
    }

    /**
     * Test createValue method with Date type
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate()
    {
        String str = "2022-12-31";
        Date date = TypeHandler.createValue(str, Date.class);
        // This line will never be executed because of the expected UnsupportedOperationException
        assertEquals(new Date(2022, 11, 30), date); // Note: Month index is 0-based
    }

    /**
     * Test createValue method with Class type
     */
    @Test
    public void testCreateValueClass()
    {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createValue(classname, Class.class);
        assertEquals(Class.forName(classname), clazz);
    }

    /**
     * Test createValue method with File type
     */
    @Test
    public void testCreateValueFile()
    {
        String path = "/path/to/file.txt";
        File file = TypeHandler.createValue(path, File.class);
        assertTrue(file.exists());
    }

    /**
     * Test createValue method with existing File type
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFile()
    {
        String path = "/non_existent_file.txt";
        File file = TypeHandler.createValue(path, File.class);
        // This line will never be executed because of the expected FileNotFoundException
        assertTrue(file.exists());
    }

    /**
     * Test createValue method with Files type
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles()
    {
        String paths = "/path/to/file1.txt,/path/to/file2.txt";
        File[] files = TypeHandler.createValue(paths, File[].class);
        // This line will never be executed because of the expected UnsupportedOperationException
        assertEquals(2, files.length);
    }

    /**
     * Test createValue method with URL type
     */
    @Test
    public void testCreateValueURL()
    {
        String urlString = "http://www.example.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertEquals("http://www.example.com", url.toExternalForm());
    }
}