package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

/**
 * Tests for the TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with String.
     */
    @Test
    public void testCreateValueStringString()
    {
        String str = "TestString";
        String expected = "TestString";
        Class<String> clazz = String.class;

        String result = TypeHandler.createValue(str, clazz);

        assertEquals(expected, result);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with Object.class.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueStringObject()
    {
        String str = "TestObject";
        Class<Object> clazz = Object.class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with Class<?>.
     */
    @SuppressWarnings("unchecked")
    @Test
    public <T> void testCreateValueStringClass() throws ParseException
    {
        String str = "TestClass";
        Class<T> clazz = String.class; // change clazz to the expected Class<T>

        T result = TypeHandler.createValue(str, clazz);

        assertEquals(str, result);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.STRING_VALUE.
     */
    @Test
    public void testCreateValueStringStringValue()
    {
        String str = "TestString";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertEquals(str.getClass(), result.getClass());
        assertEquals(str, result);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.OBJECT_VALUE.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueStringObjectValue()
    {
        String str = "TestObject";
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.NUMBER_VALUE.
     */
    @Test
    public void testCreateValueStringNumberValue() throws ParseException
    {
        String str = "123";
        Class<Number> clazz = Number.class;

        Number result = TypeHandler.createValue(str, clazz);

        assertEquals(Long.class, result.getClass());
        assertEquals(123L, result.longValue());
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.NUMBER_VALUE.
     */
    @Test
    public void testCreateValueStringDoubleValue() throws ParseException
    {
        String str = "123.45";
        Class<Number> clazz = Double.class;

        Number result = TypeHandler.createValue(str, clazz);

        assertEquals(Double.class, result.getClass());
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.DATE_VALUE.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringDateValue()
    {
        String str = "2023-03-01";
        Class<Date> clazz = Date.class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.CLASS_VALUE.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueStringClassValue()
    {
        String str = "TestClass";
        Class<Class<?>> clazz = Class.class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.FILE_VALUE.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueStringFileValue()
    {
        String str = "TestFile";
        Class<File> clazz = File.class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.EXISTING_FILE_VALUE.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueStringExistingFileValue()
    {
        String str = "TestFile";
        Class<File> clazz = File.class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.FILES_VALUE.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringFilesValue()
    {
        String str = "TestFile1,TestFile2";
        Class<File[]> clazz = File[].class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with PatternOptionBuilder.URL_VALUE.
     */
    @Test
    public void testCreateValueStringUrlValue() throws ParseException
    {
        String str = "http://example.com";
        Class<URL> clazz = URL.class;

        URL result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
        assertEquals("http://example.com", result.toExternalForm());
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)} with valid classname.
     */
    @Test
    public void testCreateObjectValidClassName() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<?> clazz = TypeHandler.createClass(classname);

        Object result = TypeHandler.createObject(classname);

        assertEquals(clazz.getName(), result.getClass().getName());

        if (result instanceof Date)
        {
            Date date = (Date) result;
            assertNotNull(date);
        }
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)} with invalid classname.
     */
    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException
    {
        String classname = "TestInvalidClassName";

        TypeHandler.createObject(classname);
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)} with valid number.
     */
    @Test
    public void testCreateNumberValidNumber() throws ParseException
    {
        String str = "1234567890";
        Number result = TypeHandler.createNumber(str);

        assertEquals(Long.class, result.getClass());
        assertEquals(1234567890L, result.longValue());
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)} with invalid number.
     */
    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumber() throws ParseException
    {
        String str = "TestInvalidNumber";

        TypeHandler.createNumber(str);
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)} with valid file path.
     */
    @Test
    public void testCreateFileValidFilePath()
    {
        String filePath = "path/to/testfile.txt";
        File result = TypeHandler.createFile(filePath);

        assertNotNull(result);
        assertTrue(result.exists());
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)} with non-existent file path.
     */
    @Test
    public void testCreateFileNonExistentFilePath()
    {
        String filePath = "non-existent/file/path.txt";
        File result = TypeHandler.createFile(filePath);

        assertNotNull(result);
        assertFalse(result.exists());
    }

    /**
     * Test method for {@link TypeHandler#openFile(String)} with valid file path.
     */
    @Test
    public void testOpenFileValidFilePath() throws IOException
    {
        String filePath = "path/to/testfile.txt";
        File file = new File(filePath);
        FileInputStream result = TypeHandler.openFile(filePath);

        assertNotNull(result);
        assertEquals(file.getAbsolutePath(), result.getFileDescriptor().getFileDescriptor().getFileDescriptor().getName());
    }

    /**
     * Test method for {@link TypeHandler#openFile(String)} with non-existent file path.
     */
    @Test(expected = ParseException.class)
    public void testOpenFileNonExistentFilePath()
    {
        String filePath = "non-existent/file/path.txt";

        TypeHandler.openFile(filePath);
    }
}