package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.ClassLoaderUtils;

import org.junit.Test;

/**
 * Test cases for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String object.
     */
    @Test
    public void testCreateStringValue()
    {
        String testStr = "testString";
        assertEquals(testStr, TypeHandler.createValue(testStr, String.class));
    }

    /**
     * Test createValue method with Object object.
     */
    @Test
    public void testCreateObjectValueEmptyConstructor()
    {
        try
        {
            Class<?> clazz = Class.forName("java.util.Date");
            Object obj = TypeHandler.createValue("", clazz);
            assertInstanceOf(java.util.Date.class, obj);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Unable to find Date class", e);
        }
    }

    /**
     * Test createValue method with Object object with constructor.
     */
    @Test
    public void testCreateObjectValueWithConstructor() throws Exception
    {
        Class<?> clazz = Class.forName("java.lang.Integer");
        Object obj = TypeHandler.createValue("42", clazz);
        assertInstanceOf(java.lang.Integer.class, obj);
        assertEquals(42, obj);
    }

    /**
     * Test createValue method with Number class.
     */
    @Test
    public void testCreateNumberValue()
    {
        long testLong = 123;
        double testDouble = 3.14;

        assertEquals(testLong, TypeHandler.createValue("123", Long.class));
        assertEquals(testDouble, TypeHandler.createValue("3.14", Double.class));
    }

    /**
     * Test createValue method with Date class.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateValue()
    {
        String testDateStr = "2022-03-01";
        TypeHandler.createValue(testDateStr, Date.class);
    }

    /**
     * Test createValue method with Class class.
     */
    @Test
    public void testCreateClassValue()
    {
        String testClassName = "java.util.Date";
        Class<?> clazz = TypeHandler.createValue(testClassName, Class.class);
        assertEquals(Class.forName(testClassName), clazz);
    }

    /**
     * Test createValue method with File class.
     */
    @Test
    public void testCreateFileValue()
    {
        String testFilePath = "testFile.txt";
        File file = new File(testFilePath);
        File testFile = TypeHandler.createValue(testFilePath, File.class);
        assertEquals(file, testFile);
    }

    /**
     * Test createValue method with existing File class.
     */
    @Test(expected = FileNotFoundException.class)
    public void testOpenFileValue()
    {
        String testFilePath = "testFile.nonExistent";
        TypeHandler.openFile(testFilePath);
    }

    /**
     * Test createValue method with Files class.
     * This method is not yet implemented in TypeHandler.
     * Instead, we test it using ClassLoaderUtils.
     */
    @Test
    public void testCreateFilesValue()
    {
        String testFilePath = "testFiles/";
        File testDirectory = new File(testFilePath);
        File[] files = new File("testFiles/testFile1.txt").listFiles();
        File[] testFiles = ClassLoaderUtils.fileList(testFilePath);

        assertEquals(files.length, testFiles.length);
        for (int i = 0; i < files.length; i++)
        {
            assertEquals(files[i].getName(), testFiles[i].getName());
        }
    }

    /**
     * Test createValue method with URL class.
     */
    @Test
    public void testCreateURLValue()
    {
        String testURLString = "http://example.com";
        URL testURL = new URL(testURLString);
        URL url = TypeHandler.createValue(testURLString, URL.class);
        assertEquals(testURL, url);
    }

    /**
     * Test createValue method with unsupported class.
     */
    @Test(expected = ParseException.class)
    public void testCreateUnsupportedValue()
    {
        String testValue = "testValue";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(testValue, Boolean.class));
    }
}