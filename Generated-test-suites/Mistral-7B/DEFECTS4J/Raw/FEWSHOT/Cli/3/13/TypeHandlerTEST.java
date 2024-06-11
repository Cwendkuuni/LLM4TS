package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "foo";
        Object obj = "java.lang.String";
        Object result = TypeHandler.createValue(str, obj);
        assertEquals("foo", result);

        str = "123";
        obj = "java.lang.Integer";
        result = TypeHandler.createValue(str, obj);
        assertEquals(123, result);

        str = "456.78";
        obj = "java.lang.Double";
        result = TypeHandler.createValue(str, obj);
        assertEquals(456.78, result);

        str = "2022-12-31";
        obj = "java.util.Date";
        result = TypeHandler.createValue(str, obj);
        assertNotNull(result);

        str = "com.example.MyClass";
        obj = "java.lang.Class";
        result = TypeHandler.createValue(str, obj);
        assertNotNull(result);

        str = "file:/path/to/myfile.txt";
        obj = "java.io.File";
        result = TypeHandler.createValue(str, obj);
        assertInstanceOf(File.class, result);

        str = "http://www.example.com";
        obj = "java.net.URL";
        result = TypeHandler.createValue(str, obj);
        assertInstanceOf(URL.class, result);

        str = ".";
        obj = "java.io.File";
        result = TypeHandler.createValue(str, obj);
        assertInstanceOf(File.class, result);

        str = "dir/subdir/";
        obj = "java.io.File";
        result = TypeHandler.createValue(str, obj);
        assertInstanceOf(File.class, result);

        str = "1, 2, 3";
        obj = "[I";
        result = TypeHandler.createValue(str, obj);
        assertArrayEquals(new int[]{1, 2, 3}, result);

        str = "\"foo\"";
        obj = "java.lang.String[";
        result = TypeHandler.createValue(str, obj);
        assertEquals("foo", result.toString());

        str = "null";
        obj = "java.lang.String";
        result = TypeHandler.createValue(str, obj);
        assertEquals("null", result.toString());

        str = "null";
        obj = "java.io.File";
        result = TypeHandler.createValue(str, obj);
        assertNull(result);

        str = "";
        obj = "java.lang.String";
        result = TypeHandler.createValue(str, obj);
        assertEquals("", result.toString());

        str = "";
        obj = "java.io.File";
        result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-12-31";
        Date result = TypeHandler.createDate(str);
        assertNotNull(result);
        assertEquals(2022, result.getYear());
        assertEquals(12, result.getMonth());
        assertEquals(31, result.getDate());

        str = "2022-12-31 12:34:56.789";
        result = TypeHandler.createDate(str);
        assertNotNull(result);
        assertEquals(2022, result.getYear());
        assertEquals(12, result.getMonth());
        assertEquals(31, result.getDate());
        assertEquals(12, result.getHours());
        assertEquals(34, result.getMinutes());
        assertEquals(56, result.getMinutes());
        assertEquals(789, result.getSeconds());
    }

    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
        assertEquals("www.example.com", result.getHost());
        assertEquals("/", result.getFile());
        assertEquals("", result.getRef());
        assertEquals(443, result.getPort());

        str = "file:/path/to/myfile.txt";
        result = TypeHandler.createURL(str);
        assertNotNull(result);
        assertEquals("file", result.getProtocol());
        assertEquals("", result.getHost());
        assertEquals("/path/to/myfile.txt", result.getFile());
        assertEquals("", result.getRef());
        assertEquals(-1, result.getPort());

        str = "http://www.example.com/path/to/myfile.txt";
        result = TypeHandler.createURL(str);
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
        assertEquals("www.example.com", result.getHost());
        assertEquals("/path/to/myfile.txt", result.getFile());
        assertEquals("/path/to/myfile.txt", result.getPath());
        assertEquals("", result.getRef());
        assertEquals(80, result.getPort());

        str = "http://www.example.com:8080/path/to/myfile.txt";
        result = TypeHandler.createURL(str);
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
        assertEquals("www.example.com", result.getHost());
        assertEquals("/path/to/myfile.txt", result.getFile());
        assertEquals("/path/to/myfile.txt", result.getPath());
        assertEquals("/", result.getRef());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testCreateClass() {
        String str = "java.lang.String";
        Class<?> result = TypeHandler.createClass(str);
        assertEquals(String.class, result);

        str = "java.util.Date";
        result = TypeHandler.createClass(str);
        assertEquals(Date.class, result);

        str = "java.io.File";
        result = TypeHandler.createClass(str);
        assertEquals(File.class, result);

        str = "java.net.URL";
        result = TypeHandler.createClass(str);
        assertEquals(URL.class, result);

        str = "com.example.MyClass";
        result = TypeHandler.createClass(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateObject() {
        String str = "java.lang.String";
        Object result = TypeHandler.createObject(str);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());

        str = "java.util.Date";
        result = TypeHandler.createObject(str);
        assertNotNull(result);
        assertEquals(Date.class, result.getClass());

        str = "java.io.File";
        result = TypeHandler.createObject(str);
        assertNotNull(result);
        assertEquals(File.class, result.getClass());

        str = "java.net.URL";
        result = TypeHandler.createObject(str);
        assertNotNull(result);
        assertEquals(URL.class, result.getClass());

        str = "com.example.MyClass";
        result = TypeHandler.createObject(str);
        assertNotNull(result);

        try {
            result = TypeHandler.createObject("java.lang.String[]");
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123, result.intValue());

        str = "456";
        result = TypeHandler.createNumber(str);
        assertEquals(456, result.intValue());

        str = "789";
        result = TypeHandler.createNumber(str);
        assertEquals(789, result.intValue());

        str = "-123";
        result = TypeHandler.createNumber(str);
        assertEquals(-123, result.intValue());

        str = "12345";
        result = TypeHandler.createNumber(str);
        assertEquals(12345, result.longValue());

        str = "-12345";
        result = TypeHandler.createNumber(str);
        assertEquals(-12345, result.longValue());

        str = "123.45";
        result = TypeHandler.createNumber(str);
        assertEquals(123.45, result.doubleValue());

        str = "1.23E4";
        result = TypeHandler.createNumber(str);
        assertEquals(12300.0, result.doubleValue());

        str = "-1.23E4";
        result = TypeHandler.createNumber(str);
        assertEquals(-12300.0, result.doubleValue());

        str = "1.23E-4";
        result = TypeHandler.createNumber(str);
        assertEquals(0.000123, result.doubleValue(), 0.0001);

        str = "-1.23E-4";
        result = TypeHandler.createNumber(str);
        assertEquals(-0.000123, result.doubleValue(), 0.0001);

        try {
            result = TypeHandler.createNumber("foo");
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testCreateFile() {
        String str = "file:/path/to/myfile.txt";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals("/path/to/myfile.txt", result.getAbsolutePath());

        str = ".";
        result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals(".", result.getAbsolutePath());

        str = "dir/subdir/";
        result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals("dir/subdir/", result.getAbsolutePath());

        try {
            result = TypeHandler.createFile("http://www.example.com");
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testCreateFiles() {
        String str = "file1.txt, file2.txt, file3.txt";
        File[] result = TypeHandler.createFiles(str);
        assertNotNull(result);
        assertEquals(3, result.length);
        for (int i = 0; i < result.length; i++) {
            File file = result[i];
            String name = String.format("file%d.txt", i + 1);
            assertEquals(name, file.getName());
        }

        str = "file1.txt, file2.txt, file3.txt, ";
        result = TypeHandler.createFiles(str);
        assertArrayEquals(new File[]{new File("file1.txt"), new File("file2.txt"), new File("file3.txt")}, result);

        str = "file1.txt, file2.txt, file3.txt, file4.txt, ";
        result = TypeHandler.createFiles(str);
        assertArrayEquals(new File[]{new File("file1.txt"), new File("file2.txt"), new File("file3.txt"), new File("file4.txt")}, result);

        str = "file1.txt, file2.txt, file3.txt, file4.txt, file5.txt";
        result = TypeHandler.createFiles(str);
        assertArrayEquals(new File[]{new File("file1.txt"), new File("file2.txt"), new File("file3.txt"), new File("file4.txt"), new File("file5.txt")}, result);

        str = "";
        result = TypeHandler.createFiles(str);
        assertArrayEquals(new File[0], result);

        try {
            result = TypeHandler.createFiles("http://www.example.com");
            fail();
        } catch (Exception e) {
            // expected
        }
    }
}