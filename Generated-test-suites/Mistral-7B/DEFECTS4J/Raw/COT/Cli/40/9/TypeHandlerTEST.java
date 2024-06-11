package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;

import org.junit.Test;

/**
 * Test class for TypeHandler methods.
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with String type.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "test";
        Object obj = "String";
        assertEquals(str, TypeHandler.createValue(str, obj));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        Object obj = new Object() {
            @Override
            public String toString()
            {
                return "Object";
            }
        };
        String str = "Object";
        assertEquals(obj, TypeHandler.createValue(str, obj.getClass()));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with unsupported type.
     */
    @Test
    public void testCreateValueUnsupportedType()
    {
        String str = "test";
        Class<?> unsupportedType = Integer.class;
        assertNull(TypeHandler.createValue(str, unsupportedType));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with String type.
     */
    @Test
    public void testCreateValueStringClass()
    {
        String str = "test";
        Class<String> stringClass = String.class;
        assertEquals(str, TypeHandler.createValue(str, stringClass));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with Object type.
     */
    @Test
    public void testCreateValueObjectClass()
    {
        String str = "Object";
        Class<Object> objectClass = Object.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, objectClass));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with unsupported type.
     */
    @Test
    public void testCreateValueUnsupportedClass()
    {
        String str = "test";
        Class<Integer> unsupportedClass = Integer.class;
        assertNull(TypeHandler.createValue(str, unsupportedClass));
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)} with valid classname.
     */
    @Test
    public void testCreateObjectValidClassName() throws ParseException
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createObject(classname);
        assertEquals(String.class, obj.getClass());
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)} with invalid classname.
     */
    @Test
    public void testCreateObjectInvalidClassName() throws ParseException
    {
        String classname = "InvalidClassName";
        assertThrows(ParseException.class, () -> TypeHandler.createObject(classname));
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)} with valid number string.
     */
    @Test
    public void testCreateNumberValidNumberString() throws ParseException
    {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(Long.class, number.getClass());
        assertEquals(123L, number.longValue());

        str = "123.45";
        number = TypeHandler.createNumber(str);
        assertEquals(Double.class, number.getClass());
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)} with invalid number string.
     */
    @Test
    public void testCreateNumberInvalidNumberString() throws ParseException
    {
        String str = "abc";
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(str));
    }

    /**
     * Test method for {@link TypeHandler#createClass(String)} with valid classname.
     */
    @Test
    public void testCreateClassValidClassName() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(String.class, clazz);
    }

    /**
     * Test method for {@link TypeHandler#createClass(String)} with invalid classname.
     */
    @Test
    public void testCreateClassInvalidClassName() throws ParseException
    {
        String classname = "InvalidClassName";
        assertThrows(ParseException.class, () -> TypeHandler.createClass(classname));
    }

    /**
     * Test method for {@link TypeHandler#createDate(String)} with valid date string.
     */
    @Test
    public void testCreateDateValidDateString() throws ParseException
    {
        String str = "2023-03-13";
        Date date = TypeHandler.createDate(str);
        assertEquals("Tue Mar 13 00:00:00 CEST 2023", date.toString());
    }

    /**
     * Test method for {@link TypeHandler#createDate(String)} with invalid date string.
     */
    @Test
    public void testCreateDateInvalidDateString()
    {
        String str = "InvalidDateString";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(str));
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)} with valid URL string.
     */
    @Test
    public void testCreateURLValidURLString() throws ParseException
    {
        String str = "http://example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals("http://example.com/", url.toString());
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)} with invalid URL string.
     */
    @Test
    public void testCreateURLInvalidURLString()
    {
        String str = "InvalidURLString";
        assertThrows(ParseException.class, () -> TypeHandler.createURL(str));
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)} with valid file path.
     */
    @Test
    public void testCreateFileValidFilePath()
    {
        String str = "/path/to/file.txt";
        File file = TypeHandler.createFile(str);
        assertTrue(file.exists());
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)} with invalid file path.
     */
    @Test
    public void testCreateFileInvalidFilePath()
    {
        String str = "InvalidFilePath";
        assertThrows(ParseException.class, () -> TypeHandler.createFile(str));
    }

    /**
     * Test method for {@link TypeHandler#openFile(String)} with existent file.
     */
    @Test
    public void testOpenFileExistentFile() throws ParseException, IOException
    {
        String str = "/path/to/file.txt";
        File file = new File(str);
        FileInputStream fileInput = TypeHandler.openFile(str);
        assertEquals(file, fileInput.getFileDescriptor().getFileDescriptor());
    }

    /**
     * Test method for {@link TypeHandler#openFile(String)} with non-existent file.
     */
    @Test
    public void testOpenFileNonExistentFile() throws ParseException
    {
        String str = "/path/to/non-existent-file.txt";
        assertThrows(ParseException.class, () -> TypeHandler.openFile(str));
    }

    /**
     * Test method for {@link TypeHandler#createFiles(String)} with valid file paths.
     */
    @Test
    public void testCreateFilesValidFilePaths()
    {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        File[] files = TypeHandler.createFiles(str);
        assertEquals(2, files.length);
        for (File file : files)
        {
            assertTrue(file.exists());
        }
    }

    /**
     * Test method for {@link TypeHandler#createFiles(String)} with invalid file paths.
     */
    @Test
    public void testCreateFilesInvalidFilePaths()
    {
        String str = "InvalidFilePath1,InvalidFilePath2";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(str));
    }
}