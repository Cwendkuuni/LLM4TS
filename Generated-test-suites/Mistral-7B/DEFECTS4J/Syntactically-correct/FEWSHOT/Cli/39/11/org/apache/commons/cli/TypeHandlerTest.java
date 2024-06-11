package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueString() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueUnknownType() {
        TypeHandler.createValue("foo", Integer.class);
    }

    @Test
    public void testCreateValueClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = "java.util.Date";
        Class<?> clazz = TypeHandler.createValue(className, Class.class);
        assertEquals(Date.class, clazz);
        Date date = (Date) TypeHandler.createValue(className, Date.class);
        assertNotNull(date);
    }

    @Test
    public void testCreateValueNumber() throws ParseException {
        assertEquals(123L, TypeHandler.createValue("123", Long.TYPE));
        assertEquals(123.45, TypeHandler.createValue("123.45", Double.TYPE));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueNumberInvalid() throws ParseException {
        TypeHandler.createValue("foo", Double.TYPE);
    }

    @Test
    public void testCreateValueDate() throws ParseException {
        String dateString = "2022-12-31";
        Date date = (Date) TypeHandler.createValue(dateString, Date.class);
        assertNotNull(date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDateUnsupported() {
        TypeHandler.createValue("2022-12-31", Date.class);
    }

    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException {
        String urlString = "http://example.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertNotNull(url);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueURLInvalid() throws ParseException {
        TypeHandler.createValue("http://example.com/foo", URL.class);
    }

    @Test
    public void testCreateValueFile() {
        String filePath = "test.txt";
        File file = TypeHandler.createValue(filePath, File.class);
        assertNotNull(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueFileNotFound() {
        String filePath = "non-existent.txt";
        File file = TypeHandler.createValue(filePath, File.class);
        fail("Expected FileNotFoundException");
    }

    @Test(expected = ParseException.class)
    public void testCreateValueFileInvalidPath() {
        String filePath = "foo:";
        TypeHandler.createValue(filePath, File.class);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValueFileInputStream() throws IOException, FileNotFoundException {
        String filePath = "test.txt";
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        try {
            Object object = TypeHandler.createValue(filePath, FileInputStream.class);
            fail("Expected FileInputStream");
        } catch (ParseException e) {
            // Expected
        }
        assertEquals(inputStream, TypeHandler.createValue(filePath, FileInputStream.class));
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueFileInputStreamNotFound() throws IOException, FileNotFoundException {
        String filePath = "non-existent.txt";
        try {
            FileInputStream inputStream = (FileInputStream) TypeHandler.createValue(filePath, FileInputStream.class);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected
        }
    }
}