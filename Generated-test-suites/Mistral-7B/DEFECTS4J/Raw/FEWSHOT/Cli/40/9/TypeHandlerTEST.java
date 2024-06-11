package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() {
        Object result = TypeHandler.createValue("foo", String.class);
        assertEquals("foo", result);
    }

    @Test
    public void testCreateValue_StringClass_String() {
        Object result = TypeHandler.createValue("123", Integer.class);
        assertEquals(123, result);

        result = TypeHandler.createValue("123.45", Double.class);
        assertEquals(123.45, result);

        result = TypeHandler.createValue("true", boolean.class);
        assertEquals(true, result);

        result = TypeHandler.createValue("java.lang.String", String.class);
        assertEquals("java.lang.String", result.getClass().getName());

        try {
            result = TypeHandler.createValue("com.example.MyClass", MyClass.class);
            assertInstanceOf(MyClass.class, result);
        } catch (ParseException e) {
            // expected
        }

        try {
            result = TypeHandler.createValue("invalid", Object.class);
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName("java.lang.String");
        Object result = TypeHandler.createObject("java.lang.String");
        assertEquals(clazz, result.getClass());

        clazz = Class.forName("com.example.MyClass");
        try {
            result = TypeHandler.createObject("com.example.MyClass");
            assertInstanceOf(clazz, result);
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Number result = TypeHandler.createNumber("123");
        assertEquals(123, result.intValue());

        result = TypeHandler.createNumber("123.45");
        assertEquals(123.45, result.doubleValue());

        try {
            result = TypeHandler.createNumber("invalid");
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException {
        Class<?> clazz = Class.forName("java.lang.String");
        Class<?> resultClass = TypeHandler.createClass("java.lang.String");
        assertEquals(clazz, resultClass);

        try {
            resultClass = TypeHandler.createClass("invalid");
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testCreateDate() throws ParseException {
        try {
            Date result = TypeHandler.createDate("2022-01-01");
            assertNotNull(result);

            Date date1 = new Date();
            Date date2 = TypeHandler.createDate(date1.toString());
            assertEquals(date1, date2);

            Date date3 = TypeHandler.createDate("Invalid Date");
            assertNull(date3);
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        URL result = TypeHandler.createURL("http://example.com");
        assertEquals("http://example.com", result.toString());

        try {
            result = TypeHandler.createURL("invalid");
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("path/to/file.txt");
        assertEquals("path/to/file.txt", result.getAbsolutePath());

        try {
            result = TypeHandler.createFile("invalid");
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testOpenFile() throws ParseException, FileNotFoundException {
        String filePath = "path/to/file.txt";
        File file = new File(filePath);
        FileInputStream expected = new FileInputStream(file);

        FileInputStream result = TypeHandler.openFile(filePath);
        assertEquals(expected.getClass(), result.getClass());

        try {
            result = TypeHandler.openFile("invalid");
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("path/to/file1.txt,path/to/file2.txt");
        fail();
    }
}

class MyClass {
    MyClass() {}
}