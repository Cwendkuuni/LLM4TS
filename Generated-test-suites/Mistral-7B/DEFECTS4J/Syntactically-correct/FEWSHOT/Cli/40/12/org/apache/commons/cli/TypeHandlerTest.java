package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
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
import java.text.ParseException;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = new Object();
        assertEquals(str, TypeHandler.createValue(str, obj).toString());
    }

    @Test
    public void testCreateValueStringString() {
        String str = "test";
        assertEquals(str, TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValueStringClassNotFoundException() {
        String className = "NonExistentClass";
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createValue(className, String.class));
    }

    @Test
    public void testCreateValueStringInstantiationException() throws ClassNotFoundException {
        String className = "java.util.Date";
        assertThrows(InstantiationException.class, () -> TypeHandler.createValue(className, String.class));
    }

    @Test
    public void testCreateValueStringIllegalAccessException() throws ClassNotFoundException {
        String className = "java.util.Date";
        assertThrows(IllegalAccessException.class, () -> TypeHandler.createValue(className, String.class));
    }

    @Test
    public void testCreateValueStringInvocationTargetException() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException {
        String className = "java.util.Date";
        assertEquals(new Date(), TypeHandler.createValue(className, String.class));
    }

    @Test
    public void testCreateValueStringClassTypeMismatch() {
        String str = "123";
        assertNull(TypeHandler.createValue(str, Boolean.class));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderStringValue() {
        assertEquals("test", TypeHandler.createValue( "test", PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderObjectValue() {
        String className = "java.util.Date";
        assertEquals(new Date(), TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderNumberValue() {
        String str = "123";
        assertEquals(123L, TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE));
        assertEquals(123.0, TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderDateValue() {
        String dateString = "2022-12-31";
        assertEquals(new Date(Long.parseLong("2022-12-31")*1000L), TypeHandler.createValue(dateString, PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderClassValue() {
        String className = "java.util.Date";
        assertEquals(new Date(), TypeHandler.createValue(className, PatternOptionBuilder.CLASS_VALUE));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderFileValue() {
        String filePath = "test.txt";
        assertEquals(new File(filePath), TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderExistingFileValue() {
        String filePath = "test.txt";
        File file = new File(filePath);
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE));
        assertEquals(file, TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderFilesValue() {
        String filePaths = "test1.txt, test2.txt";
        File[] files = new File[2];
        files[0] = new File("test1.txt");
        files[1] = new File("test2.txt");
        assertEquals(files, TypeHandler.createValue(filePaths, PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValueStringClassPatternOptionBuilderURLValue() {
        String urlString = "http://www.example.com";
        URL url = new URL(urlString);
        assertEquals(url, TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE));
    }

    @Test
    public void testCreateObject() throws ParseException {
        String className = "java.util.Date";
        assertEquals(new Date(), TypeHandler.createObject(className));
    }

    @Test
    public void testCreateObjectClassNotFoundException() {
        String className = "NonExistentClass";
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createObject(className));
    }

    @Test
    public void testCreateNumber() {
        String numberString = "123";
        assertEquals(123, TypeHandler.createNumber(numberString).intValue());
        assertEquals(123L, TypeHandler.createNumber(numberString).longValue());
        assertEquals(123.0, TypeHandler.createNumber(numberString).doubleValue());
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-12-31";
        Date date = new Date(Long.parseLong(dateString.replaceAll("-", "").replaceAll("\\.", ""))*1000L);
        assertEquals(date, TypeHandler.createDate(dateString));
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        assertEquals(new File(filePath), TypeHandler.createFile(filePath));
    }

    @Test
    public void testOpenFile() throws ParseException, FileNotFoundException, IOException {
        String filePath = "test.txt";
        FileInputStream inputStream = new FileInputStream(new File(filePath));
        assertEquals(inputStream, TypeHandler.openFile(filePath));
    }

    @Test
    public void testCreateFiles() {
        String filePaths = "test1.txt, test2.txt";
        File[] files = new File[2];
        files[0] = new File("test1.txt");
        files[1] = new File("test2.txt");
        // to implement/port:
        //assertEquals(files, TypeHandler.createFiles(filePaths));
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        URL url = new URL(urlString);
        assertEquals(url, TypeHandler.createURL(urlString));
    }
}