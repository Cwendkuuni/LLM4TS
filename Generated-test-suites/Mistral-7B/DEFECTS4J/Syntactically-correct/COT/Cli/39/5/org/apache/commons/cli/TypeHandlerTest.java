package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test cases for TypeHandler class.
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
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals("test", obj);
    }

    /**
     * Test createValue method with Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        Object obj = TypeHandler.createValue("org.apache.commons.cli.test.TestClass", Object.class);
        assertNotNull(obj);
    }

    /**
     * Test createValue method with Number type.
     */
    @Test
    public void testCreateValueNumber()
    {
        Number number = TypeHandler.createValue("123", Number.class);
        assertEquals(123, number.intValue());

        number = TypeHandler.createValue("456.78", Number.class);
        assertEquals(456.78d, number.doubleValue());
    }

    /**
     * Test createValue method with Date type.
     */
    @Test
    public void testCreateValueDate()
    {
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createValue("2022-12-31", Date.class);
        });
    }

    /**
     * Test createValue method with Class type.
     */
    @Test
    public void testCreateValueClass()
    {
        try
        {
            Class<?> clazz = TypeHandler.createValue("org.apache.commons.cli.test.TestClass", Class.class);
            assertEquals(TestClass.class, clazz);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test createValue method with File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "testFile.txt";
        File file = TypeHandler.createValue(filePath, File.class);
        assertNotNull(file);
        assertEquals(filePath, file.getAbsolutePath());
    }

    /**
     * Test createValue method with existing File type.
     */
    @Test
    public void testCreateValueExistingFile()
    {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        if (file.exists())
        {
            File existingFile = TypeHandler.createValue(filePath, File.class);
            assertNotNull(existingFile);
            assertEquals(filePath, existingFile.getAbsolutePath());
        }
        else
        {
            assertThrows(FileNotFoundException.class, () -> {
                TypeHandler.createValue(filePath, File.class);
            });
        }
    }

    /**
     * Test createValue method with File[] type.
     * <p>
     * This method is not yet implemented and always throws an
     * {@link UnsupportedOperationException}.
     */
    @Test
    public void testCreateValueFiles()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Test createValue method with URL type.
     */
    @Test
    public void testCreateValueURL()
    {
        String urlString = "http://www.example.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertNotNull(url);
        assertEquals(urlString, url.toString());
    }

    /**
     * Test createObject method.
     */
    @Test
    public void testCreateObject()
    {
        String className = "org.apache.commons.cli.test.TestClass";
        Object obj = TypeHandler.createObject(className);
        assertNotNull(obj);
    }

    /**
     * Test createNumber method.
     */
    @Test
    public void testCreateNumber()
    {
        Number number = TypeHandler.createNumber("123");
        assertEquals(123, number.intValue());

        number = TypeHandler.createNumber("456.78");
        assertEquals(456.78d, number.doubleValue());

        assertThrows(ParseException.class, () -> {
            TypeHandler.createNumber("not a number");
        });
    }

    /**
     * Test createClass method.
     */
    @Test
    public void testCreateClass()
    {
        String classname = "org.apache.commons.cli.test.TestClass";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(TestClass.class, clazz);

        assertThrows(ParseException.class, () -> {
            TypeHandler.createClass("InvalidClass");
        });
    }

    /**
     * Test createFile method.
     */
    @Test
    public void testCreateFile()
    {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        File createdFile = TypeHandler.createFile(filePath);
        assertNotNull(createdFile);
        assertEquals(filePath, createdFile.getAbsolutePath());
    }

    /**
     * Test createFileInputStream method.
     */
    @Test
    public void testCreateFileInputStream()
    {
        String filePath = "testFile.txt";
        File file = new File(filePath);

        if (file.exists())
        {
            FileInputStream fileInputStream = TypeHandler.createFileInputStream(filePath);
            assertNotNull(fileInputStream);
        }
        else
        {
            assertThrows(FileNotFoundException.class, () -> {
                TypeHandler.createFileInputStream(filePath);
            });
        }
    }
}