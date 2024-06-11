package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() {
        assertEquals("hello", TypeHandler.createValue("hello", String.class));
        assertNull(TypeHandler.createValue("123", String.class));
        assertNull(TypeHandler.createValue(null, String.class));
    }

    @Test
    public void testCreateValue_Class() throws Exception {
        Class<?> clazz = Class.forName("java.lang.Integer");
        assertEquals(Integer.valueOf(42), TypeHandler.createValue("42", clazz));
        assertEquals(Integer.valueOf(0), TypeHandler.createValue("0", clazz));
        assertEquals(Integer.valueOf(-1), TypeHandler.createValue("-1", clazz));
        assertEquals(Integer.valueOf(123456), TypeHandler.createValue("123456", clazz));
        assertEquals(Double.valueOf(3.14), TypeHandler.createValue("3.14", clazz));

        assertThrows(ParseException.class, () -> TypeHandler.createValue("hello", clazz));
        assertThrows(ParseException.class, () -> TypeHandler.createValue(null, clazz));
    }

    @Test
    public void testCreateValue_ClassObject() throws Exception {
        Class<?> clazz = String.class;
        assertEquals("hello", TypeHandler.createValue("hello", clazz, null));
        assertEquals("123", TypeHandler.createValue("123", clazz, null));
        assertEquals("null", TypeHandler.createValue(null, clazz, null));

        Object obj = new Object();
        assertEquals(obj, TypeHandler.createValue("obj", clazz, obj));
    }

    @Test
    public void testCreateObject() throws Exception {
        Class<?> stringClass = String.class;
        Class<?> integerClass = Integer.class;

        assertEquals("hello", TypeHandler.createObject("java.lang.String", "hello").toString());
        assertEquals(42, ((Number) TypeHandler.createObject("java.lang.Integer", "42")).intValue());

        assertThrows(ParseException.class, () -> TypeHandler.createObject("java.lang.Xyz", "hello"));
        assertThrows(ParseException.class, () -> TypeHandler.createObject(null, "hello"));
    }

    @Test
    public void testCreateNumber() throws Exception {
        assertEquals(42, TypeHandler.createNumber("42").intValue());
        assertEquals(123, TypeHandler.createNumber("123").intValue());
        assertEquals(-1, TypeHandler.createNumber("-1").intValue());
        assertEquals(123456, TypeHandler.createNumber("123456").longValue());
        assertEquals(3.14, TypeHandler.createNumber("3.14").doubleValue());

        assertThrows(ParseException.class, () -> TypeHandler.createNumber("hello"));
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(null));
    }

    @Test
    public void testCreateClass() throws Exception {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertSame(Integer.class, TypeHandler.createClass("java.lang.Integer"));

        assertThrows(ParseException.class, () -> TypeHandler.createClass("java.lang.Xyz"));
        assertThrows(ParseException.class, () -> TypeHandler.createClass(null));
    }

    @Test
    public void testCreateDate() {
        // Not yet implemented
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate("2022-12-31"));
    }

    @Test
    public void testCreateURL() throws Exception {
        URL url = new URL("http://example.com");
        assertEquals(url, TypeHandler.createURL("http://example.com"));

        assertThrows(ParseException.class, () -> TypeHandler.createURL("invalid://example.com"));
        assertThrows(ParseException.class, () -> TypeHandler.createURL(null));
    }

    @Test
    public void testCreateFile() {
        File file = new File("test.txt");
        assertEquals(file, TypeHandler.createFile("test.txt"));

        assertFalse(TypeHandler.createFile(null).exists());
    }

    @Test
    public void testOpenFile() throws Exception {
        File file = new File("test.txt");
        InputStream inputStream = new FileInputStream(file);

        assertEquals(inputStream, TypeHandler.openFile("test.txt"));

        assertThrows(FileNotFoundException.class, () -> TypeHandler.openFile(null));
    }

    @Test
    public void testCreateFiles() {
        File[] files = new File[] { new File("test1.txt"), new File("test2.txt") };

        assertArrayEquals(files, TypeHandler.createFiles("test1.txt,test2.txt"));

        // Not yet implemented
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(null));
    }
}