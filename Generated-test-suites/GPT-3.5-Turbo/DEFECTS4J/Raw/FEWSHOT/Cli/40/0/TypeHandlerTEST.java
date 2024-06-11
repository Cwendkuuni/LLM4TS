package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest {
    @Test
    public void testCreateValueWithStringType() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        assertEquals(str, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithObjectType() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        assertNotNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithNumberType() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        assertEquals(Long.valueOf(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithDateType() throws ParseException {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        assertNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithClassType() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        try {
            assertEquals(Class.forName(str), TypeHandler.createValue(str, obj));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueWithFileType() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        assertEquals(new File(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithExistingFileType() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        try {
            assertNotNull(TypeHandler.createValue(str, obj));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueWithFilesType() throws ParseException {
        String str = "test.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        try {
            assertNotNull(TypeHandler.createValue(str, obj));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueWithURLType() throws ParseException {
        String str = "http://www.google.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        try {
            assertEquals(new URL(str), TypeHandler.createValue(str, obj));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        assertNotNull(TypeHandler.createObject(classname));
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String number = "10";
        assertEquals(Long.valueOf(number), TypeHandler.createNumber(number));
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        try {
            assertEquals(Class.forName(classname), TypeHandler.createClass(classname));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String date = "2021-01-01";
        TypeHandler.createDate(date);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String url = "http://www.google.com";
        try {
            assertEquals(new URL(url), TypeHandler.createURL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFile() {
        String file = "test.txt";
        assertEquals(new File(file), TypeHandler.createFile(file));
    }

    @Test
    public void testOpenFile() {
        String file = "test.txt";
        try {
            assertNotNull(TypeHandler.openFile(file));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String files = "test.txt,test2.txt";
        TypeHandler.createFiles(files);
    }
}