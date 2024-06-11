package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test
    public void testCreateValue_StringClass_String() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", TypeHandler.class, String.class));
    }

    @Test
    public void testCreateValue_StringClass_ClassNotFoundException() {
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createValue("com.example.MyClass", TypeHandler.class, String.class));
    }

    @Test
    public void testCreateValue_StringClass_InstantiationException() throws ParseException {
        Class<?> clazz = Class.forName("java.lang.StringBuilder");
        assertThrows(InstantiationException.class, () -> TypeHandler.createValue("", TypeHandler.class, clazz));
    }

    @Test
    public void testCreateValue_StringClass_IllegalAccessException() throws ParseException {
        Class<?> clazz = Class.forName("java.lang.StringBuilder");
        assertThrows(IllegalAccessException.class, () -> TypeHandler.createValue("", TypeHandler.class, clazz));
    }

    @Test
    public void testCreateValue_StringClass_InvocationTargetException() throws ParseException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName("java.lang.StringBuilder");
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getDeclaredMethod("append", String.class);
        method.setAccessible(true);

        assertThrows(InvocationTargetException.class, () -> TypeHandler.createValue("", TypeHandler.class, clazz));

        InvocationTargetException e = (InvocationTargetException) assertThrows(InvocationTargetException.class, () -> TypeHandler.createValue("", TypeHandler.class, clazz));
        assertThrows(IllegalArgumentException.class, () -> e.getCause().getMessage());
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_STRING_VALUE() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", TypeHandler.class, String[].class, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_OBJECT_VALUE() throws ParseException {
        Class<?> clazz = Class.forName("java.lang.StringBuilder");
        assertEquals(clazz.newInstance(), TypeHandler.createValue("", TypeHandler.class, clazz, PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_NUMBER_VALUE() throws ParseException {
        assertEquals((Long) 123, TypeHandler.createValue("123", TypeHandler.class, Long.TYPE, PatternOptionBuilder.NUMBER_VALUE));
        assertEquals((Double) 123.45, TypeHandler.createValue("123.45", TypeHandler.class, Double.TYPE, PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_DATE_VALUE() throws ParseException {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue("2022-03-31", TypeHandler.class, Date.class, PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_CLASS_VALUE() throws ParseException, ClassNotFoundException {
        Class<?> clazz = Class.forName("java.lang.StringBuilder");
        assertEquals(clazz, TypeHandler.createValue("java.lang.StringBuilder", TypeHandler.class, clazz, PatternOptionBuilder.CLASS_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_FILE_VALUE() {
        File file = new File("test.txt");
        assertEquals(file, TypeHandler.createValue("test.txt", TypeHandler.class, File.class, PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_EXISTING_FILE_VALUE() {
        File file = new File("test.txt");
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue("test.txt", TypeHandler.class, File.class, PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_FILES_VALUE() {
        File[] files = new File[] { new File("test1.txt"), new File("test2.txt") };
        String[] paths = { "test1.txt", "test2.txt" };
        assertEquals(files, TypeHandler.createValue(String.join(File.pathSeparator, paths), TypeHandler.class, File[].class, PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValue_StringClass_PatternOptionBuilder_URL_VALUE() throws ParseException, MalformedURLException {
        URL url = new URL("http://example.com");
        assertEquals(url, TypeHandler.createValue("http://example.com", TypeHandler.class, URL.class, PatternOptionBuilder.URL_VALUE));
    }
}