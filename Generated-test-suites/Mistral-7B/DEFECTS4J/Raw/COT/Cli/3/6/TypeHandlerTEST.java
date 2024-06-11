package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

/**
 * TypeHandlerTest.java - This class contains JUnit 4 test cases for the TypeHandler class.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueWithStringObject()
    {
        Object strObj = TypeHandler.createValue("test", String.class);
        assertEquals("test", strObj);
    }

    @Test
    public void testCreateValueWithStringClass()
    {
        String str = "test";
        Class clazz = String.class;

        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueWithInvalidType()
    {
        String str = "test";
        Object obj = new Object();

        Object result = TypeHandler.createValue(str, obj.getClass());
        assertNull(result);
    }

    @Test
    public void testCreateObject()
    {
        String className = "java.lang.String";
        Object obj = TypeHandler.createObject(className);

        assertEquals(className, ClassUtils.getClassSimpleName(obj.getClass()));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectWithInvalidClassName()
    {
        String className = "InvalidClassName";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumber()
    {
        String numberStr = "123";
        Number number = TypeHandler.createNumber(numberStr);

        assertEquals(123, number.intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberWithInvalidString()
    {
        String numberStr = "invalid";
        TypeHandler.createNumber(numberStr);
    }

    @Test
    public void testCreateClass()
    {
        String className = "java.lang.String";
        Class clazz = TypeHandler.createClass(className);

        assertEquals(className, clazz.getName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClassWithInvalidClassName()
    {
        String className = "InvalidClassName";
        TypeHandler.createClass(className);
    }

    @Test
    public void testCreateDate()
    {
        String dateString = "2023-03-29";
        Date date = TypeHandler.createDate(dateString);

        assertNull(date);
    }

    @Test
    public void testCreateURL()
    {
        String urlString = "http://example.com";
        URL url = TypeHandler.createURL(urlString);

        assertNotNull(url);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLWithInvalidString()
    {
        String urlString = "invalid-url";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile()
    {
        String filePath = "test.txt";
        File file = TypeHandler.createFile(filePath);

        assertNotNull(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileWithInvalidPath()
    {
        String filePath = "non-existent-file.txt";
        TypeHandler.createFile(filePath);
    }

    @Test(expected = IOException.class)
    public void testCreateFilesWithInvalidString()
    {
        String filePaths = "invalid-file-paths.txt";
        TypeHandler.createFiles(filePaths);
    }
}