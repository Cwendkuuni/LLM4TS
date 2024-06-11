package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringClass() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        assertEquals(str, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithObjectClass() throws ParseException {
        String classname = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        assertEquals(classname, TypeHandler.createValue(classname, obj));
    }

    @Test
    public void testCreateValueWithNumberClass() throws ParseException {
        String numberStr = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        assertEquals(Long.valueOf(numberStr), TypeHandler.createValue(numberStr, obj));
    }

    @Test
    public void testCreateValueWithDateClass() throws ParseException {
        String dateStr = "2020-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        assertEquals(null, TypeHandler.createValue(dateStr, obj));
    }

    @Test
    public void testCreateValueWithClassClass() throws ParseException {
        String classStr = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        assertEquals(String.class, TypeHandler.createValue(classStr, obj));
    }

    @Test
    public void testCreateValueWithFileClass() throws ParseException {
        String fileStr = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        assertEquals(new File(fileStr), TypeHandler.createValue(fileStr, obj));
    }

    @Test
    public void testCreateValueWithExistingFileClass() throws ParseException {
        String fileStr = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        assertEquals(new File(fileStr), TypeHandler.createValue(fileStr, obj));
    }

    @Test
    public void testCreateValueWithFilesClass() throws ParseException {
        String filesStr = "test1.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        assertEquals(null, TypeHandler.createValue(filesStr, obj));
    }

    @Test
    public void testCreateValueWithURLClass() throws ParseException {
        String urlStr = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        assertEquals(new URL(urlStr), TypeHandler.createValue(urlStr, obj));
    }

    @Test
    public void testCreateValueWithInvalidClass() throws ParseException {
        String str = "test";
        Object obj = "InvalidClass";
        assertNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        assertNotNull(TypeHandler.createObject(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClass() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        String numberStr = "123";
        assertEquals(Long.valueOf(numberStr), TypeHandler.createNumber(numberStr));
    }

    @Test
    public void testCreateNumberWithDecimal() throws ParseException {
        String numberStr = "123.45";
        assertEquals(Double.valueOf(numberStr), TypeHandler.createNumber(numberStr));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        String numberStr = "invalid";
        TypeHandler.createNumber(numberStr);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        assertEquals(String.class, TypeHandler.createClass(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClass() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateDate() throws ParseException {
        String dateStr = "2020-01-01";
        TypeHandler.createDate(dateStr);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        String urlStr = "http://www.example.com";
        assertEquals(new URL(urlStr), TypeHandler.createURL(urlStr));
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException, MalformedURLException {
        String urlStr = "invalid";
        TypeHandler.createURL(urlStr);
    }

    @Test
    public void testCreateFile() throws ParseException {
        String fileStr = "test.txt";
        assertEquals(new File(fileStr), TypeHandler.createFile(fileStr));
    }

    @Test
    public void testCreateFiles() throws ParseException {
        String filesStr = "test1.txt,test2.txt";
        assertNull(TypeHandler.createFiles(filesStr));
    }
}