package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Test cases for TypeHandler class.
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "hello";
        Object obj = new String("world");
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals("world", result);
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "123";
        Class clazz = int.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals(123, result);

        str = "456.78";
        clazz = double.class;
        result = TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals(456.78, result, 0.0);

        str = "true";
        clazz = boolean.class;
        result = TypeHandler.createValue(str, clazz);
        assertTrue((boolean) result);

        str = "java.lang.String";
        try {
            result = TypeHandler.createValue(str, Date.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateObject_String() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertInstanceOf(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_UnknownClass() {
        String className = "UnknownClass";
        TypeHandler.createObject(className);
        fail("Expected ClassNotFoundException");
    }

    @Test
    public void testCreateNumber_String() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertEquals(123, result.intValue());

        str = "456.78";
        result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertEquals(456.78, result.doubleValue(), 0.0);

        str = "abc";
        result = TypeHandler.createNumber(str);
        assertNull(result);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidString() {
        String str = "12a3";
        TypeHandler.createNumber(str);
        fail("Expected NumberFormatException");
    }

    @Test
    public void testCreateClass_String() {
        String className = "java.lang.String";
        Class result = TypeHandler.createClass(className);
        assertInstanceOf(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_UnknownClass() {
        String className = "UnknownClass";
        TypeHandler.createClass(className);
        fail("Expected ClassNotFoundException");
    }

    @Test
    public void testCreateDate_String() {
        String dateString = "2022-03-01";
        Date result = TypeHandler.createDate(dateString);
        assertNotNull(result);
        assertEquals(new Date(2022, 2, 1), result);

        dateString = "2022-03-31";
        result = TypeHandler.createDate(dateString);
        assertNotNull(result);
        assertEquals(new Date(2022, 2, 31), result);

        dateString = "2022-13-31";
        result = TypeHandler.createDate(dateString);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_InvalidFormat() {
        String dateString = "2022-3-31";
        TypeHandler.createDate(dateString);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateURL_String() {
        String urlString = "http://example.com";
        URL result = TypeHandler.createURL(urlString);
        assertInstanceOf(URL.class, result);
        assertEquals("http", result.getProtocol());
        assertEquals("example.com", result.getHost());

        urlString = "https://example.com:8080/path/to/resource";
        result = TypeHandler.createURL(urlString);
        assertInstanceOf(URL.class, result);
        assertEquals("https", result.getProtocol());
        assertEquals("example.com", result.getHost());
        assertEquals(8080, result.getPort());
        assertEquals("/path/to/resource", result.getPath());

        urlString = "foo://example.com";
        try {
            result = TypeHandler.createURL(urlString);
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidFormat() {
        String urlString = "http:example.com";
        TypeHandler.createURL(urlString);
        fail("Expected MalformedURLException");
    }

    @Test
    public void testCreateFile_String() {
        String filePath = "path/to/file.txt";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
        assertEquals("path/to/file.txt", result.getAbsolutePath());

        filePath = "path/to/non_existent_file.txt";
        result = TypeHandler.createFile(filePath);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFile_InvalidPath() {
        String filePath = "path/to/..";
        TypeHandler.createFile(filePath);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateFiles_String() {
        String filePath = "path/to/files/*.txt";
        File[] result = TypeHandler.createFiles(filePath);
        assertNotNull(result);
        assertEquals(2, result.length);
        File file1 = result[0];
        File file2 = result[1];
        assertEquals("path/to/files/file1.txt", file1.getAbsolutePath());
        assertEquals("path/to/files/file2.txt", file2.getAbsolutePath());

        filePath = "path/to/non_existent_files/*.txt";
        result = TypeHandler.createFiles(filePath);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFiles_InvalidPath() {
        String filePath = "path/to/../*.txt";
        TypeHandler.createFiles(filePath);
        fail("Expected IllegalArgumentException");
    }
}