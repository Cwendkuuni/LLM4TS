package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.ClassNotFoundException;
import java.lang.Class;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() {
        String str = "foo";
        assertEquals(str, TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValue_StringClass_StringType() {
        String str = "foo";
        Class<?> clazz = String.class;
        assertEquals(str, TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_StringClass_ObjectType() {
        assertNull(TypeHandler.createValue("foo", Object.class));
    }

    @Test
    public void testCreateValue_StringClass_NumberType() {
        String str = "42";
        Number number = TypeHandler.createValue(str, Number.class);
        assertEquals(42, number.intValue());
    }

    @Test
    public void testCreateValue_StringClass_DateType() {
        String str = "2023-03-31";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, Date.class));
    }

    @Test
    public void testCreateValue_StringClass_ClassType() throws Exception {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        Object obj = TypeHandler.createValue(className, clazz);
        assertInstanceOf(String.class, obj);
    }

    @Test
    public void testCreateValue_StringClass_URLType() {
        String urlString = "https://example.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testCreateValue_StringClass_FileType() {
        String filePath = "test.txt";
        File file = new File(filePath);
        File expectedFile = new File(filePath);
        File fileFromTypeHandler = TypeHandler.createValue(filePath, File.class);
        assertEquals(expectedFile, fileFromTypeHandler);
    }

    @Test
    public void testCreateValue_StringClass_FileInputStreamType() {
        String filePath = "test.txt";
        File file = new File(filePath);
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue(filePath, FileInputStream.class));
    }

    @Test
    public void testCreateValue_StringClass_FileArrayType() {
        String filePaths = "test1.txt,test2.txt";
        File[] files = new File[] {new File("test1.txt"), new File("test2.txt")};
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(filePaths, File[].class));
    }
}