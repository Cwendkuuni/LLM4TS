package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;

import org.junit.Test;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String type.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "test";
        String expected = "test";
        Object result = TypeHandler.createValue(str, String.class);
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        String className = String.class.getName();
        Object result = TypeHandler.createValue("", Class.forName(className));
        assertNull(result);
    }

    /**
     * Test createValue method with Number type.
     */
    @Test
    public void testCreateValueNumber()
    {
        String str = "123";
        Long expected = 123L;
        Object result = TypeHandler.createValue(str, Long.class);
        assertEquals(expected, result);

        String strWithDecimal = "123.45";
        Double expectedDouble = 123.45;
        Object resultDouble = TypeHandler.createValue(strWithDecimal, Double.class);
        assertEquals(expectedDouble, resultDouble);
    }

    /**
     * Test createValue method with Date type.
     * This method is not yet implemented and always throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate()
    {
        String str = "2023-03-01";
        TypeHandler.createValue(str, Date.class);
    }

    /**
     * Test createValue method with Class type.
     */
    @Test
    public void testCreateValueClass()
    {
        String classname = String.class.getName();
        Class<?> expected = Class.forName(classname);
        Class<?> result = TypeHandler.createValue(classname, Class.class);
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "test.txt";
        File expected = new File(filePath);
        File result = TypeHandler.createFile(filePath);
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with existing File type.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFile()
    {
        String nonExistentFilePath = "nonExistentFile.txt";
        FileInputStream fileInputStream = TypeHandler.openFile(nonExistentFilePath);
    }

    /**
     * Test createValue method with URL type.
     */
    @Test
    public void testCreateValueURL()
    {
        String urlString = "http://example.com";
        URL expected = new URL(urlString);
        URL result = TypeHandler.createURL(urlString);
        assertEquals(expected, result);
    }

    /**
     * Test createObject method.
     */
    @Test
    public void testCreateObject()
    {
        String classname = String.class.getName();
        Class<?> expected = Class.forName(classname);
        Class<?> result = TypeHandler.createObject(classname);
        assertEquals(expected, result);
    }

    /**
     * Test createObject method with non-existent class.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectClassNotFound()
    {
        String nonExistentClassname = "NonExistentClass";
        TypeHandler.createObject(nonExistentClassname);
    }

    /**
     * Test createObject method with uninstantiable class.
     */
    @Test(expected = InstantiationException.class)
    public void testCreateObjectInstantiationFailure()
    {
        String uninstantiableClassname = "java.lang.String";
        TypeHandler.createObject(uninstantiableClassname);
    }

    /**
     * Test createNumber method.
     */
    @Test
    public void testCreateNumber()
    {
        String str = "123";
        Long expected = 123L;
        Number result = TypeHandler.createNumber(str);
        assertEquals(expected, result);
    }

    /**
     * Test createNumber method with invalid number.
     */
    @Test(expected = NumberFormatException.class)
    public void testCreateNumberInvalidNumber()
    {
        String invalidNumberStr = "test";
        TypeHandler.createNumber(invalidNumberStr);
    }
}