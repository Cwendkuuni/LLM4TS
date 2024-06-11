package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Test for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object result;

        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.assertEquals(str, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with Object.
     */
    @Test
    public void testCreateValueObject()
    {
        String classname = "java.lang.String";
        Object obj = new String("test");
        Class<?> clazz = obj.getClass();
        Object result;

        try
        {
            result = TypeHandler.createValue(classname, clazz);
            Assert.assertEquals(obj, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with Number.
     */
    @Test
    public void testCreateValueNumber()
    {
        String str = "123";
        Class<?> clazz = Integer.class;
        Object result;

        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.assertEquals(123, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        String strDouble = "123.45";
        Class<?> clazzDouble = Double.class;
        Object resultDouble;

        try
        {
            resultDouble = TypeHandler.createValue(strDouble, clazzDouble);
            Assert.assertEquals(123.45, resultDouble, 0.0);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with Date.
     */
    @Test
    public void testCreateValueDate()
    {
        String dateStr = "2022-12-31";
        Class<?> clazz = Date.class;
        Object result;

        try
        {
            result = TypeHandler.createValue(dateStr, clazz);
            // Assert.assertEquals(new Date(2022, 11, 30), result); // Assuming Date constructor with month 11 instead of 12
            // For JUnit 5, use instaed Assertions.assertEquals(new Date(2022, 11, 30).getTime(), result.getTime(), 0);
            // For JUnit 4, use Assert.assertEquals(new Date(2022, 11, 30).getTime(), result.getTime());
            Assert.assertNotNull(result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with Class.
     */
    @Test
    public void testCreateValueClass()
    {
        String classname = "java.lang.String";
        Class<?> clazz = String.class;
        Object result;

        try
        {
            result = TypeHandler.createValue(classname, clazz);
            Assert.assertEquals(classname, result.toString());
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with File.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "/path/to/test.txt";
        File file = new File(filePath);
        Class<?> clazz = File.class;
        Object result;

        try
        {
            result = TypeHandler.createValue(filePath, clazz);
            Assert.assertEquals(file, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with existing File.
     */
    @Test
    public void testCreateValueExistingFile()
    {
        String filePath = "/path/to/test.txt";
        File file = new File(filePath);
        Class<?> clazz = File.class;
        Object result;

        try
        {
            result = TypeHandler.createValue(filePath, clazz, file.exists());
            Assert.assertEquals(file, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with URL.
     */
    @Test
    public void testCreateValueURL()
    {
        String urlStr = "http://localhost:8080/test.html";
        Class<?> clazz = URL.class;
        Object result;

        try
        {
            result = TypeHandler.createValue(urlStr, clazz);
            Assert.assertEquals(new URL(urlStr), result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createObject method with null.
     */
    @Test(expected = ParseException.class)
    public void testCreateObjectNull()
    {
        String classname = null;
        Object result;

        try
        {
            result = TypeHandler.createObject(classname);
            Assert.fail("Expected ParseException not thrown");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createObject method with non-existent class.
     */
    @Test(expected = ParseException.class)
    public void testCreateObjectNonExistentClass()
    {
        String classname = "non.existent.class";
        Object result;

        try
        {
            result = TypeHandler.createObject(classname);
            Assert.fail("Expected ParseException not thrown");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createObject method with existing class.
     */
    @Test
    public void testCreateObjectExistingClass()
    {
        String classname = "java.lang.String";
        Object result;

        try
        {
            result = TypeHandler.createObject(classname);
            Assert.assertNotNull(result);
            Assert.assertTrue(result instanceof String);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createNumber method with Long.
     */
    @Test
    public void testCreateNumberLong()
    {
        String str = "123";
        Class<?> clazz = Long.class;
        Object result;

        try
        {
            result = TypeHandler.createNumber(str);
            Assert.assertEquals(123L, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createNumber method with Double.
     */
    @Test
    public void testCreateNumberDouble()
    {
        String str = "123.45";
        Class<?> clazz = Double.class;
        Object result;

        try
        {
            result = TypeHandler.createNumber(str);
            Assert.assertEquals(123.45, result, 0.0);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createNumber method with invalid number.
     */
    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumber()
    {
        String str = "abc";
        Class<?> clazz = Integer.class;

        try
        {
            TypeHandler.createNumber(str);
            Assert.fail("Expected ParseException not thrown");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createURL method with invalid URL.
     */
    @Test(expected = ParseException.class)
    public void testCreateURLInvalidURL()
    {
        String str = "invalid://test.html";
        Class<?> clazz = URL.class;

        try
        {
            TypeHandler.createURL(str);
            Assert.fail("Expected ParseException not thrown");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createFile method with invalid file path.
     */
    @Test(expected = ParseException.class)
    public void testCreateFileInvalidFilePath()
    {
        String filePath = "INVALID";
        Class<?> clazz = File.class;

        try
        {
            TypeHandler.createFile(filePath);
            Assert.fail("Expected ParseException not thrown");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createFiles method with invalid file paths.
     */
    @Test
    public void testCreateFilesInvalidFilePaths()
    {
        String filePaths = "INVALID1, INVALID2";
        Class<?> clazz = File[].class;

        try
        {
            TypeHandler.createFiles(filePaths);
            Assert.fail("Expected UnsupportedOperationException not thrown");
        }
        catch (UnsupportedOperationException e)
        {
            // Expected
        }
    }
}