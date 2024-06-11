package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringClass() {
        String str = "testString";
        Class<String> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        String str = "testString";
        assertNull(TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        String str = "test";
        TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        String str = "test";
        TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String str = "test";
        TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String str = "test";
        TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        String str = "test";
        TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        String str = "test";
        TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderURLValue() {
        String str = "test";
        TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueNullStringObject() {
        String str = null;
        Object obj = TypeHandler.createValue(str, Object.class);
        assertNull(obj);
    }

    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = TypeHandler.createValue(str, Object.class);
        assertNotNull(obj);
    }

    @Test
    public void testCreateValueStringClassWithException() {
        String str = "test";
        Class<StringBuilder> clazz = StringBuilder.class;
        try {
            Object obj = TypeHandler.createValue(str, clazz);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String classname = "java.lang.StringBuilder";
        Object obj = TypeHandler.createObject(classname);
        assertInstanceof(StringBuilder.class, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException {
        String classname = "InvalidClassName";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123, number.intValue());
        str = "456.78";
        number = TypeHandler.createNumber(str);
        assertEquals(456, number.intValue());
        assertEquals(78, number.floatValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumber() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(String.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassInvalidClassName() throws ParseException {
        String classname = "InvalidClassName";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-01-01";
        Date date = TypeHandler.createDate(str);
        assertNotNull(date);
        assertEquals("Fri Jan 01 00:00:00 CST 2022", date.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateInvalidFormat() {
        String str = "invalid_date";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        String urlStr = "http://example.com";
        URL url = TypeHandler.createURL(urlStr);
        assertEquals("http://example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidFormat() throws ParseException {
        String urlStr = "invalid_url";
        TypeHandler.createURL(urlStr);
    }

    @Test
    public void testCreateFile() {
        String fileStr = "test.txt";
        File file = TypeHandler.createFile(fileStr);
        assertNotNull(file);
        assertEquals(fileStr, file.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileDoesNotExist() throws ParseException, IOException {
        String fileStr = "nonexistentfile.txt";
        TypeHandler.openFile(fileStr);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileNull() throws ParseException, IOException {
        String fileStr = null;
        TypeHandler.openFile(fileStr);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileInvalidFormat() throws ParseException, IOException {
        String fileStr = "invalid_file";
        TypeHandler.openFile(fileStr);
    }
}