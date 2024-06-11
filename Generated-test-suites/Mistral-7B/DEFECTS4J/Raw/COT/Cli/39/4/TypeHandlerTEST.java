import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * TypeHandlerTest
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValue_StringAndObject_ReturnsCorrectInstance() throws ParseException
    {
        String str = "Hello World";
        Object obj = new String("Hello World");
        Object returnedObject = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(obj, returnedObject);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringAndInvalidClass_ThrowsParseException() throws ParseException
    {
        String str = "123";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValue_StringAndStringClass_ReturnsOriginalString() throws ParseException
    {
        String str = "StringValue";
        Class<?> clazz = String.class;
        Object returnedObject = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, returnedObject);
    }

    @Test
    public void testCreateValue_StringAndObjectClass_ReturnsNewInstance() throws ParseException
    {
        String classname = "java.util.Date";
        Class<?> clazz = Class.forName(classname);
        Object returnedObject = TypeHandler.createValue("2023-01-01", clazz);
        Assert.assertNotNull(returnedObject);
        Assert.assertInstanceOf(Date.class, returnedObject);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidString_ThrowsParseException() throws ParseException
    {
        String str = "InvalidString";
        Object obj = new String("Hello World");
        TypeHandler.createValue(str, obj.getClass());
    }

    @Test
    public void testCreateNumber_ValidString_ReturnsCorrectNumberInstance() throws ParseException
    {
        String str = "123.45";
        Number number = new Double(123.45);
        Number returnedNumber = TypeHandler.createNumber(str);
        Assert.assertEquals(number, returnedNumber);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidString_ThrowsParseException() throws ParseException
    {
        String str = "InvalidString";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateFile_ValidString_ReturnsCorrectFileInstance() throws IOException, ParseException
    {
        String filePath = "/test/TypeHandlerTest/testFile.txt";
        File expectedFile = new File(filePath);
        File returnedFile = TypeHandler.createFile(filePath);
        Assert.assertEquals(expectedFile, returnedFile);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_InvalidString_ThrowsFileNotFoundException() throws IOException, ParseException
    {
        String filePath = "/test/TypeHandlerTest/nonexistentFile.txt";
        TypeHandler.createFile(filePath);
    }

    @Test
    public void testCreateURL_ValidString_ReturnsCorrectURLInstance() throws MalformedURLException, ParseException
    {
        String urlString = "http://www.example.com";
        URL expectedURL = new URL(urlString);
        URL returnedURL = TypeHandler.createURL(urlString);
        Assert.assertEquals(expectedURL, returnedURL);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidString_ThrowsMalformedURLException() throws MalformedURLException, ParseException
    {
        String urlString = "InvalidURL";
        TypeHandler.createURL(urlString);
    }
}