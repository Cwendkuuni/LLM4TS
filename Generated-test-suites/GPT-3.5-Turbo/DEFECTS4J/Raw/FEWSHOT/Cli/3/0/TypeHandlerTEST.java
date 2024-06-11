package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        // Testing STRING_VALUE
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);

        // Testing OBJECT_VALUE
        result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(null, result);

        // Testing NUMBER_VALUE
        result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(NumberUtils.createNumber("10"), result);

        // Testing DATE_VALUE
        result = TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
        assertEquals(null, result);

        // Testing CLASS_VALUE
        result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, result);

        // Testing FILE_VALUE
        result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals(new File("test.txt"), result);

        // Testing EXISTING_FILE_VALUE
        result = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(new File("test.txt"), result);

        // Testing FILES_VALUE
        result = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertEquals(null, result);

        // Testing URL_VALUE
        result = TypeHandler.createValue("http://www.google.com", PatternOptionBuilder.URL_VALUE);
        try {
            assertEquals(new URL("http://www.google.com"), result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValue_String_Class() {
        // Testing STRING_VALUE
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);

        // Testing OBJECT_VALUE
        result = TypeHandler.createValue("test", Object.class);
        assertEquals(null, result);

        // Testing NUMBER_VALUE
        result = TypeHandler.createValue("10", Number.class);
        assertEquals(NumberUtils.createNumber("10"), result);

        // Testing DATE_VALUE
        result = TypeHandler.createValue("2020-01-01", Date.class);
        assertEquals(null, result);

        // Testing CLASS_VALUE
        result = TypeHandler.createValue("java.lang.String", Class.class);
        assertEquals(String.class, result);

        // Testing FILE_VALUE
        result = TypeHandler.createValue("test.txt", File.class);
        assertEquals(new File("test.txt"), result);

        // Testing EXISTING_FILE_VALUE
        result = TypeHandler.createValue("test.txt", File.class);
        assertEquals(new File("test.txt"), result);

        // Testing FILES_VALUE
        result = TypeHandler.createValue("file1.txt,file2.txt", File[].class);
        assertEquals(null, result);

        // Testing URL_VALUE
        result = TypeHandler.createValue("http://www.google.com", URL.class);
        try {
            assertEquals(new URL("http://www.google.com"), result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateObject() {
        // Testing existing class
        Object result = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, result);

        // Testing non-existing class
        result = TypeHandler.createObject("non.existing.Class");
        assertEquals(null, result);
    }

    @Test
    public void testCreateNumber() {
        // Testing valid number
        Number result = TypeHandler.createNumber("10");
        assertEquals(NumberUtils.createNumber("10"), result);

        // Testing invalid number
        result = TypeHandler.createNumber("nonumber");
        assertEquals(null, result);
    }

    @Test
    public void testCreateClass() {
        // Testing existing class
        Class result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);

        // Testing non-existing class
        result = TypeHandler.createClass("non.existing.Class");
        assertEquals(null, result);
    }

    @Test
    public void testCreateDate() {
        // Testing valid date
        Date result = TypeHandler.createDate("2020-01-01");
        assertEquals(null, result);
    }

    @Test
    public void testCreateURL() {
        // Testing valid URL
        URL result = TypeHandler.createURL("http://www.google.com");
        try {
            assertEquals(new URL("http://www.google.com"), result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Testing invalid URL
        result = TypeHandler.createURL("invalidurl");
        assertEquals(null, result);
    }

    @Test
    public void testCreateFile() {
        // Testing valid file
        File result = TypeHandler.createFile("test.txt");
        assertEquals(new File("test.txt"), result);
    }

    @Test
    public void testCreateFiles() {
        // Testing valid file
        File[] result = TypeHandler.createFiles("file1.txt,file2.txt");
        assertEquals(null, result);
    }
}