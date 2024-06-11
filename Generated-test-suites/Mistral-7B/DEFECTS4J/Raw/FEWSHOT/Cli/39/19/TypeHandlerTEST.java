package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
    public void testCreateValue_StringObject() {
        String str = "foo";
        Object obj = new Object();

        // String value
        assertEquals(str, TypeHandler.createValue(str, Object.class));
        assertInstanceOf(String.class, TypeHandler.createValue(str, String.class));

        // Object value
        assertNull(TypeHandler.createValue(str, Object[].class));

        // Other types
        assertNull(TypeHandler.createValue(str, int.class));
        assertNull(TypeHandler.createValue(str, double.class));
        assertNull(TypeHandler.createValue(str, Date.class));
        assertNull(TypeHandler.createValue(str, File.class));
        assertNull(TypeHandler.createValue(str, FileInputStream.class));
        assertNull(TypeHandler.createValue(str, URL.class));
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "foo";

        // String value
        assertEquals(str, TypeHandler.createValue(str, String.class));

        // Object value
        assertNull(TypeHandler.createValue(str, Object[].class));

        // Other types
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, int.class));
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, double.class));
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, Date.class));
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue(str, FileInputStream.class));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createValue(str, URL.class));
        assertNull(TypeHandler.createValue(str, File.class));
    }

    @Test
    public void testCreateObject() {
        String classname = "java.util.Date";

        // Create class
        assertInstanceOf(Date.class, TypeHandler.createObject(classname));
    }

    @Test
    public void testCreateNumber() {
        String str = "123";

        // Long value
        assertEquals(123L, TypeHandler.createNumber(str).longValue());

        // Double value
        assertEquals(123.0, TypeHandler.createNumber(str + ".").doubleValue(), 0.0);
    }

    @Test
    public void testCreateDate() {
        // UnsupportedOperationException is expected
        assertThrows(UnsupportedOperationException.class, TypeHandler::createDate);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";

        // Create URL
        URL url = TypeHandler.createURL(urlString);
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testCreateFile() {
        String filePath = "/path/to/file.txt";

        // Create File
        File file = TypeHandler.createFile(filePath);
        assertEquals(filePath, file.getAbsolutePath());
    }

    @Test
    public void testCreateFileInputStream() {
        String filePath = "/path/to/file.txt";

        // Create FileInputStream
        File file = new File(filePath);
        FileInputStream inputStream = TypeHandler.createFileInputStream(filePath);

        // Check if the created input stream is the same as the one created from the file
        assertEquals(file.getAbsoluteFile(), inputStream.getFileDescriptor().getFileDescriptor());
    }

    @Test
    public void testCreateFiles() {
        String filePath = "/path/to/files/*.txt";

        // UnsupportedOperationException is expected
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(filePath));
    }
}