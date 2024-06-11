package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

public class TypeHandlerTest
{
    private Class<?> stringClass;
    private Class<?> objectClass;
    private Class<?> numberClass;
    private Class<?> dateClass;
    private Class<?> classClass;
    private Class<?> fileClass;
    private Class<?> existingFileClass;
    private Class<?> filesClass;
    private Class<?> urlClass;

    @Before
    public void init()
    {
        stringClass = String.class;
        objectClass = Object.class;
        numberClass = Number.class;
        dateClass = Date.class;
        classClass = Class.class;
        fileClass = File.class;
        existingFileClass = File.class; // same as File, but for test purpose
        filesClass = File[].class;
        urlClass = URL.class;
    }

    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, stringClass);
        assertEquals("test", obj);
    }

    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, objectClass);
        assertNull(obj);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, stringClass);
        assertEquals("test", obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderStringValue() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        fail("Expected ParseException but got: " + obj);
    }

    @Test
    public void testCreateValueStringObjectValue() throws ParseException
    {
        String str = "testObj";
        Object obj = TypeHandler.createObject(str);
        assertEquals(str, obj.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringObjectValueClassNotFound() throws ParseException
    {
        String str = "testNonExistentClass";
        try
        {
            Object obj = TypeHandler.createObject(str);
            fail("Expected ParseException but got: " + obj);
        }
        catch (ParseException e)
        {
            assertEquals("Unable to find the class: testNonExistentClass", e.getMessage());
        }
    }

    @Test
    public void testCreateValueStringNumber() throws ParseException
    {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123L, number.longValue());

        str = "123.45";
        number = TypeHandler.createNumber(str);
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringNumberInvalid() throws ParseException
    {
        String str = "abcd";
        try
        {
            Number number = TypeHandler.createNumber(str);
            fail("Expected ParseException but got: " + number);
        }
        catch (ParseException e)
        {
            assertEquals("Unable to parse number: abcd", e.getMessage());
        }
    }

    @Test
    public void testCreateValueStringClassValue() throws ParseException
    {
        String str = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(str);
        assertEquals(String.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassValueClassNotFound() throws ParseException
    {
        String str = "org.apache.commons.cli.test.NonExistentClass";
        try
        {
            Class<?> clazz = TypeHandler.createClass(str);
            fail("Expected ParseException but got: " + clazz);
        }
        catch (ParseException e)
        {
            assertEquals("Unable to find the class: org.apache.commons.cli.test.NonExistentClass", e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringDate() throws ParseException
    {
        String str = "2022-03-15";
        try
        {
            Date date = TypeHandler.createDate(str);
            fail("Expected UnsupportedOperationException but got: " + date);
        }
        catch (UnsupportedOperationException e)
        {
            assertTrue(true); // expected
        }
    }

    @Test
    public void testCreateValueStringURL() throws ParseException, MalformedURLException
    {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringURLMalformed() throws ParseException, MalformedURLException
    {
        String str = "http://www.example.com:80/test";
        try
        {
            URL url = TypeHandler.createURL(str);
            fail("Expected ParseException but got: " + url);
        }
        catch (ParseException e)
        {
            assertEquals("Unable to parse the URL: http://www.example.com:80/test", e.getMessage());
        }
    }

    @Test
    public void testCreateValueStringFile() throws ParseException
    {
        String str = "path/to/file.txt";
        File file = TypeHandler.createFile(str);
        assertEquals(new File(str), file);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringFileNotFound() throws ParseException
    {
        String str = "nonexistentfile.txt";
        try
        {
            File file = TypeHandler.createFile(str);
            fail("Expected ParseException but got: " + file);
        }
        catch (ParseException e)
        {
            assertEquals("File not found: nonexistentfile.txt", e.getMessage());
        }
    }

    @Test
    public void testCreateValueStringExistingFile() throws ParseException
    {
        String str = "path/to/existingfile.txt";
        File existingFile = new File(str);
        File file = TypeHandler.createFile(str);
        assertEquals(existingFile, file);
    }

    @Test
    public void testCreateValueStringFiles() throws ParseException
    {
        String str = "path/to/file1.txt,path/to/file2.txt";
        File[] files = TypeHandler.createFiles(str);
        File file1 = new File("path/to/file1.txt");
        File file2 = new File("path/to/file2.txt");
        assertArrayEquals(new File[] { file1, file2 }, files);
    }
}