package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_String_Object() throws ParseException {
        Object result = TypeHandler.createValue("foo", "java.lang.String");
        assertEquals("foo", result);

        result = TypeHandler.createValue("123", "java.lang.Integer");
        assertEquals(123, result);

        result = TypeHandler.createValue("12.34", "java.lang.Double");
        assertEquals(12.34, result);

        result = TypeHandler.createValue("true", "java.lang.Boolean");
        assertEquals(true, result);

        result = TypeHandler.createValue("2021-01-01", "java.util.Date");
        assertNotNull(result);

        result = TypeHandler.createValue("org.apache.commons.cli.TypeHandler", "java.lang.Class");
        assertNotNull(result);

        result = TypeHandler.createValue("file.txt", "java.io.File");
        assertNotNull(result);

        result = TypeHandler.createValue("http://www.example.com", "java.net.URL");
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        Object result = TypeHandler.createValue("foo", java.lang.String.class);
        assertEquals("foo", result);

        result = TypeHandler.createValue("123", java.lang.Integer.class);
        assertEquals(123, result);

        result = TypeHandler.createValue("12.34", java.lang.Double.class);
        assertEquals(12.34, result);

        result = TypeHandler.createValue("true", java.lang.Boolean.class);
        assertEquals(true, result);

        result = TypeHandler.createValue("2021-01-01", java.util.Date.class);
        assertNotNull(result);

        result = TypeHandler.createValue("org.apache.commons.cli.TypeHandler", java.lang.Class.class);
        assertNotNull(result);

        result = TypeHandler.createValue("file.txt", java.io.File.class);
        assertNotNull(result);

        result = TypeHandler.createValue("http://www.example.com", java.net.URL.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);

        result = TypeHandler.createObject("java.lang.Integer");
        assertNotNull(result);

        result = TypeHandler.createObject("java.lang.Double");
        assertNotNull(result);

        result = TypeHandler.createObject("java.lang.Boolean");
        assertNotNull(result);

        result = TypeHandler.createObject("java.util.Date");
        assertNotNull(result);

        result = TypeHandler.createObject("org.apache.commons.cli.TypeHandler");
        assertNotNull(result);

        result = TypeHandler.createObject("java.io.File");
        assertNotNull(result);

        result = TypeHandler.createObject("java.net.URL");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFoundException() throws ParseException {
        TypeHandler.createObject("com.example.NonExistentClass");
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_Exception() throws ParseException {
        TypeHandler.createObject("java.lang.Object");
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Number result = TypeHandler.createNumber("123");
        assertEquals(123, result);

        result = TypeHandler.createNumber("12.34");
        assertEquals(12.34, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_Exception() throws ParseException {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);

        result = TypeHandler.createClass("java.lang.Integer");
        assertEquals(Integer.class, result);

        result = TypeHandler.createClass("java.lang.Double");
        assertEquals(Double.class, result);

        result = TypeHandler.createClass("java.lang.Boolean");
        assertEquals(Boolean.class, result);

        result = TypeHandler.createClass("java.util.Date");
        assertEquals(Date.class, result);

        result = TypeHandler.createClass("org.apache.commons.cli.TypeHandler");
        assertEquals(TypeHandler.class, result);

        result = TypeHandler.createClass("java.io.File");
        assertEquals(File.class, result);

        result = TypeHandler.createClass("java.net.URL");
        assertEquals(URL.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_Exception() throws ParseException {
        TypeHandler.createClass("com.example.NonExistentClass");
    }

    @Test
    public void testCreateDate() {
        Date result = TypeHandler.createDate("2021-01-01");
        assertNotNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_Exception() throws ParseException {
        TypeHandler.createURL("example.com");
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("file.txt");
        assertNotNull(result);
    }

    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("dir1/file1.txt,dir2/file2.txt");
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_Exception() {
        TypeHandler.createFiles("dir1/file1.txt,dir2/file2.txt");
    }
}