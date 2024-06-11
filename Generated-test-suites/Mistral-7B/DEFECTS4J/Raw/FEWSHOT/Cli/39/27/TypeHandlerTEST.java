package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
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

    @Test
    public void testCreateValueStringClass() {
        assertThrows(ParseException.class, () -> TypeHandler.createValue("foo", int.class));
        assertEquals(42, TypeHandler.createValue("42", int.class));
        assertEquals(42L, TypeHandler.createValue("42", long.class));
        assertEquals(42.0, TypeHandler.createValue("42", double.class));
        assertEquals(new Date(), TypeHandler.createValue("2022-01-01", Date.class));
        assertEquals(new File("test.txt"), TypeHandler.createValue("test.txt", File.class));
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue("test.txt", FileInputStream.class));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createValue("http://example.com", File.class));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createValue("http://example.com", URL.class));
        assertEquals(new URL("http://example.com"), TypeHandler.createValue("http://example.com", URL.class));
        assertNull(TypeHandler.createValue("", Class.forName("java.lang.String").asSubclass(Object.class)));
    }

    @Test
    public void testCreateObject() {
        assertInstanceOf(String.class.cast(TypeHandler.createValue("java.lang.String", String.class)), TypeHandler.createObject("java.lang.String"));
        assertThrows(ParseException.class, () -> TypeHandler.createObject("java.lang.String.StringBuilder"));
    }

    @Test
    public void testCreateNumber() {
        assertEquals(42, TypeHandler.createNumber("42"));
        assertEquals(42.0, TypeHandler.createNumber("42."));
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertNull(TypeHandler.createClass("java.lang.String.StringBuilder"));
    }

    @Test
    public void testCreateDate() {
        // To be implemented
    }

    @Test
    public void testCreateURL() {
        assertEquals(new URL("http://example.com"), TypeHandler.createURL("http://example.com"));
    }

    @Test
    public void testCreateFile() {
        File file = new File("test.txt");
        assertEquals(file, TypeHandler.createFile("test.txt"));
    }

    @Test
    public void testCreateFileInputStream() {
        File file = new File("test.txt");
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createFileInputStream("test.txt"));
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            assertEquals(fileInputStream, TypeHandler.createFileInputStream("test.txt"));
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
    }

    @Test
    public void testCreateFiles() {
        // To be implemented
    }
}