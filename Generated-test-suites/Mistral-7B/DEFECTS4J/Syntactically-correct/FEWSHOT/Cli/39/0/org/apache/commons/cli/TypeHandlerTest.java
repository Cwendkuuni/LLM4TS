package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringType() {
        Object value = TypeHandler.createValue("foo", String.class);
        assertEquals("foo", value);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidStringType() {
        TypeHandler.createValue("foo", Integer.class);
    }

    @Test
    public void testCreateValueWithObjectType() {
        String className = "java.util.Date";
        Object value = TypeHandler.createValue("2022-12-31", Class.forName(className));
        assertInstanceOf(Date.class, value);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidObjectType() {
        String className = "java.util.Date";
        TypeHandler.createValue("foo", Integer.class);
    }

    @Test
    public void testCreateValueWithNumberType() {
        Object value = TypeHandler.createValue("42", Integer.class);
        assertEquals(42, value);

        Object value2 = TypeHandler.createValue("42.2", Double.class);
        assertEquals(42.2, value2, 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidNumberType() {
        TypeHandler.createValue("foo", Integer.class);
    }

    @Test
    public void testCreateValueWithDateType() {
        String dateString = "2022-12-31";
        Date date = TypeHandler.createValue(dateString, Date.class);
        assertEquals(dateString, new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithUnsupportedDateType() {
        TypeHandler.createValue("2022-12-31", String.class);
    }

    @Test
    public void testCreateValueWithURLType() {
        String urlString = "http://example.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertEquals(urlString, url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidURLType() {
        TypeHandler.createValue("foo", URL.class);
    }

    @Test
    public void testCreateValueWithFileType() {
        String filePath = "path/to/file.txt";
        File file = TypeHandler.createValue(filePath, File.class);
        assertTrue(file.exists());
        assertEquals(filePath, file.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidFileType() {
        Class<?> invalidFileType = Integer.class;
        TypeHandler.createValue("path/to/file.txt", invalidFileType);
    }

    @Test
    public void testCreateValueWithExistingFileValueType() {
        String filePath = "path/to/non-existent-file.txt";
        File file = TypeHandler.createValue(filePath, File.class);
        assertNull(file);
    }

    @Test
    public void testCreateValueWithFilesValueType() {
        String filePaths = "path/to/file1.txt, path/to/file2.txt";
        File[] files = TypeHandler.createValue(filePaths, File[].class);
        assertEquals(2, files.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithUnsupportedFilesValueType() {
        TypeHandler.createValue("path/to/file1.txt, path/to/file2.txt", String[].class);
    }
}