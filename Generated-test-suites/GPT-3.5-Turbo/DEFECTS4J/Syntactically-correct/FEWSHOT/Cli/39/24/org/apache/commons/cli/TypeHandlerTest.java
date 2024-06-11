package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        // Test with a string value
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
        // Test with an object value
        assertEquals(new Object(), TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE));
        // Test with a number value
        assertEquals(10L, TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE));
        // Test with a date value
        assertNull(TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE));
        // Test with a class value
        assertEquals(String.class, TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE));
        // Test with a file value
        assertEquals(new File("test.txt"), TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE));
        // Test with an existing file value
        assertEquals(new File("test.txt"), TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE));
        // Test with files value
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        });
        // Test with a URL value
        assertEquals(new URL("http://www.google.com"), TypeHandler.createValue("http://www.google.com", PatternOptionBuilder.URL_VALUE));
        // Test with an unknown value
        assertNull(TypeHandler.createValue("unknown", PatternOptionBuilder.class));
    }

    @Test
    public void testCreateObject() {
        assertEquals(new Object(), TypeHandler.createObject("java.lang.Object"));
        assertThrows(ParseException.class, () -> {
            TypeHandler.createObject("unknown");
        });
    }

    @Test
    public void testCreateNumber() {
        // Test with a decimal number
        assertEquals(Double.valueOf(10.0), TypeHandler.createNumber("10.0"));
        // Test with an integer number
        assertEquals(Long.valueOf(10L), TypeHandler.createNumber("10"));
        // Test with an invalid number
        assertThrows(ParseException.class, () -> {
            TypeHandler.createNumber("invalid");
        });
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertThrows(ParseException.class, () -> {
            TypeHandler.createClass("unknown");
        });
    }

    @Test
    public void testCreateDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createDate("2020-01-01");
        });
    }

    @Test
    public void testCreateURL() {
        assertEquals(new URL("http://www.google.com"), TypeHandler.createURL("http://www.google.com"));
        assertThrows(ParseException.class, () -> {
            TypeHandler.createURL("invalid");
        });
    }

    @Test
    public void testCreateFile() {
        assertEquals(new File("test.txt"), TypeHandler.createFile("test.txt"));
    }

    @Test
    public void testCreateFiles() {
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createFiles("file1.txt,file2.txt");
        });
    }
}