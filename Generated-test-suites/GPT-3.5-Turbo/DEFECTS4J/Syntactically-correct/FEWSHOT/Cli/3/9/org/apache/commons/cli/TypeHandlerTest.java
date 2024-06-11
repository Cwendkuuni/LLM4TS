package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_String_Object() {
        // Test STRING_VALUE
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
        assertEquals("bar", TypeHandler.createValue("bar", PatternOptionBuilder.STRING_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.STRING_VALUE));

        // Test OBJECT_VALUE
        Object obj1 = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(String.class, obj1.getClass());
        assertEquals("java.lang.String", obj1.toString());

        Object obj2 = TypeHandler.createValue("java.util.Date", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(Date.class, obj2.getClass());
        assertEquals("java.util.Date", obj2.toString());

        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.OBJECT_VALUE));

        // Test NUMBER_VALUE
        Number number1 = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Long.class, number1.getClass());
        assertEquals(10L, number1);

        Number number2 = TypeHandler.createValue("10.5", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Double.class, number2.getClass());
        assertEquals(10.5, number2);

        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.NUMBER_VALUE));

        // Test DATE_VALUE
        assertNull(TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.DATE_VALUE));

        // Test CLASS_VALUE
        assertEquals(String.class, TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.CLASS_VALUE));

        // Test FILE_VALUE
        assertEquals(new File("path/to/file"), TypeHandler.createValue("path/to/file", PatternOptionBuilder.FILE_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.FILE_VALUE));

        // Test EXISTING_FILE_VALUE
        assertEquals(new File("path/to/file"), TypeHandler.createValue("path/to/file", PatternOptionBuilder.EXISTING_FILE_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.EXISTING_FILE_VALUE));

        // Test FILES_VALUE
        assertNull(TypeHandler.createValue("file1,file2,file3", PatternOptionBuilder.FILES_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.FILES_VALUE));

        // Test URL_VALUE
        assertEquals(new URL("http://www.google.com"), TypeHandler.createValue("http://www.google.com", PatternOptionBuilder.URL_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.URL_VALUE));

        // Test null clazz
        assertNull(TypeHandler.createValue("foo", null));
        assertNull(TypeHandler.createValue(null, null));
    }

    @Test
    public void testCreateValue_String_Class() {
        // Test STRING_VALUE
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
        assertEquals("bar", TypeHandler.createValue("bar", String.class));
        assertNull(TypeHandler.createValue(null, String.class));

        // Test OBJECT_VALUE
        Object obj1 = TypeHandler.createValue("java.lang.String", Object.class);
        assertEquals(String.class, obj1.getClass());
        assertEquals("java.lang.String", obj1.toString());

        Object obj2 = TypeHandler.createValue("java.util.Date", Object.class);
        assertEquals(Date.class, obj2.getClass());
        assertEquals("java.util.Date", obj2.toString());

        assertNull(TypeHandler.createValue(null, Object.class));

        // Test NUMBER_VALUE
        Number number1 = TypeHandler.createValue("10", Number.class);
        assertEquals(Long.class, number1.getClass());
        assertEquals(10L, number1);

        Number number2 = TypeHandler.createValue("10.5", Number.class);
        assertEquals(Double.class, number2.getClass());
        assertEquals(10.5, number2);

        assertNull(TypeHandler.createValue(null, Number.class));

        // Test DATE_VALUE
        assertNull(TypeHandler.createValue("2021-01-01", Date.class));
        assertNull(TypeHandler.createValue(null, Date.class));

        // Test CLASS_VALUE
        assertEquals(String.class, TypeHandler.createValue("java.lang.String", Class.class));
        assertNull(TypeHandler.createValue(null, Class.class));

        // Test FILE_VALUE
        assertEquals(new File("path/to/file"), TypeHandler.createValue("path/to/file", File.class));
        assertNull(TypeHandler.createValue(null, File.class));

        // Test EXISTING_FILE_VALUE
        assertEquals(new File("path/to/file"), TypeHandler.createValue("path/to/file", File.class));
        assertNull(TypeHandler.createValue(null, File.class));

        // Test FILES_VALUE
        assertNull(TypeHandler.createValue("file1,file2,file3", File[].class));
        assertNull(TypeHandler.createValue(null, File[].class));

        // Test URL_VALUE
        assertEquals(new URL("http://www.google.com"), TypeHandler.createValue("http://www.google.com", URL.class));
        assertNull(TypeHandler.createValue(null, URL.class));

        // Test null clazz
        assertNull(TypeHandler.createValue("foo", null));
        assertNull(TypeHandler.createValue(null, null));
    }
}