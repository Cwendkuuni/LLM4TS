import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * TypeHandler Test Class
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String object
     */
    @Test
    public void testCreateValueStringObject()
    {
        String str = "TestString";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    /**
     * Test createValue method with Object type
     */
    @Test(expected = ParseException.class)
    public void testCreateValueObjectObject()
    {
        Object obj = TypeHandler.createValue("TestString", Object.class);
        fail("Expected ParseException but got " + obj.getClass().getName());
    }

    /**
     * Test createValue method with Class object
     */
    @Test
    public <T> void testCreateValueClassObject() throws ParseException
    {
        String str = "TestString";
        Class<T> clazz = String.class;
        T obj = (T) TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    /**
     * Test createValue method with invalid Class object
     */
    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClassObject()
    {
        String str = "TestString";
        Class<?> invalidClass = new Class<Object>() {
        };
        try
        {
            TypeHandler.createValue(str, invalidClass);
            fail("Expected ParseException but got null");
        }
        catch (NullPointerException e)
        {
            // expected since the Class is invalid
        }
    }

    /**
     * Test createObject method
     */
    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.lang.String";
        Object obj = TypeHandler.createObject(className);
        assertEquals(String.class, obj.getClass());
    }

    /**
     * Test createObject method with invalid class name
     */
    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName()
    {
        String className = "java.lang.InvalidClass";
        try
        {
            TypeHandler.createObject(className);
            fail("Expected ParseException but got null");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test createNumber method with valid number string
     */
    @Test
    public void testCreateNumberValidNumberString()
    {
        String str = "1234567890.123456";
        Number number = TypeHandler.createNumber(str);
        if (number instanceof Double)
        {
            assertEquals(1234567890.123456, ((Double) number).doubleValue(), 0.0);
        }
        else
        {
            assertEquals(1234567890L, number.longValue());
        }
    }

    /**
     * Test createNumber method with invalid number string
     */
    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumberString()
    {
        String str = "InvalidNumberString";
        TypeHandler.createNumber(str);
        fail("Expected ParseException but got null");
    }

    /**
     * Test createDate method
     * This method is not yet implemented and always throws an
     * {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate()
    {
        String str = "TestDate";
        Date date = TypeHandler.createDate(str);
        fail("Expected UnsupportedOperationException but got " + date.getClass().getName());
    }

    /**
     * Test createURL method with valid URL string
     */
    @Test
    public void testCreateURLValidURLString()
    {
        String urlString = "http://www.apache.org";
        URL url = TypeHandler.createURL(urlString);
        assertNotNull(url);
    }

    /**
     * Test createURL method with invalid URL string
     */
    @Test(expected = ParseException.class)
    public void testCreateURLInvalidURLString()
    {
        String urlString = "InvalidURLString";
        try
        {
            TypeHandler.createURL(urlString);
            fail("Expected ParseException but got null");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test createFile method with valid file path
     */
    @Test
    public void testCreateFileValidFilePath()
    {
        String filePath = "testFile.txt";
        File file = TypeHandler.createFile(filePath);
        assertNotNull(file);
    }

    /**
     * Test createFile method with invalid file path
     */
    @Test(expected = ParseException.class)
    public void testCreateFileInvalidFilePath()
    {
        String filePath = "InvalidFilePath";
        try
        {
            TypeHandler.createFile(filePath);
            fail("Expected ParseException but got null");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test openFile method with existent file
     */
    @Test
    public void testOpenFileExistentFile() throws ParseException, FileNotFoundException, IOException
    {
        String filePath = "testFile.txt";
        FileInputStream inputStream = TypeHandler.openFile(filePath);
        assertNotNull(inputStream);
    }

    /**
     * Test openFile method with nonexistent file
     */
    @Test(expected = ParseException.class)
    public void testOpenFileNonexistentFile()
    {
        String filePath = "testFileNonexistent.txt";
        try
        {
            TypeHandler.openFile(filePath);
            fail("Expected ParseException but got null");
        }
        catch (ParseException e)
        {
            // expected
        }
    }
}