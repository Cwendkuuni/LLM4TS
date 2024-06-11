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

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_StringObject() throws ParseException
    {
        Object obj = new Object();
        String str = "test";
        Object returnedObj = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(str, returnedObj);
    }

    @Test
    public void testCreateValue_StringStringClass() throws ParseException
    {
        String clazz = "java.lang.String";
        String str = "test";
        String returnedStr = (String) TypeHandler.createValue(str, (Class<String>) Class.forName(clazz));
        Assert.assertEquals(str, returnedStr);
    }

    @Test
    public void testCreateValue_StringClassNull() throws ParseException
    {
        String clazz = "java.lang.String";
        String str = "test";
        Assert.assertNull(TypeHandler.createValue(str, (Class<Object>) Class.forName(clazz.substring(0, clazz.lastIndexOf(".")))));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidClass() throws ParseException
    {
        String clazz = "java.lang.InvalidClass";
        String str = "test";
        TypeHandler.createValue(str, (Class<Object>) Class.forName(clazz));
    }

    @Test
    public void testCreateObject() throws ParseException
    {
        String classname = "java.util.Date";
        Date date = new Date();
        Object returnedDate = TypeHandler.createObject(classname);
        Assert.assertEquals(date.getClass(), returnedDate.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassThrowsParseException() throws ParseException
    {
        String classname = "java.lang.InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_InvalidClassThrowsInstantiationException() throws ParseException, InstantiationException
    {
        String classname = "java.lang.String";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        Assert.assertEquals(Long.class, number.getClass());
        Assert.assertEquals(123L, number.longValue());

        String strWithDecimal = "123.45";
        Number numberWithDecimal = TypeHandler.createNumber(strWithDecimal);
        Assert.assertEquals(Double.class, numberWithDecimal.getClass());
        Assert.assertEquals(123.45, numberWithDecimal.doubleValue(), 0.01);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidNumberFormat() throws ParseException, NumberFormatException
    {
        String str = "invalidNumber";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.util.Date";
        Class<?> returnedClass = TypeHandler.createClass(classname);
        Assert.assertEquals(Date.class, returnedClass);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_InvalidClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() throws ParseException
    {
        String str = "2022-12-31";
        Date date = TypeHandler.createDate(str);
        Assert.assertNotNull(date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_UnsupportedOperationException() throws ParseException
    {
        String str = "2022-12-31";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String str = "http://example.com";
        URL url = TypeHandler.createURL(str);
        Assert.assertEquals("http", url.getProtocol());
        Assert.assertEquals("example.com", url.getHost());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL() throws ParseException, MalformedURLException
    {
        String str = "invalidURL";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() throws ParseException
    {
        String str = "/path/to/file.txt";
        File file = TypeHandler.createFile(str);
        Assert.assertTrue(file.exists());
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_InvalidFile() throws ParseException
    {
        String str = "invalidFile";
        TypeHandler.createFile(str);
    }

    @Test
    public void testOpenFile() throws ParseException, FileNotFoundException, IOException
    {
        String str = "/path/to/file.txt";
        FileInputStream fileInputStream = TypeHandler.openFile(str);
        Assert.assertNotNull(fileInputStream);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_InvalidFile() throws ParseException, FileNotFoundException
    {
        String str = "invalidFile";
        TypeHandler.openFile(str);
    }
}