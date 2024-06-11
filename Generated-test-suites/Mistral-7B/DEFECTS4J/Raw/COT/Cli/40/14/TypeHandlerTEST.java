import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.*;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "test";
        Object obj = new Object();
        Object actual = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(obj, actual);
    }

    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "test";
        Class<String> clazz = String.class;
        String actual = (String) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() throws ParseException
    {
        String str = "test";
        Class<File> clazz = File.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObjectString() throws ParseException
    {
        String classname = "java.lang.String";
        Object actual = TypeHandler.createObject(classname);
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual.getClass().getName().equals(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClass() throws ParseException
    {
        String classname = "java.lang.Integer";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberStringLong() throws ParseException
    {
        String str = "123";
        Long actual = (Long) TypeHandler.createNumber(str);
        Assert.assertEquals(123L, actual.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidString() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateNumberStringDouble() throws ParseException
    {
        String str = "123.45";
        Double actual = (Double) TypeHandler.createNumber(str);
        Assert.assertEquals(123.45, actual.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateDateInvalidString() throws ParseException
    {
        String str = "invalidDate";
        TypeHandler.createDate(str);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateString() throws ParseException
    {
        String str = "2022-12-31";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURLString() throws ParseException
    {
        String str = "http://example.com";
        URL actual = TypeHandler.createURL(str);
        Assert.assertNotNull(actual);
        Assert.assertEquals("http://example.com", actual.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidString() throws ParseException
    {
        String str = "invalidURL";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFileString()
    {
        String filePath = "testFile.txt";
        File actual = TypeHandler.createFile(filePath);
        Assert.assertNotNull(actual);
        Assert.assertEquals(filePath, actual.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testOpenFileInvalidString() throws ParseException, IOException
    {
        String filePath = "testFile.txt";
        TypeHandler.openFile(filePath);
    }

    @Test
    public void testOpenFileExistingFile() throws ParseException, IOException
    {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        FileInputStream actual = TypeHandler.openFile(filePath);
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual.available() > 0);
        actual.close();
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileNonexistentFile() throws ParseException, IOException
    {
        String filePath = "nonexistentFile.txt";
        TypeHandler.openFile(filePath);
    }
}