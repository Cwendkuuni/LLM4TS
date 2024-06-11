package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Test class for TypeHandler
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(java.lang.String, java.lang.String)}
     */
    @Test
    public void testCreateValueWithStringObject() throws Exception
    {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(str, obj);
    }

    /**
     * Test method for {@link TypeHandler#createValue(java.lang.String, java.lang.Class)}
     */
    @Test
    public void testCreateValueWithStringClass() throws Exception
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, obj);
    }

    /**
     * Test method for {@link TypeHandler#createObject(java.lang.String)}
     */
    @Test
    public void testCreateObject() throws Exception
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createObject(classname);
        Assert.assertNotNull(obj);
    }

    /**
     * Test method for {@link TypeHandler#createNumber(java.lang.String)}
     */
    @Test
    public void testCreateNumber() throws Exception
    {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        Assert.assertEquals(123, number.intValue());

        str = "456.78";
        number = TypeHandler.createNumber(str);
        Assert.assertEquals(456.78, number.doubleValue(), 0.01);
    }

    /**
     * Test method for {@link TypeHandler#createDate(java.lang.String)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws Exception
    {
        String str = "2023-03-01";
        Date date = TypeHandler.createDate(str);
        // UnsupportedOperationException is expected
    }

    /**
     * Test method for {@link TypeHandler#createURL(java.lang.String)}
     */
    @Test
    public void testCreateURL() throws Exception
    {
        String str = "http://example.com";
        URL url = TypeHandler.createURL(str);
        Assert.assertNotNull(url);
    }

    /**
     * Test method for {@link TypeHandler#createFile(java.lang.String)}
     */
    @Test
    public void testCreateFile() throws Exception
    {
        String str = "testFile.txt";
        File file = TypeHandler.createFile(str);
        Assert.assertTrue(file.exists());
        file.delete(); // Clean up
    }

    /**
     * Test method for {@link TypeHandler#createFiles(java.lang.String)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() throws Exception
    {
        String str = "testFile1.txt,testFile2.txt";
        File[] files = TypeHandler.createFiles(str);
        // UnsupportedOperationException is expected
    }
}