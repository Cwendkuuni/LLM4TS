package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateStringValue() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test
    public void testCreateObjectValueNull() {
        assertNull(TypeHandler.createValue(null, Object.class));
    }

    @Test
    public void testCreateObjectValueEmpty() {
        assertNull(TypeHandler.createValue("", Object.class));
    }

    @Test
    public void testCreateObjectValueClassNotFound() {
        assertNull(TypeHandler.createValue("com.test.MyClass", Object.class));
    }

    @Test
    public void testCreateObjectValueSuccess() {
        Class clazz = Class.forName("java.util.Date");
        Object obj = TypeHandler.createValue("2023-03-01", clazz);
        assertTrue(obj instanceof Date);
    }

    @Test
    public void testCreateNumberValueLong() {
        Long number = (Long) TypeHandler.createValue("123", Long.class);
        assertEquals(123L, number.longValue());
    }

    @Test
    public void testCreateNumberValueDouble() {
        Double number = (Double) TypeHandler.createValue("123.45", Double.class);
        assertEquals(123.45, number.doubleValue(), 0.0);
    }

    @Test
    public void testCreateNumberValueInvalid() {
        assertNull(TypeHandler.createValue("abc", Long.class));
    }

    @Test
    public void testCreateDateValueNull() {
        assertNull(TypeHandler.createValue(null, Date.class));
    }

    @Test
    public void testCreateDateValueEmpty() {
        assertNull(TypeHandler.createValue("", Date.class));
    }

    @Test
    public void testCreateDateValueInvalid() {
        assertNull(TypeHandler.createValue("2023-03-01-01:01:01", Date.class));
    }

    @Test
    public void testCreateDateValueSuccess() {
        Date date = (Date) TypeHandler.createValue("2023-03-01", Date.class);
        assertNotNull(date);
    }

    @Test
    public void testCreateURLValueNull() {
        assertNull(TypeHandler.createValue(null, URL.class));
    }

    @Test
    public void testCreateURLValueEmpty() {
        assertNull(TypeHandler.createValue("", URL.class));
    }

    @Test
    public void testCreateURLValueInvalid() {
        assertNull(TypeHandler.createValue("http://example.com:8080/asdf", URL.class));
    }

    @Test
    public void testCreateURLValueSuccess() {
        URL url = (URL) TypeHandler.createValue("http://example.com/", URL.class);
        assertNotNull(url);
    }

    @Test
    public void testCreateFileValueNull() {
        assertNull(TypeHandler.createValue(null, File.class));
    }

    @Test
    public void testCreateFileValueEmpty() {
        assertNull(TypeHandler.createValue("", File.class));
    }

    @Test
    public void testCreateFileValueFileNotFound() {
        assertNull(TypeHandler.createValue("/nonexistentfile.txt", File.class));
    }

    @Test
    public void testCreateFileValueSuccess() {
        File file = (File) TypeHandler.createValue("/testfile.txt", File.class);
        assertNotNull(file);
        assertTrue(file.exists());
    }

    @Test
    public void testCreateFilesValueNull() {
        assertNull(TypeHandler.createValue(null, File[].class));
    }

    @Test
    public void testCreateFilesValueEmpty() {
        assertNull(TypeHandler.createValue("", File[].class));
    }

    @Test
    public void testCreateFilesValueInvalid() {
        assertNull(TypeHandler.createValue("/testfile.txt, /testfile2.txt", File[].class));
    }

    @Test
    public void testCreateFilesValueSuccess() {
        File file1 = new File("/testfile1.txt");
        File file2 = new File("/testfile2.txt");
        File[] files = (File[]) TypeHandler.createValue(file1.getAbsolutePath() + "," + file2.getAbsolutePath(), File[].class);
        assertNotNull(files);
        assertEquals(2, files.length);
    }
}