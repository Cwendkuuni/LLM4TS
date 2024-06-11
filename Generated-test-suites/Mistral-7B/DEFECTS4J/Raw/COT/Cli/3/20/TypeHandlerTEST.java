package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest {

    /**
     * Test createValue method with string object.
     */
    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    /**
     * Test createValue method with PatternOptionBuilder.STRING_VALUE.
     */
    @Test
    public void testCreateValueStringStringValue() {
        String str = "test";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, result);
    }

    /**
     * Test createValue method with null string and Object.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateValueNullStringObject() {
        Object obj = new Object();
        String str = null;
        TypeHandler.createValue(str, obj.getClass());
    }

    /**
     * Test createValue method with null string and PatternOptionBuilder.STRING_VALUE.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateValueNullStringStringValue() {
        String str = null;
        TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
    }

    /**
     * Test createValue method with invalid PatternOptionBuilder.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInvalidPatternOptionBuilder() {
        String str = "test";
        int clazz = 1;
        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test createObject method with valid class name.
     */
    @Test
    public void testCreateObjectValidClassName() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    /**
     * Test createObject method with invalid class name.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectInvalidClassName() {
        String className = "InvalidClassName";
        TypeHandler.createObject(className);
    }

    /**
     * Test createObject method with null class name.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateObjectNullClassName() {
        String className = null;
        TypeHandler.createObject(className);
    }

    /**
     * Test createNumber method with valid number string.
     */
    @Test
    public void testCreateNumberValidNumberString() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertEquals(123, result.longValue());
    }

    /**
     * Test createNumber method with invalid number string.
     */
    @Test
    public void testCreateNumberInvalidNumberString() {
        String str = "abc";
        Number result = TypeHandler.createNumber(str);
        assertNull(result);
    }

    /**
     * Test createNumber method with null number string.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateNumberNullNumberString() {
        String str = null;
        TypeHandler.createNumber(str);
    }

    /**
     * Test createDate method with valid date string.
     */
    @Test
    public void testCreateDateValidDateString() {
        String str = "2022-01-01";
        Date result = TypeHandler.createDate(str);
        assertNotNull(result);
    }

    /**
     * Test createDate method with invalid date string.
     */
    @Test
    public void testCreateDateInvalidDateString() {
        String str = "2022-01-33";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }

    /**
     * Test createDate method with null date string.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateDateNullDateString() {
        String str = null;
        TypeHandler.createDate(str);
    }

    /**
     * Test createURL method with valid URL string.
     */
    @Test
    public void testCreateURLValidURLString() {
        String str = "http://test.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
    }

    /**
     * Test createURL method with invalid URL string.
     */
    @Test(expected = MalformedURLException.class)
    public void testCreateURLInvalidURLString() {
        String str = "http://test.com/invalid";
        TypeHandler.createURL(str);
    }

    /**
     * Test createURL method with null URL string.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateURLNullURLString() {
        String str = null;
        TypeHandler.createURL(str);
    }

    /**
     * Test createFile method with valid file path.
     */
    @Test
    public void testCreateFileValidFilePath() {
        String path = "/path/to/file.txt";
        File result = TypeHandler.createFile(path);
        assertNotNull(result);
        assertTrue(result.exists());
    }

    /**
     * Test createFile method with invalid file path.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInvalidFilePath() {
        String path = "/path/to/invalidfile.txt";
        TypeHandler.createFile(path);
    }

    /**
     * Test createFile method with null file path.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateFileNullFilePath() {
        String path = null;
        TypeHandler.createFile(path);
    }

    /**
     * Test createFiles method with valid file paths.
     */
    @Test
    public void testCreateFilesValidFilePaths() {
        String paths = "/path/to/file1.txt,/path/to/file2.txt";
        File[] result = TypeHandler.createFiles(paths);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    /**
     * Test createFiles method with invalid file paths.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateFilesInvalidFilePaths() {
        String paths = "/path/to/invalidfile1.txt,/path/to/invalidfile2.txt";
        TypeHandler.createFiles(paths);
    }

    /**
     * Test createFiles method with null file paths.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateFilesNullFilePaths() {
        String paths = null;
        TypeHandler.createFiles(paths);
    }
}