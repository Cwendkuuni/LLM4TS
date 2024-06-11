package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueForString() throws ParseException {
        String str = "Test String";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertEquals("Test String", value);
    }

    @Test
    public void testCreateValueForObject() throws ParseException {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertTrue(value instanceof Object);
    }

    @Test
    public void testCreateValueForNumber() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertTrue(value instanceof Number);
    }

    @Test
    public void testCreateValueForDate() throws ParseException {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertTrue(value instanceof Date);
    }

    @Test
    public void testCreateValueForClass() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertTrue(value instanceof Class);
    }

    @Test
    public void testCreateValueForFile() throws ParseException {
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertTrue(value instanceof File);
    }

    @Test
    public void testCreateValueForExistingFile() throws ParseException {
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertTrue(value instanceof FileInputStream);
    }

    @Test
    public void testCreateValueForFiles() throws ParseException {
        String str = "/path/to/*.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertTrue(value instanceof File[]);
    }

    @Test
    public void testCreateValueForURL() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object value = TypeHandler.createValue(str, obj);
        assertTrue(value instanceof URL);
    }

    @Test
    public void testCreateValueForUnknownType() throws ParseException {
        String str = "unknown";
        Object obj = "UnknownType";
        Object value = TypeHandler.createValue(str, obj);
        assertNull(value);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.Object";
        Object obj = TypeHandler.createObject(classname);
        assertTrue(obj instanceof Object);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectForUnknownClass() throws ParseException {
        String classname = "unknown";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberForDouble() throws ParseException {
        String str = "10.5";
        Number number = TypeHandler.createNumber(str);
        assertTrue(number instanceof Double);
        assertEquals(10.5, number);
    }

    @Test
    public void testCreateNumberForLong() throws ParseException {
        String str = "10";
        Number number = TypeHandler.createNumber(str);
        assertTrue(number instanceof Long);
        assertEquals(10L, number);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberForInvalidNumber() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(String.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassForUnknownClass() throws ParseException {
        String classname = "unknown";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2021-01-01";
        Date date = TypeHandler.createDate(str);
        assertNull(date);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLForMalformedURL() throws ParseException {
        String str = "example.com";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File file = TypeHandler.createFile(str);
        assertEquals("/path/to/file.txt", file.getPath());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "/path/to/file.txt";
        FileInputStream inputStream = TypeHandler.openFile(str);
        assertNotNull(inputStream);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileForNonExistingFile() throws ParseException {
        String str = "/path/to/nonexisting.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/*.txt";
        File[] files = TypeHandler.createFiles(str);
        assertNull(files);
    }
}