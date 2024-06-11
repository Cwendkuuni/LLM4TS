package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void testCreateValue_String_String() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_String_InvalidType() {
        TypeHandler.createValue("foo", int.class);
    }

    @Test
    public void testCreateValue_String_Class_StringType() {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValue_String_Class_ObjectType() {
        try {
            Object obj = TypeHandler.createValue("java.lang.String", Object.class);
            assertEquals("java.lang.String", obj.getClass().getName());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValue_String_Class_NumberType() {
        assertEquals(123, TypeHandler.createValue("123", int.class).intValue());
        assertEquals(123.45, TypeHandler.createValue("123.45", Double.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_NumberType_InvalidNumber() {
        TypeHandler.createValue("foo", int.class);
    }

    @Test
    public void testCreateValue_String_Class_DateType() {
        Date date = TypeHandler.createValue("2022-12-31", Date.class);
        assertTrue(date.after(new Date()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_String_Class_DateType_Unsupported() {
        TypeHandler.createValue("2022-12-31", String.class);
    }

    @Test
    public void testCreateValue_String_Class_ClassType() {
        try {
            Class<?> clazz = TypeHandler.createValue("java.lang.String", Class.class);
            assertEquals("java.lang.String", clazz.getName());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValue_String_Class_URLType() {
        URL url = TypeHandler.createValue("http://example.com", URL.class);
        assertEquals("http", url.getProtocol());
        assertEquals("/example.com", url.getFile());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValue_String_Class_URLType_InvalidURL() {
        TypeHandler.createValue("foo:bar", URL.class);
    }

    @Test
    public void testCreateValue_String_Class_FileType() {
        File file = TypeHandler.createValue("test.txt", File.class);
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_String_Class_FileType_NonExistentFile() {
        TypeHandler.createValue("nonexistent.txt", File.class);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_FileType_InvalidFile() {
        TypeHandler.createValue("/etc/passwd", String[].class);
    }

    @Test
    public void testCreateValue_String_Class_FileArrayType() {
        File[] files = TypeHandler.createValue("test.txt,test2.txt", File[].class);
        assertEquals(2, files.length);
        assertTrue(new File(files[0]).exists());
        assertTrue(new File(files[1]).exists());
    }
}