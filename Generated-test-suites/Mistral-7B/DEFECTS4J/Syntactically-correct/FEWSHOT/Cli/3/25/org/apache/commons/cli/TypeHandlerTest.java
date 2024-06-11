package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.net.URL;

import java.util.Date;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new String("test");
        String str = "test";
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(obj, result);
    }

    @Test
    public void testCreateValueStringClassString() {
        Class<String> clazz = String.class;
        String str = "test";
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassObject() {
        String str = "test";
        Class<String> clazz = String.class;
        Object obj = new String("test2");
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
        assertNotSame(obj, result);
    }

    @Test
    public void testCreateValueNullStringObject() {
        Object obj = new String("test");;
        String str = null;
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertNull(result);
    }

    @Test
    public void testCreateValueNullStringClass() {
        String str = null;
        Class<String> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringClassNoArgConstructorNotPresent() {
        String str = "test";
        Class<Date> clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringClassArgConstructorPresent() {
        String str = "java.util.Date";
        Class<Date> clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValueStringClassArgConstructorPresentWithArguments() {
        String str = "java.util.Date(1, 1, 2022)";
        Class<Date> clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertTrue(result instanceof Date);
        Date date = (Date) result;
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(2022, date.getYear());
    }

    @Test
    public void testCreateValueStringClassArgConstructorPresentWithInvalidArguments() {
        String str = "java.util.Date(1, 13, 2022)";
        Class<Date> clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringClassArgConstructorPresentWithInvalidClassName() {
        String str = "java.util.WrongClass(1, 1, 2022)";
        Class<Date> clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateNumber() {
        Object result = TypeHandler.createNumber("123");
        assertTrue(result instanceof Long);
        assertEquals(123L, ((Number) result).longValue());

        result = TypeHandler.createNumber("123.45");
        assertTrue(result instanceof Double);
        assertEquals(123.45, ((Number) result).doubleValue(), 0.001);
    }

    @Test
    public void testCreateNumberInvalid() {
        Object result = TypeHandler.createNumber("abcd");
        assertNull(result);
    }

    @Test
    public void testCreateClass() {
        Object result = TypeHandler.createClass("java.util.Date");
        assertTrue(result instanceof Class);
        assertEquals(Date.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateClassInvalid() {
        Object result = TypeHandler.createClass("java.util.WrongClass");
        assertNull(result);
    }

    @Test
    public void testCreateDate() {
        Object result = TypeHandler.createDate("2022-12-31");
        assertTrue(result instanceof Date);
        Date date = (Date) result;
        assertEquals(2022, date.getYear());
        assertEquals(12, date.getMonth());
        assertEquals(31, date.getDate());
    }

    @Test
    public void testCreateDateInvalid1() {
        Object result = TypeHandler.createDate("2022-13-31");
        assertNull(result);
    }

    @Test
    public void testCreateDateInvalid2() {
        Object result = TypeHandler.createDate("2022-12-32");
        assertNull(result);
    }

    @Test
    public void testCreateURL() {
        Object result = TypeHandler.createURL("http://example.com");
        assertTrue(result instanceof URL);
        URL url = (URL) result;
        assertEquals("http", url.getProtocol());
        assertEquals("example.com", url.getHost());
        assertEquals("/", url.getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateURLInvalid() {
        Object result = TypeHandler.createURL("http://example.com/foo/");
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        Object result = TypeHandler.createFile("/tmp/test.txt");
        assertTrue(result instanceof File);
        File file = (File) result;
        assertEquals("/tmp/test.txt", file.getAbsolutePath());
        assertTrue(file.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileNotFound() {
        Object result = TypeHandler.createFile("/tmp/non_existent.txt");
        assertNull(result);
    }

    @Test
    public void testCreateFilesNull() {
        Object result = TypeHandler.createFiles(null);
        assertNull(result);
    }

    @Test
    public void testCreateFilesEmptyString() {
        Object result = TypeHandler.createFiles("");
        assertArrayEquals(new File[0], (File[]) result);
    }

    @Test
    public void testCreateFilesSingleFile() {
        Object result = TypeHandler.createFiles("/tmp/test.txt");
        File[] files = (File[]) result;
        assertEquals(1, files.length);
        File file = files[0];
        assertEquals("/tmp/test.txt", file.getAbsolutePath());
        assertTrue(file.exists());
    }

    @Test
    public void testCreateFilesMultipleFiles() {
        Object result = TypeHandler.createFiles("/tmp/test1.txt, /tmp/test2.txt");
        File[] files = (File[]) result;
        assertEquals(2, files.length);
        File file1 = files[0];
        File file2 = files[1];
        assertEquals("/tmp/test1.txt", file1.getAbsolutePath());
        assertTrue(file1.exists());
        assertEquals("/tmp/test2.txt", file2.getAbsolutePath());
        assertTrue(file2.exists());
    }
}