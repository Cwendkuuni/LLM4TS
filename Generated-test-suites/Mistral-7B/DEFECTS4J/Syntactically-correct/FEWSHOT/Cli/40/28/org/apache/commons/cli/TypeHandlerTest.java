package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

import java.text.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Class_String() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test
    public void testCreateValue_String_Class_String_ParseException() {
        assertThrows(ParseException.class, () -> TypeHandler.createValue("1a", Integer.class));
    }

    @Test
    public void testCreateValue_String_Class_String_ClassNotFoundException() {
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createValue("com.example.MyClass", Object.class));
    }

    @Test
    public void testCreateValue_String_Class_String_InstantiationException() {
        assertThrows(InstantiationException.class, () -> TypeHandler.createValue("com.example.MyClass", MyClass.class));
    }

    @Test
    public void testCreateValue_String_Class_String_IllegalAccessException() {
        assertThrows(IllegalAccessException.class, () -> TypeHandler.createValue("com.example.MyClass$InnerClass", MyClass.InnerClass.class));
    }

    @Test
    public void testCreateValue_String_Class_String_Class_ClassCastException() {
        assertThrows(ClassCastException.class, () -> TypeHandler.createValue("123", String[].class));
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_STRING_VALUE() {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_OBJECT_VALUE() {
        try {
            assertInstanceOf(MyObject.class, TypeHandler.createValue("new MyObject()", PatternOptionBuilder.OBJECT_VALUE));
        } catch (ParseException e) {
            throw new AssertionError("Create value should not throw an exception for PatternOptionBuilder.OBJECT_VALUE.", e);
        }
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_NUMBER_VALUE() {
        assertEquals(123L, TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_DATE_VALUE() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue("2022-12-31", PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_CLASS_VALUE() {
        try {
            assertInstanceOf(MyClass.class, TypeHandler.createValue("com.example.MyClass", PatternOptionBuilder.CLASS_VALUE));
        } catch (ParseException e) {
            throw new AssertionError("Create value should not throw an exception for PatternOptionBuilder.CLASS_VALUE.", e);
        }
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_FILE_VALUE() {
        File expectedFile = new File("expectedFile.txt");
        assertInstanceOf(File.class, TypeHandler.createValue(expectedFile.getAbsolutePath(), PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_EXISTING_FILE_VALUE() {
        File expectedFile = new File("expectedFile.txt");
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue(expectedFile.getAbsolutePath(), PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_URL_VALUE() {
        URL expectedURL = new URL("http://example.com");
        assertInstanceOf(URL.class, TypeHandler.createValue(expectedURL.toString(), PatternOptionBuilder.URL_VALUE));
    }

    @Test
    public void testCreateValue_String_Class_String_PatternOptionBuilder_FILES_VALUE() {
        File[] expectedFiles = new File[] { new File("expectedFile1.txt"), new File("expectedFile2.txt") };
        assertInstanceOf(File[].class, TypeHandler.createValue(new String[] { "expectedFile1.txt", "expectedFile2.txt" }, PatternOptionBuilder.FILES_VALUE));
        assertArrayEquals(expectedFiles, (File[]) TypeHandler.createValue(new String[] { "expectedFile1.txt", "expectedFile2.txt" }, PatternOptionBuilder.FILES_VALUE));
    }

    private static class MyClass {
        MyClass() {
        }
    }

    private static class MyClass$InnerClass {
        MyClass$InnerClass() {
        }
    }
}

class MyObject {
    MyObject() {
    }
}