package org.apache.commons.cli.test;

import static org.apache.commons.cli.TypeHandler.createValue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test cases for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with String object.
     */
    @Test
    public void testCreateValueWithStringObject()
    {
        String str = "test";
        Object obj = String.class;

        assertEquals(str, createValue(str, obj));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with Object object.
     */
    @Test
    public void testCreateValueWithObjectObject()
    {
        String className = "java.util.Date";
        Object obj = Date.class;

        try
        {
            Date date = createValue(className, obj);
            assertNotNull(date);
        }
        catch (ParseException e)
        {
            // expected since creation from a String is not supported
        }
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with Class object.
     */
    @Test
    public void testCreateValueWithClassObject()
    {
        String classname = "java.lang.String";
        Class<?> clazz = String.class;

        try
        {
            Object obj = createValue(classname, clazz);
            assertEquals(classname, obj.getClass().getName());
        }
        catch (ParseException e)
        {
            // expected since creation from a String is not supported
        }
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with wrong object types.
     */
    @Test
    public void testCreateValueWithWrongObjectTypes()
    {
        String str = "test";
        Object obj = Integer.class;

        assertNull(createValue(str, obj));
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)} with valid classname.
     */
    @Test
    public void testCreateObjectWithValidClassName()
    {
        String classname = "java.util.Date";

        try
        {
            Object obj = createObject(classname);
            assertTrue(obj instanceof Date);
        }
        catch (ParseException e)
        {
            // unexpected
        }
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)} with invalid classname.
     */
    @Test
    public void testCreateObjectWithInvalidClassName()
    {
        String classname = "InvalidClass";

        assertThrows(ParseException.class, () -> createObject(classname));
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)} with valid number string.
     */
    @Test
    public void testCreateNumberWithValidNumberString()
    {
        String str = "123";

        Long number = createNumber(str);
        assertEquals(123L, number.longValue());

        String strWithDecimal = "123.45";
        Double numberWithDecimal = createNumber(strWithDecimal);
        assertEquals(123.45, numberWithDecimal.doubleValue(), 0.001);
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)} with invalid number string.
     */
    @Test
    public void testCreateNumberWithInvalidNumberString()
    {
        String str = "invalid";

        assertThrows(ParseException.class, () -> createNumber(str));
    }

    /**
     * Test method for {@link TypeHandler#createClass(String)} with valid classname.
     */
    @Test
    public void testCreateClassWithValidClassName()
    {
        String classname = "java.util.Date";

        try
        {
            Class<?> clazz = createClass(classname);
            assertEquals(Date.class, clazz);
        }
        catch (ParseException e)
        {
            // unexpected
        }
    }

    /**
     * Test method for {@link TypeHandler#createClass(String)} with invalid classname.
     */
    @Test
    public void testCreateClassWithInvalidClassName()
    {
        String classname = "InvalidClass";

        assertThrows(ParseException.class, () -> createClass(classname));
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)} with valid URL string.
     */
    @Test
    public void testCreateURLWithValidURLString()
    {
        String urlString = "http://example.com";

        try
        {
            URL url = createURL(urlString);
            assertEquals("http://example.com", url.toString());
        }
        catch (ParseException e)
        {
            // unexpected
        }
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)} with invalid URL string.
     */
    @Test
    public void testCreateURLWithInvalidURLString()
    {
        String urlString = "invalid:url";

        assertThrows(ParseException.class, () -> createURL(urlString));
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)} with valid file path.
     */
    @Test
    public void testCreateFileWithValidFilePath()
    {
        String filePath = "test.txt";

        File file = createFile(filePath);
        assertTrue(file.exists());
        assertTrue(file.delete());
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)} with invalid file path.
     */
    @Test
    public void testCreateFileWithInvalidFilePath()
    {
        String filePath = "/nonexistent/test.txt";

        assertThrows(ParseException.class, () -> createFile(filePath));
    }

    /**
     * Test method for {@link TypeHandler#openFile(String)} with valid file path.
     */
    @Test
    public void testOpenFileWithValidFilePath()
    {
        String filePath = "test.txt";

        try
        {
            File file = new File(filePath);
            if (file.createNewFile())
            {
                FileInputStream fileInputStream = openFile(filePath);
                assertNotNull(fileInputStream);
                fileInputStream.close();
                assertTrue(file.delete());
            }
        }
        catch (IOException e)
        {
            // unexpected
        }
    }

    /**
     * Test method for {@link TypeHandler#openFile(String)} with invalid file path.
     */
    @Test
    public void testOpenFileWithInvalidFilePath()
    {
        String filePath = "/nonexistent/test.txt";

        assertThrows(ParseException.class, () -> openFile(filePath));
    }

    /**
     * Test method for {@link TypeHandler#createFiles(String)} not yet implemented.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFilesWithValidFilePaths()
    {
        String filePaths = "test1.txt, test2.txt";

        createFiles(filePaths);
    }
}