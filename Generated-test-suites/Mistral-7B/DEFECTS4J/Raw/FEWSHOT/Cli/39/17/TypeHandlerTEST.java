package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString_String() {
        String str = "foo";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValue_StringString_Class_String() {
        String str = "3";
        Class<?> clazz = Integer.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(3, obj.intValue());
    }

    @Test
    public void testCreateValue_StringString_Class_Double() {
        String str = "3.14";
        Class<?> clazz = Double.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(3.14, obj.doubleValue());
    }

    @Test
    public void testCreateValue_StringString_Class_Date() {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_StringString_Class_File() {
        String str = "path/to/file.txt";
        Class<?> clazz = File.class;
        File file = TypeHandler.createValue(str, clazz);
        assertTrue(file.exists()); // Assuming the file exists in the classpath
    }

    @Test
    public void testCreateValue_StringString_Class_URL() {
        String str = "http://example.com";
        Class<?> clazz = URL.class;
        URL url = TypeHandler.createValue(str, clazz);
        assertEquals("http://example.com", url.toExternalForm());
    }

    @Test
    public void testCreateValue_StringString_Class_Object() {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        Object obj = TypeHandler.createValue(className, clazz);
        assertInstanceOf(String.class, obj);
    }

    @Test
    public void testCreateValue_StringString_Class_Class() {
        String className = "java.lang.Integer";
        Class<?> clazz = Class.forName(className);
        assertThrows(IllegalArgumentException.class, () -> TypeHandler.createValue("", clazz));
        assertThrows(IllegalArgumentException.class, () -> TypeHandler.createValue("foo", clazz));
        Object obj = TypeHandler.createValue("3", clazz);
        assertEquals(3, obj.intValue());
    }

    @Test
    public void testCreateValue_StringString_Class_Number() {
        String str = "3.14";
        Class<?> clazz = Number.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertInstanceOf(Double.class, obj);
    }

    @Test
    public void testCreateValue_StringString_Class_Null() {
        String str = null;
        Class<?> clazz = String.class;
        assertNull(TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateObject() throws Exception {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        Object obj = TypeHandler.createObject(className);
        assertInstanceOf(String.class, obj);
    }

    @Test
    public void testCreateNumber() throws Exception {
        String str = "3";
        Object obj = TypeHandler.createNumber(str);
        assertEquals(3, obj.intValue());
    }

    @Test
    public void testCreateNumber_InvalidInput() throws Exception {
        String str = "foo";
        assertThrows(NumberFormatException.class, () -> TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateClass() throws Exception {
        String className = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(className);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testCreateClass_NonExistentClass() throws Exception {
        String className = "java.lang.NonExistentClass";
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createClass(className));
    }

    @Test
    public void testCreateDate() {
        String str = "2022-12-31";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() throws Exception {
        String str = "http://example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals("http://example.com", url.toExternalForm());
    }

    @Test
    public void testCreateFile() {
        String str = "path/to/file.txt";
        File file = TypeHandler.createFile(str);
        assertTrue(file.exists()); // Assuming the file exists in the file system
    }

    @Test
    public void testCreateFile_NonExistentFile() {
        String str = "path/to/non-existent-file.txt";
        assertNull(TypeHandler.createFile(str));
    }

    @Test
    public void testCreateFiles() {
        String str = "path/to/file1.txt, path/to/file2.txt";
        File[] files = TypeHandler.createFiles(str);
        assertArrayEquals(new File[] {new File("path/to/file1.txt"), new File("path/to/file2.txt")}, files);
    }
}