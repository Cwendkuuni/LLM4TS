package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.ClassNotFoundException;
import java.lang.Class;

import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringClass() {
        String str = "TestString";
        Class<?> clazz = String.class;

        try {
            Object value = TypeHandler.createValue(str, clazz);
            assertEquals(str, value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueClassObject() {
        String classname = "java.lang.String";
        Class<?> clazz = String.class;

        try {
            Object value = TypeHandler.createValue(classname, clazz);
            assertEquals(Class.forName(classname), value.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueStringClassNotSupported() {
        String str = "123";
        Class<?> clazz = String.class;

        try {
            Object value = TypeHandler.createValue(str, clazz);
            assertNull(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueStringClassNumber() {
        String str = "123";
        Class<?> clazz = int.class;

        try {
            Object value = TypeHandler.createValue(str, clazz);
            assertEquals(123, value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueStringClassDate() {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;

        try {
            Object value = TypeHandler.createValue(str, clazz);
            Date date = new Date(0);
            date.setYear(2022);
            date.setMonth(11);
            date.setDate(31);
            assertEquals(date, value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueStringClassURL() {
        String str = "http://localhost:8080";
        Class<?> clazz = URL.class;

        try {
            Object value = TypeHandler.createValue(str, clazz);
            URL url = new URL("http://localhost:8080");
            assertEquals(url, value);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueStringClassFile() {
        String str = "test.txt";
        Class<?> clazz = File.class;

        try {
            Object value = TypeHandler.createValue(str, clazz);
            File file = new File(str);
            assertEquals(file, value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValueStringClassFileInputSteam() {
        String str = "test.txt";
        Class<?> clazz = FileInputStream.class;

        try {
            Object value = TypeHandler.createValue(str, clazz);
            assertInstanceOf(FileInputStream.class, value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringClassDateNotImplemented() {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;

        try {
            Object value = TypeHandler.createDate(str);
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValuesStringClassFilesNotImplemented() {
        String str = "test1.txt,test2.txt";
        Class<?> clazz = File[].class;

        try {
            Object value = TypeHandler.createFiles(str);
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}