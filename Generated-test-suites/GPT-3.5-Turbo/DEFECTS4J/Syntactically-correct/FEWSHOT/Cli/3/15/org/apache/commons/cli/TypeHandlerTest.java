package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue() {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
        assertEquals(123, TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE));
        assertEquals(new Date(), TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE));
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.OBJECT_VALUE));
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.CLASS_VALUE));
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.FILE_VALUE));
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.EXISTING_FILE_VALUE));
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.FILES_VALUE));
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.URL_VALUE));
    }

    @Test
    public void testCreateObject() {
        assertEquals(new Object(), TypeHandler.createObject("java.lang.Object"));
        assertNull(TypeHandler.createObject("foo"));
    }

    @Test
    public void testCreateNumber() {
        assertEquals(123, TypeHandler.createNumber("123"));
        assertEquals(1.23, TypeHandler.createNumber("1.23"));
        assertNull(TypeHandler.createNumber("foo"));
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertNull(TypeHandler.createClass("foo"));
    }

    @Test
    public void testCreateDate() {
        assertEquals(new Date(), TypeHandler.createDate("2020-01-01"));
    }

    @Test
    public void testCreateURL() {
        assertEquals(new URL("http://www.example.com"), TypeHandler.createURL("http://www.example.com"));
        assertNull(TypeHandler.createURL("foo"));
    }

    @Test
    public void testCreateFile() {
        assertEquals(new File("test.txt"), TypeHandler.createFile("test.txt"));
        assertNull(TypeHandler.createFile("foo"));
    }

    @Test
    public void testCreateFiles() {
        assertNull(TypeHandler.createFiles("foo"));
    }
}