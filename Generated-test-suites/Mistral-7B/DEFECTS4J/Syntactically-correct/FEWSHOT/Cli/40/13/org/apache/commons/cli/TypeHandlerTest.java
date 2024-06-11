package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringStringInvalid() {
        TypeHandler.createValue("invalid", String.class);
    }

    @Test
    public void testCreateValueClassClass() throws Exception {
        Class<?> clazz = Class.forName("java.lang.String");
        assertEquals("", (String) TypeHandler.createValue("", clazz));
    }

    @Test
    public void testCreateValueClassClassObject() throws Exception {
        Class<?> clazz = Class.forName("java.lang.String");
        assertEquals("foo", (String) TypeHandler.createValue("foo", clazz, "foo"));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueClassClassInvalid() {
        TypeHandler.createValue("invalid", Class.forName("java.lang.Object"));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueClassClassNull() {
        TypeHandler.createValue(null, Class.forName("java.lang.String"));
    }

    @Test
    public void testCreateValueObjectClass() throws Exception {
        Class<?> clazz = Class.forName("java.lang.String");
        assertEquals(new String("foo"), (String) TypeHandler.createValue("foo", clazz.asSubclass(Object.class)));
    }

    @Test(expected = ClassCastException.class)
    public void testCreateValueObjectClassMismatchedType() throws Exception {
        Class<?> clazz = Class.forName("java.lang.Integer");
        String value = "foo";
        TypeHandler.createValue(value, clazz.asSubclass(Object.class));
    }

    @Test
    public void testCreateValueNumberString() {
        long value = 42;
        assertEquals(value, TypeHandler.createValue("42", Long.TYPE));
        assertEquals(42.0, TypeHandler.createValue("42.0", Double.TYPE));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueNumberInvalid() {
        TypeHandler.createValue("invalid", Integer.TYPE);
    }

    @Test
    public void testCreateValueDateString() throws Exception {
        String dateString = "2022-03-23";
        Date date = new Date(2022, 2, 23);
        assertEquals(date, TypeHandler.createValue(dateString, Date.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueDateInvalidFormat() throws Exception {
        String dateString = "InvalidDateFormat";
        TypeHandler.createValue(dateString, Date.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDateEmpty() throws Exception {
        TypeHandler.createValue("", Date.class);
    }

    @Test
    public void testCreateValueURLString() throws Exception {
        String urlString = "http://example.com";
        URL url = new URL(urlString);
        assertEquals(url, TypeHandler.createValue(urlString, URL.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueURLInvalid() throws Exception {
        String urlString = "invalidURL";
        TypeHandler.createValue(urlString, URL.class);
    }

    @Test
    public void testCreateValueFileString() throws Exception {
        String filePath = "test/fixtures/testFile.txt";
        File expected = new File(filePath);
        assertEquals(expected, TypeHandler.createValue(filePath, File.class));
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueFileNotFound() throws Exception {
        String filePath = "test/nonExistentFile.txt";
        TypeHandler.createValue(filePath, File.class);
    }

    @Test(expected = IOException.class)
    public void testOpenFileInvalid() throws Exception {
        String invalidFilePath = "invalidFilePath";
        TypeHandler.openFile(invalidFilePath);
    }

    @Test
    public void testCreateValueFilesStringArray() throws Exception {
        String[] filePaths = {"test/fixtures/testFile1.txt", "test/fixtures/testFile2.txt"};
        File[] expectedFiles = new File[2];
        expectedFiles[0] = new File(filePaths[0]);
        expectedFiles[1] = new File(filePaths[1]);
        assertEquals(expectedFiles, (File[]) TypeHandler.createValue(filePaths, File[].class));
    }

    @Test
    public void testCreateValueURLsStringArray() throws Exception {
        String[] urlStrings = {"http://example1.com", "http://example2.com"};
        URL[] expectedURLs = new URL[2];
        expectedURLs[0] = new URL(urlStrings[0]);
        expectedURLs[1] = new URL(urlStrings[1]);
        assertEquals(expectedURLs, (URL[]) TypeHandler.createValue(urlStrings, URL[].class));
    }
}