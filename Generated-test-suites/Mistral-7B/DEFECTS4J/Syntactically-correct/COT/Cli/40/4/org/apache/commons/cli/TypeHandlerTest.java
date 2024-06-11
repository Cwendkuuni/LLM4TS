package org.apache.commons.cli.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.PatternOptionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@RunWith(JUnit4.class)
public class TypeHandlerTest
{
    // Basic test cases for createValue method

    @Test
    public void testCreateValue_StringObject()
    {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertInstanceOf(obj.getClass(), result);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringStringClass()
    {
        String str = "test";
        Class<String> clazz = String.class;
        String result = (String) TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilderClass()
    {
        String str = "123";
        Class<Number> clazz = Number.class;
        Number result = TypeHandler.createValue(str, clazz);
        assertInstanceOf(Number.class, result);
        assertEquals(123, result.longValue());

        str = "123.45";
        result = TypeHandler.createValue(str, clazz);
        assertInstanceOf(Double.class, result);
        assertEquals(123.45, result.doubleValue());
    }

    // Test cases for createObject method

    @Test
    public void testCreateObject_String()
    {
        String classname = "java.lang.String";
        String expected = "Test";
        Object result = TypeHandler.createObject(classname);
        assertEquals(expected, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_NonExistingClass()
    {
        String classname = "java.lang.NonExistingClass";
        TypeHandler.createObject(classname);
    }

    // Test cases for createNumber method

    @Test
    public void testCreateNumber_LongString()
    {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertInstanceOf(Long.class, result.getClass());
        assertEquals(123, result.longValue());
    }

    @Test
    public void testCreateNumber_DoubleString()
    {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        assertInstanceOf(Double.class, result.getClass());
        assertEquals(123.45, result.doubleValue(), 0.01);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidString()
    {
        String str = "Test";
        TypeHandler.createNumber(str);
    }

    // Test cases for createClass method

    @Test
    public void testCreateClass_String()
    {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_InvalidClass()
    {
        String classname = "java.lang.NonExistingClass";
        TypeHandler.createClass(classname);
    }

    // Test cases for createDate method

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_String()
    {
        String str = "2023-03-01";
        TypeHandler.createDate(str);
    }

    // Test cases for createURL method

    @Test
    public void testCreateURL_String()
    {
        String urlString = "http://example.com";
        URL result = TypeHandler.createURL(urlString);
        assertEquals("http", result.getProtocol());
        assertEquals("example.com", result.getHost());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidString()
    {
        String urlString = "invalid://example.com";
        TypeHandler.createURL(urlString);
    }

    // Test cases for createFile method

    @Test
    public void testCreateFile_String()
    {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        assertTrue(result.exists());
        assertTrue(result.delete());
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_InvalidString()
    {
        String filePath = "/nonexistent/test.txt";
        TypeHandler.createFile(filePath);
    }

    // Test cases for openFile method

    @Test
    public void testOpenFile_String() throws IOException
    {
        String filePath = "test.txt";
        File testFile = new File(filePath);
        testFile.createNewFile();

        FileInputStream result = TypeHandler.openFile(filePath);
        assertEquals(filePath, result.getFileDescriptor().getFileDescriptor());

        testFile.delete();
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_InvalidString()
    {
        String filePath = "/nonexistent/test.txt";
        TypeHandler.openFile(filePath);
    }
}