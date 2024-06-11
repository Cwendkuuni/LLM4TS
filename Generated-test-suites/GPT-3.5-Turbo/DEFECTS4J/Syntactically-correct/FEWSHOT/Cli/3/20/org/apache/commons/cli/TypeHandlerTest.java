package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        String str = "test";
        Object obj = new Object();

        // Test for PatternOptionBuilder.STRING_VALUE
        assertEquals(str, TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE));

        // Test for PatternOptionBuilder.OBJECT_VALUE
        assertNotNull(TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE));

        // Test for PatternOptionBuilder.NUMBER_VALUE
        assertNotNull(TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE));

        // Test for PatternOptionBuilder.DATE_VALUE
        assertNotNull(TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE));

        // Test for PatternOptionBuilder.CLASS_VALUE
        assertNotNull(TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE));

        // Test for PatternOptionBuilder.FILE_VALUE
        assertNotNull(TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE));

        // Test for PatternOptionBuilder.EXISTING_FILE_VALUE
        assertNotNull(TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE));

        // Test for PatternOptionBuilder.FILES_VALUE
        assertNotNull(TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE));

        // Test for PatternOptionBuilder.URL_VALUE
        assertNotNull(TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE));

        // Test for unknown type
        assertNull(TypeHandler.createValue(str, Object.class));
    }

    @Test
    public void testCreateObject() {
        String str = "java.lang.String";
        assertNotNull(TypeHandler.createObject(str));
    }

    @Test
    public void testCreateNumber() {
        String str = "12345";
        assertNotNull(TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateClass() {
        String str = "java.lang.String";
        assertNotNull(TypeHandler.createClass(str));
    }

    @Test
    public void testCreateDate() {
        String str = "2021-01-01";
        assertNotNull(TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        assertNotNull(TypeHandler.createURL(str));
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file";
        assertNotNull(TypeHandler.createFile(str));
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/files";
        assertNotNull(TypeHandler.createFiles(str));
    }

}