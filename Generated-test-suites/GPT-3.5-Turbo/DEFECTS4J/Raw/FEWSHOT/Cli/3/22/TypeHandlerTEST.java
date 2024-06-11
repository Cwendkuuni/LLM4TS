package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue() {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
        assertNull(TypeHandler.createValue("bar", PatternOptionBuilder.OBJECT_VALUE));
        assertNotNull(TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE));
        assertNull(TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE));
        assertNotNull(TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE));
        assertNotNull(TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE));
        assertNotNull(TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE));
        assertNull(TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE));
        assertNotNull(TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE));
        assertNull(TypeHandler.createValue("invalid_type", "Invalid"));
    }

    @Test
    public void testCreateObject() {
        assertNotNull(TypeHandler.createObject("java.lang.String"));
        assertNull(TypeHandler.createObject("InvalidClass"));
    }

    @Test
    public void testCreateNumber() {
        assertEquals(NumberUtils.createNumber("123"), TypeHandler.createNumber("123"));
        assertNull(TypeHandler.createNumber("abc"));
    }

    @Test
    public void testCreateClass() {
        assertNotNull(TypeHandler.createClass("java.lang.String"));
        assertNull(TypeHandler.createClass("InvalidClass"));
    }

    @Test
    public void testCreateDate() {
        assertNull(TypeHandler.createDate("2021-01-01"));
    }

    @Test
    public void testCreateURL() {
        try {
            URL expected = new URL("http://www.example.com");
            assertEquals(expected, TypeHandler.createURL("http://www.example.com"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assertNull(TypeHandler.createURL("invalid_url"));
    }

    @Test
    public void testCreateFile() {
        File expected = new File("test.txt");
        assertEquals(expected, TypeHandler.createFile("test.txt"));
    }
    
    @Test
    public void testCreateFiles() {
        assertNull(TypeHandler.createFiles("file1.txt,file2.txt"));
    }
}