package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringStringInvalidClass() {
        TypeHandler.createValue("foo", int.class);
    }

    @Test
    public void testCreateValueStringClassString() {
        assertEquals("foo", TypeHandler.createValue("foo", TypeHandler.PatternOptionBuilder.STRING_VALUE));
        assertEquals(1L, TypeHandler.createValue("1", Long.TYPE));
        assertEquals(1.0D, TypeHandler.createValue("1.", Double.TYPE));

        assertInstanceOf(Date.class, TypeHandler.createValue("2023-01-01", Date.class));
        assertInstanceOf(File.class, TypeHandler.createValue("/path/to/file.txt", File.class));
        assertNull(TypeHandler.createValue(null, File.class));

        assertInstanceOf(URL.class, TypeHandler.createValue("http://example.com", URL.class));
        assertInstanceOf(FileInputStream.class, TypeHandler.createValue("/path/to/file.txt", FileInputStream.class));
        assertNull(TypeHandler.createValue(null, FileInputStream.class));
    }

    @Test
    public void testCreateObject() {
        try {
            assertEquals(new Object(), TypeHandler.createObject("Object"));
        } catch (ParseException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClass() {
        TypeHandler.createObject("Object");
    }

    @Test
    public void testCreateNumber() {
        assertEquals(1L, TypeHandler.createNumber("1"));
        assertEquals(1.0D, TypeHandler.createNumber("1."));
        assertNull(TypeHandler.createNumber(null));

        try {
            TypeHandler.createNumber("foo");
            fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateClass() {
        try {
            assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        } catch (ParseException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateClassInvalidClass() {
        TypeHandler.createClass("java.lang.String");
    }

    @Test
    public void testCreateDate() {
        // Unsupported Operation
    }

    @Test
    public void testCreateURL() {
        assertEquals(new URL("http://example.com"), TypeHandler.createURL("http://example.com"));
        assertNull(TypeHandler.createURL(null));

        try {
            TypeHandler.createURL("foo");
            fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFile() {
        File file = new File("/path/to/file.txt");
        assertEquals(file, TypeHandler.createFile("/path/to/file.txt"));
        assertNull(TypeHandler.createFile(null));
    }

    @Test(expected = ParseException.class)
    public void testOpenFileNull() {
        TypeHandler.openFile(null);
    }

    @Test
    public void testOpenFile() throws IOException {
        File file = new File("/path/to/file.txt");
        FileInputStream inputStream = new FileInputStream(file);
        FileInputStream fileInputStream = TypeHandler.openFile("/path/to/file.txt");

        assertEquals(inputStream, fileInputStream);
    }

    @Test(expected = ParseException.class)
    public void testCreateFilesNull() {
        TypeHandler.createFiles(null);
    }

    // Unsupported Operation
    // To implement/port: return FileW.findFiles(str);
    @Test
    public void testCreateFiles() {
        // Unsupported Operation
    }
}