package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest {
    @Test
    public void testCreateValue() {
        try {
            TypeHandler.createValue("testString", String.class);
            TypeHandler.createValue("testInt", Integer.class);
            TypeHandler.createValue("testDouble", Double.class);
            TypeHandler.createValue("testDate", Date.class);
            TypeHandler.createValue("testClass", Class.class);
            TypeHandler.createValue("testFile", File.class);
            TypeHandler.createValue("testURL", URL.class);
            assertNotNull(TypeHandler.createValue("", Object.class));
        } catch (ParseException e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testCreateObject() {
        try {
            TypeHandler.createObject("org.apache.commons.cli.TestClass");
            TypeHandler.createObject("org.apache.commons.cli.NonExistentClass");
        } catch (ParseException e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            assertEquals(TypeHandler.createNumber("0"), 0);
            assertEquals(TypeHandler.createNumber("3.14"), 3.14);
            assertEquals(TypeHandler.createNumber("-5.67"), -5.67);
        } catch (ParseException e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testCreateClass() {
        try {
            assertEquals(TypeHandler.createClass("java.lang.String"), String.class);
            assertEquals(TypeHandler.createClass("java.util.ArrayList"), ArrayList.class);
        } catch (ParseException e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testCreateDate() {
        try {
            assertNotNull(TypeHandler.createDate("2020-01-01"));
        } catch (UnsupportedOperationException e) {
            assertNull(e);
        }
    }

    @Test
    public void testCreateURL() {
        try {
            assertNotNull(TypeHandler.createURL("https://www.google.com"));
            assertNotNull(TypeHandler.createURL("file:///path/to/file"));
        } catch (ParseException e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testCreateFile() {
        assertNotNull(TypeHandler.createFile("path/to/file"));
    }

    @Test
    public void testCreateFiles() {
        try {
            assertNotNull(TypeHandler.createFiles("path/to/files"));
        } catch (UnsupportedOperationException e) {
            assertNull(e);
        }
    }
}