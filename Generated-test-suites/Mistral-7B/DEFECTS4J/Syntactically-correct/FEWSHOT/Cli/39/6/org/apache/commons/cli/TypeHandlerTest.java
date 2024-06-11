package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "foo";
        Object obj = new Object() {
            @Override
            public String toString() {
                return "MyClass";
            }
        };
        assertEquals(obj, TypeHandler.createValue(str, obj.getClass()));
    }

    @Test
    public void testCreateValue_StringClass() {
        Class<?> clazz = String.class;
        String str = "foo";
        assertEquals(str, TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_STRING_VALUE() {
        String str = "foo";
        assertEquals(str, TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_OBJECT_VALUE() {
        String str = "MyClass";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_NUMBER_VALUE() {
        String str = "123";
        assertEquals(123L, TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE));

        String strDouble = "123.45";
        assertEquals(123.45, TypeHandler.createValue(strDouble, PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_DATE_VALUE() {
        String str = "2023-03-31";
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertEquals(date, TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_CLASS_VALUE() {
        String className = "java.lang.String";
        assertEquals(String.class, TypeHandler.createValue(className, PatternOptionBuilder.CLASS_VALUE));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_FILE_VALUE() {
        String filePath = "test.txt";
        File expectedFile = new File(filePath);
        assertEquals(expectedFile, TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_EXISTING_FILE_VALUE() {
        String filePath = "test.txt";
        File expectedFile = new File(filePath);
        assertTrue(expectedFile.exists());
        assertEquals(expectedFile, TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_FILES_VALUE() {
        String filesPath = "test1.txt,test2.txt";
        File[] expectedFiles = new File[] { new File("test1.txt"), new File("test2.txt") };
        assertEquals(expectedFiles, TypeHandler.createValue(filesPath, PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_URL_VALUE() {
        String urlString = "http://example.com";
        URL expectedUrl = new URL(urlString);
        assertEquals(expectedUrl, TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE));
    }

    @Test
    public void testCreateObject() {
        String classname = "java.lang.String";
        String expected = "Hello World";
        String str = "Hello World";
        String result = (String) TypeHandler.createObject(classname);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFoundException() {
        String classname = "NonExistentClass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_InstantiationException() {
        String classname = "java.lang.String";
        Class<?> clazz = Class.forName(classname);
        clazz.getConstructor(String.class).setAccessible(true);
        assertThrows(InstantiationException.class, () -> TypeHandler.createObject(classname));
    }

    @Test
    public void testCreateNumber() {
        String strNumber = "123";
        Long expectedNumber = 123L;
        assertEquals(expectedNumber, TypeHandler.createNumber(strNumber));

        String strDouble = "123.45";
        Double expectedDouble = 123.45;
        assertEquals(expectedDouble, TypeHandler.createNumber(strDouble));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_NumberFormatException() {
        String strInvalidNumber = "abc";
        TypeHandler.createNumber(strInvalidNumber);
    }

    @Test
    public void testCreateClass() {
        String classname = "java.lang.String";
        Class<?> expectedClass = String.class;
        assertEquals(expectedClass, TypeHandler.createClass(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_ClassNotFoundException() {
        String classname = "NonExistentClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2023-03-31";
        Date expectedDate = new Date();
        try {
            expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // UnsupportedOperationException is expected as this method is not yet implemented
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(dateString));
        assertEquals(expectedDate, TypeHandler.createValue(dateString, PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";
        URL expectedUrl = new URL(urlString);
        assertEquals(expectedUrl, TypeHandler.createURL(urlString));
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_MalformedURLException() {
        String urlString = "invalid://example.com";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File expectedFile = new File(filePath);
        assertEquals(expectedFile, TypeHandler.createFile(filePath));
    }

    @Test
    public void testCreateFileInputStream() {
        String filePath = "test.txt";
        File file = new File(filePath);
        FileInputStream expectedInputStream = new FileInputStream(file);
        assertNull(TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_INPUT_STREAM_VALUE));
        assertInstanceof(FileInputStream.class, TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_INPUT_STREAM_VALUE));
        assertEquals(expectedInputStream, TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_INPUT_STREAM_VALUE));

        try {
            expectedInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}