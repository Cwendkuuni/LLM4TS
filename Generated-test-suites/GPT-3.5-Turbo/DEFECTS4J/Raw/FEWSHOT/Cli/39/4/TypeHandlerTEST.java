package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValueWithString() throws Exception {
        String value = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        assertEquals(value, TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithObject() throws Exception {
        String value = "org.apache.commons.cli.TypeHandlerTest$TestObject";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        assertEquals(value, TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithNumber() throws Exception {
        String value = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        assertEquals(10L, TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithDate() throws Exception {
        String value = "2019-05-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        assertNull(TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithClass() throws Exception {
        String value = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        assertEquals(String.class, TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithFile() throws Exception {
        String value = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        assertEquals(new File(value), TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithExistingFile() throws Exception {
        String value = "/path/to/existing_file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        assertEquals(new File(value), TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithFiles() throws Exception {
        String value = "/path/to/files";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        assertNull(TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithURL() throws Exception {
        String value = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        assertEquals(new URL(value), TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateValueWithInvalidType() throws Exception {
        String value = "invalid";
        Object obj = null;
        assertNull(TypeHandler.createValue(value, obj));
    }
    
    @Test
    public void testCreateObject() throws Exception {
        String classname = "org.apache.commons.cli.TypeHandlerTest$TestObject";
        assertEquals(new TestObject(), TypeHandler.createObject(classname));
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClass() throws Exception {
        String classname = "invalid.Class";
        TypeHandler.createObject(classname);
    }
    
    @Test
    public void testCreateNumberWithLong() throws Exception {
        String value = "10";
        assertEquals(10L, TypeHandler.createNumber(value));
    }
    
    @Test
    public void testCreateNumberWithDouble() throws Exception {
        String value = "10.5";
        assertEquals(10.5, TypeHandler.createNumber(value));
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws Exception {
        String value = "invalid";
        TypeHandler.createNumber(value);
    }
    
    @Test
    public void testCreateClass() throws Exception {
        String classname = "java.lang.String";
        assertEquals(String.class, TypeHandler.createClass(classname));
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClass() throws Exception {
        String classname = "invalid.Class";
        TypeHandler.createClass(classname);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws Exception {
        String value = "2019-05-01";
        TypeHandler.createDate(value);
    }
    
    @Test
    public void testCreateURL() throws Exception {
        String value = "http://www.example.com";
        assertEquals(new URL(value), TypeHandler.createURL(value));
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws Exception {
        String value = "invalid_url";
        TypeHandler.createURL(value);
    }
    
    @Test
    public void testCreateFile() throws Exception {
        String value = "/path/to/file.txt";
        assertEquals(new File(value), TypeHandler.createFile(value));
    }
    
    @Test
    public void testCreateFiles() throws Exception {
        String value = "/path/to/files";
        assertNull(TypeHandler.createFiles(value));
    }
    
    public static class TestObject {}
}