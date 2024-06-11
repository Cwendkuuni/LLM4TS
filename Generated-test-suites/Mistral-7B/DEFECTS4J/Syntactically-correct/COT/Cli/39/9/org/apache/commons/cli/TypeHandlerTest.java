package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.utils.PatternOptionBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

/**
 * Test class for TypeHandler
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String argument and PatternOptionBuilder.STRING_VALUE class.
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_StringValue() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, obj);
    }

    /**
     * Test createValue method with Object argument and PatternOptionBuilder.OBJECT_VALUE class.
     * This test assumes that the createObject method is working correctly.
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_ObjectValue() throws ParseException
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createValue("2023-03-15", PatternOptionBuilder.OBJECT_VALUE, Class.forName(classname));
        assertTrue(obj instanceof Date);
    }

    /**
     * Test createValue method with String argument and PatternOptionBuilder.NUMBER_VALUE class.
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_NumberValue() throws ParseException
    {
        String str = "123456789";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(obj instanceof Long);
        assertEquals(Long.parseLong(str), ((Long) obj).longValue());

        str = "123.45";
        obj = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(obj instanceof Double);
        assertEquals(Double.parseDouble(str), ((Double) obj).doubleValue());
    }

    /**
     * Test createValue method with String argument and PatternOptionBuilder.DATE_VALUE class.
     * This test assumes that the createDate method is working correctly.
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_DateValue() throws ParseException
    {
        String str = "2023-03-15";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertTrue(obj instanceof Date);
    }

    /**
     * Test createValue method with String argument and PatternOptionBuilder.CLASS_VALUE class.
     * This test assumes that the createClass method is working correctly.
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_ClassValue() throws ParseException
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createValue(classname, PatternOptionBuilder.CLASS_VALUE);
        assertTrue(obj instanceof Date);
    }

    /**
     * Test createValue method with String argument and PatternOptionBuilder.FILE_VALUE class.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_FileValue() throws FileNotFoundException, ParseException
    {
        String str = "path/to/file.txt";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertTrue(obj instanceof File);
        File file = (File) obj;
        assertTrue(file.exists());
    }

    /**
     * Test createValue method with String argument and PatternOptionBuilder.EXISTING_FILE_VALUE class.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_ExistingFileValue() throws FileNotFoundException, ParseException
    {
        String str = "path/to/non-existent-file.txt";
        try
        {
            Object obj = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            assertEquals("Unable to create a File for the value: " + str, e.getMessage());
        }
    }

    /**
     * Test createValue method with String argument and PatternOptionBuilder.FILES_VALUE class.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_FilesValue() throws FileNotFoundException, ParseException
    {
        String str = "path/to/file1.txt,path/to/file2.txt";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertTrue(obj instanceof File[]);
        File[] files = (File[]) obj;
        assertEquals(files.length, 2);
        for (File file : files)
        {
            assertTrue(file.exists());
        }
    }

    /**
     * Test createValue method with String argument and PatternOptionBuilder.URL_VALUE class.
     * @throws MalformedURLException
     * @throws ParseException
     */
    @org.junit.Test
    public void testCreateValue_String_URLValue() throws MalformedURLException, ParseException
    {
        String str = "http://example.com";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertTrue(obj instanceof URL);
    }

    /**
     * Test createObject method with invalid classname.
     * @throws ParseException
     */
    @org.junit.Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassName() throws ParseException
    {
        String classname = "java.util.InvalidClass";
        TypeHandler.createObject(classname);
    }

    /**
     * Test createNumber method with invalid string.
     * @throws ParseException
     */
    @org.junit.Test(expected = ParseException.class)
    public void testCreateNumber_InvalidString() throws ParseException
    {
        String str = "invalid";
        TypeHandler.createNumber(str);
    }
}