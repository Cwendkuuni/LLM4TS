package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "foo";
        Object obj = new Object();
        Object value = TypeHandler.createValue(str, obj.getClass());
        assertNull(value);

        str = "java.lang.String";
        obj = "bar";
        value = TypeHandler.createValue(str, obj.getClass());
        assertEquals(obj, value);

        str = "123";
        obj = Integer.class;
        value = TypeHandler.createValue(str, obj);
        assertEquals(new Integer(123), value);

        obj = Double.class;
        value = TypeHandler.createValue(str, obj);
        assertEquals(new Double(123), value);

        str = "123.45";
        value = TypeHandler.createValue(str, Double.class);
        assertEquals(new Double(123.45), value);

        str = "true";
        obj = Boolean.class;
        value = TypeHandler.createValue(str, obj);
        assertEquals(Boolean.TRUE, value);

        str = "2022-01-01";
        obj = Date.class;
        value = TypeHandler.createValue(str, obj);
        assertNotNull(value);

        str = "java.lang.Class";
        obj = String.class;
        value = TypeHandler.createValue(str, obj.getClass());
        assertEquals(String.class, value);

        str = "com.example.MyClass";
        obj = String.class;
        value = TypeHandler.createValue(str, obj.getClass());
        assertEquals(Class.forName(str), value);

        str = "http://example.com";
        obj = URL.class;
        value = TypeHandler.createValue(str, obj);
        assertEquals(new URL("http://example.com"), value);

        str = "path/to/file.txt";
        obj = File.class;
        value = TypeHandler.createValue(str, obj);
        assertEquals(new File("path/to/file.txt"), value);
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "foo";
        Class<?> clazz = String.class;
        Object value = TypeHandler.createValue(str, clazz);
        assertEquals(str, value);

        str = "java.lang.String";
        clazz = String[].class;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);

        str = "123";
        clazz = Integer.class;
        value = TypeHandler.createValue(str, clazz);
        assertEquals(new Integer(123), value);

        clazz = Double.class;
        value = TypeHandler.createValue(str, clazz);
        assertEquals(new Double(123), value);

        clazz = Double.TYPE;
        value = TypeHandler.createValue(str, clazz);
        assertEquals(123.0, value, 0.0);

        clazz = Float.class;
        value = TypeHandler.createValue(str, clazz);
        assertEquals(new Float(123.0), value);

        clazz = Float.TYPE;
        value = TypeHandler.createValue(str, clazz);
        assertEquals(123.0f, value, 0.0f);

        clazz = Boolean.class;
        value = TypeHandler.createValue(str, clazz);
        assertEquals(Boolean.TRUE, value);

        clazz = PatternOptionBuilder.STRING_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertEquals(str, value);

        clazz = PatternOptionBuilder.OBJECT_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);

        clazz = PatternOptionBuilder.NUMBER_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);

        clazz = PatternOptionBuilder.DATE_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);

        clazz = PatternOptionBuilder.CLASS_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);

        clazz = PatternOptionBuilder.FILE_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);

        clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);

        clazz = PatternOptionBuilder.FILES_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);

        clazz = PatternOptionBuilder.URL_VALUE;
        value = TypeHandler.createValue(str, clazz);
        assertNull(value);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object value = TypeHandler.createObject(className);
        assertEquals(String.class, value.getClass());

        className = "java.util.Date";
        value = TypeHandler.createObject(className);
        assertNotNull(value);

        className = "java.net.URL";
        value = TypeHandler.createObject(className);
        assertEquals(URL.class, value.getClass());

        className = "java.io.File";
        value = TypeHandler.createObject(className);
        assertEquals(File.class, value.getClass());

        className = "org.apache.commons.cli.TypeHandler";
        try {
            value = TypeHandler.createObject(className);
            fail();
        } catch (Throwable e) {
            // Expected
        }

        className = "com.example.MyClass";
        try {
            value = TypeHandler.createObject(className);
            fail();
        } catch (ClassNotFoundException cnfe) {
            // Expected
        }
    }

    @Test
    public void testCreateNumber() {
        String numberStr = "123";
        Number number = TypeHandler.createNumber(numberStr);
        assertEquals(new Integer(123), number);

        numberStr = "123.45";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Double(123.45), number);

        numberStr = "-123";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Integer(-123), number);

        numberStr = "-123.45";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Double(-123.45), number);

        numberStr = "0x123";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Integer(171), number);

        numberStr = "0X123";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Integer(171), number);

        numberStr = "0b1111011";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Integer(131), number);

        numberStr = "0B1111011";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Integer(131), number);

        numberStr = "1.2e3";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Double(1200.0), number);

        numberStr = "1.2E3";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(new Double(1200.0), number);

        numberStr = "infinity";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(Double.POSITIVE_INFINITY, number);

        numberStr = "-infinity";
        number = TypeHandler.createNumber(numberStr);
        assertEquals(Double.NEGATIVE_INFINITY, number);

        numberStr = "NaN";
        number = TypeHandler.createNumber(numberStr);
        assertNotNull(number);
        assertEquals(Double.NaN, number);

        try {
            numberStr = "foo";
            number = TypeHandler.createNumber(numberStr);
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(className);
        assertEquals(String.class, clazz);

        className = "java.util.Date";
        clazz = TypeHandler.createClass(className);
        assertNotNull(clazz);

        className = "java.net.URL";
        clazz = TypeHandler.createClass(className);
        assertEquals(URL.class, clazz);

        className = "java.io.File";
        clazz = TypeHandler.createClass(className);
        assertEquals(File.class, clazz);

        className = "org.apache.commons.cli.TypeHandler";
        try {
            clazz = TypeHandler.createClass(className);
            fail();
        } catch (ClassNotFoundException e) {
            // Expected
        }

        try {
            className = "com.example.MyClass$InnerClass";
            clazz = TypeHandler.createClass(className);
            fail();
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-01-01";
        Date date = TypeHandler.createDate(dateString);
        assertEquals(new Date(0L), date);

        dateString = "2022-01-01 12:34:56.789";
        date = TypeHandler.createDate(dateString);
        assertNotNull(date);

        try {
            dateString = "2022-01-01 12:34:56.789Z";
            date = TypeHandler.createDate(dateString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            dateString = "2022-01-01 12:34:56.789+01:00";
            date = TypeHandler.createDate(dateString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            dateString = "2022-01-01 12:34:56.789+01:00:00";
            date = TypeHandler.createDate(dateString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            dateString = "2022-01-01 12:34:56.789+01:00:00.123";
            date = TypeHandler.createDate(dateString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";
        URL url = TypeHandler.createURL(urlString);
        assertEquals(new URL("http://example.com"), url);

        urlString = "http://example.com/path/to/file.txt";
        url = TypeHandler.createURL(urlString);
        assertEquals(new URL("http://example.com/path/to/file.txt"), url);

        urlString = "http://example.com:8080/path/to/file.txt";
        url = TypeHandler.createURL(urlString);
        assertEquals(new URL("http://example.com:8080/path/to/file.txt"), url);

        urlString = "https://example.com";
        url = TypeHandler.createURL(urlString);
        assertEquals(new URL("https://example.com"), url);

        urlString = "https://example.com/path/to/file.txt";
        url = TypeHandler.createURL(urlString);
        assertEquals(new URL("https://example.com/path/to/file.txt"), url);

        urlString = "https://example.com:8080/path/to/file.txt";
        url = TypeHandler.createURL(urlString);
        assertEquals(new URL("https://example.com:8080/path/to/file.txt"), url);

        try {
            urlString = "http://example.com:8080/path/to/file.txt#fragment";
            url = TypeHandler.createURL(urlString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            urlString = "http://example.com/#fragment";
            url = TypeHandler.createURL(urlString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            urlString = "http://example.com:8080/#fragment";
            url = TypeHandler.createURL(urlString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            urlString = "http://example.com/path/to/file.txt#fragment?query=string";
            url = TypeHandler.createURL(urlString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            urlString = "http://example.com/#fragment?query=string";
            url = TypeHandler.createURL(urlString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            urlString = "http://example.com:8080/#fragment?query=string";
            url = TypeHandler.createURL(urlString);
            fail();
        } catch (IllegalArgumentException iae) {
            // Expected
        }
    }

    @Test
    public void testCreateFile() {
        String filePath = "path/to/file.txt";
        File file = TypeHandler.createFile(filePath);
        assertEquals(new File(filePath), file);

        filePath = "/path/to/file.txt";
        file = TypeHandler.createFile(filePath);
        assertEquals(new File("/path/to/file.txt"), file);

        filePath = "path/to/file.txt";
        file = TypeHandler.createFile(filePath);
        assertNotNull(file);

        try {
            filePath = null;
            file = TypeHandler.createFile(filePath);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            filePath = "path/to/nonexistent/file.txt";
            file = TypeHandler.createFile(filePath);
            fail();
        } catch (FileNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFiles() {
        String filePaths = "path/to/file1.txt,path/to/file2.txt";
        File[] files = TypeHandler.createFiles(filePaths);
        assertEquals(new File[] {new File("path/to/file1.txt"), new File("path/to/file2.txt")}, files);

        try {
            filePaths = null;
            files = TypeHandler.createFiles(filePaths);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }
}