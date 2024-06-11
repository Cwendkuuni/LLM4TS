package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

/**
 * Test class for TypeHandler methods.
 */
public class TypeHandlerTest
{
    /**
     * Test creating value of type String.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "Hello World";
        String expected = "Hello World";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, String.class);
        }
        catch (ParseException e)
        {
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of unknown type.
     */
    @Test
    public void testCreateValueUnknownType()
    {
        String str = "Hello World";
        assertNull(TypeHandler.createValue(str, Integer.class));
    }

    /**
     * Test creating value of type Object.
     */
    @Test
    public void testCreateValueObject()
    {
        Class<?> clazz = String.class;
        String classname = "java.lang.String";
        Object expected = "Hello World";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, clazz);
        }
        catch (ParseException e)
        {
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of type Object with wrong class name.
     */
    @Test
    public void testCreateValueObjectWrongClassName()
    {
        Class<?> clazz = Integer.class;
        String classname = "java.lang.String";
        assertNull(TypeHandler.createValue(classname, clazz));
    }

    /**
     * Test creating value of type Number.
     */
    @Test
    public void testCreateValueNumberLong()
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
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of type Number.
     */
    @Test
    public void testCreateValueNumberDouble()
    {
        String str = "123.45";
        Double expected = 123.45;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, Double.class);
        }
        catch (ParseException e)
        {
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of type Date.
     */
    @Test
    public void testCreateValueDate()
    {
        String str = "2021-12-31";
        Date expected = new Date(Long.parseLong("20211231111111111")); // arbitrary date value
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, Date.class);
        }
        catch (ParseException e)
        {
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of type Date with invalid format.
     */
    @Test
    public void testCreateValueDateInvalidFormat()
    {
        String str = "2021-12-31-01";
        assertNull(TypeHandler.createValue(str, Date.class));
    }

    /**
     * Test creating value of type Class.
     */
    @Test
    public void testCreateValueClass()
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
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of type File.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "/test/TestFile.txt";
        File expected = new File(filePath);
        File result = null;

        try
        {
            result = (File) TypeHandler.createValue(filePath, File.class);
        }
        catch (ParseException e)
        {
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of type FileInputStream.
     */
    @Test
    public void testCreateValueFileInputStream()
    {
        String filePath = "/test/TestFile.txt";
        File file = new File(filePath);
        FileInputStream expected = null;
        FileInputStream result = null;

        try
        {
            expected = new FileInputStream(file);
            result = (FileInputStream) TypeHandler.createValue(filePath, FileInputStream.class);
        }
        catch (IOException | ParseException e)
        {
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of type URL.
     */
    @Test
    public void testCreateValueURL()
    {
        String urlString = "http://test.com";
        URL expected = new URL(urlString);
        URL result = null;

        try
        {
            result = (URL) TypeHandler.createValue(urlString, URL.class);
        }
        catch (ParseException e)
        {
            // Should not happen
            assertNull(e);
        }

        assertEquals(expected, result);
    }

    /**
     * Test creating value of type URL with invalid format.
     */
    @Test
    public void testCreateValueURLInvalidFormat()
    {
        String urlString = "http://test.com:8080";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(urlString, URL.class));
    }

    /**
     * Test creating value of type Class with wrong class name.
     */
    @Test
    public void testCreateValueClassWrongClassName()
    {
        String classname = "java.util.Date";
        assertNull(TypeHandler.createValue(classname, Class.class));
    }
}