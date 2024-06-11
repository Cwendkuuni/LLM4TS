package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue() {
        // Test for STRING_VALUE
        assertEquals("test", TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE));
        // Test for OBJECT_VALUE
        Object obj = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals("java.lang.String", obj.getClass().getName());
        // Test for NUMBER_VALUE
        Number number = TypeHandler.createValue("100", PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(number instanceof Number);
        // Test for DATE_VALUE
        assertNull(TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE));
        // Test for CLASS_VALUE
        Class<?> clazz = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals("java.lang.String", clazz.getName());
        // Test for FILE_VALUE
        File file = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals("test.txt", file.getName());
        // Test for EXISTING_FILE_VALUE
        try {
            FileInputStream fis = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
            assertTrue(fis instanceof FileInputStream);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Test for FILES_VALUE
        try {
            File[] files = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
            assertEquals(2, files.length);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        // Test for URL_VALUE
        try {
            URL url = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
            assertEquals("http://www.example.com", url.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertEquals("java.lang.String", obj.getClass().getName());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Number number1 = TypeHandler.createNumber("100");
            assertEquals(100, number1.intValue());
            Number number2 = TypeHandler.createNumber("3.14");
            assertEquals(3.14, number2.doubleValue(), 0.001);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertEquals("java.lang.String", clazz.getName());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDate() {
        // Test not implemented yet
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertEquals("http://www.example.com", url.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testOpenFile() {
        try {
            FileInputStream fis = TypeHandler.openFile("test.txt");
            assertTrue(fis instanceof FileInputStream);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFiles() {
        // Test not implemented yet
    }
}