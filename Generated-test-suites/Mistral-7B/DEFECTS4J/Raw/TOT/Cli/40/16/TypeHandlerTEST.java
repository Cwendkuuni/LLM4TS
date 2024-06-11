import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_String_String() throws ParseException
    {
        String str = "test string";
        String expected = "test string";
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_String() throws ParseException
    {
        String str = "test string";
        Class<?> clazz = String.class;
        String expected = "test string";
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);

        clazz = Date.class;
        try
        {
            actual = TypeHandler.createValue(str, clazz);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateValue_String_Class_ValidDate() throws ParseException, IOException
    {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;
        Date expected = new Date(Long.parseLong("2022-12-31") * 24L * 60L * 60L * 1000);
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_InvalidDate() throws ParseException
    {
        String str = "2022-12-32";
        Class<?> clazz = Date.class;
        Object actual = TypeHandler.createValue(str, clazz);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_String_Class_File() throws ParseException, IOException
    {
        String str = "/path/to/test.txt";
        Class<?> clazz = File.class;
        File expected = new File(str);
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInput_String_FileNotFoundException() throws ParseException, FileNotFoundException
    {
        String str = "/path/to/non_existent_file.txt";
        Object actual = TypeHandler.openFile(str);
        fail("Expected FileNotFoundException");
    }

    @Test
    public void testCreateValue_String_Class_URL() throws ParseException
    {
        String str = "http://example.com";
        Class<?> clazz = URL.class;
        URL expected = new URL(str);
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_Number() throws ParseException
    {
        String str = "12345";
        Class<?> clazz = int.class;
        Number expected = 12345;
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);

        clazz = double.class;
        Number expectedDouble = 12345.0;
        Object actualDouble = TypeHandler.createValue(str, clazz);
        assertEquals(expectedDouble, actualDouble);

        try
        {
            actual = TypeHandler.createValue("abcd", clazz);
            fail("Expected NumberFormatException");
        }
        catch (NumberFormatException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateValue_String_Class_Object() throws ParseException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<?> expectedClass = Date.class;
        Object actual = TypeHandler.createObject(classname);
        assertEquals(expectedClass, actual.getClass());

        try
        {
            String invalidClassname = "java.util.DateInvalid";
            actual = TypeHandler.createObject(invalidClassname);
            fail("Expected ClassNotFoundException or InstantiationException or IllegalAccessException");
        }
        catch (ClassNotFoundException e)
        {
            // Expected
        }
        catch (InstantiationException e)
        {
            // Expected
        }
        catch (IllegalAccessException e)
        {
            // Expected
        }
    }
}