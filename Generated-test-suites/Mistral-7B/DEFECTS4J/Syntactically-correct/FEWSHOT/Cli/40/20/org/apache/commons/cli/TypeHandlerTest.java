package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.lang.ClassCastException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Class_String() {
        String str = "test";

        Class<String> clazz = String.class;
        String result = (String) TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Class_InvalidClass() {
        String str = "test";

        Class<Integer> clazz = Integer.class;
        assertNull(TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_String_Class_StringArray() {
        String str = "test,test2";

        Class<String[]> clazz = String[].class;
        String[] result = (String[]) TypeHandler.createValue(str, clazz);
        assertArrayEquals(new String[]{"test", "test2"}, result);
    }

    @Test
    public void testCreateValue_String_Class_Object() {
        String className = "java.lang.String";
        String str = "test";

        Class<Object> clazz = Object.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(className, result.getClass().getName());

        String resultStr = (String) result;
        assertEquals(str, resultStr);
    }

    @Test
    public void testCreateValue_String_Class_Object_InvalidClass() {
        String str = "test";
        Class<Integer> clazz = Integer.class;

        assertThrows(ClassCastException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_String_Class_Number() {
        String str = "123";

        Class<Long> clazz = Long.class;
        Long result = (Long) TypeHandler.createValue(str, clazz);
        assertEquals(123L, result.longValue());

        str = "123.45";
        Class<Double> clazzDouble = Double.class;
        Double resultDouble = (Double) TypeHandler.createValue(str, clazzDouble);
        assertEquals(123.45, resultDouble.doubleValue(), 0.0);
    }

    @Test
    public void testCreateValue_String_Class_Date() {
        String str = "2023-02-01";

        Class<Date> clazz = Date.class;
        Date result = (Date) TypeHandler.createValue(str, clazz);
        assertNotNull(result);

        Date expected = new Date(2023 - 1900, 1, 1);
        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_String_Class_Date_Unsupported() {
        String str = "2023-02-01";

        Class<File> clazz = File.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValue_String_Class_URL() {
        String str = "http://example.com";

        Class<URL> clazz = URL.class;
        URL result = (URL) TypeHandler.createValue(str, clazz);
        assertNotNull(result);

        assertEquals("http", result.getProtocol());
        assertEquals("example.com", result.getHost());
    }

    @Test
    public void testCreateValue_String_Class_URL_Malformed() {
        String str = "http://example.com/not-a-real-url";

        Class<URL> clazz = URL.class;
        assertThrows(MalformedURLException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_String_Class_File() {
        String str = "path/to/file.txt";

        Class<File> clazz = File.class;
        File result = (File) TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertTrue(result.exists());

        assertEquals("path/to/file.txt", result.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_String_Class_File_NotFound() {
        String str = "path/to/nonexistentfile.txt";

        Class<File> clazz = File.class;
        File result = (File) TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateValue_String_Class_FileInputStream() {
        String str = "path/to/file.txt";

        Class<FileInputStream> clazz = FileInputStream.class;
        File file = new File(str);
        FileInputStream result = (FileInputStream) TypeHandler.createValue(str, clazz);

        assertNotNull(result);
        assertTrue(result.available() > 0);

        byte[] expected = new byte[10];
        int read = result.read(expected, 0, expected.length);
        assertFalse(read == -1);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_String_Class_FileInputStream_NotFound() {
        String str = "path/to/nonexistentfile.txt";

        Class<FileInputStream> clazz = FileInputStream.class;
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_String_Class_Class() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String className = "java.util.Date";
        String str = "2023-02-01";

        Class<Class<?>> clazz = Class.class;
        Class<?> result = (Class<?>) TypeHandler.createValue(str, clazz);
        assertEquals(Class.forName(className), result);

        Object instance = result.getDeclaredConstructor().newInstance();
        Date date = (Date) result.getMethod("fromString", String.class).invoke(instance, str);
        assertEquals(new Date(2023 - 1900, 1, 1), date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_String_Class_Class_Unsupported() {
        String className = "java.util.Properties";

        Class<Class<?>> clazz = Class.class;
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(className, clazz));
    }
}