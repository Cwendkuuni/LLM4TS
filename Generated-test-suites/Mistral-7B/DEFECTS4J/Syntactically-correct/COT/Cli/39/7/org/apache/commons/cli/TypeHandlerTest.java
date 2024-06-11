package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

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
 * Test class for TypeHandler
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(String, Object)}.
     * @throws Exception
     */
    @Test
    public void testCreateValueStringObject() throws Exception
    {
        Object strObj = "Test string";
        Object intObj = 123;
        Object doubleObj = 123.45;
        Object dateObj = new Date();
        Object fileObj = new File("TestFile.txt");
        Object urlObj = new URL("http://www.test.com");

        Object value = TypeHandler.createValue((String) strObj, strObj.getClass());
        assertEquals(strObj, value);

        value = TypeHandler.createValue((String) intObj, intObj.getClass());
        assertEquals(intObj, value);

        value = TypeHandler.createValue((String) doubleObj, doubleObj.getClass());
        assertEquals(doubleObj, value);

        value = TypeHandler.createValue((String) dateObj, dateObj.getClass());
        assertEquals(dateObj, value);

        value = TypeHandler.createValue((String) fileObj, File.class);
        assertSame(fileObj, value);

        value = TypeHandler.createValue((String) urlObj, URL.class);
        assertSame(urlObj, value);

        String invalidStr = "";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(invalidStr, String.class));
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)}.
     */
    @Test
    public void testCreateObject()
    {
        String className = "java.lang.String";
        Object value = TypeHandler.createValue((String) className, Class.class);

        assertEquals(className, value.getClass().getName());

        assertNull(TypeHandler.createValue((String) "nonExistentClass", Class.class));
    }

    /**
     * Test method for {@link TypeHandler#createDate(String)}.
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws Exception
    {
        String dateStr = "2022-12-31";
        TypeHandler.createDate(dateStr);
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)}.
     */
    @Test
    public void testCreateURL()
    {
        String urlStr = "http://www.test.com";
        Object value = TypeHandler.createValue((String) urlStr, URL.class);

        assertSame(new URL(urlStr), value);

        String invalidUrlStr = "invalidURL";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(invalidUrlStr, URL.class));
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)}.
     */
    @Test
    public void testCreateFile()
    {
        String filePath = "TestFile.txt";
        Object value = TypeHandler.createValue((String) filePath, File.class);

        assertSame(new File(filePath), value);

        String invalidFilePath = "";
        assertThrows(FileNotFoundException.class, () -> {
            FileInputStream fileInput = new FileInputStream((File) TypeHandler.createValue(invalidFilePath, File.class));
        });
    }

    /**
     * Test method for {@link TypeHandler#createFiles(String)}.
     * @throws IOException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() throws IOException
    {
        String filePaths = "TestFile1.txt, TestFile2.txt";
        File[] values = (File[]) TypeHandler.createValue((String) filePaths, File[].class);
    }
}