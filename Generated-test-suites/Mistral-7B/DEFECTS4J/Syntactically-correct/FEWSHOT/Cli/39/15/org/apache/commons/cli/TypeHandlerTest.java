package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        assertInstanceOf(String.class, TypeHandler.createValue("", String.class));
    }

    @Test
    public void testCreateValueStringClassWithStringConstructor() throws Exception {
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        StringBuilder stringBuilder = (StringBuilder) TypeHandler.createValue("test", stringBuilderClass);
        assertEquals("test", stringBuilder.toString());
    }

    @Test
    public void testCreateValueStringClassWithNoArgConstructor() throws Exception {
        Class<Date> dateClass = Date.class;
        Date date = (Date) TypeHandler.createValue("", dateClass);
        assertNull(date);
    }

    @Test
    public void testCreateValueStringClassWithInvalidConstructorThrowsException() throws Exception {
        Class<File> fileClass = File.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue("", fileClass));
    }

    @Test
    public void testCreateValueStringClassWithLongConstructor() throws Exception {
        Class<Integer> intClass = Integer.class;
        Integer number = (Integer) TypeHandler.createValue("123", intClass);
        assertEquals(123, number.intValue());
    }

    @Test
    public void testCreateValueStringClassWithDoubleConstructor() throws Exception {
        Class<Double> doubleClass = Double.class;
        Double number = (Double) TypeHandler.createValue("123.45", doubleClass);
        assertEquals(123.45, number.doubleValue());
    }

    @Test
    public void testCreateValueStringClassWithDateConstructor() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue("2023-03-01", Date.class));
    }

    @Test
    public void testCreateValueStringClassWithURLConstructor() throws Exception {
        Class<URL> urlClass = URL.class;
        URL url = (URL) TypeHandler.createValue("https://example.com", urlClass);
        assertEquals("https://example.com", url.toString());
    }

    @Test
    public void testCreateValueStringClassWithFileConstructor() throws Exception {
        String filePath = "testFile.txt";
        Class<File> fileClass = File.class;
        File file = (File) TypeHandler.createValue(filePath, fileClass);
        assertEquals(new File(filePath), file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStreamFileNotFound() throws Exception {
        String filePath = "nonExistentFile.txt";
        Class<FileInputStream> fileInputStreamClass = FileInputStream.class;
        TypeHandler.createValue(filePath, fileInputStreamClass);
    }

    @Test
    public void testCreateFileInputStreamExistingFile() throws Exception {
        String filePath = "testFile.txt";
        Class<FileInputStream> fileInputStreamClass = FileInputStream.class;
        File file = new File(filePath);
        FileInputStream fileInputStream = (FileInputStream) TypeHandler.createValue(filePath, fileInputStreamClass);
        assertEquals(file, fileInputStream.getFileDescriptor().getFileDescriptor());
    }

    @Test
    public void testCreateValueStringClassWithArrayOfFiles() {
        String filePaths = "testFile1.txt,testFile2.txt";
        Class<File[]> fileArrayClass = File[].class;
        File[] files = (File[]) TypeHandler.createValue(filePaths, fileArrayClass);
        assertEquals(2, files.length);
        for (int i = 0; i < files.length; i++) {
            String filePath = "testFile" + (i + 1) + ".txt";
            File file = new File(filePath);
            assertEquals(file, files[i]);
        }
    }

    @Test
    public void testCreateValueStringClassWithInvalidTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> TypeHandler.createValue("", Boolean.class));
    }
}