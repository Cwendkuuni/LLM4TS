package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue() {
        String str = "test";
        Object obj = new Object();
        Class<?> clazz = Object.class;
        try {
            assertEquals(str, TypeHandler.createValue(str, obj));
            assertEquals(str, TypeHandler.createValue(str, clazz));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateObject() {
        String classname = "java.lang.Object";
        try {
            assertEquals(Object.class, TypeHandler.createObject(classname).getClass());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateNumber() {
        String str = "200";
        try {
            assertEquals(Long.valueOf(str), TypeHandler.createNumber(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateClass() {
        String classname = "java.lang.Object";
        try {
            assertEquals(Object.class, TypeHandler.createClass(classname));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDate() {
        String str = "2021-01-01";
        assertNull(TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        try {
            assertEquals(new URL(str), TypeHandler.createURL(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFile() {
        String str = "path/to/file.txt";
        assertEquals(new File(str), TypeHandler.createFile(str));
    }

    @Test
    public void testCreateFiles() {
        String str = "path/to/file1.txt, path/to/file2.txt";
        try {
            assertEquals(new File[]{new File("path/to/file1.txt"), new File("path/to/file2.txt")}, TypeHandler.createFiles(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}