package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() {
        String str = "foo";
        assertEquals(str, TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValue_StringObject() throws ParseException {
        String str = "java.lang.String";
        assertInstanceOf(String.class, TypeHandler.createValue(str, String.class));
        assertEquals("foo", TypeHandler.createValue(str, "java.lang.String".getClass()));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringWrongType() throws ParseException {
        String str = "java.lang.String";
        TypeHandler.createValue(str, Integer.TYPE);
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        Object obj = TypeHandler.createObject(className);
        assertEquals(clazz, obj.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = "java.lang.InvalidClass";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String str = "123";
        Long expected = 123L;
        Number actual = TypeHandler.createNumber(str);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumber() throws ParseException {
        String str = "foo";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateDate() {
        String dateStr = "2023-03-31";
        Date expected = new Date(2023 - 1900, 2 - 1, 31);
        Date actual = TypeHandler.createDate(dateStr);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_Unsupported() {
        String dateStr = "2023-03-31";
        TypeHandler.createDate(dateStr);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        String urlStr = "http://localhost:8080";
        URL expected = new URL("http://localhost:8080");
        URL actual = TypeHandler.createURL(urlStr);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL() throws ParseException {
        String urlStr = "invalid_url";
        TypeHandler.createURL(urlStr);
    }

    @Test
    public void testCreateFile() {
        String filePath = "/test/file.txt";
        File expected = new File(filePath);
        File actual = TypeHandler.createFile(filePath);
        assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStream_NonExistentFile() throws FileNotFoundException, ParseException {
        String filePath = "/test/non_existent_file.txt";
        File file = TypeHandler.createFile(filePath);
        FileInputStream inputStream = TypeHandler.createFileInputStream(filePath);
    }

    @Test(expected = ParseException.class)
    public void testCreateFileInputStream_NonFile() throws ParseException, FileNotFoundException {
        String filePath = "non_file";
        TypeHandler.createFileInputStream(filePath);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String filePaths = "/test/file1.txt,/test/file2.txt";
        File[] expected = new File[] { new File("/test/file1.txt"), new File("/test/file2.txt") };
        // UnsupportedOperationException is thrown in createFiles() method
        TypeHandler.createFiles(filePaths);
        assertArrayEquals(expected, TypeHandler.createFiles(filePaths));
    }
}