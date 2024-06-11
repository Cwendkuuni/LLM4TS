package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test
    public void testCreateValue_StringClass_String() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test
    public void testCreateValue_StringClass_ObjectTypeMismatch() {
        assertNull(TypeHandler.createValue("foo", Integer.class));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderStringValue() {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderObjectValue() {
        assertThrows(ParseException.class, () -> TypeHandler.createValue("foo", PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderNumberValue() {
        assertEquals(1L, TypeHandler.createValue("1", Long.class));
        assertEquals(1.0, TypeHandler.createValue("1.", Double.class));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderDateValue() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue("2022-12-31", Date.class));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderClassValue() {
        try {
            Class<?> clazz = Class.forName("java.lang.String");
            assertEquals(clazz, TypeHandler.createValue("java.lang.String", Class.class));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderFileValue() {
        File file = new File("test.txt");
        assertEquals(file, TypeHandler.createValue("test.txt", File.class));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderExistingFileValue() {
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue("test.txt", File.class, File.exists(new File("test.txt"))));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderFilesValue() {
        File[] files = new File[] {new File("test1.txt"), new File("test2.txt")};
        assertEquals(files, TypeHandler.createValue("test1.txt test2.txt", File[].class));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilderURLValue() {
        URL url = new URL("http://www.example.com");
        assertEquals(url, TypeHandler.createValue("http://www.example.com", URL.class));
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        assertEquals("", TypeHandler.createObject(className).toString());
    }

    @Test
    public void testCreateObject_ClassNotFoundException() {
        String className = "NonExistentClass";
        assertThrows(ParseException.class, () -> TypeHandler.createObject(className));
    }

    @Test
    public void testCreateNumber() {
        assertEquals(1L, TypeHandler.createNumber("1"));
        assertEquals(1.0, TypeHandler.createNumber("1."));
    }

    @Test
    public void testCreateNumber_NumberFormatException() {
        assertThrows(ParseException.class, () -> TypeHandler.createNumber("foo"));
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        assertEquals(String.class, TypeHandler.createClass(className));
    }

    @Test
    public void testCreateClass_ClassNotFoundException() {
        String className = "NonExistentClass";
        assertThrows(ParseException.class, () -> TypeHandler.createClass(className));
    }

    @Test
    public void testCreateDate() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate("2022-12-31"));
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        URL url = TypeHandler.createURL(urlString);
        assertEquals("http", url.getProtocol());
        assertEquals("www.example.com", url.getHost());
    }

    @Test
    public void testCreateURL_MalformedURLException() {
        assertThrows(ParseException.class, () -> TypeHandler.createURL("foo://www.example.com"));
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File file = TypeHandler.createFile(filePath);
        assertEquals(filePath, file.getAbsolutePath());
    }

    @Test
    public void testOpenFile() {
        String filePath = "test.txt";
        assertThrows(FileNotFoundException.class, () -> TypeHandler.openFile(filePath));
    }

    @Test
    public void testCreateFiles() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles("test1.txt test2.txt"));
    }
}