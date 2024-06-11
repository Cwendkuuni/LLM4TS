package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;

import org.junit.Test;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject()
    {
        Object obj = new Object();
        String input = "test";
        Object output = TypeHandler.createValue(input, obj.getClass());
        assertEquals(input, output.toString());
    }

    @Test
    public void testCreateValueStringString()
    {
        String input = "test";
        Class<String> clazz = String.class;
        String output = TypeHandler.createValue(input, clazz).toString();
        assertEquals(input, output);
    }

    @Test
    public void testCreateValueStringClassNotFoundException()
    {
        Class<?> clazz = null;
        String input = "java.lang.String";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, clazz));
    }

    @Test
    public void testCreateValueStringClassNewInstance()
    {
        Class<?> clazz = String[].class;
        String input = "test";
        assertThrows(IllegalArgumentException.class, () -> TypeHandler.createValue(input, clazz));
    }

    @Test
    public void testCreateValueStringClassForName()
    {
        Class<?> clazz = String[].class;
        String input = "java.lang.String[]";
        String[] output = (String[]) TypeHandler.createValue(input, clazz);
        assertEquals(input, output[0]);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue()
    {
        String input = "test";
        Class<String> clazz = PatternOptionBuilder.STRING_VALUE;
        String output = TypeHandler.createValue(input, clazz).toString();
        assertEquals(input, output);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue()
    {
        Class<Object> clazz = Object.class;
        String input = "test";
        Object output = TypeHandler.createValue(input, clazz);
        assertNotNull(output);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue()
    {
        String input = "123";
        Class<Number> clazz = Number.class;
        Number output = TypeHandler.createValue(input, clazz);
        assertTrue(output instanceof Long);
        assertEquals(Long.parseLong(input), ((Long) output).longValue());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String input = "2022-12-31";
        Class<Date> clazz = Date.class;
        Date output = TypeHandler.createValue(input, clazz);
        assertEquals(input, dateFormat.format(output));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue()
    {
        String input = "java.lang.String";
        Class<Class<?>> clazz = Class.class;
        Class<?> output = (Class<?>) TypeHandler.createValue(input, clazz);
        assertEquals(input, output.getName());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue()
    {
        String input = "path/to/file.txt";
        Class<File> clazz = File.class;
        File output = (File) TypeHandler.createValue(input, clazz);
        assertTrue(output.exists());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue()
    {
        String input = "path/to/non/existing/file.txt";
        Class<File> clazz = File.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, clazz));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue()
    {
        String input = "path/to/file1.txt,path/to/file2.txt";
        Class<File[]> clazz = File[].class;
        File[] output = (File[]) TypeHandler.createValue(input, clazz);
        assertTrue(output.length > 0);
        for (int i = 0; i < output.length; i++)
        {
            File file = output[i];
            assertTrue(file.exists());
        }
    }

    @Test
    public void testCreateValueStringPatternOptionBuildeURLValue()
    {
        String input = "http://example.com";
        Class<URL> clazz = URL.class;
        URL output = TypeHandler.createValue(input, clazz);
        assertEquals(input, output.toString());
    }

    @Test
    public void testCreateValueInvalidStringPatternOptionBuilderURLValue()
    {
        String input = "http://example.com/";
        Class<URL> clazz = URL.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, clazz));
    }

    @Test
    public void testCreateObject() throws ParseException
    {
        Class<TestClass> clazz = TestClass.class;
        String input = "org.apache.commons.cli.test.TestClass";
        TestClass output = (TestClass) TypeHandler.createObject(input);
        assertEquals(input, output.getClass().getName());
    }

    @Test
    public void testCreateNumber()
    {
        String input = "123";
        Class<Number> clazz = Number.class;
        Number output = TypeHandler.createNumber(input);
        assertTrue(output instanceof Long);
        assertEquals(Long.parseLong(input), ((Long) output).longValue());
    }

    @Test
    public void testCreateNumberInvalid()
    {
        String input = "test";
        Class<Number> clazz = Number.class;
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(input));
    }

    @Test
    public void testCreateClass()
    {
        String input = "java.lang.String";
        Class<Class<?>> clazz = Class.class;
        Class<?> output = TypeHandler.createClass(input);
        assertEquals(input, output.getName());
    }

    @Test
    public void testCreateClassInvalid()
    {
        String input = "java.lang.StringTest";
        Class<Class<?>> clazz = Class.class;
        assertThrows(ParseException.class, () -> TypeHandler.createClass(input));
    }

    @Test
    public void testCreateDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String input = "2022-12-31";
        Class<Date> clazz = Date.class;
        Date output = TypeHandler.createDate(input);
        assertEquals(input, dateFormat.format(output));
    }

    @Test
    public void testCreateDateInvalid()
    {
        String input = "2022-12-32";
        Class<Date> clazz = Date.class;
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(input));
    }

    @Test
    public void testCreateURL() throws ParseException
    {
        String input = "http://example.com";
        Class<URL> clazz = URL.class;
        URL output = TypeHandler.createURL(input);
        assertEquals(input, output.toString());
    }

    @Test
    public void testCreateURLInvalid()
    {
        String input = "http://example.com/";
        Class<URL> clazz = URL.class;
        assertThrows(ParseException.class, () -> TypeHandler.createURL(input));
    }

    @Test
    public void testCreateFile()
    {
        String input = "path/to/file.txt";
        Class<File> clazz = File.class;
        File output = TypeHandler.createFile(input);
        assertTrue(output.exists());
    }

    @Test
    public void testCreateFileInvalid()
    {
        String input = "path/to/non/existing/file.txt";
        Class<File> clazz = File.class;
        assertThrows(ParseException.class, () -> TypeHandler.createFile(input));
    }

    @Test
    public void testOpenFile() throws ParseException
    {
        String input = "path/to/file.txt";
        Class<FileInputStream> clazz = FileInputStream.class;
        FileInputStream output = TypeHandler.openFile(input);
        assertNotNull(output);
    }

    @Test
    public void testOpenFileInvalid() throws ParseException
    {
        String input = "path/to/non/existing/file.txt";
        Class<FileInputStream> clazz = FileInputStream.class;
        assertThrows(ParseException.class, () -> TypeHandler.openFile(input));
    }

    @Test
    public void testCreateFiles() throws ParseException
    {
        String input = "path/to/file1.txt,path/to/file2.txt";
        Class<File[]> clazz = File[].class;
        File[] output = TypeHandler.createFiles(input);
        assertTrue(output.length > 0);
        for (int i = 0; i < output.length; i++)
        {
            File file = output[i];
            assertTrue(file.exists());
        }
    }

    @Test
    public void testCreateFilesInvalid() throws ParseException
    {
        String input = "path/to/file1.txt,path/to/non/existing/file2.txt";
        Class<File[]> clazz = File[].class;
        assertThrows(ParseException.class, () -> TypeHandler.createFiles(input));
    }

    private static class TestClass
    {
    }
}