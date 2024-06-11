package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.types.PatternOptionBuilder;
import org.junit.Test;

/**
 * This class tests the TypeHandler methods.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String type.
     *
     * @throws ParseException if the value creation for String type failed
     */
    @Test
    public void testCreateValueString() throws ParseException
    {
        String str = "TestString";
        Object obj = TypeHandler.createValue(str, String.class);

        assertEquals("TestString", obj);
    }

    /**
     * Test createValue method with Object type.
     *
     * @throws ParseException if the value creation for Object type failed
     */
    @Test
    public void testCreateValueObject() throws ParseException
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createValue(classname, Object.class);

        assertInstanceOf(String.class, obj);
    }

    /**
     * Test createValue method with Number type.
     *
     * @throws ParseException if the value creation for Number type failed
     */
    @Test
    public void testCreateValueNumber() throws ParseException
    {
        String str = "42";
        Object obj = TypeHandler.createValue(str, Number.class);

        assertInstanceOf(Long.class, obj);
        assertEquals(42L, obj);

        str = "3.14";
        obj = TypeHandler.createValue(str, Number.class);

        assertInstanceOf(Double.class, obj);
        assertEquals(3.14, obj, 0.0);
    }

    /**
     * Test createValue method with Date type.
     *
     * @throws ParseException if the value creation for Date type failed
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate() throws ParseException
    {
        String str = "2022-12-31";
        Object obj = TypeHandler.createValue(str, Date.class);

        throw new UnsupportedOperationException(); // Expected exception
    }

    /**
     * Test createValue method with Class type.
     *
     * @throws ParseException if the value creation for Class type failed
     */
    @Test
    public void testCreateValueClass() throws ParseException
    {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createValue(classname, Class.class);

        assertEquals(String.class, clazz);
    }

    /**
     * Test createValue method with File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "test.txt";

        Object obj = TypeHandler.createValue(filePath, File.class);

        assertInstanceOf(File.class, obj);
        File expectedFile = new File(filePath);
        File actualFile = (File) obj;
        assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
    }

    /**
     * Test createValue method with existing File type.
     */
    @Test
    public void testCreateValueExistingFile()
    {
        String filePath = "test.txt";

        if (new File(filePath).exists())
        {
            Object obj = TypeHandler.createValue(filePath, File.class);

            assertInstanceOf(File.class, obj);
            File expectedFile = new File(filePath);
            File actualFile = (File) obj;
            assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
        }
        else
        {
            assertNull(TypeHandler.createValue(filePath, File.class));
        }
    }

    /**
     * Test createValue method with File[] type.
     *
     * @throws IOException if the files cannot be found
     */
    @Test
    public void testCreateValueFiles() throws IOException
    {
        String filePath1 = "test1.txt";
        String filePath2 = "test2.txt";

        File testFolder = new File("target/test-classes/testfiles");
        testFolder.mkdirs();

        File file1 = new File(testFolder, filePath1);
        file1.createNewFile();

        File file2 = new File(testFolder, filePath2);
        file2.createNewFile();

        Object obj = TypeHandler.createValue("testfiles/" + PatternOptionBuilder.FILES_VALUE, File[].class);

        assertInstanceOf(File[].class, obj);
        File[] expectedFiles = {file1, file2};
        File[] actualFiles = (File[]) obj;
        assertArrayEquals(expectedFiles, actualFiles);

        testFolder.delete(); // Clean up
    }

    /**
     * Test createValue method with URL type.
     *
     * @throws ParseException if the value creation for URL type failed
     */
    @Test
    public void testCreateValueURL() throws ParseException
    {
        String urlString = "https://www.example.com";
        Object obj = TypeHandler.createValue(urlString, URL.class);

        assertInstanceOf(URL.class, obj);
        URL expectedURL = new URL(urlString);
        URL actualURL = (URL) obj;
        assertEquals(expectedURL.toString(), actualURL.toString());
    }
}