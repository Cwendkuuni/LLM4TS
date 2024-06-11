package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        String str = "foo";
        Object obj = "java.lang.String";
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);

        obj = TypeHandler.class;
        result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);

        obj = new Object() {};
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_String_Class() {
        String str = "foo";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);

        clazz = TypeHandler.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_String_String() {
        String str = "java.lang.String";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertInstanceOf(String.class, result);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertInstanceOf(String.class, result);
        assertEquals(className, result.getClass().getName());

        className = "NonExistentClass";
        assertThrows(ParseException.class, () -> TypeHandler.createObject(className));
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123L, result.longValue());

        str = "123.45";
        result = TypeHandler.createNumber(str);
        assertEquals(123.45, result.doubleValue(), 0.001);

        str = "foo";
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        Class<?> result = TypeHandler.createClass(className);
        assertEquals(String.class, result);

        className = "NonExistentClass";
        assertThrows(ParseException.class, () -> TypeHandler.createClass(className));
    }

    @Test
    public void testCreateDate() {
        String str = "2022-01-01";
        // UnsupportedOperationException is expected
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() {
        String str = "http://example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals("http://example.com", result.toString());

        str = "invalid://example.com";
        assertThrows(ParseException.class, () -> TypeHandler.createURL(str));
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File result = TypeHandler.createFile(str);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());

        str = "non-existent-file.txt";
        assertThrows(ParseException.class, () -> TypeHandler.createFile(str));
    }

    @Test
    public void testOpenFile() throws IOException {
        String str = "/path/to/file.txt";
        File file = new File(str);
        FileInputStream result = TypeHandler.openFile(str);
        assertEquals(file, result.getFile());

        str = "non-existent-file.txt";
        assertThrows(ParseException.class, () -> TypeHandler.openFile(str));
    }

    @Test
    public void testCreateFiles() {
        // UnsupportedOperationException is expected
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles("path/to/files/*.txt"));
    }
}