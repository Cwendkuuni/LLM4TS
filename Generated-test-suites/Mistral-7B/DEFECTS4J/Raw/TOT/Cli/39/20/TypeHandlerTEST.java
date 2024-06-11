package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    /**
     * Test for createValue(String, Class) method.
     */
    @Test
    public void testCreateValue() throws Exception
    {
        // Test for String value
        String strValue = "test";
        Class<?> stringClass = String.class;
        Object value = TypeHandler.createValue(strValue, stringClass);
        Assert.assertEquals(strValue, value);

        // Test for Object value
        String classname = "java.util.Date";
        Class<?> dateClass = Class.forName(classname);
        Object dateValue = TypeHandler.createValue("2022-12-31", dateClass);
        Assert.assertNotNull(dateValue);

        // Test for Number value
        String numberStr = "123";
        Class<?> numberClass = int.class;
        Object numberValue = TypeHandler.createValue(numberStr, numberClass);
        Assert.assertEquals(123, numberValue.intValue());

        // Test for Date value (unsupported)
        String dateStr = "2022-12-31";
        Class<?> dateType = Date.class;
        try
        {
            Object dateObj = TypeHandler.createValue(dateStr, dateType);
            Assert.fail("Should throw UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Expected
        }

        // Test for Class value
        String className = "java.util.ArrayList";
        Class<?> classValue = Class.forName(className);
        Object classObj = TypeHandler.createValue(className, classValue);
        Assert.assertNotNull(classObj);

        // Test for File value
        String filePath = "/tmp/test.txt";
        File file = new File(filePath);
        Class<?> fileType = File.class;
        Object fileObj = TypeHandler.createValue(filePath, fileType);
        Assert.assertEquals(file, fileObj);

        // Test for existing File value
        String existingFilePath = "/tmp/test.txt";
        Class<?> existingFileType = File.class;
        Object existingFileObj = TypeHandler.createValue(existingFilePath, existingFileType);
        Assert.assertEquals(new File(existingFilePath), existingFileObj);

        // Test for Files value
        String filesPath = "/tmp/{test1.txt,test2.txt}";
        Class<?> filesType = File[].class;
        File[] filesArray = new File[]{new File("/tmp/test1.txt"), new File("/tmp/test2.txt")};
        Object filesObj = TypeHandler.createValue(filesPath, filesType);
        Assert.assertArrayEquals(filesArray, filesObj);

        // Test for URL value
        String urlStr = "http://example.com";
        Class<?> urlType = URL.class;
        Object urlObj = TypeHandler.createValue(urlStr, urlType);
        Assert.assertEquals(new URL(urlStr), urlObj);
    }

    /**
     * Test for createObject(String) method.
     */
    @Test
    public void testCreateObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        // Test for valid classname
        String classname = "java.util.ArrayList";
        Class<?> clazz = TypeHandler.createClass(classname);
        Object obj = TypeHandler.createObject(classname);
        Assert.assertEquals(clazz, obj.getClass());

        // Test for invalid classname
        String invalidClassName = "InvalidClassName";
        try
        {
            TypeHandler.createObject(invalidClassName);
            Assert.fail("Should throw ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for createNumber(String) method.
     */
    @Test
    public void testCreateNumber() throws ParseException
    {
        // Test for valid number
        String numberStr = "123";
        Number number = TypeHandler.createNumber(numberStr);
        Assert.assertEquals(123, number.intValue());

        // Test for invalid number (not a number)
        String invalidNumberStr = "abc";
        try
        {
            TypeHandler.createNumber(invalidNumberStr);
            Assert.fail("Should throw ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Test for invalid number (number with '.' as decimal point)
        String numberWithDecimalStr = "123.45";
        try
        {
            TypeHandler.createNumber(numberWithDecimalStr);
            Assert.fail("Should throw ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for createDate(String) method (unsupported).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate()
    {
        String dateStr = "2022-12-31";
        TypeHandler.createDate(dateStr);
    }

    /**
     * Test for createURL(String) method.
     */
    @Test
    public void testCreateURL() throws ParseException
    {
        // Test for valid URL
        String urlStr = "http://example.com";
        URL url = TypeHandler.createURL(urlStr);
        Assert.assertNotNull(url);

        // Test for invalid URL (malformed)
        String invalidURLStr = "http://example.com/not/a/valid/url";
        try
        {
            TypeHandler.createURL(invalidURLStr);
            Assert.fail("Should throw ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for createFile(String) method.
     */
    @Test
    public void testCreateFile() throws IOException
    {
        // Test for valid file path
        String filePath = "/tmp/test.txt";
        File file = new File(filePath);
        Object fileObj = TypeHandler.createValue(filePath, File.class);
        Assert.assertEquals(file, fileObj);

        // Test for invalid file path (file does not exist)
        String invalidFilePath = "/tmp/nonexistent.txt";
        try
        {
            Object invalidFileObj = TypeHandler.createValue(invalidFilePath, File.class);
            Assert.fail("Should throw FileNotFoundException");
        }
        catch (FileNotFoundException e)
        {
            // Expected
        }
    }

    /**
     * Test for createFiles(String) method (unsupported).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles()
    {
        String filesPath = "/tmp/{test1.txt,test2.txt}";
        TypeHandler.createFiles(filesPath);
    }
}