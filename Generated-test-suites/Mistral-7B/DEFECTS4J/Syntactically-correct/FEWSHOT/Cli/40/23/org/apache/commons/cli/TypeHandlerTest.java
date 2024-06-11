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
    public void testCreateValue_StringString() {
       assertEquals("string value", createValue("string value", String.class));
        assertNull(createValue("null", String.class));
        assertNull(createValue("", String.class));
        assertNull(createValue(null, String.class));

        try {
            createValue("123", String.class);
            fail();
        } catch (ParseException e) {}

        try {
            createValue("123.45", String.class);
            fail();
        } catch (ParseException e) {}

        try {
            createValue("true", String.class);
            fail();
        } catch (ParseException e) {}

        try {
            createValue("false", String.class);
            fail();
        } catch (ParseException e) {}
    }

    @Test
    public void testCreateValue_StringClass() {
        assertEquals("string value", createValue("string value", String.class));
        assertEquals((Date) new Date("2022-01-01"), createValue("2022-01-01", Date.class));
        assertEquals(123, createValue("123", int.class));
        assertEquals(123L, createValue("123", long.class));
        assertEquals(123.0, createValue("123", double.class));
        assertEquals(true, createValue("true", boolean.class));
        assertEquals(false, createValue("false", boolean.class));

        try {
            createValue("foo", int.class);
            fail();
        } catch (ParseException e) {}

        try {
            createValue("123.45", int.class);
            fail();
        } catch (ParseException e) {}

        try {
            createValue("123", String.class);
            fail();
        } catch (ParseException e) {}
    }

    @Test
    public void testCreateObject() {
        assertEquals(new Object(), createObject("Object"));

        try {
            createObject("NonExistentClass");
            fail();
        } catch (ParseException e) {}
    }

    @Test
    public void testCreateNumber() {
        assertEquals(123L, createNumber("123"));
        assertEquals(123.0, createNumber("123.45"));

        try {
            createNumber("foo");
            fail();
        } catch (ParseException e) {}
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, createClass("java.lang.String"));

        try {
            createClass("NonExistentClass");
            fail();
        } catch (ParseException e) {}
    }

    @Test
    public void testCreateDate() {
        // Not yet implemented
    }

    @Test
    public void testCreateURL() {
        URL url = new URL("http://example.com");
        assertEquals(url, createValue("http://example.com", URL.class));

        try {
            createValue("http://example.com/foo", URL.class);
            fail();
        } catch (ParseException e) {}
    }

    @Test
    public void testCreateFile() {
        File file = new File("test.txt");
        assertEquals(file, createValue("test.txt", File.class));

        try {
            createValue("/test.txt", File.class);
            fail();
        } catch (ParseException e) {}
    }

    @Test
    public void testOpenFile() throws IOException {
        File file = new File("test.txt");
        FileInputStream expected = new FileInputStream(file);

        assertEquals(expected, openFile("test.txt"));

        try {
            openFile("non-existent-file.txt");
            fail();
        } catch (ParseException e) {}
    }

    @Test
    public void testCreateFiles() {
        // Not yet implemented
    }
}