package org.apache.commons.cli.test;

import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * TypeHandlerTest class for testing the TypeHandler methods.
 */
public class TypeHandlerTest {

    /**
     * Test createValue(String, Object) method with String argument.
     */
    @Test
    public void testCreateValueStringObject() {
        Object result = TypeHandler.createValue("test", String.class);
        Assert.assertEquals("test", result);
    }

    /**
     * Test createValue(String, Object) method with ObjectType argument.
     */
    @Test
    public void testCreateValueStringObjectType() {
        Object result = TypeHandler.createValue("test", Object.class);
        Assert.assertNull(result);
    }

    /**
     * Test createValue(String, Class) method with StringArgument and String class.
     */
    @Test
    public void testCreateValueStringClassString() {
        Object result = TypeHandler.createValue("test", String.class);
        Assert.assertEquals("test", result);
    }

    /**
     * Test createValue(String, Class) method with Object type and String class.
     */
    @Test
    public void testCreateValueStringClassObject() {
        Object result = TypeHandler.createValue("test", Object.class);
        Assert.assertNull(result);
    }

    /**
     * Test createValue(String, Class) method with correct classes.
     */
    @Test
    public void testCreateValueStringClassCorrect() {
        Class clazz = PatternOptionBuilder.STRING_VALUE.getClass();
        Object result = TypeHandler.createValue("test", clazz);
        Assert.assertEquals("test", result);
    }

    /**
     * Test createObject(String) method with valid class name.
     */
    @Test
    public void testCreateObjectValidClassName() {
        Class clazz = String.class;
        Object result = TypeHandler.createObject("java.lang.String");
        Assert.assertSame(clazz, result.getClass());
    }

    /**
     * Test createObject(String) method with invalid class name.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectInvalidClassName() {
        Object result = TypeHandler.createObject("org.apache.commons.non.existent.class");
    }

    /**
     * Test createNumber(String) method with valid number.
     */
    @Test
    public void testCreateNumberValidNumber() {
        Double number = TypeHandler.createNumber("123.45");
        Assert.assertEquals(123.45, number.doubleValue());
    }

    /**
     * Test createNumber(String) method with invalid number.
     */
    @Test
    public void testCreateNumberInvalidNumber() {
        Number number = TypeHandler.createNumber("test");
        Assert.assertNull(number);
    }

    /**
     * Test createClass(String) method with valid class name.
     */
    @Test
    public void testCreateClassValidClassName() {
        Class clazz = String.class;
        Class result = TypeHandler.createClass("java.lang.String");
        Assert.assertSame(clazz, result);
    }

    /**
     * Test createClass(String) method with invalid class name.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateClassInvalidClassName() {
        Class result = TypeHandler.createClass("org.apache.commons.non.existent.class");
    }

    /**
     * Test createDate(String) method with valid date string.
     */
    @Test
    public void testCreateDateValidDateString() {
        Date date = TypeHandler.createDate("2023-02-28");
        Assert.assertNotNull(date);
    }

    /**
     * Test createDate(String) method with invalid date string.
     */
    @Test
    public void testCreateDateInvalidDateString() {
        Date date = TypeHandler.createDate("test");
        Assert.assertNull(date);
    }

    /**
     * Test createURL(String) method with valid URL string.
     */
    @Test
    public void testCreateURLValidURLString() {
        URL url = TypeHandler.createURL("http://www.example.com");
        Assert.assertNotNull(url);
    }

    /**
     * Test createURL(String) method with invalid URL string.
     */
    @Test(expected = MalformedURLException.class)
    public void testCreateURLInvalidURLString() {
        TypeHandler.createURL("test");
    }

    /**
     * Test createFile(String) method with valid file path.
     */
    @Test
    public void testCreateFileValidFilePath() {
        File file = TypeHandler.createFile("path/to/file.txt");
        Assert.assertNotNull(file);
    }

    /**
     * Test createFile(String) method with invalid file path.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInvalidFilePath() {
        TypeHandler.createFile("test");
    }

    /**
     * Test createFiles(String) method with valid file paths.
     */
    @Test
    public void testCreateFilesValidFilePaths() {
        String[] paths = {"path/to/file1.txt", "path/to/file2.txt"};
        File[] files = TypeHandler.createFiles(String.join(File.pathSeparator, paths));
        Assert.assertNotNull(files);
        Assert.assertEquals(2, files.length);
    }

    /**
     * Test createFiles(String) method with invalid file paths.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateFilesInvalidFilePaths() {
        String[] paths = {"test"};
        TypeHandler.createFiles(String.join(File.pathSeparator, paths));
    }
}