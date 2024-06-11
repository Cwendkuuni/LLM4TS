package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "testString";
        Object obj = new Object() {};

        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClass() {
        String str = "testString";
        Class clazz = String.class;

        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String str = "testString";
        Class clazz = String.class;
        assertEquals(clazz, PatternOptionBuilder.STRING_VALUE);

        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        String str = "testClass";
        Class clazz = String.class;
        assertEquals(clazz, PatternOptionBuilder.OBJECT_VALUE);

        Object result = TypeHandler.createValue(str, clazz);
        fail("Expected null, but got: " + result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        String str = "123";
        Class clazz = Number.class;
        assertEquals(clazz, PatternOptionBuilder.NUMBER_VALUE);

        Number result = (Number) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals(123L, result.longValue());

        str = "456.78";
        result = (Number) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals(456.78, result.doubleValue(), 0.01);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        String str = "2022-12-31";
        Class clazz = Date.class;
        assertEquals(clazz, PatternOptionBuilder.DATE_VALUE);

        Date result = (Date) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals("Fri Dec 31 00:00:00 CET 2022", result.toString());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String str = "java.lang.String";
        Class clazz = String.class;
        assertEquals(clazz, PatternOptionBuilder.CLASS_VALUE);

        Object result = TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertTrue(result.getClass().isAssignableFrom(String.class));

        Class<?> clazzResult = result.getClass();
        assertEquals(clazz, clazzResult);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String str = "testFile.txt";
        Class clazz = File.class;
        assertEquals(clazz, PatternOptionBuilder.FILE_VALUE);

        File result = (File) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertTrue(new File(str).exists());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        String str = "testFile.txt";
        Class clazz = File.class;
        assertEquals(clazz, PatternOptionBuilder.EXISTING_FILE_VALUE);

        File result = (File) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertTrue(new File(str).exists());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        String str = "testFile1.txt,testFile2.txt";
        Class clazz = File[].class;
        assertEquals(clazz, PatternOptionBuilder.FILES_VALUE);

        File[] result = (File[]) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals(2, result.length);

        List<String> files = List.of("testFile1.txt", "testFile2.txt");
        for (int i = 0; i < result.length; i++) {
            File file = result[i];
            assertTrue(files.get(i).equals(file.getPath()));
        }
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderUrlValue() {
        String str = "http://example.com";
        Class clazz = URL.class;
        assertEquals(clazz, PatternOptionBuilder.URL_VALUE);

        URL result = (URL) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
        assertEquals("example.com", result.getHost());
        assertEquals("/", result.getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringUnsupportedPatternOptionBuilder() {
        String str = "test";
        Class clazz = Number.class;

        Object result = TypeHandler.createValue(str, clazz);
        fail("Expected IllegalArgumentException, but got: " + result);
    }
}