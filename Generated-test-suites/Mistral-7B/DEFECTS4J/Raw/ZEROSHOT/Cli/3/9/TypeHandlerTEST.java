package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * TypeHandlerTest class file.
 */
public class TypeHandlerTest {

    /**
     * Test createValue(String, Object) method with String object.
     */
    @Test
    public void testCreateValueStringString() {
        String str = "testString";
        String expected = "testString";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(expected, obj);
    }

    /**
     * Test createValue(String, Object) method with PatternOptionBuilder.STRING_VALUE.
     */
    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, obj);
    }

    /**
     * Test createValue(String, Object) method with null object.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateValueStringNullObject() {
        String str = "testString";
        TypeHandler.createValue(str, null);
    }

    /**
     * Test createValue(String, Class) method with String class.
     */
    @Test
    public void testCreateValueStringClass() {
        String str = "testString";
        Class<String> clazz = String.class;
        String expected = "testString";
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(expected, obj);
    }

    /**
     * Test createValue(String, Class) method with PatternOptionBuilder.STRING_VALUE class.
     */
    @Test
    public void testCreateValueStringClassPatternOptionBuilderStringValue() {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, obj);
    }

    /**
     * Test createValue(String, Class) method with other classes.
     */
    @Test
    public void testCreateValueStringClassOther() {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, Integer.class);
        assertNull(obj);
    }

    /**
     * Test createValue(String, Object) method with Object object.
     */
    @Test
    public void testCreateValueStringObject() {
        String str = "testClass";
        Class<Object> clazz = Object.class;

        try {
            Class<?> cl = Class.forName(str);
            Object obj = TypeHandler.createValue(str, clazz);
            assertSame(cl.newInstance(), obj);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test createValue(String, Object) method with null object.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateValueStringObjectNull() {
        String str = "testClass";
        Object obj = null;
        TypeHandler.createValue(str, obj.getClass());
    }

    /**
     * Test createValue(String, Class) method with Object class.
     */
    @Test
    public void testCreateValueStringClassObject() {
        String str = "testClass";
        Object obj = null;

        try {
            Class<?> cl = Class.forName(str);
            obj = cl.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }

        Class<Object> clazz = Object.class;
        Object createdObj = TypeHandler.createValue(str, clazz);
        assertSame(obj, createdObj);
    }

    /**
     * Test createValue(String, Class) method with other classes.
     */
    @Test
    public void testCreateValueStringClassOtherClass() {
        String str = "java.util.Date";
        Object obj = null;

        try {
            Class<?> cl = Class.forName(str);
            obj = cl.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }

        Class<Object> clazz = Object.class;
        Object createdObj = TypeHandler.createValue(str, clazz);
        assertNull(createdObj);
    }

    /**
     * Test createNumber(String) method with valid number.
     */
    @Test
    public void testCreateNumberValidNumber() {
        String str = "12345";
        Number number = TypeHandler.createNumber(str);
        assertEquals(12345, number.intValue());
    }

    /**
     * Test createNumber(String) method with invalid string.
     */
    @Test
    public void testCreateNumberInvalidString() {
        String str = "testString";
        Number number = TypeHandler.createNumber(str);
        assertNull(number);
    }

    /**
     * Test createDate(String) method with valid date string.
     */
    @Test
    public void testCreateDateValidDateString() {
        String str = "2023-03-17";
        Date date = TypeHandler.createDate(str);
        assertNotNull(date);
    }

    /**
     * Test createDate(String) method with invalid date string.
     */
    @Test
    public void testCreateDateInvalidDateString() {
        String str = "testString";
        Date date = TypeHandler.createDate(str);
        assertNull(date);
    }

    /**
     * Test createURL(String) method with valid URL string.
     */
    @Test
    public void testCreateURLValidURLString() {
        String str = "http://test.com";
        URL url = TypeHandler.createURL(str);
        assertNotNull(url);
    }

    /**
     * Test createURL(String) method with invalid URL string.
     */
    @Test(expected = MalformedURLException.class)
    public void testCreateURLInvalidURLString() {
        String str = "testString";
        TypeHandler.createURL(str);
    }

    /**
     * Test createFile(String) method with valid file path.
     */
    @Test
    public void testCreateFileValidFilePath() {
        String str = "testFile.txt";
        File file = TypeHandler.createFile(str);
        assertNotNull(file);
    }

    /**
     * Test createFile(String) method with invalid file path.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInvalidFilePath() {
        String str = "testFile.txtx";
        TypeHandler.createFile(str);
    }

    /**
     * Test createFiles(String) method with valid file paths.
     */
    @Test
    public void testCreateFilesValidFilePaths() {
        String str = "testFile1.txt,testFile2.txt";
        File[] files = TypeHandler.createFiles(str);
        assertNotNull(files);
        assertEquals(2, files.length);
    }

    /**
     * Test createFiles(String) method with invalid file paths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFilesInvalidFilePaths() {
        String str = "testFile.txtx";
        TypeHandler.createFiles(str);
    }
}