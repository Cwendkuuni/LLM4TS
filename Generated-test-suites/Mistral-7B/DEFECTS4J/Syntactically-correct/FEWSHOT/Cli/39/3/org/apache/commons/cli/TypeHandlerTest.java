package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
        String str = "sample";
        Object obj = new Object() {
        };

        Object result = TypeHandler.createValue(str, obj.getClass());

        assertSame(obj, result);
    }

    @Test
    public void testCreateValue_StringClass_StringValue() {
        String str = "sample";

        Class<?> clazz = String.class;

        Object result = TypeHandler.createValue(str, clazz);

        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass_ObjectValue() {
        String str = "sample";

        Class<?> clazz = Object.class;

        Object result = TypeHandler.createValue(str, clazz);

        assertNull(result);
    }

    @Test
    public void testCreateValue_StringClass_NumberValue() {
        String str = "123";

        Class<?> clazz = Number.class;

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(Number.class, result);
    }

    @Test
    public void testCreateValue_StringClass_DateValue() {
        String str = "2022-01-01";

        Class<?> clazz = Date.class;

        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_StringClass_ClassValue() {
        String classname = "java.lang.String";

        Class<?> clazz = Class.forName(classname);

        Object result = TypeHandler.createValue(classname, clazz);

        assertEquals(clazz, result.getClass());
    }

    @Test
    public void testCreateClass_ValidClassName() {
        String classname = "java.lang.String";

        Object result = TypeHandler.createClass(classname);

        assertEquals(Class.forName(classname), result);
    }

    @Test
    public void testCreateClass_InvalidClassName() {
        String classname = "InvalidClassName";

        assertThrows(ParseException.class, () -> TypeHandler.createClass(classname));
    }

    @Test
    public void testCreateNumber_ValidNumber() {
        String str = "123";

        Object result = TypeHandler.createNumber(str);

        assertInstanceOf(Number.class, result);
    }

    @Test
    public void testCreateNumber_InvalidNumber() {
        String str = "invalid";

        assertThrows(ParseException.class, () -> TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateURL_ValidURL() {
        String urlString = "http://www.example.com";

        Object result = TypeHandler.createURL(urlString);

        assertInstanceOf(URL.class, result);
    }

    @Test
    public void testCreateURL_InvalidURL() {
        String urlString = "invalid";

        assertThrows(ParseException.class, () -> TypeHandler.createURL(urlString));
    }

    @Test
    public void testCreateFile_ValidFile() {
        String filePath = "/path/to/file.txt";

        Object result = TypeHandler.createFile(filePath);

        assertInstanceOf(File.class, result);
    }

    @Test
    public void testCreateFile_InvalidFile() {
        String filePath = "invalid";

        assertThrows(FileNotFoundException.class, () -> {
            File file = (File) TypeHandler.createFile(filePath);
            new FileInputStream(file);
        });

        assertNull(TypeHandler.createFile(filePath));
    }

    @Test
    public void testCreateFiles_ValidFiles() {
        String filePaths = "/path/to/file1.txt, /path/to/file2.txt";

        Object result = TypeHandler.createFiles(filePaths);

        File[] expectedFiles = new File[] {new File("/path/to/file1.txt"), new File("/path/to/file2.txt")};
        assertArrayEquals(expectedFiles, (File[]) result);
    }

    @Test
    public void testCreateFiles_InvalidFiles() {
        String filePaths = "invalid";

        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(filePaths));
    }
}