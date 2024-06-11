package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class TypeHandlerTest {

    private String strValues[] = new String[]{"123", "456.7", "\"foo\"", "MyClass.class", "File.txt", "http://example.com", "C:/path/to/file.txt", "/tmp/file1.txt,/tmp/file2.txt"};

    @Before
    public void initialize() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testCreateValue_StringObject() {
        for (String str : strValues) {
            Object obj = null;
            try {
                obj = TypeHandler.createValue(str, String.class);
                assertInstanceOf(String.class, obj);
                assertEquals(str, obj);
            } catch (ParseException e) {
                // Intentionally empty
            }
            try {
                obj = TypeHandler.createValue(str, Object.class);
                assertInstanceOf(String.class, obj);
                assertEquals(str, obj);
            } catch (ParseException e) {
                // Intentionally empty
            }
            for (Class<?> clazz : new Class<?>[]{Integer.class, Double.class, MyClass.class, File.class, URL.class}) {
                assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));
            }
            try {
                assertNull(TypeHandler.createValue(str, Date.class));
            } catch (ParseException e) {
                // Intentionally empty
            }
        }
    }

    @Test
    public void testCreateValue_StringClass() {
        for (String str : strValues) {
            for (Class<?> clazz : new Class<?>[]{String.class, Integer.class, Double.class, MyClass.class, File.class, URL.class, Date.class}) {
                try {
                    Object result = TypeHandler.createValue(str, clazz);
                    if (clazz == String.class) {
                        assertInstanceOf(String.class, result);
                        assertEquals(str, result);
                    } else if (clazz == Integer.class) {
                        assertInstanceOf(Integer.class, result);
                        assertEquals(123, ((int) result));
                    } else if (clazz == Double.class) {
                        assertInstanceOf(Double.class, result);
                        assertEquals(456.7, ((Double) result).doubleValue());
                    } else if (clazz == MyClass.class) {
                        assertInstanceOf(MyClass.class, result);
                    } else if (clazz == File.class) {
                        File expectedFile = new File(str);
                        assertInstanceOf(File.class, result);
                        File resultFile = (File) result;
                        assertEquals(expectedFile.getAbsolutePath(), resultFile.getAbsolutePath());
                    } else if (clazz == URL.class) {
                        URL expectedURL = new URL("http://example.com");
                        URL resultURL = (URL) TypeHandler.createValue(str, clazz);
                        assertEquals(expectedURL.toExternalForm(), resultURL.toExternalForm());
                    } else if (clazz == Date.class) {
                        Date expectedDate = dateFormat.parse("2000-01-01 12:00:00");
                        Date resultDate = (Date) TypeHandler.createValue(str, clazz);
                        assertEquals(expectedDate, resultDate);
                    }
                } catch (ParseException e) {
                    // Intentionally empty
                }
            }
        }
    }

    @Test
    public void testCreateObject() throws ParseException {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertInstanceOf(String.class, obj);
            assertNull(obj.getClass().newInstance());
        } catch (Exception e) {
            // Intentionally empty
        }
        try {
            Object obj = TypeHandler.createObject("MyClass");
            assertInstanceOf(MyClass.class, obj);
            assertNotNull(obj.getClass().newInstance());
        } catch (Exception e) {
            throw new ParseException("Unable to create an instance of: MyClass");
        }
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Object obj = TypeHandler.createNumber("123");
        assertInstanceOf(Integer.class, obj);
        assertEquals(123, ((int) obj));

        obj = TypeHandler.createNumber("456.7");
        assertInstanceOf(Double.class, obj);
        assertEquals(456.7, ((Double) obj).doubleValue());
    }

    @Test
    public void testCreateClass() throws ParseException {
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertInstanceOf(String.class, clazz);
        } catch (ClassNotFoundException e) {
            throw new ParseException("Unable to find the class: java.lang.String");
        }

        try {
            Class<?> clazz = TypeHandler.createClass("MyClass");
            assertInstanceOf(MyClass.class, clazz);
        } catch (ClassNotFoundException e) {
            throw new ParseException("Unable to find the class: MyClass");
        }
    }

    @Test
    public void testCreateDate() throws ParseException {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate("2000-01-01"));
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL expectedURL = new URL("http://example.com");
        URL resultURL = TypeHandler.createURL("http://example.com");
        assertEquals(expectedURL.toExternalForm(), resultURL.toExternalForm());

        assertThrows(ParseException.class, () -> TypeHandler.createURL("invalid_url"));
    }

    @Test
    public void testCreateFile() {
        File expectedFile = new File("C:/path/to/file.txt");
        File resultFile = TypeHandler.createFile("C:/path/to/file.txt");
        assertEquals(expectedFile.getAbsolutePath(), resultFile.getAbsolutePath());
    }

    @Test
    public void testOpenFile() throws ParseException, IOException {
        File expectedFile = new File("C:/path/to/file.txt");
        FileInputStream expectedFileInputStream = new FileInputStream(expectedFile);
        FileInputStream resultFileInputStream = TypeHandler.openFile("C:/path/to/file.txt");
        assertEquals(expectedFileInputStream, resultFileInputStream);

        assertThrows(ParseException.class, () -> TypeHandler.openFile("invalid_path"));
    }

    @Test
    public void testCreateFiles() {
        String[] expectedFilesPaths = {"C:/path/to/file1.txt", "C:/path/to/file2.txt"};
        File[] expectedFiles = new File[expectedFilesPaths.length];
        for (int i = 0; i < expectedFilesPaths.length; i++) {
            expectedFiles[i] = new File(expectedFilesPaths[i]);
        }
        File[] resultFiles = TypeHandler.createFiles("C:/path/to/file1.txt,C:/path/to/file2.txt");
        assertArrayEquals(expectedFiles, resultFiles);

        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles("invalid_paths"));
    }
}