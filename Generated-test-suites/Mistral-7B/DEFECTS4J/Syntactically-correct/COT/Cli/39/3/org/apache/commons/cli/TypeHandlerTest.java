package org.apache.commons.cli.test;

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

import java.util.Date;

/**
 * Test cases for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue(String, Object) method with String type.
     */
    @Test
    public void testCreateValueStringObject()
    {
        String str = "TestString";
        Object obj = "TestString";

        try
        {
            Object result = TypeHandler.createValue(str, obj.getClass());
            assertEquals(obj, result);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createValue(String, Object) method with Object type.
     */
    @Test
    public void testCreateValueStringObjectObject()
    {
        String str = "TestString";
        Object obj = new Object() {};

        try
        {
            Object result = TypeHandler.createValue(str, obj.getClass());
            assertEquals(str, result);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createValue(String, Object) method with Class type.
     */
    @Test
    public void testCreateValueStringClass()
    {
        String str = "TestString";
        Class<?> clazz = String.class;

        try
        {
            Object result = TypeHandler.createValue(str, clazz);
            assertEquals(str, result);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createValue(String, Object) method with non-supported types.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueStringObjectUnsupportedType()
    {
        String str = "TestString";
        Object obj = new Date();

        TypeHandler.createValue(str, obj.getClass());
    }

    /**
     * Test createObject(String) method.
     */
    @Test
    public void testCreateObject()
    {
        String classname = "java.lang.String";
        Class<?> clazz = String.class;

        try
        {
            Object result = TypeHandler.createObject(classname);
            assertEquals(clazz, result.getClass());
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createObject(String) method with non-existent class.
     */
    @Test(expected = ParseException.class)
    public void testCreateObjectNonExistentClass()
    {
        String classname = "java.lang.NonExistentClass";

        TypeHandler.createObject(classname);
    }

    /**
     * Test createNumber(String) method.
     */
    @Test
    public void testCreateNumber()
    {
        String str = "123";
        Number expectedNumber = 123;
        Number resultNumber;

        try
        {
            resultNumber = TypeHandler.createNumber(str);
            assertEquals(expectedNumber, resultNumber);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createNumber(String) method with non-number string.
     */
    @Test(expected = ParseException.class)
    public void testCreateNumberNonNumberString()
    {
        String str = "abc";

        TypeHandler.createNumber(str);
    }

    /**
     * Test createDate(String) method.
     * This method is not yet implemented and always throws an
     * UnsupportedOperationException.
     *
     * @throws UnsupportedOperationException expected
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate()
    {
        String str = "2021-10-08";

        TypeHandler.createDate(str);
    }

    /**
     * Test createURL(String) method.
     */
    @Test
    public void testCreateURL()
    {
        String urlString = "http://www.example.com";
        URL expectedURL = new URL(urlString);
        URL resultURL;

        try
        {
            resultURL = TypeHandler.createURL(urlString);
            assertEquals(expectedURL, resultURL);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createURL(String) method with malformed URL string.
     */
    @Test(expected = ParseException.class)
    public void testCreateURLMalformedURL()
    {
        String urlString = "http://www.example.com:8080/invalid";

        TypeHandler.createURL(urlString);
    }

    /**
     * Test createFile(String) method.
     */
    @Test
    public void testCreateFile()
    {
        String filePath = "/test/TypeHandlerTest.java";
        File expectedFile = new File(filePath);
        File resultFile;

        try
        {
            resultFile = TypeHandler.createFile(filePath);
            assertEquals(expectedFile, resultFile);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createFile(String) method with non-existent file path.
     */
    @Test
    public void testCreateFileNonExistentPath()
    {
        String filePath = "/test/nonExistentFile.txt";

        File resultFile = TypeHandler.createFile(filePath);

        assertNull(resultFile);
    }

    /**
     * Test createFileInputStream(String) method.
     */
    @Test
    public void testCreateFileInputStream() throws IOException, FileNotFoundException
    {
        String filePath = "/test/TypeHandlerTest.java";
        File file = new File(filePath);
        FileInputStream expectedFileInputStream = new FileInputStream(file);
        FileInputStream resultFileInputStream;

        resultFileInputStream = (FileInputStream) TypeHandler.createValue(filePath, FileInputStream.class);

        assertEquals(expectedFileInputStream.available(), resultFileInputStream.available());

        resultFileInputStream.close();
    }

    /**
     * Test createFileInputStream(String) method with non-existent file path.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStreamNonExistentPath() throws FileNotFoundException
    {
        String filePath = "/test/nonExistentFile.txt";

        TypeHandler.createValue(filePath, FileInputStream.class);
    }

    /**
     * Test createFiles(String) method.
     * This method is not yet implemented and always throws an
     * UnsupportedOperationException.
     *
     * @throws UnsupportedOperationException expected
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles()
    {
        String filePaths = "/test/TypeHandlerTest1.java, /test/TypeHandlerTest2.java";

        TypeHandler.createFiles(filePaths);
    }
}