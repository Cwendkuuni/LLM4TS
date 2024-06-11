package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        assertEquals("Test", TypeHandler.createValue("Test", String.class));
        assertNull(TypeHandler.createValue("123", String.class));
    }

    @Test
    public void testCreateValue_StringClass() {
        assertEquals("Test", TypeHandler.createValue("Test", String.class));
        assertNull(TypeHandler.createValue("123", String.class));

        assertEquals(123L, TypeHandler.createValue("123", Long.class));
        assertNull(TypeHandler.createValue("Test", Long.class));

        assertEquals(123.0, TypeHandler.createValue("123.", Double.class));
        assertNull(TypeHandler.createValue("123", Double.class));

        throwException(IOException.class, () -> TypeHandler.createValue("Test", File.class));

        assertNull(TypeHandler.createValue("Test", Date.class));
    }

    @Test
    public void testCreateObject() {
        assertEquals("Test", TypeHandler.createObject("TestStringClass").toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_NonExistentClass() {
        TypeHandler.createObject("NonExistentClass");
    }

    @Test
    public void testCreateNumber() {
        assertEquals(123L, TypeHandler.createNumber("123"));
        assertEquals(123.0, TypeHandler.createNumber("123."));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumber() {
        TypeHandler.createNumber("Test");
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertNull(TypeHandler.createClass("NonExistentClass"));
    }

    @Test(expected = ParseException.class)
    public void testCreateDate() {
        TypeHandler.createDate("Test");
    }

    @Test
    public void testCreateURL() {
        assertEquals("http://example.com", TypeHandler.createURL("http://example.com"));
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL() {
        TypeHandler.createURL("Test");
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("path/to/file.txt");
        assertNotNull(file);
        assertEquals("path/to/file.txt", file.getPath());
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_NonExistentFile() {
        TypeHandler.openFile("path/to/non-existent-file.txt");
    }

    private void throwException(Class<? extends Throwable> exceptionClass, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            if (exceptionClass.isInstance(e)) {
                return;
            }
            throw new RuntimeException("Unexpected exception", e);
        }
        throw new AssertionError("Expected exception not thrown");
    }
}