
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_string() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
        assertEquals("bar", TypeHandler.createValue("bar", PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValue_object() {
        assertNull(TypeHandler.createValue(null, Object.class));
        assertEquals(new Date(), TypeHandler.createValue(new Date().toString(), Object.class));
    }

    @Test
    public void testCreateValue_number() {
        assertNull(TypeHandler.createValue(null, Number.class));
        assertEquals(123, TypeHandler.createValue("123", Number.class));
    }

    @Test
    public void testCreateValue_date() {
        assertNull(TypeHandler.createValue(null, Date.class));
        assertEquals(new Date(), TypeHandler.createValue(new Date().toString(), Date.class));
    }

    @Test
    public void testCreateValue_class() {
        assertNull(TypeHandler.createValue(null, Class.class));
        assertEquals(String.class, TypeHandler.createValue("java.lang.String", Class.class));
    }

    @Test
    public void testCreateValue_file() {
        assertNull(TypeHandler.createValue(null, File.class));
        assertEquals(new File("path/to/file"), TypeHandler.createValue("path/to/file", File.class));
    }

    @Test
    public void testCreateValue_existingFile() {
        assertNull(TypeHandler.createValue(null, File.class));
        assertEquals(new File("path/to/file"), TypeHandler.createValue("path/to/file", PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValue_files() {
        assertNull(TypeHandler.createValue(null, File[].class));
        assertEquals(new File[]{new File("path/to/file1"), new File("path/to/file2")}, TypeHandler.createValue("path/to/file1,path/to/file2", PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValue_url() {
        assertNull(TypeHandler.createValue(null, URL.class));
        assertEquals(new URL("http://www.example.com"), TypeHandler.createValue("http://www.example.com", URL.class));
    }
}
