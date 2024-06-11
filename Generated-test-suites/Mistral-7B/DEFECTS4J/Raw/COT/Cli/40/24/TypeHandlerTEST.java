import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueWithStringObject() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueWithClassObject() throws ParseException
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createValue(classname, String.class);
        assertTrue(obj instanceof String);
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException
    {
        String numberStr = "123";
        Number number = TypeHandler.createValue(numberStr, int.class);
        assertEquals((int) 123, number.intValue());

        Number numberDouble = TypeHandler.createValue(numberStr, double.class);
        assertEquals(123.0, numberDouble.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidNumber() throws ParseException
    {
        String invalidNumberStr = "abc";
        TypeHandler.createValue(invalidNumberStr, int.class);
    }

    @Test
    public void testCreateValueWithDate() throws ParseException
    {
        String dateStr = "2022-01-01";
        Date date = TypeHandler.createValue(dateStr, Date.class);
        assertEquals("Fri Jan 01 00:00:00 EST 2022", date.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithUnsupportedDateFormat() throws ParseException
    {
        String unsupportedDateFormatStr = "2022/01/01";
        TypeHandler.createValue(unsupportedDateFormatStr, Date.class);
    }

    @Test
    public void testCreateValueWithClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.String";
        Class<?> clazz = Class.forName(classname);
        Object obj = TypeHandler.createValue("test", clazz);
        assertTrue(obj instanceof String);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.Integer";
        Class<?> clazz = Class.forName(classname);
        TypeHandler.createValue("test", clazz);
    }

    @Test
    public void testCreateValueWithURL() throws ParseException, MalformedURLException
    {
        String urlStr = "http://example.com";
        URL url = TypeHandler.createValue(urlStr, URL.class);
        assertEquals(urlStr, url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidURL() throws ParseException, MalformedURLException
    {
        String invalidUrlStr = "http://example.com/abc";
        TypeHandler.createValue(invalidUrlStr, URL.class);
    }

    @Test
    public void testCreateFile()
    {
        String filePath = "/path/to/file.txt";
        File file = TypeHandler.createFile(filePath);
        assertTrue(file.exists());
    }

    @Test(expected = ParseException.class)
    public void testCreateFileWithInvalidPath() throws ParseException
    {
        String invalidFilePath = "/path/to/non-existent-file.txt";
        TypeHandler.createFile(invalidFilePath);
    }

    @Test
    public void testOpenFile() throws ParseException, FileNotFoundException, IOException
    {
        String filePath = "/path/to/file.txt";
        FileInputStream inputStream = TypeHandler.openFile(filePath);
        assertNotNull(inputStream);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileWithInvalidPath() throws ParseException, FileNotFoundException, IOException
    {
        String invalidFilePath = "/path/to/non-existent-file.txt";
        TypeHandler.openFile(invalidFilePath);
    }
}