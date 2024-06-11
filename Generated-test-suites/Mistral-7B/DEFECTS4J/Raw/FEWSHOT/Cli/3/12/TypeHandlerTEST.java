package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "java.lang.String";
        Class<?> objClass = String.class;
        Object expected = "test";

        Object result = TypeHandler.createValue(expected.toString(), objClass);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_StringClass() {

        String str = "123";
        Class<?> clazz = int.class;
        int expected = 123;

        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(expected, result);

        str = "123.45";
        clazz = double.class;
        double expectedDouble = 123.45;
        Object resultDouble = TypeHandler.createValue(str, clazz);
        assertEquals(expectedDouble, resultDouble);

        str = "true";
        clazz = boolean.class;
        boolean expectedBoolean = true;
        Object resultBoolean = TypeHandler.createValue(str, clazz);
        assertEquals(expectedBoolean, resultBoolean);

        str = "null";
        clazz = String.class;
        Object resultNull = TypeHandler.createValue(str, clazz);
        assertNull(resultNull);

        str = "java.lang.Date";
        Class<?> dateClass = Date.class;
        Date expectedDate = new Date();
        Object resultDate = TypeHandler.createValue(str, dateClass);
        assertEquals(expectedDate, resultDate);

        String invalidDateStr = "InvalidDate";
        assertNull(TypeHandler.createValue(invalidDateStr, dateClass));

        str = "java.io.File";
        File expectedFile = new File("test.txt");
        Object resultFile = TypeHandler.createValue(str, File.class);
        assertSame(expectedFile, resultFile);

        str = "java.net.URL";
        URL expectedURL = new URL("http://www.example.com");
        Object resultURL = TypeHandler.createValue(str, URL.class);
        assertEquals(expectedURL, resultURL);

        str = "java.lang.Class";
        String className = "org.apache.commons.cli.TypeHandlerTest$DummyClass";
        Object expectedClass = null;
        try {
            Class<?> dummyClass = ClassUtils.forName(className, TypeHandlerTest.class.getClassLoader());
            expectedClass = dummyClass;
        } catch (ClassNotFoundException e) {
            // ignored
        }
        Class<?> classType = Class.class;
        Object resultClass = TypeHandler.createValue(str, classType);
        assertSame(expectedClass, resultClass);

        str = "org.apache.commons.cli.PatternOptionBuilder.FILE_VALUE";
        Class<?> fileValueClass = File[].class;
        File[] expectedFiles = new File[] {new File("file1.txt"), new File("file2.txt")};
        Object resultFiles = TypeHandler.createValue(str, fileValueClass);
        assertArrayEquals(expectedFiles, (File[]) resultFiles);

        String invalidFilePath = "invalid/path/to/file";
        assertNull(TypeHandler.createValue(invalidFilePath, fileValueClass));

        str = "java.lang.NumberUtils";
        assertNull(TypeHandler.createValue(str, Number.class));

        str = "java.util.Date";
        assertNull(TypeHandler.createValue(str, int.class));

        str = "java.io.File[]";
        String[] filePaths = {"file1.txt", "file2.txt"};
        Object resultFileArray = TypeHandler.createValue(StringUtils.join(filePaths, File.pathSeparator), fileValueClass);
        assertArrayEquals(filePaths, (File[]) resultFileArray);

        str = "java.lang.NullPointerException";
        assertNull(TypeHandler.createValue(str, String.class));
    }

    private static class DummyClass {
        // empty class
    }

    @Test
    public void testCreateObject() {
        String className = "org.apache.commons.cli.TypeHandlerTest$DummyClass";

        Class<?> expectedClass = null;
        try {
            Class<?> dummyClass = ClassUtils.forName(className, TypeHandlerTest.class.getClassLoader());
            expectedClass = dummyClass;
        } catch (ClassNotFoundException e) {
            // ignored
        }

        Object result = TypeHandler.createObject(className);
        assertSame(expectedClass, result.getClass());
    }

    @Test
    public void testCreateNumber() {
        String numberString = "123";
        Number expectedNumber = 123;

        Number result = TypeHandler.createNumber(numberString);
        assertEquals(expectedNumber.intValue(), result.intValue());

        numberString = "123.45";
        expectedNumber = 123.45;
        result = TypeHandler.createNumber(numberString);
        assertEquals(expectedNumber.doubleValue(), result.doubleValue(), 0.0);

        String invalidNumberString = "InvalidNumber";
        assertNull(TypeHandler.createNumber(invalidNumberString));
    }

    @Test
    public void testCreateClass() {
        String className = "org.apache.commons.cli.TypeHandlerTest$DummyClass";

        Class<?> expectedClass = null;
        try {
            Class<?> dummyClass = ClassUtils.forName(className, TypeHandlerTest.class.getClassLoader());
            expectedClass = dummyClass;
        } catch (ClassNotFoundException e) {
            // ignored
        }

        Class<?> resultClass = (Class<?>) TypeHandler.createClass(className);
        assertSame(expectedClass, resultClass);

        String invalidClassName = "InvalidClass";
        assertNull(TypeHandler.createClass(invalidClassName));
    }

    @Test
    public void testCreateDate() throws ParseException {
        String dateString = "2022-01-01";
        Date expectedDate = new Date(0);
        expectedDate.setTime(0);
        expectedDate.setYear(2022);
        expectedDate.setMonth(0);
        expectedDate.setDate(1);

        Date resultDate = (Date) TypeHandler.createDate(dateString);
        assertEquals(expectedDate.getTime(), resultDate.getTime());

        String invalidDateString = "InvalidDate";
        assertNull(TypeHandler.createDate(invalidDateString));
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        URL expectedURL = new URL(urlString);

        URL resultURL = (URL) TypeHandler.createURL(urlString);
        assertEquals(expectedURL.toString(), resultURL.toString());

        String invalidURLString = "InvalidURL";
        assertNull(TypeHandler.createURL(invalidURLString));
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File expectedFile = new File(filePath);

        File resultFile = (File) TypeHandler.createFile(filePath);
        assertEquals(expectedFile.getAbsolutePath(), resultFile.getAbsolutePath());

        String invalidFilePath = "InvalidPath";
        assertNull(TypeHandler.createFile(invalidFilePath));
    }

    @Test
    public void testCreateFiles() {
        String filePathsString = "test1.txt,test2.txt";
        File[] expectedFiles = new File[] {new File("test1.txt"), new File("test2.txt")};

        File[] resultFiles = (File[]) TypeHandler.createFiles(filePathsString);
        assertArrayEquals(expectedFiles, resultFiles);

        String invalidFilePathString = "InvalidPath,InvalidPath";
        assertNull(TypeHandler.createFiles(invalidFilePathString));
    }

    @Test
    public void testCreateURLs() {
        String urlStrings = "http://www.example1.com,http://www.example2.com";
        URL[] expectedURLs = new URL[] {new URL("http://www.example1.com"), new URL("http://www.example2.com")};

        URL[] resultURLs = (URL[]) TypeHandler.createFiles(urlStrings);
        assertArrayEquals(expectedURLs, resultURLs);

        String invalidURLString = "InvalidURL";
        assertNull(TypeHandler.createURL(invalidURLString));
    }

    @Test
    public void testCreateFiles_emptyPath() {
        String filePathsString = "";
        File[] expectedFiles = new File[0];

        File[] resultFiles = (File[]) TypeHandler.createFiles(filePathsString);
        assertArrayEquals(expectedFiles, resultFiles);
    }

    @Test
    public void testCreateFiles_nullPath() {
        String filePathsString = null;
        File[] expectedFiles = new File[0];

        File[] resultFiles = (File[]) TypeHandler.createFiles(filePathsString);
        assertArrayEquals(expectedFiles, resultFiles);
    }

    @Test
    public void testCreateURLs_emptyString() {
        String urlStrings = "";
        URL[] expectedURLs = new URL[0];

        URL[] resultURLs = (URL[]) TypeHandler.createFiles(urlStrings);
        assertArrayEquals(expectedURLs, resultURLs);
    }

    @Test
    public void testCreateURLs_nullString() {
        String urlStrings = null;
        URL[] expectedURLs = new URL[0];

        URL[] resultURLs = (URL[]) TypeHandler.createFiles(urlStrings);
        assertArrayEquals(expectedURLs, resultURLs);
    }

    @Test
    public void testCreateFiles_invalidPath() throws IOException {
        String filePathsString = "/../test.txt";
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return false;
            }
        };
        File[] expectedFiles = new File[0];

        File[] resultFiles = (File[]) TypeHandler.createFiles(filePathsString, filter);
        assertArrayEquals(expectedFiles, resultFiles);

        String invalidFilePathString = "InvalidPath";
        assertNull(TypeHandler.createFiles(invalidFilePathString));
    }

    @Test
    public void testCreateURLs_invalidURL() {
        String urlStrings = "InvalidURL";
        assertNull(TypeHandler.createFiles(urlStrings));
    }

    @Test
    public void testCreateURLs_loadClassFromURL() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String urlStrings = "file:/path/to/test/TypeHandlerTest.class";
        URL[] expectedURLs = {new URL("file:/path/to/test/TypeHandlerTest.class")};

        URL[] resultURLs = (URL[]) TypeHandler.createFiles(urlStrings);
        assertArrayEquals(expectedURLs, resultURLs);

        Class<?> resultClass = (Class<?>) TypeHandler.createValue(String.valueOf(Class.forName("org.apache.commons.cli.TypeHandlerTest").hashCode()), Class.class);
        assertEquals(TypeHandlerTest.class, resultClass);
    }

    @Test
    public void testCreateURLs_loadClassFromJarURL() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String urlStrings = "jar:file:/path/to/test/commons-cli-3.3.2.jar!/org/apache/commons/cli/TypeHandlerTest.class";
        URL[] expectedURLs = {new URL("jar:file:/path/to/test/commons-cli-3.3.2.jar!/org/apache/commons/cli/TypeHandlerTest.class")};

        URL[] resultURLs = (URL[]) TypeHandler.createFiles(urlStrings);
        assertArrayEquals(expectedURLs, resultURLs);

        ClassLoader classLoader = new URLClassLoader(new URL[] {new URL("jar:file:/path/to/test/commons-cli-3.3.2.jar!")}, TypeHandlerTest.class.getClassLoader());
        Class<?> resultClass = (Class<?>) classLoader.loadClass("org.apache.commons.cli.TypeHandlerTest");
        assertEquals(TypeHandlerTest.class, resultClass);
    }
}