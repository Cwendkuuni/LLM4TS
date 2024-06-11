package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        String str = "foo";
        assertEquals("foo", TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValueStringClassString() throws Exception {
        String str = "foo";
        Class<?> clazz = String.class;
        assertEquals("foo", TypeHandler.createValue(str, clazz));

        clazz = Integer.class;
        assertNull(TypeHandler.createValue(str, clazz));

        clazz = Double.class;
        assertEquals(Double.valueOf("3.14"), TypeHandler.createValue(str, clazz));

        clazz = Date.class;
        assertNull(TypeHandler.createValue(str, clazz));

        clazz = File.class;
        assertNull(TypeHandler.createValue(str, clazz));

        clazz = FileInputStream.class;
        assertNull(TypeHandler.createValue(str, clazz));

        clazz = URL.class;
        assertNull(TypeHandler.createValue(str, clazz));

        clazz = Object[].class;
        Object[] expected = new Object[]{"foo"};
        assertEquals(expected, TypeHandler.createValue(str, clazz.getComponentType()));

        clazz = Object[].class;
        assertNull(TypeHandler.createValue(str, clazz));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringClass_Date() {
        String str = "2022-01-01";
        TypeHandler.createValue(str, Date.class);
    }

    @Test
    public void testCreateObject() throws Exception {
        Class<?> clazz = String.class;
        Object object = TypeHandler.createObject(clazz.getName());
        assertEquals(clazz, object.getClass());

        clazz = MyClass.class;
        Object object2 = TypeHandler.createObject(clazz.getName());
        assertEquals(clazz, object2.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectUnknownClass() throws Exception {
        Class<?> clazz = String.class;
        TypeHandler.createObject(clazz.getName() + "Unknown");
    }

    @Test
    public void testCreateNumber() {
        assertEquals(Integer.valueOf(1), TypeHandler.createNumber("1"));
        assertEquals(Integer.valueOf(-1), TypeHandler.createNumber("-1"));
        assertEquals(Long.valueOf(1L), TypeHandler.createNumber("1"));
        assertEquals(Long.valueOf(-1L), TypeHandler.createNumber("-1"));
        assertEquals(Double.valueOf(0.5), TypeHandler.createNumber("0.5"));
        assertEquals(Double.valueOf(1.5), TypeHandler.createNumber("1.5"));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalid() {
        TypeHandler.createNumber("foo");
    }

    @Test
    public void testCreateClass() throws Exception {
        Class<?> clazz = String.class;
        Class<?> result = TypeHandler.createClass(clazz.getName());
        assertEquals(clazz, result);

        clazz = MyClass.class;
        result = TypeHandler.createClass(clazz.getName());
        assertEquals(clazz, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassUnknown() throws Exception {
        Class<?> clazz = String.class;
        TypeHandler.createClass(clazz.getName() + "Unknown");
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-01-01";
        Date expected = new Date(Long.parseLong("2022-01-01") * 24 * 60 * 60 * 1000L);
        assertEquals(expected, TypeHandler.createDate(dateString));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateInvalid() {
        TypeHandler.createDate("foo");
    }

    @Test
    public void testCreateURL() throws Exception {
        String urlString = "http://example.com";
        URL expected = new URL(urlString);
        assertEquals(expected, TypeHandler.createURL(urlString));
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalid() {
        TypeHandler.createURL("foo");
    }

    @Test
    public void testCreateFile() {
        String filePath = "path/to/file.txt";
        File expected = new File(filePath);
        assertEquals(expected, TypeHandler.createFile(filePath));
    }

    @Test(expected = ParseException.class)
    public void testCreateFileNull() {
        TypeHandler.createFile(null);
    }

    @Test
    public void testOpenFile() throws IOException {
        String filePath = "path/to/file.txt";
        File file = TypeHandler.createFile(filePath);
        FileInputStream expected = new FileInputStream(file);
        assertEquals(expected, TypeHandler.openFile(filePath));
    }

    @Test(expected = ParseException.class)
    public void testOpenFileNull() {
        TypeHandler.openFile(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileNonExistentFile() throws IOException {
        String filePath = "non-existent-file.txt";
        TypeHandler.openFile(filePath);
    }

    private static class MyClass {
        MyClass() {
        }
    }
}