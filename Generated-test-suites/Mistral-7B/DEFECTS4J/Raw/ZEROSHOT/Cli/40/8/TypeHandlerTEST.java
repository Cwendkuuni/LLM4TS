package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

/**
 * TypeHandlerTest
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method, when str is null and obj is String.class
     */
    @Test(expected = ParseException.class)
    public void testCreateValueNullStrString()
    {
        String str = null;
        String objType = "String.class";
        try
        {
            TypeHandler.createValue(str, (Class<?>) String.class);
        }
        catch (ParseException e)
        {
            assertEquals("Argument 'str' cannot be null", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is null and obj is Object.class
     */
    @Test(expected = ParseException.class)
    public void testCreateValueNullStrObject()
    {
        String str = null;
        String objType = "Object.class";
        try
        {
            TypeHandler.createValue(str, (Class<?>) Object.class);
        }
        catch (ParseException e)
        {
            assertEquals("Argument 'str' cannot be null", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is null and obj is PatternOptionBuilder.STRING_VALUE
     */
    @Test(expected = ParseException.class)
    public void testCreateValueNullStrStringValue()
    {
        String str = null;
        String objType = "PatternOptionBuilder.STRING_VALUE";
        try
        {
            TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.STRING_VALUE);
        }
        catch (ParseException e)
        {
            assertEquals("Argument 'str' cannot be null", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is empty and obj is String.class
     */
    @Test
    public void testCreateValueEmptyStrString()
    {
        String str = "";
        String objType = "String.class";
        assertEquals("", TypeHandler.createValue(str, (Class<?>) String.class));
    }

    /**
     * Test createValue method, when str is empty and obj is Object.class
     */
    @Test
    public void testCreateValueEmptyStrObject()
    {
        String str = "";
        String objType = "Object.class";
        assertNull(TypeHandler.createValue(str, (Class<?>) Object.class));
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.STRING_VALUE
     */
    @Test
    public void testCreateValueEmptyStrStringValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.STRING_VALUE";
        assertEquals("", TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.STRING_VALUE));
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.OBJECT_VALUE
     */
    @Test
    public void testCreateValueEmptyStrObjectValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.OBJECT_VALUE";
        assertNull(TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.OBJECT_VALUE));
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.NUMBER_VALUE
     */
    @Test(expected = ParseException.class)
    public void testCreateValueEmptyStrNumberValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.NUMBER_VALUE";
        try
        {
            TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.NUMBER_VALUE);
        }
        catch (ParseException e)
        {
            assertEquals("Unable to parse number from empty string", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.DATE_VALUE
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueEmptyStrDateValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.DATE_VALUE";
        TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.DATE_VALUE);
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.CLASS_VALUE
     */
    @Test(expected = ParseException.class)
    public void testCreateValueEmptyStrClassValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.CLASS_VALUE";
        try
        {
            TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.CLASS_VALUE);
        }
        catch (ParseException e)
        {
            assertEquals("Unable to parse the class name from empty string", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.FILE_VALUE
     */
    @Test(expected = ParseException.class)
    public void testCreateValueEmptyStrFileValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.FILE_VALUE";
        try
        {
            TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.FILE_VALUE);
        }
        catch (ParseException e)
        {
            assertEquals("Argument 'str' cannot be empty", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.EXISTING_FILE_VALUE
     */
    @Test(expected = ParseException.class)
    public void testCreateValueEmptyStrExistingFileValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.EXISTING_FILE_VALUE";
        try
        {
            TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.EXISTING_FILE_VALUE);
        }
        catch (ParseException e)
        {
            assertEquals("Argument 'str' cannot be empty", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.FILES_VALUE
     */
    @Test(expected = ParseException.class)
    public void testCreateValueEmptyStrFilesValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.FILES_VALUE";
        try
        {
            TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.FILES_VALUE);
        }
        catch (ParseException e)
        {
            assertEquals("Argument 'str' cannot be empty", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is empty and obj is PatternOptionBuilder.URL_VALUE
     */
    @Test(expected = ParseException.class)
    public void testCreateValueEmptyStrUrlValue()
    {
        String str = "";
        String objType = "PatternOptionBuilder.URL_VALUE";
        try
        {
            TypeHandler.createValue(str, (Class<?>) PatternOptionBuilder.URL_VALUE);
        }
        catch (ParseException e)
        {
            assertEquals("Argument 'str' cannot be empty", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is "123" and obj is Long.class
     */
    @Test
    public void testCreateValueLong()
    {
        String str = "123";
        Long obj = TypeHandler.createValue(str, (Class<?>) Long.class);
        assertEquals(123L, obj.longValue());
    }

    /**
     * Test createValue method, when str is "123" and obj is Integer.class
     */
    @Test
    public void testCreateValueInteger()
    {
        String str = "123";
        Integer obj = TypeHandler.createValue(str, (Class<?>) Integer.class);
        assertEquals(123, obj.intValue());
    }

    /**
     * Test createValue method, when str is "123" and obj is Number.class
     */
    @Test
    public void testCreateValueNumber()
    {
        String str = "123";
        Number obj = TypeHandler.createValue(str, (Class<?>) Number.class);
        assertEquals(123, obj.intValue());
    }

    /**
     * Test createValue method, when str is "123.45" and obj is Double.class
     */
    @Test
    public void testCreateValueDouble()
    {
        String str = "123.45";
        Double obj = TypeHandler.createValue(str, (Class<?>) Double.class);
        assertEquals(123.45, obj.doubleValue(), 0.0);
    }

    /**
     * Test createValue method, when str is "true" and obj is Boolean.class
     */
    @Test
    public void testCreateValueBooleanTrue()
    {
        String str = "true";
        Boolean obj = TypeHandler.createValue(str, (Class<?>) Boolean.class);
        assertTrue(obj);
    }

    /**
     * Test createValue method, when str is "false" and obj is Boolean.class
     */
    @Test
    public void testCreateValueBooleanFalse()
    {
        String str = "false";
        Boolean obj = TypeHandler.createValue(str, (Class<?>) Boolean.class);
        assertFalse(obj);
    }

    /**
     * Test createValue method, when str is "java.lang.String" and obj is Class.class
     */
    @Test
    public void testCreateValueStringClass()
    {
        String str = "java.lang.String";
        Class<?> obj = TypeHandler.createValue(str, (Class<?>) Class.class);
        assertEquals(String.class, obj);
    }

    /**
     * Test createValue method, when str is "java.util.Date" and obj is Class.class
     */
    @Test
    public void testCreateValueDateClass()
    {
        String str = "java.util.Date";
        Class<?> obj = TypeHandler.createValue(str, (Class<?>) Class.class);
        assertEquals(Date.class, obj);
    }

    /**
     * Test createValue method, when str is "java.io.File" and obj is Class.class
     */
    @Test
    public void testCreateValueFileClass()
    {
        String str = "java.io.File";
        Class<?> obj = TypeHandler.createValue(str, (Class<?>) Class.class);
        assertEquals(File.class, obj);
    }

    /**
     * Test createValue method, when str is "file.txt" and obj is File.class
     */
    @Test
    public void testCreateValueFile()
    {
        String str = "file.txt";
        File obj = TypeHandler.createValue(str, (Class<?>) File.class);
        assertNotNull(obj);
        assertEquals("file.txt", obj.getAbsolutePath());
    }

    /**
     * Test createValue method, when str is "/path/to/file.txt" and obj is File.class
     */
    @Test
    public void testCreateValueFileAbsolutePath()
    {
        String str = "/path/to/file.txt";
        File obj = TypeHandler.createValue(str, (Class<?>) File.class);
        assertNotNull(obj);
        assertEquals("/path/to/file.txt", obj.getAbsolutePath());
    }

    /**
     * Test createValue method, when str is "file.txt" and obj is FileInputStream.class
     */
    @Test(expected = ParseException.class)
    public void testCreateValueFileInputStreamNullFile()
    {
        String str = "file.txt";
        String objType = "java.io.FileInputStream";
        try
        {
            TypeHandler.createValue(str, (Class<?>) FileInputStream.class);
        }
        catch (ParseException e)
        {
            assertEquals("Unable to create file from the given path: file.txt", e.getMessage());
        }
    }

    /**
     * Test createValue method, when str is "/path/to/file.txt" and obj is FileInputStream.class
     */
    @Test
    public void testCreateValueFileInputStream() throws IOException
    {
        String str = "/path/to/file.txt";
        String objType = "java.io.FileInputStream";
        File file = new File(str);
        FileInputStream inputStream = (FileInputStream) TypeHandler.createValue(str, (Class<?>) FileInputStream.class);
        assertEquals(file, inputStream.getFileDescriptor().getFileDescriptor());
    }

    /**
     * Test createValue method, when str is "file1.txt, file2.txt" and obj is File[].class
     */
    @Test
    public void testCreateValueFiles()
    {
        String str = "file1.txt, file2.txt";
        File[] obj = TypeHandler.createValue(str, (Class<?>) File[].class);
        assertNotNull(obj);
        assertEquals(2, obj.length);
        for (int i = 0; i < obj.length; i++)
        {
            File file = obj[i];
            assertNotNull(file);
        }
    }

    /**
     * Test createValue method, when str is "http://example.com" and obj is URL.class
     */
    @Test
    public void testCreateValueURL()
    {
        String str = "http://example.com";
        URL obj = TypeHandler.createValue(str, (Class<?>) URL.class);
        assertNotNull(obj);
        assertEquals("http://example.com", obj.toString());
    }
}