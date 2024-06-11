package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import java.util.Date;

/**
 * Test class for TypeHandler
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "testString";
        String expected = "testString";
        Object result = TypeHandler.createValue(str, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "testString";
        String className = "java.lang.String";
        String expected = "testString";
        Object result = TypeHandler.createValue(str, Class.forName(className));
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringInvalidClass() throws ParseException
    {
        String str = "testString";
        String className = "java.lang.Integer";
        TypeHandler.createValue(str, Class.forName(className));
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValuePatternOptionBuilderStringObject() throws ParseException
    {
        String str = "testObject";
        Object expected = new Object();
        Object result = TypeHandler.createValue(str, Object.class);
        assertEquals(expected.getClass(), result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateValuePatternOptionBuilderInvalidClass() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String[].class;
        TypeHandler.createValue(str, clazz);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String className = "java.lang.Integer";
        Number expected = 42;
        Object result = TypeHandler.createObject(className);
        assertTrue(result instanceof Number);
        if (result instanceof Integer)
        {
            Integer integerResult = (Integer) result;
            assertEquals(expected.intValue(), integerResult.intValue());
        }
        if (result instanceof Long)
        {
            Long longResult = (Long) result;
            assertEquals(expected.longValue(), longResult.longValue());
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String className = "java.lang.String";
        TypeHandler.createObject(className);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String str = "42";
        Number expected = 42;
        Number result = TypeHandler.createNumber(str);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidString() throws ParseException
    {
        String str = "test";
        TypeHandler.createNumber(str);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassInvalidClassName() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.InvalidClass";
        TypeHandler.createClass(classname);
        fail("Expected ParseException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate()
    {
        String str = "2022-01-01";
        TypeHandler.createDate(str);
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testCreateURL() throws ParseException
    {
        String urlString = "http://www.apache.org/commons/cli/TypeHandlerTest.html";
        URL expected = new URL("http://www.apache.org/commons/cli/TypeHandlerTest.html");
        URL result = TypeHandler.createURL(urlString);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidString() throws ParseException
    {
        String urlString = "test";
        TypeHandler.createURL(urlString);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateFile()
    {
        String filePath = "testFile.txt";
        File expected = new File(filePath);
        File result = TypeHandler.createFile(filePath);
        assertEquals(expected, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInvalidPath()
    {
        String filePath = "testFileNotFound.txt";
        File result = TypeHandler.createFile(filePath);
        fail("Expected FileNotFoundException");
    }

    @Test
    public void testCreateFiles()
    {
        String filePaths = "testFile1.txt,testFile2.txt";
        File[] expectedFiles = new File[] { new File("testFile1.txt"), new File("testFile2.txt") };
        File[] resultFiles = TypeHandler.createFiles(filePaths);
        assertArrayEquals(expectedFiles, resultFiles);
    }
}