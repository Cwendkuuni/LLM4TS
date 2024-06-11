package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_ClassObject() {
        Class<String> clazz = String.class;
        String str = "test";
        Object obj = new Object();

        String actual = (String) TypeHandler.createValue(str, clazz);
        assertEquals(str, actual);

        assertNull(TypeHandler.createValue(str, obj.getClass()));
    }

    @Test
    public void testCreateValue_String_ClassNumber() {
        Class<Number> clazz = Number.class;
        String str = "123";

        Number actual = (Number) TypeHandler.createValue(str, clazz);
        assertEquals(Integer.valueOf(123), actual);

        assertNull(TypeHandler.createValue("abc", clazz));
    }

    @Test
    public void testCreateValue_String_ClassDate() {
        Class<Date> clazz = Date.class;
        String str = "2022-12-31";

        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_String_ClassClass() {
        String className = "java.lang.String";
        Class<Class<?>> clazz = Class.class;

        Class<?> actual = (Class<?>) TypeHandler.createValue(className, clazz);
        assertEquals(Class.forName(className), actual);

        assertNull(TypeHandler.createValue("abc", clazz));
    }

    @Test
    public void testCreateValue_String_ClassURL() {
        Class<URL> clazz = URL.class;
        String urlString = "http://example.com";

        URL actual = (URL) TypeHandler.createValue(urlString, clazz);
        assertEquals(new URL(urlString), actual);

        assertNull(TypeHandler.createValue("invalidURL", clazz));
    }

    @Test
    public void testCreateValue_String_ClassFile() {
        Class<File> clazz = File.class;
        String filePath = "test.txt";

        File actual = (File) TypeHandler.createValue(filePath, clazz);
        assertEquals(new File(filePath), actual);

        assertNull(TypeHandler.createValue("invalidPath", clazz));
    }

    @Test
    public void testCreateValue_String_ClassFileInputStream() {
        Class<FileInputStream> clazz = FileInputStream.class;
        String filePath = "test.txt";

        File file = new File(filePath);
        FileInputStream expected = new FileInputStream(file);

        FileInputStream actual = (FileInputStream) TypeHandler.createValue(filePath, clazz);
        assertEquals(expected, actual);

        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue("invalidPath", clazz));
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";

        String actualClassName = (String) TypeHandler.createObject(className);
        assertEquals(className, actualClassName);
    }

    @Test
    public void testCreateObject_InvalidClassname() {
        String invalidClassName = "java.abc.Xyz";

        assertThrows(ParseException.class, () -> TypeHandler.createObject(invalidClassName));
    }

    @Test
    public void testCreateNumber() {
        String numberString = "123";

        Number actual = TypeHandler.createNumber(numberString);
        assertEquals(Integer.valueOf(123), actual);

        String decimalNumberString = "123.45";
        Double actualDecimalNumber = TypeHandler.createNumber(decimalNumberString);
        assertEquals(Double.valueOf(123.45), actualDecimalNumber);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-12-31";

        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(dateString));
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";

        URL actual = TypeHandler.createURL(urlString);
        assertEquals(new URL(urlString), actual);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";

        File actual = TypeHandler.createFile(filePath);
        assertEquals(new File(filePath), actual);
    }

    @Test
    public void testCreateFiles() {
        String paths = "test1.txt,test2.txt";

        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(paths));
    }

    @Test
    public void testOpenFile() {
        String filePath = "test.txt";

        File file = new File(filePath);
        FileInputStream expected = new FileInputStream(file);

        FileInputStream actual = TypeHandler.openFile(filePath);
        assertEquals(expected, actual);

        assertThrows(FileNotFoundException.class, () -> TypeHandler.openFile("invalidPath"));
    }
}