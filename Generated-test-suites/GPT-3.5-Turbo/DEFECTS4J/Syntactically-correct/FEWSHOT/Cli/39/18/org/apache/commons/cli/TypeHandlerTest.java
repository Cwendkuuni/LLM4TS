package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_StringObject() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
        assertEquals("bar", TypeHandler.createValue("bar", PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValue_StringClass() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
        assertEquals("bar", TypeHandler.createValue("bar", String.class));
    }

    @Test
    public void testCreateValue_Object() throws ParseException {
        Object obj = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(Object.class, obj.getClass());

        obj = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(String.class, obj.getClass());
    }

    @Test
    public void testCreateValue_Number() throws ParseException {
        assertEquals(10.5, TypeHandler.createValue("10.5", PatternOptionBuilder.NUMBER_VALUE));
        assertEquals(20, TypeHandler.createValue("20", PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_Number_Exception() throws ParseException {
        TypeHandler.createValue("ten", PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test
    public void testCreateValue_Date() {
        // not implemented yet
    }

    @Test
    public void testCreateValue_Class() throws ParseException {
        assertEquals(String.class, TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE));
        assertEquals(Integer.class, TypeHandler.createValue("java.lang.Integer", PatternOptionBuilder.CLASS_VALUE));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_Class_Exception() throws ParseException {
        TypeHandler.createValue("com.my.package.MyClass", PatternOptionBuilder.CLASS_VALUE);
    }

    @Test
    public void testCreateValue_File() {
        File file = TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals("path/to/file.txt", file.getPath());
    }

    @Test
    public void testCreateValue_ExistingFile() {
        File file = TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals("path/to/file.txt", file.getPath());
    }

    @Test
    public void testCreateValue_Files() {
        // not implemented yet
    }

    @Test
    public void testCreateValue_URL() throws ParseException, MalformedURLException {
        URL url = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_URL_Exception() throws ParseException {
        TypeHandler.createValue("htp://www.example.com", PatternOptionBuilder.URL_VALUE);
    }
}