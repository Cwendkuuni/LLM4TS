package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.lang.ClassFormatter;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws Exception
    {
        Object actual = TypeHandler.createValue("test", String.class);
        assertEquals("test", actual);
    }

    @Test
    public void testCreateValueStringClass() throws Exception
    {
        String className = "java.lang.Integer";
        Object actual = TypeHandler.createValue("42", Class.forName(className));
        Integer expected = 42;
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() throws Exception
    {
        String className = "java.lang.InvalidClass";
        TypeHandler.createValue("test", Class.forName(className));
    }

    @Test
    public void testCreateNumber() throws Exception
    {
        Object actual = TypeHandler.createNumber("42");
        Integer expected = 42;
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumber() throws Exception
    {
        Object actual = TypeHandler.createNumber("test");
        fail();
    }

    @Test
    public void testCreateDate() throws Exception
    {
        String dateString = "2022-01-01";
        Date actual = (Date) TypeHandler.createValue(dateString, Date.class);
        Date expected = new Date(0L);
        expected.setYear(2022);
        expected.setMonth(0);
        expected.setDate(1);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateInvalidDateFormat() throws Exception
    {
        String dateString = "2022-01-01T23:59:59";
        TypeHandler.createValue(dateString, Date.class);
    }

    @Test
    public void testCreateURL() throws Exception
    {
        String urlString = "http://example.com";
        URL actual = TypeHandler.createValue(urlString, URL.class);
        URL expected = new URL("http://example.com");
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidFormat() throws Exception
    {
        String urlString = "http://example.com:8080/path/to/file";
        TypeHandler.createValue(urlString, URL.class);
    }

    @Test
    public void testCreateFile() throws Exception
    {
        String filePath = "test.txt";
        File actual = TypeHandler.createValue(filePath, File.class);
        File expected = new File(filePath);
        assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileNotFound() throws Exception
    {
        String filePath = "nonexistentfile.txt";
        TypeHandler.createValue(filePath, File.class);
    }

    @Test
    public void testCreateFileInputStream() throws Exception
    {
        String filePath = "test.txt";
        File file = new File(filePath);
        if (file.exists())
        {
            file.delete();
        }
        file.createNewFile();
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        inputStream.close();
        file.delete();

        Object actual = TypeHandler.createValue(filePath, FileInputStream.class);
        FileInputStream expected = new FileInputStream(filePath);
        assertEquals(expected.getClass(), actual.getClass());
        try (FileInputStream testActual = (FileInputStream) actual)
        {
            byte[] testBytes = new byte[1024];
            testActual.read(testBytes);
            byte[] expectedBytes = new byte[testBytes.length];
            System.arraycopy(bytes, 0, expectedBytes, 0, testBytes.length);
            assertArrayEquals(expectedBytes, testBytes);
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateFileInputStreamFileNotFound() throws Exception
    {
        String filePath = "nonexistentfile.txt";
        Object actual = TypeHandler.createValue(filePath, FileInputStream.class);
        fail();
    }

    @Test
    public void testCreateClass() throws Exception
    {
        String className = "java.lang.Integer";
        Object actual = TypeHandler.createValue("42", Class.forName(className));
        Integer expected = 42;
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);

        Constructor<?> constructor = Class.forName(className).getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        assertEquals(instance.getClass(), Integer.class);
        int value = 42;
        Field field = Integer.class.getField("value");
        field.setAccessible(true);
        field.set(instance, value);
        assertEquals(expected, instance);
    }
}