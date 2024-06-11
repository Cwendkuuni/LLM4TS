package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_String_Object() {
        try {
            assertEquals("hello", TypeHandler.createValue("hello", String.class));
            assertNull(TypeHandler.createValue(null, String.class));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValue_String_Class() {
        try {
            assertEquals("hello", TypeHandler.createValue("hello", String.class));
            assertNull(TypeHandler.createValue(null, String.class));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateObject() {
        try {
            assertEquals(String.class, TypeHandler.createObject("java.lang.String").getClass());
            assertNull(TypeHandler.createObject("invalid.class.name"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            assertEquals(10L, TypeHandler.createNumber("10").longValue());
            assertEquals(3.14, TypeHandler.createNumber("3.14").doubleValue(), 0.001);
            assertNull(TypeHandler.createNumber("not_a_number"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateClass() {
        try {
            assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
            assertNull(TypeHandler.createClass("invalid.class.name"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDate() {
        try {
            assertNull(TypeHandler.createDate("2020-01-01"));
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateURL() {
        try {
            assertEquals(new URL("http://www.example.com"), TypeHandler.createURL("http://www.example.com"));
            assertNull(TypeHandler.createURL("invalid/url"));
        } catch (ParseException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFile() {
        assertEquals(new File("path/to/file"), TypeHandler.createFile("path/to/file"));
    }

    @Test
    public void testOpenFile() {
        try {
            assertEquals(new FileInputStream("path/to/file"), TypeHandler.openFile("path/to/file"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            assertNull(TypeHandler.createFiles("path/to/file"));
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }
}