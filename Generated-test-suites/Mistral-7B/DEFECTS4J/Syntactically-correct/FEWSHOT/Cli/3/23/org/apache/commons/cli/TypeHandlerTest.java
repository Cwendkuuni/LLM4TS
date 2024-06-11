package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        assertEquals(new Integer(42), TypeHandler.createValue("42", Integer.class));
        assertEquals(new Long(42), TypeHandler.createValue("42", Long.class));
        assertEquals(new Double(3.14), TypeHandler.createValue("3.14", Double.class));
        assertEquals(new Date(1234567890000L), TypeHandler.createValue(" Wed Jan 01 12:34:56 CET 2003", Date.class));
        assertEquals(new File("path/to/file"), TypeHandler.createValue("path/to/file", File.class));
        assertEquals(new URL("http://example.com"), TypeHandler.createValue("http://example.com", URL.class));
        assertEquals(new String[]{"path/to/file1", "path/to/file2"}, TypeHandler.createValues("path/to/file1,path/to/file2", String[].class));
        assertNull(TypeHandler.createValue("", Integer.class));
        assertNull(TypeHandler.createValue("42", String.class));
        assertNull(TypeHandler.createValue("42", Float.class));
        assertNull(TypeHandler.createValue("42", Boolean.class));
        assertNull(TypeHandler.createValue("", Date.class));
        assertNull(TypeHandler.createValue("", URL.class));
        assertNull(TypeHandler.createValue("", File.class));
    }

    @Test
    public void testCreateValueArrayStringObject() {
        String[] paths = {"path/to/file1", "path/to/file2"};
        assertArrayEquals(paths, (String[]) TypeHandler.createValues("path/to/file1,path/to/file2", String[].class));
        assertArrayEquals(new String[0], (String[]) TypeHandler.createValues("", String[].class));
        assertArrayEquals(new String[0], (String[]) TypeHandler.createValues("", String[].class));
        assertNull(TypeHandler.createValues("", Object[].class));
        assertNull(TypeHandler.createValues("", int[].class));
        assertNull(TypeHandler.createValues("", double[].class));
        assertNull(TypeHandler.createValues("", boolean[].class));
        assertNull(TypeHandler.createValues("", Date[].class));
        assertNull(TypeHandler.createValues("", URL[].class));
        assertNull(TypeHandler.createValues("", File[].class));
        assertThrows(IllegalArgumentException.class, () -> TypeHandler.createValues("path/to/file", String[].class));
    }

    @Test
    public void testCreateObject() {
        try {
            Class clazz = Class.forName("java.lang.String");
            Object obj = TypeHandler.createObject("java.lang.String");
            assertSame(clazz, obj.getClass());
        } catch (ClassNotFoundException e) {
            fail("Failed to load class");
        }

        try {
            Class clazz = Class.forName("java.awt.Color");
            Object obj = TypeHandler.createObject("java.awt.Color");
            assertNotNull(obj);
            assertTrue(obj instanceof Color);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class");
        }

        try {
            Class clazz = Class.forName("NonExistentClass");
            Object obj = TypeHandler.createObject("NonExistentClass");
            assertNull(obj);
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testCreateNumber() {
        assertEquals(3, TypeHandler.createNumber("3").intValue());
        assertEquals(3, TypeHandler.createNumber("3L").longValue());
        assertEquals(3.0, TypeHandler.createNumber("3.0").doubleValue(), 0.0);
        assertEquals(3.0, TypeHandler.createNumber("3.0D").doubleValue(), 0.0);
        assertEquals(3, TypeHandler.createNumber("003").intValue());
        assertEquals(3, TypeHandler.createNumber("003L").longValue());
        assertEquals(3, TypeHandler.createNumber("-3").intValue());
        assertEquals(-3, TypeHandler.createNumber("-3").longValue());
        assertEquals(3.0, TypeHandler.createNumber("3.0F").floatValue(), 0.0F);
        assertEquals(3.0, TypeHandler.createNumber("3.0F").doubleValue(), 0.0);
        assertEquals(3.0, TypeHandler.createNumber("3.0d").doubleValue(), 0.0);
        assertEquals(3.0, TypeHandler.createNumber("3.0D").doubleValue(), 0.0);
        assertEquals(0, TypeHandler.createNumber("").intValue());
        assertEquals(0, TypeHandler.createNumber("").longValue());
        assertEquals(0.0, TypeHandler.createNumber("").doubleValue(), 0.0);
        assertNull(TypeHandler.createNumber(""));
        try {
            Number number = TypeHandler.createNumber("abc");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, TypeHandler.createClass("String"));
        assertEquals(Date.class, TypeHandler.createClass("Date"));
        assertEquals(File.class, TypeHandler.createClass("File"));
        assertEquals(URL.class, TypeHandler.createClass("URL"));
        assertNull(TypeHandler.createClass(""));
        assertNull(TypeHandler.createClass("NonExistentClass"));
        try {
            Class clazz = TypeHandler.createClass("java.util.Date");
            assertEquals(Date.class, clazz);
        } catch (ClassNotFoundException e) {
            fail("Failed to load class");
        }
    }

    @Test
    public void testCreateDate() {
        Date date = new Date(1234567890000L);
        assertEquals(date, TypeHandler.createDate("Wed Jan 01 12:34:56 CET 2003"));
        assertEquals(date, TypeHandler.createDate("1234567890000"));
        assertNull(TypeHandler.createDate(""));
        assertThrows(ParseException.class, () -> TypeHandler.createDate("InvalidDateString"));
    }

    @Test
    public void testCreateURL() {
        URL url = new URL("http://example.com");
        assertEquals(url, TypeHandler.createURL("http://example.com"));
        assertEquals(url, TypeHandler.createURL("http://example.com/"));
        assertEquals(url, TypeHandler.createURL("http://example.com:80/"));
        assertEquals(url, TypeHandler.createURL("http://user:password@example.com:80/path"));
        assertNull(TypeHandler.createURL(""));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createURL("InvalidURLString"));
    }

    @Test
    public void testCreateFile() {
        File file = new File("path/to/file");
        assertEquals(file, TypeHandler.createFile("path/to/file"));
        assertEquals(file, TypeHandler.createFile("path/to/file/"));
        assertFalse(TypeHandler.createFile("path/to/file/").isDirectory());
        assertTrue(TypeHandler.createFile("path/to/directory").isDirectory());
        assertNull(TypeHandler.createFile(""));
        assertThrows(IOException.class, () -> TypeHandler.createFile("InvalidFileString"));
    }

    @Test
    public void testCreateFiles() {
        File file1 = new File("path/to/file1");
        File file2 = new File("path/to/file2");
        File[] files = {file1, file2};
        File[] result = TypeHandler.createFiles("path/to/file1,path/to/file2");
        assertArrayEquals(files, result);
        assertArrayEquals(new File[0], TypeHandler.createFiles(""));
        assertArrayEquals(new File[0], TypeHandler.createFiles("", File[].class));
        assertNull(TypeHandler.createFiles("", Object[].class));
        assertNull(TypeHandler.createFiles("path/to/nonExistentFile"));
    }
}