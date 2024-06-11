package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        String str = "testString";
        assertEquals("testString", TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValueStringClassObject() {
        String str = "testString";
        assertInstanceOf(String.class, TypeHandler.createValue(str, Object.class));
        assertEquals("testString", TypeHandler.createValue(str, Object.class));
    }

    @Test
    public void testCreateValueStringClassString() {
        String str = "testString";
        assertEquals("testString", TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValueStringClassObjectWithException() {
        String str = "testClass";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValueStringClassClass() {
        String str = "testClass";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, String.class));

        String className = "java.lang.String";
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertInstanceOf(clazz, TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValueStringClassObjectWithException2() {
        String str = "testClass";
        String className = "java.lang.Integer";
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        assertEquals(123L, TypeHandler.createNumber(str));

        String strWithDecimal = "123.45";
        assertEquals(123.45, TypeHandler.createNumber(strWithDecimal));
    }

    @Test
    public void testCreateNumberWithException() {
        String str = "test";
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        Class<?> expectedClass = String.class;

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertInstanceOf(expectedClass, TypeHandler.createClass(className));
        assertInstanceOf(clazz, TypeHandler.createClass(className));
    }

    @Test
    public void testCreateClassWithException() {
        String className = "java.lang.Integer";
        assertThrows(ParseException.class, () -> TypeHandler.createClass(className));
    }

    @Test
    public void testCreateDate() throws Exception {
        String dateString = "2023-03-25";
        Date date = new Date(0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = formatter.parse(dateString);

        Date createdDate = TypeHandler.createDate(dateString);
        assertEquals(date.getTime(), createdDate.getTime());
    }

    @Test
    public void testCreateDateWithException() {
        String dateString = "test";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(dateString));
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://test.com";
        URL url = new URL(urlString);

        URL createdURL = TypeHandler.createURL(urlString);
        assertEquals(url.toString(), createdURL.toString());
    }

    @Test
    public void testCreateURLWithException() {
        String urlString = "test";
        assertThrows(ParseException.class, () -> TypeHandler.createURL(urlString));
    }

    @Test
    public void testCreateFile() {
        String filePath = "/test/file.txt";
        File file = new File(filePath);

        File createdFile = TypeHandler.createFile(filePath);
        assertEquals(file.getAbsolutePath(), createdFile.getAbsolutePath());
    }

    @Test
    public void testOpenFile() throws IOException {
        String filePath = "/test/file.txt";
        File file = new File(filePath);

        FileInputStream fileInput = new FileInputStream(file);

        FileInputStream openedFile = TypeHandler.openFile(filePath);
        assertEquals(fileInput.available(), openedFile.available());

        openedFile.close();
    }

    @Test
    public void testOpenFileWithException() {
        String filePath = "/test/non-existent-file.txt";

        assertThrows(ParseException.class, () -> TypeHandler.openFile(filePath));
    }

    @Test
    public void testCreateFiles() {
        String filePaths = "/test/file1.txt,/test/file2.txt";
        File[] expectedFiles = {new File("/test/file1.txt"), new File("/test/file2.txt")};

        File[] createdFiles = TypeHandler.createFiles(filePaths);
        assertArrayEquals(expectedFiles, createdFiles);
    }

    @Test
    public void testCreateFilesWithException() {
        String filePaths = "test";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(filePaths));
    }
}