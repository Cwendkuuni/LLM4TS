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
        String str = "testString";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "testString";
        Class<String> clazz = String.class;
        String result = (String) TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassWithObjectCreation() throws ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        String classname = "java.util.Date";
        Object result = TypeHandler.createValue(classname, Date.class);
        Date expectedDate = new Date();
        assertTrue(expectedDate.getClass().isInstance(result));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassWithException() throws ParseException, ClassNotFoundException
    {
        String classname = "NonExistentClass";
        Class<Object> clazz = Object.class;
        TypeHandler.createValue(classname, clazz);
    }

    @Test
    public void testCreateNumberLong() throws ParseException
    {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        Long expectedValue = 123L;
        assertEquals(expectedValue.longValue(), result.longValue());
    }

    @Test
    public void testCreateNumberDouble() throws ParseException
    {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        Double expectedValue = 123.45;
        assertEquals(expectedValue.doubleValue(), result.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalid() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.util.Date";
        Class<?> result = TypeHandler.createClass(classname);
        Class<Date> expectedClass = Date.class;
        assertEquals(expectedClass, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassNotFound() throws ParseException
    {
        String classname = "NonExistentClass";
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate()
    {
        String str = "2022-12-31";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String str = "http://example.com";
        URL result = TypeHandler.createURL(str);
        String expectedString = "http://example.com";
        assertEquals(expectedString, result.toExternalForm());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalid() throws ParseException
    {
        String str = "http://example.com/invalid";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile()
    {
        String str = "path/to/file.txt";
        File result = TypeHandler.createFile(str);
        File expectedFile = new File(str);
        assertEquals(expectedFile, result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile()
    {
        String str = "nonexistentfile.txt";
        TypeHandler.openFile(str);
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileNotFound() throws FileNotFoundException
    {
        String str = "nonexistentfile.txt";
        FileInputStream result = TypeHandler.openFile(str);
        result.close();
    }

    @Test
    public void testCreateFiles() throws IOException
    {
        String str = "path/to/files/*.txt";
        File[] result = TypeHandler.createFiles(str);
        File expectedFile1 = new File("path/to/files/file1.txt");
        File expectedFile2 = new File("path/to/files/file2.txt");
        File[] expectedFiles = {expectedFile1, expectedFile2};
        Assert.assertArrayEquals(expectedFiles, result);
    }
}