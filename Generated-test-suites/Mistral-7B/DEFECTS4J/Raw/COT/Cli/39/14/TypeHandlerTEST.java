import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        Object obj = new Object();
        String str = "test";
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassStringClass() throws ParseException
    {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        String str = "test";
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() throws ParseException
    {
        String str = "test";
        Class<?> clazz = Object.class;
        TypeHandler.createValue(str, clazz);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateObjectString() throws ParseException
    {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assertTrue(result instanceof String);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClass() throws ParseException
    {
        String classname = "java.lang.Integer";
        TypeHandler.createObject(classname);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateNumberString() throws ParseException
    {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123, result.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidString() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateDateString() throws ParseException, IOException
    {
        String str = "2022-12-31";
        Date result = TypeHandler.createDate(str);
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateInvalidString() throws ParseException
    {
        String str = "2022-12-32";
        TypeHandler.createDate(str);
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testCreateURLString() throws ParseException
    {
        String str = "http://example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidString() throws ParseException
    {
        String str = "http:example.com";
        TypeHandler.createURL(str);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateFileString()
    {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileNonExistentFile()
    {
        String str = "non_existent.txt";
        File result = TypeHandler.createFile(str);
        assertNull(result);
    }

    @Test
    public void testCreateFileInputStreamString() throws IOException, FileNotFoundException
    {
        String str = "test.txt";
        File file = new File(str);
        FileInputStream result = TypeHandler.createFileInputStream(str);
        assertEquals(file, result.getFileDescriptor().getFileDescriptor());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStreamNonExistentFile() throws IOException
    {
        String str = "non_existent.txt";
        TypeHandler.createFileInputStream(str);
        fail("Expected FileNotFoundException");
    }
}