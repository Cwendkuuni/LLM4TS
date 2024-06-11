package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.ClassLoaderUtils;
import org.junit.Test;

/**
 * Test for {@link TypeHandler} class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String type.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "Test String";
        assertEquals("Test String", TypeHandler.createValue(str, String.class));
    }

    /**
     * Test createValue method with Object type.
     */
    @Test
    public void testCreateValueObject() throws ParseException
    {
        String classname = "java.util.Date";
        Class<?> clazz = TypeHandler.createClass(classname);
        Object obj = TypeHandler.createValue("2022-01-01", clazz);
        assertEquals(new Date(1641350400000L), obj); // 2022-01-01
    }

    /**
     * Test createValue method with Number type.
     */
    @Test
    public void testCreateValueNumber() throws ParseException
    {
        String str = "12345";
        Number number = TypeHandler.createValue(str, int.class);
        assertEquals((int) 12345, number.intValue());

        number = TypeHandler.createValue(str, long.class);
        assertEquals((long) 12345, number.longValue());

        number = TypeHandler.createValue("123.45", double.class);
        assertEquals(123.45, number.doubleValue(), 0.0);
    }

    /**
     * Test createValue method with Date type.
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate() throws Exception
    {
        String str = "2022-01-01";
        TypeHandler.createValue(str, Date.class);
    }

    /**
     * Test createValue method with Class type.
     * @throws ParseException
     */
    @Test
    public void testCreateValueClass() throws ParseException
    {
        String classname = "java.util.Date";
        Class<?> clazz = Class.forName(classname);
        Object obj = TypeHandler.createValue("2022-01-01", clazz);
        assertEquals(new Date(1641350400000L), obj); // 2022-01-01
    }

    /**
     * Test createValue method with File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "/test/file.txt";
        File file = new File(filePath);
        Object obj = TypeHandler.createValue(filePath, File.class);
        assertEquals(file, obj);
    }

    /**
     * Test createValue method with existing File type.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFile()
    {
        String filePath = "/test/nonExistentFile.txt";
        Object obj = TypeHandler.createValue(filePath, File.class);
        assertNull(obj);
    }

    /**
     * Test createValue method with Files type.
     * @throws IOException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles() throws IOException
    {
        String filePaths = "/test/file1.txt, /test/file2.txt";
        File[] files = (File[]) TypeHandler.createValue(filePaths, File[].class);
        assertNull(files); // Not yet implemented
    }

    /**
     * Test createValue method with URL type.
     */
    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        String urlString = "http://example.com";
        URL url = new URL(urlString);
        Object obj = TypeHandler.createValue(urlString, URL.class);
        assertEquals(url, obj);
    }

    /**
     * Test createObject method with valid classname.
     */
    @Test
    public void testCreateObjectValidClassName() throws ParseException
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createObject(classname);
        assertEquals(new Date(), obj);
    }

    /**
     * Test createObject method with invalid classname.
     */
    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException
    {
        String classname = "java.util.InvalidClass";
        TypeHandler.createObject(classname);
    }

    /**
     * Test createNumber method with valid number string.
     */
    @Test
    public void testCreateNumberValidNumberString() throws ParseException
    {
        String numberString = "12345";
        Number number = TypeHandler.createNumber(numberString);
        assertEquals(12345, number.intValue());
    }

    /**
     * Test createNumber method with invalid number string.
     */
    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumberString() throws ParseException
    {
        String numberString = "abc";
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(numberString));
    }
}