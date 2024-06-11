package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        Object obj = new Object();
        String str = "test";
        assertEquals(str, TypeHandler.createValue(str, obj.getClass()).toString());
    }

    @Test
    public void testCreateValueStringClassString() {
        String className = "java.lang.String";
        String str = "test";
        Class<String> expectedClass = (Class<String>) Class.forName(className);
        String result = (String) TypeHandler.createValue(str, expectedClass);
        assertEquals(expectedClass, result.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassWithException() {
        String className = "java.lang.Integer";
        String str = "test";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, Class.forName(className)));
    }

    @Test
    public void testCreateObjectString() {
        String className = "java.lang.String";
        String str = "test";
        String result = (String) TypeHandler.createObject(className);
        assertEquals(str, result);
    }

    @Test
    public void testCreateObjectNonExistingClass() {
        String className = "java.NonExistingClass";
        assertThrows(ParseException.class, () -> TypeHandler.createObject(className));
    }

    @Test
    public void testCreateNumberString() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123, result.intValue());

        str = "456.78";
        result = TypeHandler.createNumber(str);
        assertEquals(456.78, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateNumberInvalidString() {
        String str = "test";
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateClassString() {
        String className = "java.lang.String";
        Class<String> expectedClass = (Class<String>) Class.forName(className);
        Class<?> result = TypeHandler.createClass(className);
        assertInstanceOf(expectedClass, result);
    }

    @Test
    public void testCreateClassInvalidClassName() {
        String className = "java.NonExistingClass";
        assertThrows(ParseException.class, () -> TypeHandler.createClass(className));
    }

    @Test
    public void testCreateDateString() {
        String str = "2022-12-31";
        Date result = TypeHandler.createDate(str);
        assertEquals(new Date(Long.parseLong("1671152000000")), result);
    }

    @Test
    public void testCreateDateInvalidString() {
        String str = "test";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURLString() {
        String urlString = "https://example.com";
        URL result = TypeHandler.createURL(urlString);
        assertEquals(new URL("https://example.com"), result);
    }

    @Test
    public void testCreateURLInvalidString() {
        String urlString = "test";
        assertThrows(ParseException.class, () -> TypeHandler.createURL(urlString));
    }

    @Test
    public void testCreateFileString() {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        File expectedFile = new File(filePath);
        assertEquals(expectedFile, result);
    }

    @Test
    public void testOpenFileNonExistingFile() {
        String filePath = "nonExistingFile.txt";
        assertThrows(ParseException.class, () -> TypeHandler.openFile(filePath));
    }

    @Test
    public void testOpenFile() {
        String filePath = "test.txt";
        File file = new File(filePath);
        FileInputStream expectedInputStream = new FileInputStream(file);
        FileInputStream result = TypeHandler.openFile(filePath);
        assertEquals(expectedInputStream, result);
    }
}