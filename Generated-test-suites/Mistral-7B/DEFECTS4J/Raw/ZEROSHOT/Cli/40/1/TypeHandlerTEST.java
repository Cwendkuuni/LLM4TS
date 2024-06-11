package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String class.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "test";
        String expected = "test";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, String.class);
        }
        catch (ParseException e)
        {
            // should not throw an exception
        }

        assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Object class.
     */
    @Test
    public void testCreateValueObject()
    {
        Class<?> clazz = String.class;
        Object expected = "test";
        Object result = null;

        try
        {
            result = TypeHandler.createValue("test", clazz);
        }
        catch (ParseException e)
        {
            // should not throw an exception
        }

        assertNotNull(result);
        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Class class.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueClassThrowsParseException()
    {
        String str = "test";
        Class<?> clazz = ParseException.class;

        try
        {
            TypeHandler.createValue(str, clazz);
        }
        catch (Exception e)
        {
            if (e instanceof ParseException)
            {
                throw (ParseException) e;
            }
            throw new AssertionError("Unexpected exception: " + e.getClass().getName());
        }
    }

    /**
     * Test createValue method with Number class.
     */
    @Test
    public void testCreateValueNumber()
    {
        String str = "123";
        Number expected = 123;
        Number result = null;

        try
        {
            result = TypeHandler.createValue(str, Number.class);
        }
        catch (ParseException e)
        {
            // should not throw an exception
        }

        assertNotNull(result);
        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Date class.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDateThrowsUnsupportedOperationException()
    {
        String str = "2022-03-01";
        Date expected = null;
        Date result = null;

        try
        {
            result = TypeHandler.createValue(str, Date.class);
        }
        catch (Exception e)
        {
            if (e instanceof UnsupportedOperationException)
            {
                throw (UnsupportedOperationException) e;
            }
            throw new AssertionError("Unexpected exception: " + e.getClass().getName());
        }

        assertNull(result);
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Class class.
     */
    @Test
    public void testCreateValueClass()
    {
        String classname = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> result = null;

        try
        {
            result = TypeHandler.createValue(classname, Class.class);
        }
        catch (ParseException e)
        {
            // should not throw an exception
        }

        assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with File class.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "test.txt";
        File expected = new File(filePath);
        File result = null;

        try
        {
            result = (File) TypeHandler.createValue(filePath, File.class);
        }
        catch (ParseException e)
        {
            // should not throw an exception
        }

        assertNotNull(result);
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    /**
     * Test createValue method with FileInputStream class.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueFileInputStreamThrowsParseException()
    {
        String filePath = "test.txt";
        FileInputStream expected = null;

        try
        {
            expected = new FileInputStream(new File(filePath));
        }
        catch (FileNotFoundException e)
        {
            // should not occur in this test
        }

        FileInputStream result = null;

        try
        {
            result = (FileInputStream) TypeHandler.createValue(filePath, FileInputStream.class);
        }
        catch (Exception e)
        {
            if (e instanceof ParseException)
            {
                throw (ParseException) e;
            }
            throw new AssertionError("Unexpected exception: " + e.getClass().getName());
        }

        assertNull(result);
        assertEquals(expected, null);
    }

    /**
     * Test createValue method with URL class.
     */
    @Test
    public void testCreateValueURL()
    {
        String urlString = "http://example.com";
        URL expected = new URL(urlString);
        URL result = null;

        try
        {
            result = (URL) TypeHandler.createValue(urlString, URL.class);
        }
        catch (ParseException e)
        {
            // should not throw an exception
        }

        assertNotNull(result);
        assertEquals(expected.toString(), result.toString());
    }
}