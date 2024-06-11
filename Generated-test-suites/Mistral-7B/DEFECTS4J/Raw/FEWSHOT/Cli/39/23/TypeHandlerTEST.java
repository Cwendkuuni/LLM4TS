package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() throws ParseException {
        assertEquals(PatternOptionBuilder.STRING_VALUE.getClass(), TypeHandler.createValue("test", String.class));
    }

    @Test
    public void testCreateValue_StringClass_String() throws ParseException {
        assertEquals(String.class, TypeHandler.createValue("test", String.class.getComponentType()).getClass());
    }

    @Test
    public void testCreateValue_StringClass_Object() throws ParseException, ClassNotFoundException, InstantiationException {
        Class<?> clazz = Class.forName("java.util.Date");
        Object date = TypeHandler.createValue("2023-03-01", clazz);
        assertTrue(date instanceof Date);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringClass_InvalidType() throws ParseException {
        TypeHandler.createValue("test", Integer.class);
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException {
        Object obj = TypeHandler.createObject("java.util.Date");
        assertTrue(obj instanceof Date);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassName() throws ParseException {
        TypeHandler.createObject("java.util.InvalidClass");
    }

    @Test
    public void testCreateNumber() throws ParseException {
        assertEquals(1L, TypeHandler.createNumber("1"));
        assertEquals(1.0, TypeHandler.createNumber("1.0"));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumber() throws ParseException {
        TypeHandler.createNumber("test");
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_InvalidClassName() throws ParseException {
        TypeHandler.createClass("java.util.InvalidClass");
    }

    @Test
    public void testCreateDate() {
        assertNull(TypeHandler.createDate(null));
        assertNull(TypeHandler.createDate(""));
        assertNull(TypeHandler.createDate("test"));
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL url = TypeHandler.createURL("https://www.example.com");
        assertNotNull(url);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL() throws ParseException {
        TypeHandler.createURL("test");
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStream_InvalidFilePath() throws IOException, ParseException {
        TypeHandler.createFileInputStream("test.txt");
    }

    @Test
    public void testCreateFileInputStream_ValidFilePath() throws IOException, ParseException {
        File file = new File("test.txt");
        InputStream inputStream = TypeHandler.createFileInputStream(file.getAbsolutePath());
        assertNotNull(inputStream);
    }

    @Test(expected = ParseException.class)
    public void testCreateFiles_InvalidFilePath() throws ParseException {
        TypeHandler.createFiles("test.txt");
    }

    @Test
    public void testCreateFiles_ValidFilePaths() throws ParseException {
        String[] paths = {"test1.txt", "test2.txt"};
        File[] files = TypeHandler.createFiles(String.join(File.pathSeparator, paths));
        assertNotNull(files);
        assertFalse(files.length == 0);
    }
}