package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new Object();
        String str = "test";
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringString() {
        String str = "test";
        String expected = "test";
        String result = (String) TypeHandler.createValue(str, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueStringClassNotFoundException() {
        String str = "com.test.MyClass";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, Object.class));
    }

    @Test
    public void testCreateValueStringClass() throws ClassNotFoundException {
        String str = "com.test.MyClass";
        Class<?> clazz = Class.forName("com.test.MyClass");
        Object result = TypeHandler.createValue(str, clazz);
        assertInstanceOf(clazz, result);
    }

    @Test
    public void testCreateValueStringNumber() {
        String str = "123";
        Number expected = 123;
        Number result = (Number) TypeHandler.createValue(str, Number.class);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueStringNumberInvalidNumberFormat() {
        String str = "abc";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, Number.class));
    }

    @Test
    public void testCreateValueStringDate() {
        String str = "2022-12-31";
        Date expected = new Date(2022, 11, 31);
        Date result = TypeHandler.createDate(str);
        assertInstanceOf(Date.class, result);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueStringDateUnsupportedOperationException() {
        String str = "2022-12-31";
        assertThrows(UnsupportedOperationException.class, TypeHandler::createDate);
    }

    @Test
    public void testCreateValueStringURL() {
        String str = "http://test.com";
        URL expected = new URL("http://test.com");
        URL result = TypeHandler.createURL(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueStringURLMalformedURLException() {
        String str = "http://test.com/foo/bar/";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, URL.class));
    }

    @Test
    public void testCreateValueStringFile() {
        String str = "path/to/file.txt";
        File expected = new File(str);
        File result = TypeHandler.createFile(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueStringFileIOException() {
        String str = "non-existent/file.txt";
        assertThrows(ParseException.class, () -> TypeHandler.createFile(str));
    }

    @Test
    public void testCreateValueStringFileInputStream() throws IOException {
        String str = "path/to/file.txt";
        File file = new File(str);
        FileInputStream expected = new FileInputStream(file);
        InputStream result = (InputStream) TypeHandler.createValue(str, FileInputStream.class);
        assertSame(expected, result);
    }

    @Test
    public void testCreateValueStringFileArray() {
        String str = "path/to/file1.txt,path/to/file2.txt";
        File[] expected = new File[] {new File("path/to/file1.txt"), new File("path/to/file2.txt")};
        File[] result = TypeHandler.createFiles(str);
        assertInstanceOf(File[].class, result);
        assertArrayEquals(expected, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringFileArrayUnsupportedOperationException() {
        String str = "path/to/file1.txt,path/to/file2.txt";
        TypeHandler.createFiles(str);
    }
}