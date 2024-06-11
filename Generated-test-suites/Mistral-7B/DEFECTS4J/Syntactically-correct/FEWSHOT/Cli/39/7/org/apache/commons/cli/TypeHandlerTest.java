package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_String_String() {
        String str = "foo";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValue_String_Class_String() throws ParseException {
        String str = "42";
        Class<?> clazz = Integer.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(42, obj);

        str = "3.14";
        clazz = Double.class;
        obj = TypeHandler.createValue(str, clazz);
        assertEquals(3.14, obj);

        str = "2022-12-31";
        clazz = Date.class;
        obj = TypeHandler.createValue(str, clazz);
        assertInstanceOf(Date.class, obj);

        str = "com.example.MyClass";
        clazz = MyClass.class; // assuming MyClass is a valid class name
        obj = TypeHandler.createValue(str, clazz);
        assertInstanceOf(MyClass.class, obj);

        str = "/path/to/file.txt";
        clazz = File.class;
        obj = TypeHandler.createValue(str, clazz);
        assertEquals(new File(str), obj);

        str = "http://example.com";
        clazz = URL.class;
        obj = TypeHandler.createValue(str, clazz);
        assertInstanceOf(URL.class, obj);
    }

    @Test
    public void testCreateValue_String_Class_String_ParseException() {
        String str = "invalid";
        Class<?> clazz = Integer.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));

        str = "invalid.double";
        clazz = Double.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));

        str = "29-12-2022";
        clazz = Date.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));

        str = "com.example.NonExistentClass";
        Class<?> clazz = MyNonExistentClass.class; // assuming MyNonExistentClass is a non-existent class name
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));

        str = "/non-existent/file.txt";
        clazz = File.class;
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue(str, clazz));

        str = "http://example.com/non-existent";
        clazz = URL.class;
        assertThrows(MalformedURLException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createObject(classname);
        assertInstanceOf(Date.class, obj);
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String str = "42";
        Number number = TypeHandler.createNumber(str);
        assertEquals(42, number.intValue());

        str = "3.14";
        number = TypeHandler.createNumber(str);
        assertEquals(3.14, number.doubleValue());
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException {
        String classname = "java.util.Date";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertInstanceOf(Date.class, clazz);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-12-31";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        String str = "http://example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals(new URL("http://example.com"), url);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File file = TypeHandler.createFile(str);
        assertEquals(new File(str), file);
    }

    @Test
    public void testCreateFileInputStream() throws IOException, ParseException, FileNotFoundException {
        String str = "/path/to/file.txt";
        File file = new File(str);
        FileInputStream inputStream = TypeHandler.createFile(str);
        FileInputStream expectedInputstream = new FileInputStream(file);
        assertEquals(expectedInputstream, inputStream);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        File[] files = TypeHandler.createFiles(str);
        File file1 = new File("/path/to/file1.txt");
        File file2 = new File("/path/to/file2.txt");
        File[] expectedFiles = {file1, file2};
        assertArrayEquals(expectedFiles, files);
    }
}