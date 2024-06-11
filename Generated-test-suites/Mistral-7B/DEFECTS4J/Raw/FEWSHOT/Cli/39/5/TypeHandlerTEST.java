package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueString() {
        assertEquals("testString", TypeHandler.createValue("testString", String.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidString() {
        TypeHandler.createValue("invalid", String.class);
    }

    @Test
    public void testCreateValueObject() {
        assertEquals(new Object(), TypeHandler.createValue("", Object.class));
    }

    @Test
    public void testCreateValueClass() {
        Class<?> clazz = TestClass.class;
        TestClass testObject = (TestClass) TypeHandler.createValue("TestClass", clazz);
        assertNotNull(testObject);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() {
        TypeHandler.createValue("TestClass", Integer.class);
    }

    @Test
    public void testCreateValueNumber() {
        assertEquals(123L, TypeHandler.createValue("123", Long.TYPE));
        assertEquals(123.45, TypeHandler.createValue("123.45", Double.TYPE));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidNumber() {
        TypeHandler.createValue("123a", Long.TYPE);
    }

    @Test
    public void testCreateValueDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = dateFormat.parse("2022-12-31");
        } catch (ParseException e) {
            fail("Failed to parse date: " + e.getMessage());
        }
        assertEquals(date, TypeHandler.createValue("2022-12-31", Date.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDateUnsupported() {
        TypeHandler.createValue("2022-12-31", String.class);
    }

    @Test
    public void testCreateValueURL() {
        String urlString = "http://test.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertNotNull(url);
        assertEquals(urlString, url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidURL() {
        TypeHandler.createValue("invalid:url", URL.class);
    }

    @Test
    public void testCreateValueFile() {
        File file = new File("test.txt");
        File fileResult = (File) TypeHandler.createValue("test.txt", File.class);
        assertNotNull(fileResult);
        assertEquals(file.getAbsolutePath(), fileResult.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueInvalidFile() {
        TypeHandler.createValue("nonExistentFile.txt", File.class);
    }

    @Test
    public void testCreateValueFileArray() {
        File[] files = new File[] { new File("file1.txt"), new File("file2.txt") };
        File[] filesResult = (File[]) TypeHandler.createValue("file1.txt file2.txt", File[].class);
        assertArrayEquals(files, filesResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFileArrayUnsupported() {
        TypeHandler.createValue("file1.txt file2.txt", Integer.TYPE);
    }
}

class TestClass {
}