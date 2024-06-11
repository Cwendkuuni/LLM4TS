package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TypeHandlerTest class to test the TypeHandler methods.
 */
public class TypeHandlerTest
{
    /**
     * Initializes the test by setting up the TestUtils class.
     */
    @BeforeClass
    public static void setUpBeforeClass()
    {
        TestUtils.loadDefaultSystemProperties();
    }

    /**
     * Tests the createValue method with a String object.
     */
    @Test
    public void testStringValue()
    {
        String str = "Test String";
        String expected = "Test String";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, String.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    /**
     * Tests the createValue method with a Object object.
     */
    @Test
    public void testObjectValue()
    {
        String classname = "java.lang.String";
        String expected = "Test String";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, Object.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result.toString());
    }

    /**
     * Tests the createValue method with a Number object.
     */
    @Test
    public void testNumberValue()
    {
        String str = "123";
        Long expected = 123L;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, Long.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result);

        String strDouble = "123.45";
        Double expectedDouble = 123.45;
        Object resultDouble = null;

        try
        {
            resultDouble = TypeHandler.createValue(strDouble, Double.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expectedDouble, resultDouble);
    }

    /**
     * Tests the createValue method with a Date object.
     */
    @Test
    public void testDateValue()
    {
        String dateString = "2022-03-28";
        Date expected = new Date(2022, 2, 28);
        Object result = null;

        try
        {
            result = TypeHandler.createValue(dateString, Date.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    /**
     * Tests the createValue method with a Class object.
     */
    @Test
    public void testClassValue()
    {
        String classname = "java.lang.String";
        Class<?> expected = String.class;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, Class.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    /**
     * Tests the createValue method with a File object.
     */
    @Test
    public void testFileValue()
    {
        String filePath = "testFile.txt";
        File expected = new File(filePath);
        Object result = null;

        try
        {
            result = TypeHandler.createValue(filePath, File.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    /**
     * Tests the createValue method with an existing File object.
     */
    @Test
    public void testExistingFileValue()
    {
        String filePath = "testFile.txt";
        File expected = new File(filePath);
        FileInputStream fileInputStream = null;
        Object result = null;

        try
        {
            fileInputStream = new FileInputStream(filePath);
            result = TypeHandler.createValue(filePath, FileInputStream.class);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileInputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        assertEquals(expected, ((File) result).getAbsoluteFile());
    }

    /**
     * Tests the createValue method with a URL object.
     */
    @Test
    public void testURLValue()
    {
        String urlString = "http://example.com";
        URL expected = new URL(urlString);
        Object result = null;

        try
        {
            result = TypeHandler.createValue(urlString, URL.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result);
    }

    /**
     * Tests the createValue method with a class that does not exist.
     */
    @Test
    public void testCreateClassThatDoesNotExist()
    {
        String classname = "org.apache.commons.cli.TypeHandlerTest$DoesNotExist";
        Class<?> expected = null;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, Class.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result);

        assertThrows(ParseException.class, () -> TypeHandler.createValue(classname, Object.class));
    }

    /**
     * Tests the createValue method with an invalid class name.
     */
    @Test
    public void testCreateInvalidClassName()
    {
        String classname = "123";
        Class<?> expected = null;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, Class.class);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        assertEquals(expected, result);

        assertThrows(ParseException.class, () -> TypeHandler.createValue(classname, Object.class));
    }
}