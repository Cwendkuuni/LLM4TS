package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Test for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test creating string value.
     */
    @Test
    public void testStringValue() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(str, obj);
    }

    /**
     * Test creating object value with valid class name.
     */
    @Test
    public void testObjectValueValidClassName() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createValue(classname, String.class);
        Assert.assertNotNull(obj);
    }

    /**
     * Test creating object value with invalid class name.
     */
    @Test(expected = ParseException.class)
    public void testObjectValueInvalidClassName() throws ParseException
    {
        String classname = "java.lan.ging";
        TypeHandler.createValue(classname, String.class);
    }

    /**
     * Test creating number value with valid number.
     */
    @Test
    public void testNumberValueLong() throws ParseException
    {
        String str = "123";
        Number number = TypeHandler.createValue(str, Long.class);
        Assert.assertEquals(123L, number.longValue());
    }

    /**
     * Test creating number value with valid double.
     */
    @Test
    public void testNumberValueDouble() throws ParseException
    {
        String str = "123.45";
        Number number = TypeHandler.createValue(str, Double.class);
        Assert.assertEquals(123.45, number.doubleValue(), 0.0001);
    }

    /**
     * Test creating date value with valid date string.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDateValue() throws ParseException
    {
        String str = "2021-12-31";
        Date date = TypeHandler.createValue(str, Date.class);
        Assert.assertNotNull(date);
    }

    /**
     * Test creating URL value with valid URL string.
     */
    @Test
    public void testURLValue() throws ParseException, MalformedURLException
    {
        String str = "https://www.example.com";
        URL url = TypeHandler.createValue(str, URL.class);
        Assert.assertNotNull(url);
        Assert.assertEquals("https://www.example.com", url.toString());
    }

    /**
     * Test creating file value with valid file path.
     */
    @Test
    public void testFileValue() throws ParseException
    {
        String str = "path/to/file.txt";
        File file = TypeHandler.createValue(str, File.class);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.exists());
    }

    /**
     * Test creating file value with invalid file path.
     */
    @Test(expected = ParseException.class)
    public void testFileValueInvalidPath() throws ParseException
    {
        String str = "path/to/non_existent_file.txt";
        TypeHandler.createValue(str, File.class);
    }

    /**
     * Test creating file value array with valid file paths.
     */
    @Test
    public void testFilesValue() throws ParseException, IOException
    {
        String str = "path/to/file1.txt, path/to/file2.txt";
        File[] files = TypeHandler.createFiles(str);
        Assert.assertArrayEquals(new File[]{"path/to/file1.txt", "path/to/file2.txt"}, files);
        for (File file : files)
        {
            Assert.assertNotNull(file);
            Assert.assertTrue(file.exists());
        }
    }

    /**
     * Test creating file value array with invalid file path.
     */
    @Test(expected = ParseException.class)
    public void testFilesValueInvalidPath() throws ParseException
    {
        String str = "path/to/non_existent_file1.txt, path/to/non_existent_file2.txt";
        TypeHandler.createFiles(str);
    }

    /**
     * Test creating file value array with empty string.
     */
    @Test(expected = ParseException.class)
    public void testFilesValueEmptyString() throws ParseException
    {
        String str = "";
        TypeHandler.createFiles(str);
    }
}