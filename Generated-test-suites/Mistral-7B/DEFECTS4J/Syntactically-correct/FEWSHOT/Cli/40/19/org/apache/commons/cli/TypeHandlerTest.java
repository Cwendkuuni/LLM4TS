package org.apache.commons.cli;

import static org.junit.Assert.*;

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
    public void testCreateValueStringClass() {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringStringClass() {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, String.class);
        assertEquals(str, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidStringClass() {
        String str = "test";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
        fail("Expected ParseException but got none");
    }

    @Test
    public void testCreateValueStringClassObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, Object.class);
        assertNotSame(obj, result);
        assertEquals(str.getClass(), result.getClass());
    }

    @Test
    public void testCreateValueStringClassNumber() {
        String str = "123";
        Class<?> clazz = Number.class;
        Number result = (Number) TypeHandler.createValue(str, clazz);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateValueStringClassDouble() {
        String str = "123.45";
        Class<?> clazz = Double.class;
        Double result = (Double) TypeHandler.createValue(str, clazz);
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidStringNumber() {
        String str = "test";
        Class<?> clazz = Double.class;
        TypeHandler.createValue(str, clazz);
        fail("Expected ParseException but got none");
    }

    @Test
    public void testCreateValueStringClassDate() {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;
        Date result = (Date) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals("Sun Dec 31 00:00:00 CST 2022", result.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringClassDateUnsupported() {
        String str = "test";
        Class<?> clazz = Date.class;
        TypeHandler.createValue(str, clazz);
        fail("Expected UnsupportedOperationException but got none");
    }

    @Test
    public void testCreateValueStringClassURL() {
        String str = "http://test.com";
        Class<?> clazz = URL.class;
        URL result = (URL) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals("http://test.com", result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidStringURL() {
        String str = "test";
        Class<?> clazz = URL.class;
        TypeHandler.createValue(str, clazz);
        fail("Expected ParseException but got none");
    }

    @Test
    public void testCreateValueStringClassFile() {
        String str = "test.txt";
        Class<?> clazz = File.class;
        File result = (File) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertTrue(new File(str).exists());
        assertEquals(str, result.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueInvalidStringFile() {
        String str = "test.non_existent.txt";
        Class<?> clazz = File.class;
        TypeHandler.createValue(str, clazz);
        fail("Expected FileNotFoundException but got none");
    }

    @Test
    public void testCreateValueStringClassFileInputStream() throws IOException {
        String str = "test.txt";
        Class<?> clazz = FileInputStream.class;
        File file = new File(str);
        FileInputStream result = (FileInputStream) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals(file, result.getFileDescriptor().getFileDescriptor());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueInvalidStringFileInputStream() {
        String str = "test.non_existent.txt";
        Class<?> clazz = FileInputStream.class;
        TypeHandler.createValue(str, clazz);
        fail("Expected FileNotFoundException but got none");
    }

    @Test
    public void testCreateValueStringClassArrayFile() {
        String str = "test1.txt,test2.txt";
        Class<?> clazz = File[].class;
        File[] result = (File[]) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals(2, result.length);
        for (File file : result) {
            assertTrue(new File(file.getAbsolutePath()).exists());
        }
    }

    @Test(expected = ArrayStoreException.class)
    public void testCreateValueInvalidStringArrayFile() {
        String str = "test.txt,123";
        Class<?> clazz = File[].class;
        TypeHandler.createValue(str, clazz);
        fail("Expected ArrayStoreException but got none");
    }
}