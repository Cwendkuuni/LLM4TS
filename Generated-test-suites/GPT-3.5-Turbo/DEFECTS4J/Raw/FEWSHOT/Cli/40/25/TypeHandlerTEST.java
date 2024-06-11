package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_string() throws ParseException {
        String str = "test";
        String obj = PatternOptionBuilder.STRING_VALUE;
        assertEquals(str, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_object() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        assertEquals(str, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_number() throws ParseException {
        String str = "123";
        Number obj = PatternOptionBuilder.NUMBER_VALUE;
        assertEquals(123, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_date() throws ParseException {
        String str = "2020-01-01";
        Date obj = PatternOptionBuilder.DATE_VALUE;
        assertEquals(null, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_class() throws ParseException {
        String str = "java.util.List";
        Class<?> obj = PatternOptionBuilder.CLASS_VALUE;
        assertEquals(List.class, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_file() throws ParseException {
        String str = "/path/to/file.txt";
        File obj = PatternOptionBuilder.FILE_VALUE;
        assertEquals(new File(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_existingFile() throws ParseException {
        String str = "/path/to/existing/file.txt";
        FileInputStream obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        FileInputStream expected = new FileInputStream(str);
        assertEquals(expected, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_files() throws ParseException {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        File[] obj = PatternOptionBuilder.FILES_VALUE;
        File[] expected = {new File("/path/to/file1.txt"), new File("/path/to/file2.txt")};
        assertArrayEquals(expected, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_url() throws ParseException {
        String str = "http://www.example.com";
        URL obj = PatternOptionBuilder.URL_VALUE;
        URL expected = new URL(str);
        assertEquals(expected, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_default() throws ParseException {
        String str = "test";
        Object obj = null;
        assertNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "org.apache.commons.cli.TestObject";
        Object obj = TypeHandler.createObject(classname);
        assertEquals(TestObject.class, obj.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_classNotFoundException() throws ParseException {
        String classname = "org.apache.commons.cli.NonExistentClass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_exception() throws ParseException {
        String classname = "org.apache.commons.cli.InvalidObject";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_integer() throws ParseException {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123, number);
    }

    @Test
    public void testCreateNumber_decimal() throws ParseException {
        String str = "123.45";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123.45, number);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_exception() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.util.List";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(List.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_classNotFoundException() throws ParseException {
        String classname = "org.apache.commons.cli.NonExistentClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2020-01-01";
        Date date = TypeHandler.createDate(str);
        assertEquals(null, date);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals(new URL(str), url);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_malformedURLException() throws ParseException {
        String str = "http:www.example.com";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File file = TypeHandler.createFile(str);
        assertEquals(new File(str), file);
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "/path/to/existing/file.txt";
        FileInputStream fileInputStream = TypeHandler.openFile(str);
        FileInputStream expected = new FileInputStream(str);
        assertEquals(expected, fileInputStream);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_fileNotFoundException() throws ParseException {
        String str = "/path/to/nonexistent/file.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        File[] files = TypeHandler.createFiles(str);
        File[] expected = {new File("/path/to/file1.txt"), new File("/path/to/file2.txt")};
        assertArrayEquals(expected, files);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_unsupportedOperationException() {
        String str = "/path/to/files.txt";
        TypeHandler.createFiles(str);
    }
}