package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.types.PatternOptionBuilder;
import org.junit.Test;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    /**
     * Test for createValue(String, Object) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObject() throws Exception
    {
        // Test String type
        Object strObj = TypeHandler.createValue("testStr", String.class);
        assertEquals("testStr", strObj);

        // Test Integer type
        Object intObj = TypeHandler.createValue("1234", Integer.class);
        assertEquals(1234, intObj);

        // Test Double type
        Object doubleObj = TypeHandler.createValue("1234.56", Double.class);
        assertEquals(1234.56, doubleObj, 0.001);

        // Test Date type
        // UnsupportedOperationException is thrown as expected
        try
        {
            Object dateObj = TypeHandler.createValue("2022-12-31", Date.class);
        }
        catch (UnsupportedOperationException e)
        {
            // Expected
        }

        // Test File type
        File file = new File("testFile.txt");
        Object fileObj = TypeHandler.createValue("testFile.txt", File.class);
        assertSame(file, fileObj);

        // Test FileInputStream type
        Object fileInputStreamObj = TypeHandler.createValue("testFile.txt", FileInputStream.class);
        assertSame(new FileInputStream("testFile.txt"), fileInputStreamObj);

        // Test URL type
        URL url = new URL("http://test.com");
        Object urlObj = TypeHandler.createValue("http://test.com", URL.class);
        assertSame(url, urlObj);

        // Test Class type
        Class<?> clazz = String.class;
        Object clazzObj = TypeHandler.createValue("java.lang.String", clazz);
        assertEquals(clazz, clazzObj.getClass());

        // Test Object type
        Object objectObj = TypeHandler.createValue("TestObject", Object.class);
        assertNull(objectObj);

        // Test wrong type
        try
        {
            Object wrongTypeObj = TypeHandler.createValue("test", Long.class);
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Test null input
        try
        {
            Object nullInputObj = TypeHandler.createValue(null, String.class);
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for createObject(String) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateObjectString() throws Exception
    {
        // Test valid class name
        Class<?> clazz = TestClass.class;
        Object obj = TypeHandler.createObject("test.TestClass");
        assertEquals(clazz, obj.getClass());

        // Test invalid class name
        try
        {
            Object invalidObj = TypeHandler.createObject("test.invalidClass");
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Test null input
        try
        {
            Object nullInputObj = TypeHandler.createObject(null);
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for createNumber(String) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateNumberString() throws Exception
    {
        // Test Long type
        Object longObj = TypeHandler.createNumber("1234");
        assertEquals(1234, longObj);

        // Test Double type
        Object doubleObj = TypeHandler.createNumber("1234.56");
        assertEquals(1234.56, doubleObj, 0.001);

        // Test invalid input
        try
        {
            Object invalidObj = TypeHandler.createNumber("test");
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Test null input
        try
        {
            Object nullInputObj = TypeHandler.createNumber(null);
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for createDate(String) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateDateString() throws Exception
    {
        // UnsupportedOperationException is thrown as expected
        try
        {
            Object dateObj = TypeHandler.createDate("2022-12-31");
        }
        catch (UnsupportedOperationException e)
        {
            // Expected
        }
    }

    /**
     * Test for createURL(String) method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateURLString() throws Exception
    {
        // Test valid URL
        Object urlObj = TypeHandler.createURL("http://test.com");
        URL url = (URL) urlObj;
        assertEquals("http://test.com", url.toString());

        // Test invalid URL
        try
        {
            Object invalidUrlObj = TypeHandler.createURL("test");
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Test null input
        try
        {
            Object nullInputUrlObj = TypeHandler.createURL(null);
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for createFile(String) method.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testCreateFileString() throws IOException
    {
        // Test valid file path
        File file = new File("testFile.txt");
        Object fileObj = TypeHandler.createFile("testFile.txt");
        assertSame(file, fileObj);

        // Test non-existent file path
        File nonExistentFile = new File("testFileDoesNotExist.txt");
        try
        {
            Object nonExistentFileObj = TypeHandler.createFile("testFileDoesNotExist.txt");
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Test null input
        try
        {
            Object nullInputFileObj = TypeHandler.createFile(null);
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Clean up test file
        if (file.exists())
        {
            file.delete();
        }
    }

    /**
     * Test for openFile(String) method.
     *
     * @throws IOException if an error occurs
     * @throws ParseException if the file does not exist or is not readable
     */
    @Test
    public void testOpenFileString() throws IOException, ParseException
    {
        // Test valid file path
        File file = new File("testFile.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        Object fileInputStreamObj = TypeHandler.openFile("testFile.txt");
        assertSame(fileInputStream, fileInputStreamObj);

        // Test non-existent file path
        File nonExistentFile = new File("testFileDoesNotExist.txt");
        try
        {
            Object nonExistentFileInputStreamObj = TypeHandler.openFile("testFileDoesNotExist.txt");
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Test null input
        try
        {
            Object nullInputFileInputStreamObj = TypeHandler.openFile(null);
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Clean up test file and test file stream
        if (file.exists())
        {
            file.delete();
        }
        if (fileInputStream != null)
        {
            fileInputStream.close();
        }
    }

    /**
     * Test for createFiles(String) method.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testCreateFilesString() throws IOException
    {
        // To implement/port:
        //        File[] files = TypeHandler.createFiles("testFile1.txt, testFile2.txt");
        //        for (File file : files)
        //        {
        //            System.out.println("File created: " + file.getAbsolutePath());
        //        }

        //        File testDir = new File("testDir");
        //        testDir.mkdir();

        //        File[] files = TypeHandler.createFiles("testDir/*.txt");
        //        for (File file : files)
        //        {
        //            System.out.println("File created: " + file.getAbsolutePath());
        //        }

        //        testDir.delete();
    }

    /**
     * Test class for TestClass.
     */
    static class TestClass
    {
        // Empty constructor
    }
}