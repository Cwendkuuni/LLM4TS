package org.apache.commons.cli.test;

import static org.junit.Assert.*;

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
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String object.
     */
    @Test
    public void testCreateValueStringObject()
    {
        String str = "test";
        Object obj = new Object();
        String expected = "test";

        Object result = TypeHandler.createValue(str, obj.getClass());

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with PatternOptionBuilder.STRING_VALUE.
     */
    @Test
    public void testCreateValueStringStringValue()
    {
        String str = "test";

        String expected = "test";
        String result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with PatternOptionBuilder.OBJECT_VALUE.
     */
    @Test
    public void testCreateValueObjectObjectValue()
    {
        String classname = "java.lang.Integer";
        Object expected = new Integer(1);

        Object result = TypeHandler.createValue(classname, Object.class);

        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with PatternOptionBuilder.NUMBER_VALUE.
     */
    @Test
    public void testCreateValueNumberNumberValue()
    {
        String str = "123";
        Double expected = 123.0;

        Object result = TypeHandler.createValue(str, Double.TYPE);

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with PatternOptionBuilder.DATE_VALUE.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDateDateValue()
    {
        String str = "2023-03-31";

        Object result = TypeHandler.createValue(str, Date.class);

        fail("UnsupportedOperationException expected but not thrown");
    }

    /**
     * Test createValue method with PatternOptionBuilder.CLASS_VALUE.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueClassClassValue()
    {
        String classname = "java.lang.String";

        Object result = TypeHandler.createValue(classname, String.class);

        fail("ParseException expected but not thrown");
    }

    /**
     * Test createValue method with PatternOptionBuilder.FILE_VALUE.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueFileFileValue()
    {
        String filePath = "non-existent-file.txt";

        Object result = TypeHandler.createValue(filePath, File.class);

        fail("ParseException expected but not thrown");
    }

    /**
     * Test createValue method with PatternOptionBuilder.EXISTING_FILE_VALUE.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFileFileValue() throws IOException
    {
        String filePath = "non-existent-file.txt";
        File file = new File(filePath);

        Object result = TypeHandler.createValue(filePath, File.class);

        fail("FileNotFoundException expected but not thrown");
    }

    /**
     * Test createValue method with PatternOptionBuilder.FILES_VALUE.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFilesFilesValue()
    {
        String filePaths = "non-existent-file1.txt, non-existent-file2.txt";

        Object result = TypeHandler.createValue(filePaths, File[].class);

        fail("UnsupportedOperationException expected but not thrown");
    }

    /**
     * Test createValue method with PatternOptionBuilder.URL_VALUE.
     */
    @Test
    public void testCreateValueURLURLValue()
    {
        String urlString = "https://example.com";
        URL expected = new URL(urlString);

        Object result = TypeHandler.createValue(urlString, URL.class);

        assertEquals(expected, result);
    }
}