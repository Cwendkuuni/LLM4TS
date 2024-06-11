package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

/**
 * TypesHandler Test
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "testString";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertSame(obj, result);
    }

    @Test
    public void testCreateValueStringClassString() throws ParseException
    {
        String str = "testString";
        String expectedClass = "java.lang.String.class";
        Class<?> clazz = Class.forName(expectedClass);
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(expectedClass, result.getClass().getName());
        assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObjectClassname() throws ParseException
    {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assertEquals(String.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassname() throws ParseException
    {
        String classname = "java.lang.InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberInvalidNumber() throws ParseException, NumberFormatException
    {
        String str = "testInvalidNumber";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateNumberLong() throws ParseException, NumberFormatException
    {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123L, result.longValue());
    }

    @Test
    public void testCreateNumberDouble() throws ParseException, NumberFormatException
    {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateClassClassname() throws ParseException
    {
        String classname = "java.lang.String.class";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassInvalidClassname() throws ParseException
    {
        String classname = "java.lang.InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateInvalidDateString() throws ParseException
    {
        String str = "invalidDateString";
        TypeHandler.createDate(str);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException, UnsupportedOperationException
    {
        String str = "2023-01-31";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }

    @Test
    public void testCreateURLValidURLString() throws ParseException, MalformedURLException
    {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidURLString() throws ParseException, MalformedURLException
    {
        String str = "invalidURLString";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFileValidFileString()
    {
        String str = "C:/Temp/testFile.txt";
        File result = TypeHandler.createFile(str);
        File expected = new File(str);
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileInvalidFileString()
    {
        String str = "C:/notExist/testFile.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFilesValidFilePaths()
    {
        String str = "C:/Temp/testFile1.txt,C:/Temp/testFile2.txt";
        File[] result = TypeHandler.createFiles(str);
        String[] expectedPaths = {"C:/Temp/testFile1.txt", "C:/Temp/testFile2.txt"};
        assertArrayEquals(expectedPaths, TypeHandler.getFilesNames(result));
    }
}