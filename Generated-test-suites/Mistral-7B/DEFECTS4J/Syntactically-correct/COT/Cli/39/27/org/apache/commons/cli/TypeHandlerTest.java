import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        Object obj = new String("test");
        String str = "test";
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(obj, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringWrongObjectType() throws ParseException
    {
        Object obj = new Date();
        String str = "test";
        TypeHandler.createValue(str, Integer.class);
    }

    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String expected = "test";
        String str = "test";
        Object result = TypeHandler.createValue(str, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueStringObjectClass() throws ParseException
    {
        Object obj = new String("test");
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(obj, result);
    }

    @Test
    public void testCreateObjectEmptyConstructor() throws ParseException
    {
        String classname = "java.lang.String";
        Object expected = new String();
        Object result = TypeHandler.createObject(classname);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException
    {
        String classname = "InvalidClassName";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberLong() throws ParseException
    {
        String str = "1";
        Number expected = new Long(1);
        Number result = TypeHandler.createNumber(str);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidString() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateNumberDouble() throws ParseException
    {
        String str = "1.1";
        Number expected = new Double(1.1);
        Number result = TypeHandler.createNumber(str);
        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException
    {
        String str = "2022-01-01";
        Date result = TypeHandler.createDate(str);
        // UnsupportedOperationException is thrown, so no need to assert anything here
    }

    @Test
    public void testCreateURL() throws ParseException
    {
        String str = "http://www.example.com";
        URL expected = new URL("http://www.example.com");
        URL result = TypeHandler.createURL(str);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidString() throws ParseException
    {
        String str = "invalid:url";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile()
    {
        String path = "test.txt";
        File expected = new File(path);
        File result = TypeHandler.createFile(path);
        assertEquals(expected, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileNotFound()
    {
        String path = "non-existent.txt";
        File result = TypeHandler.createFile(path);
        // FileNotFoundException is thrown, so no need to assert anything here
    }

    @Test(expected = IOException.class)
    public void testCreateFileInputStream() throws IOException, FileNotFoundException
    {
        String path = "test.txt";
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        FileInputStream result = TypeHandler.createFileInputStream(path);
        assertEquals(inputStream, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStreamFileNotFound()
    {
        String path = "non-existent.txt";
        TypeHandler.createFileInputStream(path);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() throws UnsupportedOperationException
    {
        String str = "test,test2.txt";
        File[] expected = new File[] { new File("test.txt"), new File("test2.txt") };
        // UnsupportedOperationException is thrown, so no need to assert anything here
    }
}