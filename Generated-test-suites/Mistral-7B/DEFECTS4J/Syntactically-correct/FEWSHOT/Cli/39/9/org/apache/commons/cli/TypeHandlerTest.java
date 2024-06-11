package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringAndStringClass_ReturnsString() {
        String str = "testString";
        String expected = "testString";

        String result = (String) TypeHandler.createValue(str, String.class);

        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_StringAndClassObjectClass_ReturnsNull() {
        String str = "testString";

        assertNull(TypeHandler.createValue(str, Object.class));
    }

    @Test
    public void testCreateValue_StringAndClassStringClass_ReturnsString() {
        String str = "testString";

        String result = (String) TypeHandler.createValue(str, String[].class);

        assertNull(result);
    }

    @Test
    public void testCreateValue_StringAndClassClass_ThrowsParseException() {
        String str = "testString";

        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, int.class));
    }

    @Test
    public void testCreateValue_StringAndClassNumber_ReturnsLong() throws NumberFormatException {
        String str = "123";
        long expected = 123;

        Long result = (Long) TypeHandler.createValue(str, Number.class);

        assertEquals(expected, result.longValue());
    }

    @Test
    public void testCreateValue_StringAndClassNumberDouble_ReturnsDouble() throws NumberFormatException {
        String str = "123.45";
        double expected = 123.45;

        Double result = (Double) TypeHandler.createValue(str, Number.class);

        assertEquals(expected, result.doubleValue(), 0.01);
    }

    @Test
    public void testCreateValue_StringAndClassClass_ThrowsParseException() {
        String className = "java.lang.String";

        assertThrows(ParseException.class, () -> TypeHandler.createValue(className, String.class));
    }

    @Test
    public void testCreateValue_StringAndClassClassWithException() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = "NonExistentClass";

        assertThrows(ParseException.class, () -> TypeHandler.createValue(className, String.class));
    }

    @Test
    public void testCreateValue_StringAndClassDate_ThrowsUnsupportedOperationException() {
        String str = "2023-03-21";

        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, Date.class));
    }

    @Test
    public void testCreateValue_StringAndClassURL_ReturnsURL() throws MalformedURLException {
        String urlString = "https://www.example.com";
        URL expectedURL = new URL(urlString);

        URL resultURL = (URL) TypeHandler.createValue(urlString, URL.class);

        assertEquals(expectedURL.toString(), resultURL.toString());
    }

    @Test
    public void testCreateValue_StringAndClassFile_ReturnsFile() {
        String filePath = "path/to/file.txt";
        File expectedFile = new File(filePath);

        File resultFile = (File) TypeHandler.createValue(filePath, File.class);

        assertEquals(expectedFile.getAbsolutePath(), resultFile.getAbsolutePath());
    }

    @Test
    public void testCreateValue_StringAndClassFileInputStream_ThrowsParseException() {
        String filePath = "path/to/non-existent-file.txt";

        assertThrows(ParseException.class, () -> TypeHandler.createValue(filePath, FileInputStream.class));
    }

    @Test
    public void testCreateValue_StringAndClassFileArray_ThrowsUnsupportedOperationException() {
        String filePaths = "path/to/file1.txt, path/to/file2.txt";

        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(filePaths, File[].class));
    }
}