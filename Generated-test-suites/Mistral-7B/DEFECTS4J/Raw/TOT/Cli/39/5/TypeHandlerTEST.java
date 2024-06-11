package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Test class for the TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test for createValue method with String type.
     */
    @Test
    public void testCreateValueString() throws ParseException
    {
        String str = "test string";
        String expected = str;
        Object actual = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for createValue method with Object type.
     */
    @Test
    public void testCreateValueObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Object expected = new Date();
        Object actual = TypeHandler.createValue(classname, Object.class);
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }

    /**
     * Test for createValue method with Number type.
     */
    @Test
    public void testCreateValueNumber() throws ParseException
    {
        String numberStr = "123";
        Long expected = 123L;
        Object actual = TypeHandler.createValue(numberStr, Long.class);
        Assert.assertEquals(expected, actual);

        String numberStrWithDot = "123.45";
        Double expectedDouble = 123.45;
        Object actualDouble = TypeHandler.createValue(numberStrWithDot, Double.class);
        Assert.assertEquals(expectedDouble, actualDouble);
    }

    /**
     * Test for createValue method with Date type.
     * This test case is not yet implemented and is always skipped.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate()
    {
        String dateStr = "2023-03-31";
        Date expected = new Date(2023, 2, 31); // Suppose this is the valid Date object
        Date actual = TypeHandler.createDate(dateStr);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for createValue method with Class type.
     */
    @Test
    public void testCreateValueClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.util.Date";
        Class<?> expected = Date.class;
        Class<?> actual = TypeHandler.createClass(classname);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for createValue method with File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "test.txt";
        File expected = new File(filePath);
        File actual = TypeHandler.createFile(filePath);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for createValue method with File[] type.
     * This test case is not yet implemented and is always skipped.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles()
    {
        String filePaths = "test1.txt, test2.txt";
        File[] expectedFiles = new File[] {new File("test1.txt"), new File("test2.txt")};
        File[] actualFiles = TypeHandler.createFiles(filePaths);
        Assert.assertArrayEquals(expectedFiles, actualFiles);
    }

    /**
     * Test for createValue method with URL type.
     */
    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        String urlString = "http://example.com";
        URL expected = new URL("http://example.com");
        URL actual = TypeHandler.createURL(urlString);
        Assert.assertEquals(expected, actual);
    }
}