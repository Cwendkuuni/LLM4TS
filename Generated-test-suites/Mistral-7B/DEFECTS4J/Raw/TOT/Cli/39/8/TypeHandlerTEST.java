package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

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
        String str = "testStringValue";
        Class<?> clazz = String.class;

        try
        {
            Object result = TypeHandler.createValue(str, clazz);
            assertEquals("testStringValue", result);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);

        try
        {
            Object result = TypeHandler.createValue("testStringValue", clazz);
            fail("Expected ParseException but got " + result.getClass().getName());
        }
        catch (ParseException e)
        {
            // Expected
        }
        catch (ClassNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with Number type.
     */
    @Test
    public void testCreateValueNumber()
    {
        String numberString = "123";
        Class<?> clazz = Long.TYPE;

        try
        {
            Object result = TypeHandler.createValue(numberString, clazz);
            assertEquals(123L, result);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue("123.45", clazz);
            assertEquals(123L, result);
            fail("Expected Double but got " + result.getClass().getName());
        }
        catch (ParseException e)
        {
            // Expected
        }

        Class<?> clazzDouble = Double.TYPE;

        try
        {
            Object result = TypeHandler.createValue("123.45", clazzDouble);
            assertEquals(123.45, result, 0.001);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with Date type.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate()
    {
        String dateString = "2023-03-31";
        Class<?> clazz = Date.class;

        TypeHandler.createValue(dateString, clazz);
    }

    /**
     * Test createValue method with Class type.
     */
    @Test
    public void testCreateValueClass()
    {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);

        try
        {
            Object result = TypeHandler.createValue("java.lang.String", clazz);
            assertEquals(clazz, result.getClass());
        }
        catch (ClassNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        Class<?> clazz = File.class;

        try
        {
            Object result = TypeHandler.createValue(filePath, clazz);
            assertEquals(file, result);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test createValue method with existing File type.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFile()
    {
        String nonExistingFilePath = "nonExistingFile.txt";
        Class<?> clazz = File.class;

        TypeHandler.createValue(nonExistingFilePath, clazz);
    }

    /**
     * Test createValue method with File[] type.
     * <p>
     * This method is not yet implemented and always throws an
     * {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles()
    {
        String filePaths = "testFile1.txt,testFile2.txt";
        Class<?> clazz = File[].class;

        TypeHandler.createValue(filePaths, clazz);
    }

    /**
     * Test createValue method with URL type.
     */
    @Test
    public void testCreateValueUrl()
    {
        String urlString = "http://example.com";
        Class<?> clazz = URL.class;

        try
        {
            Object result = TypeHandler.createValue(urlString, clazz);
            URL url = (URL) result;
            assertEquals("http://example.com", url.toString());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}