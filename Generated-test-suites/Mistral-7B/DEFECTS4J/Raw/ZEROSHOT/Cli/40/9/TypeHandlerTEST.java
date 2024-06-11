package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.TestUtils;

import org.junit.Test;

/**
 * TypeHandlerTest class
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String type.
     *
     * @throws ParseException if the test fails
     */
    @Test
    public void testCreateValueString() throws ParseException
    {
        Object value = TypeHandler.createValue("test string", String.class);
        assertEquals("test string", value);
    }

    /**
     * Test createValue method with Object type.
     *
     * @throws ParseException if the test fails
     */
    @Test
    public void testCreateValueObject() throws ParseException
    {
        String className = TestUtils.getClassName("java.lang.Integer");
        Object value = TypeHandler.createValue("42", className);
        assertEquals(42, value);
        Class<?> clazz = value.getClass();
        assertEquals(Integer.class, clazz);
    }

    /**
     * Test createValue method with Number type.
     *
     * @throws ParseException if the test fails
     */
    @Test
    public void testCreateValueNumberLong() throws ParseException
    {
        Object value = TypeHandler.createValue("42", Long.class);
        assertEquals(42L, value);
    }

    /**
     * Test createValue method with Number type.
     *
     * @throws ParseException if the test fails
     */
    @Test
    public void testCreateValueNumberDouble() throws ParseException
    {
        Object value = TypeHandler.createValue("42.0", Double.class);
        assertEquals(42.0, value);
    }

    /**
     * Test createValue method with Date type.
     *
     * @throws ParseException if the test fails
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate() throws ParseException
    {
        Object value = TypeHandler.createValue("2023-03-15", Date.class);
        assertNull(value);
    }

    /**
     * Test createValue method with Class type.
     *
     * @throws ParseException if the test fails
     */
    @Test
    public void testCreateValueClass() throws Exception, ParseException, ClassNotFoundException
    {
        String className = TestUtils.getClassName("java.lang.Integer");
        Object value = TypeHandler.createValue(className, String.class);
        assertEquals(Integer.class, value.getClass());
        assertEquals(Integer.class.getName(), value.toString());
    }

    /**
     * Test createValue method with File type.
     *
     * @throws ParseException if the test fails
     */
    @Test
    public void testCreateValueFile() throws ParseException, IOException
    {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        Object value = TypeHandler.createValue(filePath, File.class);
        assertEquals(file, value);
    }

    /**
     * Test createValue method with ExistingFile type.
     *
     * @throws ParseException if the test fails
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFile() throws ParseException, IOException
    {
        String nonExistentFile = "nonExistentFile.txt";
        Object value = TypeHandler.createValue(nonExistentFile, File.class);
        assertNull(value);
    }

    /**
     * Test createValue method with Files type.
     *
     * @throws ParseException if the test fails
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles() throws ParseException
    {
        Object value = TypeHandler.createValue("testFiles/*.txt", File[].class);
        assertNull(value);
    }

    /**
     * Test createValue method with URL type.
     *
     * @throws ParseException if the test fails
     */
    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        String urlString = "http://www.example.com/";
        Object value = TypeHandler.createValue(urlString, URL.class);
        assertEquals(new URL(urlString), value);
    }
}