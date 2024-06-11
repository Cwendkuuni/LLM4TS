package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_string() throws ParseException {
        String str = "hello";
        String result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValue_object() throws ParseException {
        String str = "java.lang.Object";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }
    
    @Test
    public void testCreateValue_number() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertEquals(Long.class, result.getClass());
        assertEquals(123L, result.longValue());
        
        str = "3.14";
        result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertEquals(Double.class, result.getClass());
        assertEquals(3.14, result.doubleValue(), 0.0001);
    }
    
    @Test
    public void testCreateValue_date() throws ParseException {
        String str = "2020-01-01";
        Date result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertNull(result);
    }
    
    @Test
    public void testCreateValue_class() throws ParseException {
        String str = "java.lang.String";
        Class<?> result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateValue_file() throws ParseException {
        String str = "test.txt";
        File result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertNotNull(result);
        assertEquals("test.txt", result.getName());
    }
    
    @Test
    public void testCreateValue_existingFile() throws ParseException {
        String str = "test.txt";
        FileInputStream result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(result);
        assertEquals("test.txt", result.getFileName());
    }
    
    @Test
    public void testCreateValue_files() throws ParseException {
        String str = "test.txt,test2.txt";
        File[] result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("test.txt", result[0].getName());
        assertEquals("test2.txt", result[1].getName());
    }
    
    @Test
    public void testCreateValue_url() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertEquals("http://www.example.com", result.toString());
    }
    
    @Test
    public void testCreateValue_invalidCase() throws ParseException {
        String str = "invalid";
        Object result = TypeHandler.createValue(str, InvalidClass.class);
        assertNull(result);
    }
    
    private static class InvalidClass {
        
    }
}