package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Test for TypeHandler class.
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "Hello World";
        Object obj = new String("Hello World");
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(obj, result);
    }

    @Test
    public void testCreateValue_StringClassStringClass() {
        String str = "java.lang.String";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertSame(String.class, result.getClass());
        assertEquals("java.lang.String", result.toString());
    }

    @Test
    public void testCreateValue_NullStringObject() {
        Object obj = new String("Hello World");;
        Object result = TypeHandler.createValue(null, obj.getClass());
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_NullStringNullClass() {
        String str = null;
        Class clazz = null;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject_String() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertSame(String.class, result.getClass());
        assertEquals(className, result.getClass().getName());
    }

    @Test
    public void testCreateObject_InvalidClass() {
        String className = "InvalidClass";
        Object result = TypeHandler.createObject(className);
        assertNull(result);
    }

    @Test
    public void testCreateNumber_LongString() {
        String str = "123";
        Number number = 123;
        Number result = TypeHandler.createNumber(str);
        assertEquals(number.longValue(), result.longValue());
    }

    @Test
    public void testCreateNumber_DoubleString() {
        String str = "123.45";
        Double number = 123.45;
        Number result = TypeHandler.createNumber(str);
        assertEquals(number.doubleValue(), result.doubleValue(), 0.0);
    }

    @Test
    public void testCreateNumber_InvalidString() {
        String str = "abc";
        Number result = TypeHandler.createNumber(str);
        assertNull(result);
    }

    @Test
    public void testCreateDate_ValidDateString() {
        String dateString = "2022-12-31";
        Date date = new Date(0L);
        Date result = TypeHandler.createDate(dateString);
        assertEquals(dateString, TypeHandler.getDateFormat().format(result));
    }

    @Test
    public void testCreateDate_InvalidDateString() {
        String dateString = "2022-13-31";
        Date result = TypeHandler.createDate(dateString);
        assertNull(result);
    }

    @Test
    public void testCreateURL_ValidURLString() {
        String urlString = "http://www.example.com";
        URL url = new URL(urlString);
        URL result = TypeHandler.createURL(urlString);
        assertEquals(url.toExternalForm(), result.toExternalForm());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURLString() {
        String urlString = "http://www.example.com/abc";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile_ValidFilePath() {
        String filePath = "path/to/file.txt";
        File file = new File(filePath);
        File result = TypeHandler.createFile(filePath);
        assertEquals(file, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_InvalidFilePath() {
        String filePath = "path/to/invalid/file.txt";
        TypeHandler.createFile(filePath);
    }

    @Test
    public void testCreateFiles_ValidFilePaths() {
        String filePaths = "path/to/file1.txt,path/to/file2.txt";
        File[] files = new File[] { new File("path/to/file1.txt"),
            new File("path/to/file2.txt") };
        File[] result = TypeHandler.createFiles(filePaths);
        assertArrayEquals(files, result);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCreateFiles_InvalidFilePaths() {
        String filePaths = "path/to/file1.txt,path/to/file2.txt,";
        TypeHandler.createFiles(filePaths);
    }

    @Test
    public void testCreateClass_ValidClassName() {
        String className = "java.lang.String";
        Class clazz = String.class;
        Class result = TypeHandler.createClass(className);
        assertEquals(clazz, result);
    }

    @Test
    public void testCreateClass_InvalidClassName() {
        String className = "InvalidClass";
        Class result = TypeHandler.createClass(className);
        assertNull(result);
    }
}