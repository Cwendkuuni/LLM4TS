package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
        assertThrows(ParseException.class, () -> TypeHandler.createValue(null, String.class));
        assertThrows(ParseException.class, () -> TypeHandler.createValue("", String.class));
    }

    @Test
    public void testCreateValueStringClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException {
        Class<?> clazz = Class.forName("java.lang.Integer");
        assertEquals(Integer.valueOf(42), TypeHandler.createValue("42", clazz));
        assertEquals(Integer.valueOf(0), TypeHandler.createValue("0", clazz));
        assertEquals(Integer.valueOf(-1), TypeHandler.createValue("-1", clazz));

        assertThrows(NumberFormatException.class, () -> TypeHandler.createValue("foo", clazz));
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createValue("java.NotFound", clazz));
        assertThrows(InstantiationException.class, () -> TypeHandler.createValue("java.lang.String", clazz.getComponentType()));
        assertThrows(IllegalAccessException.class, () -> TypeHandler.createValue("java.lang.String", clazz.getDeclaredComponentType()));
    }

    @Test
    public void testCreateValueStringClassThrowsParseException() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName("java.lang.Double");
        assertThrows(NumberFormatException.class, () -> TypeHandler.createValue("foo", clazz));
    }

    @Test
    public void testCreateValueClassObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException {
        Class<?> clazz = Class.forName("java.lang.String");
        Object obj = TypeHandler.createValue("foo", clazz);
        assertEquals("foo", obj.toString());

        clazz = Class.forName("java.lang.Integer");
        obj = TypeHandler.createValue("42", clazz);
        assertEquals(Integer.valueOf(42), obj);
    }

    @Test
    public void testCreateValueClassThrowsClassNotFoundException() throws InstantiationException, IllegalAccessException, ParseException {
        Class<?> clazz = Class.forName("java.lang.String");
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createValue(null, clazz.getComponentType()));
    }

    @Test
    public void testCreateValueClassThrowsInstantiationException() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException {
        Class<?> clazz = Class.forName("java.lang.String");
        assertThrows(InstantiationException.class, () -> TypeHandler.createValue("foo", clazz.getDeclaredComponentType()));
    }

    @Test
    public void testCreateValueClassThrowsIllegalAccessException() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException {
        Class<?> clazz = Class.forName("java.lang.String");
        assertThrows(IllegalAccessException.class, () -> TypeHandler.createValue("foo", clazz.getDeclaredConstructors()[0].getParameterTypes()[0].getComponentType()));
    }

    @Test
    public void testCreateNumber() throws ParseException {
        assertEquals(Integer.valueOf(42), TypeHandler.createNumber("42"));
        assertEquals(Long.valueOf(42), TypeHandler.createNumber("42L"));
        assertEquals(Float.valueOf(42.0f), TypeHandler.createNumber("42.0"));
        assertEquals(Double.valueOf(42.0), TypeHandler.createNumber("42.0"));

        assertThrows(NumberFormatException.class, () -> TypeHandler.createNumber("foo"));
    }

    @Test
    public void testCreateClass() throws ClassNotFoundException, ParseException {
        assertEquals(Integer.class, TypeHandler.createClass("java.lang.Integer"));
        assertNull(TypeHandler.createClass(null));
        assertNull(TypeHandler.createClass(""));
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createClass("java.NotFound"));
    }

    @Test
    public void testCreateURL() throws MalformedURLException, ParseException {
        assertEquals(new URL("http://example.com"), TypeHandler.createURL("http://example.com"));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createURL(null));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createURL(""));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createURL("http://example.com/foo")); // missing protocol
    }

    @Test
    public void testCreateFile() {
        File file = new File("test.txt");
        assertEquals(file, TypeHandler.createFile("test.txt"));
        assertNull(TypeHandler.createFile(null));
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createFile("test.txt (does not exist)"));
    }

    @Test
    public void testCreateFileInputStream() throws FileNotFoundException, ParseException {
        File file = new File("test.txt");
        FileInputStream inputStream = new FileInputStream(file);
        assertEquals(inputStream, TypeHandler.createFileInputStream("test.txt"));
        assertNull(TypeHandler.createFileInputStream(null));
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createFileInputStream("test.txt (does not exist)"));
    }

    @Test
    public void testCreateFiles() {
        File file1 = new File("test1.txt");
        File file2 = new File("test2.txt");
        File[] files = {file1, file2};
        String[] paths = {"test1.txt", "test2.txt"};

        assertEquals(files, TypeHandler.createFiles(paths));
        assertThrows(UnsupportedOperationException.class, TypeHandler::createFiles);
    }
}