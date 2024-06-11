package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import java.util.Date;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateStringValue() {
        String str = "test";
        assertEquals(str, TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateObjectValueNull() {
        assertNull(TypeHandler.createValue(null, Object.class));
    }

    @Test
    public void testCreateObjectValueEmptyString() {
        assertNull(TypeHandler.createValue("", Object.class));
    }

    @Test
    public void testCreateObjectValueInvalidClassName() {
        assertNull(TypeHandler.createValue("java.util.BadClass", Object.class));
    }

    @Test
    public void testCreateObjectValueValidClassName() {
        String className = "java.lang.String";
        Class<?> clazz = ClassUtils.forName(className);
        Object obj = TypeHandler.createValue("test", clazz);
        assertNotNull(obj);
        assertEquals(clazz.getName(), obj.getClass().getName());
    }

    @Test
    public void testCreateNumberValueLong() {
        long l = 123;
        Number number = TypeHandler.createValue("123", Long.TYPE);
        assertEquals(l, number.longValue());
    }

    @Test
    public void testCreateNumberValueDouble() {
        double d = 123.45;
        Number number = TypeHandler.createValue("123.45", Double.TYPE);
        assertEquals(d, number.doubleValue(), 0.0);
    }

    @Test
    public void testCreateNumberValueInvalidNumber() {
        assertNull(TypeHandler.createValue("abc", Number.class));
    }

    @Test
    public void testCreateDateValueValidDateString() {
        String dateString = "2022-01-01";
        Date date = TypeHandler.createValue(dateString, Date.class);
        assertNotNull(date);
    }

    @Test
    public void testCreateDateValueInvalidDateString() {
        assertNull(TypeHandler.createValue("invalidDate", Date.class));
    }

    @Test
    public void testCreateURLValueValidURLString() {
        String urlString = "http://www.example.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertNotNull(url);
    }

    @Test
    public void testCreateURLValueInvalidURLString() {
        assertNull(TypeHandler.createValue("invalidURL", URL.class));
    }

    @Test
    public void testCreateFileValueValidFileString() {
        String filePath = "/tmp/test.txt";
        File file = new File(filePath);
        File file2 = TypeHandler.createValue(filePath, File.class);
        assertNotNull(file2);
        assertEquals(filePath, file2.getAbsolutePath());
    }

    @Test
    public void testCreateFileValueInvalidFileString() {
        assertNull(TypeHandler.createValue("invalidFile", File.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFileValueNull() {
        TypeHandler.createValue(null, File[].class);
    }

    @Test
    public void testCreateFilesValueValidFileStrings() {
        String[] filePaths = {"/tmp/test1.txt", "/tmp/test2.txt"};
        File[] files = TypeHandler.createValue(filePaths, File[].class);
        assertNotNull(files);
        assertEquals(filePaths.length, files.length);
        for (int i = 0; i < files.length; i++) {
            assertEquals(filePaths[i], files[i].getAbsolutePath());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFilesValueNull() {
        TypeHandler.createValue(null, File[].class);
    }
}