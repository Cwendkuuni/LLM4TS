package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.ClassNotFoundException;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.NoSuchMethodException;
import java.lang.reflect.Constructor;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Class_StringObject() {
        Class<?> clazz = String.class;
        String str = "foo";
        Object expected = "foo";

        Object actual = TypeHandler.createValue(str, clazz);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_ClassObject() throws Exception {
        String className = "java.lang.Integer";
        Class<?> clazz = Class.forName(className);
        String str = "42";
        Integer expected = 42;

        Object actual = TypeHandler.createValue(str, clazz);

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_InvalidClass() {
        String className = "java.lang.StringBuffer"; // does not have empty constructor
        Class<?> clazz = Class.forName(className);
        String str = "";

        TypeHandler.createValue(str, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_InvalidType() {
        Class<?> clazz = int[].class;
        String str = "42";

        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateNumber() {
        String str1 = "123";
        Long expected1 = 123L;

        Number actual1 = TypeHandler.createNumber(str1);

        assertEquals(expected1.longValue(), actual1.longValue());

        String str2 = "123.45";
        Double expected2 = 123.45;

        Number actual2 = TypeHandler.createNumber(str2);

        assertEquals(expected2.doubleValue(), actual2.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidType() {
        String str = "true";

        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws Exception {
        String className = "java.lang.String";
        String str = className;

        Class<?> expected = Class.forName(className);
        Class<?> actual = (Class<?>) TypeHandler.createValue(str, Class.class);

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_InvalidClass() throws Exception {
        String className = "java.lang.StringBuffer"; // does not have empty constructor
        String str = className;

        TypeHandler.createValue(str, Class.class);
    }

    @Test
    public void testCreateDate() throws Exception {
        String dateString = "2022-03-20";
        Date expected = new Date(0L); // any date will do for testing
        Date actual = (Date) TypeHandler.createValue(dateString, Date.class);

        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_UnsupportedMethod() {
        String dateString = "2022-03-20";

        TypeHandler.createValue(dateString, Date.class);
    }

    @Test
    public void testCreateURL() {
        String urlString = "https://example.com";
        URL expected = new URL(urlString);
        URL actual = TypeHandler.createURL(urlString);

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL() {
        String urlString = "https://example.com/foo bar";

        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File expected = new File(filePath);
        File actual = TypeHandler.createFile(filePath);

        assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStream_NonExistentFile() {
        String filePath = "test.txt";

        File file = TypeHandler.createFile(filePath);
        FileInputStream expected = null;

        FileInputStream actual = TypeHandler.createFileInputStream(filePath);

        assertNull(expected);
        assertNull(actual);
    }

    @Test
    public void testCreateFileInputStream_ExistentFile() {
        String filePath = "test.txt";
        File file = new File(filePath);
        file.createNewFile();

        FileInputStream expected = new FileInputStream(file);
        FileInputStream actual = TypeHandler.createFileInputStream(filePath);

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateFiles_InvalidString() {
        String filePaths = "test.txt, foo.txt bar.txt";

        TypeHandler.createFiles(filePaths);
    }
}