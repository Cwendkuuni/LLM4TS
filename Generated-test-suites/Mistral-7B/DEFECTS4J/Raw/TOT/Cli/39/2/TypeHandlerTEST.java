package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueWithString()
    {
        String str = "hello";
        Class<?> clazz = String.class;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, clazz);
        }
        catch (ParseException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.assertEquals("hello", result);
    }

    @Test
    public void testCreateValueWithStringAndWrongClass()
    {
        String str = "hello";
        Class<?> clazz = Integer.class;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, clazz);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Unexpected type: java.lang.String for class: java.lang.Integer", e.getMessage());
        }

        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithObject()
    {
        String classname = "java.util.Date";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, Object.class);
        }
        catch (ParseException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValueWithInvalidObjectClass()
    {
        String classname = "java.util.Date";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, int.class);
            Assert.fail("Exception should be thrown");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Unexpected type: java.lang.Class<java.util.Date> for class: int", e.getMessage());
        }

        Assert.assertNull(result);
    }

    @Test
    public void testCreateNumberWithValidNumber()
    {
        String str = "12345";
        Number result = null;

        try
        {
            result = TypeHandler.createNumber(str);
        }
        catch (ParseException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.assertEquals(12345L, result.longValue());
    }

    @Test
    public void testCreateNumberWithInvalidNumber()
    {
        String str = "hello";
        Number result = null;

        try
        {
            result = TypeHandler.createNumber(str);
            Assert.fail("Exception should be thrown");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Invalid number: hello", e.getMessage());
        }

        Assert.assertNull(result);
    }

    @Test
    public void testCreateClassWithValidClassName()
    {
        String classname = "java.util.Date";
        Class<?> result = null;

        try
        {
            result = TypeHandler.createClass(classname);
        }
        catch (ParseException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Date.class, result);
    }

    @Test
    public void testCreateClassWithInvalidClassName()
    {
        String classname = "java.util.InvalidClass";
        Class<?> result = null;

        try
        {
            result = TypeHandler.createClass(classname);
            Assert.fail("Exception should be thrown");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Unable to find the class: java.util.InvalidClass", e.getMessage());
        }

        Assert.assertNull(result);
    }

    @Test
    public void testCreateDateWithValidDateString()
    {
        String str = "2022-12-31";
        Date result = null;

        try
        {
            result = TypeHandler.createDate(str);
        }
        catch (UnsupportedOperationException e)
        {
            Assert.fail("Method should be implemented: " + e.getMessage());
        }

        Assert.assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateWithInvalidDateString()
    {
        String str = "hello";
        Date result = null;

        try
        {
            result = TypeHandler.createDate(str);
        }
        catch (ParseException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.fail("Method should throw exception");
    }

    @Test
    public void testCreateURLWithValidURLString()
    {
        String str = "https://www.google.com";
        URL result = null;

        try
        {
            result = TypeHandler.createURL(str);
        }
        catch (ParseException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURLString()
    {
        String str = "hello://www.google.com";
        URL result = null;

        try
        {
            result = TypeHandler.createURL(str);
        }
        catch (UnsupportedOperationException e)
        {
            Assert.fail("Method should not be called: " + e.getMessage());
        }

        Assert.fail("Method should throw exception");
    }

    @Test
    public void testCreateFileWithValidFileString()
    {
        String str = "path/to/file.txt";
        File result = null;

        try
        {
            result = TypeHandler.createFile(str);
        }
        catch (FileNotFoundException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileWithInvalidFileString()
    {
        String str = "path/to/nonexistentfile.txt";
        File result = null;

        try
        {
            result = TypeHandler.createFile(str);
        }
        catch (ParseException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.assertNull(result);
    }

    @Test
    public void testCreateFileWithValidFileInputStream() throws IOException
    {
        String str = "path/to/file.txt";
        File file = new File(str);
        FileInputStream inputStream = new FileInputStream(file);
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, FileInputStream.class);
        }
        catch (ParseException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.assertEquals(inputStream, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateFileWithInvalidFileString()
    {
        String str = "path/to/nonexistentfile.txt";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, File.class);
        }
        catch (FileNotFoundException e)
        {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }

        Assert.fail("Exception should be thrown");
    }

    @Test
    public void testCreateFilesWithValidFileStrings()
    {
        String str = "path/to/file1.txt,path/to/file2.txt";
        File[] result = null;

        try
        {
            result = TypeHandler.createFiles(str);
        }
        catch (UnsupportedOperationException e)
        {
            Assert.fail("Method should be implemented: " + e.getMessage());
        }

        Assert.assertNotNull(result);
        Assert.assertArrayNotNull(result);
        Assert.assertEquals(2, result.length);

        for (File file : result)
        {
            Assert.assertNotNull(file);
            Assert.assertTrue(file.exists());
        }
    }

    @Test
    public void testCreateFilesWithInvalidFileStrings()
    {
        String str = "path/to/nonexistentfile1.txt,path/to/nonexistentfile2.txt";
        File[] result = null;

        try
        {
            result = TypeHandler.createFiles(str);
        }
        catch (UnsupportedOperationException e)
        {
            Assert.fail("Method should be implemented: " + e.getMessage());
        }

        Assert.assertNull(result);
    }
}