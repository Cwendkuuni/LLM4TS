package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() {
        String str = "foo";
        Object obj = new String("bar");
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass_StringValue() {
        String str = "foo";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass_ObjectType() {
        String str = "java.lang.String";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateValue_StringClass_StringType() {
        String str = "\"foo\"";
        Class clazz = PatternOptionBuilder.STRING_VALUE.getClass();
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass_ObjectType_Null() {
        String str = null;
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.util.Date";
        Object result = TypeHandler.createObject(className);
        assertInstanceof(Date.class, result);
    }

    @Test
    public void testCreateObject_InvalidClassName() {
        String className = "java.util.InvalidClass";
        Object result = TypeHandler.createObject(className);
        assertNull(result);
    }

    @Test
    public void testCreateNumber() {
        String number = "123";
        Number result = TypeHandler.createNumber(number);
        assertEquals(123, result.intValue());

        number = "123.45";
        result = TypeHandler.createNumber(number);
        assertEquals(123.45, result.doubleValue(), 0.0);
    }

    @Test
    public void testCreateNumber_InvalidNumber() {
        String number = "foo";
        Number result = TypeHandler.createNumber(number);
        assertNull(result);
    }

    @Test
    public void testCreateClass() {
        String className = "java.util.Date";
        Class result = TypeHandler.createClass(className);
        assertInstanceof(Date.class, result.newInstance());
    }

    @Test
    public void testCreateClass_InvalidClassName() {
        String className = "java.util.InvalidClass";
        Class result = TypeHandler.createClass(className);
        assertNull(result);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-01-01";
        Date result = TypeHandler.createDate(dateString);
        assertNotNull(result);
    }

    @Test
    public void testCreateDate_InvalidDateString() {
        String dateString = "foo";
        Date result = TypeHandler.createDate(dateString);
        assertNull(result);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";
        URL result = TypeHandler.createURL(urlString);
        assertNotNull(result);
    }

    @Test
    public void testCreateURL_InvalidURLString() {
        String urlString = "foo";
        URL result = TypeHandler.createURL(urlString);
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
    }

    @Test
    public void testCreateFile_InvalidFilePath() {
        String filePath = "foo";
        File result = TypeHandler.createFile(filePath);
        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFiles_Null() {
        String filePath = null;
        File[] result = TypeHandler.createFiles(filePath);
        fail("Expected NullPointerException");
    }

    @Test(expected = AssertionError.class)
    public void testCreateFiles_EmptyString() {
        String filePath = "";
        File[] result = TypeHandler.createFiles(filePath);
        fail("Expected ArrayIndexOutOfBoundsException");
    }

    @Test
    public void testCreateFiles_MultipleFiles() {
        String filePaths = "file1.txt,file2.txt,file3.txt";
        File[] result = TypeHandler.createFiles(filePaths);
        assertArrayEquals(new File[]{"file1.txt", "file2.txt", "file3.txt"}, result);
    }
}