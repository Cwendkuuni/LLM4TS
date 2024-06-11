import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_StringObject() throws ParseException
    {
        Object result = TypeHandler.createValue("test", Object.class);
        Assert.assertNull(result);

        String strValue = "testStringValue";
        Object obj = new Object() {
            @Override
            public String toString() {
                return "testObjectValue";
            }
        };
        Object resultObj = TypeHandler.createValue(strValue, obj.getClass());
        Assert.assertEquals(strValue, resultObj);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateValue_StringClass() throws ParseException
    {
        String strValue = "testStringValue";

        Class<String> stringClass = String.class;
        String result = (String) TypeHandler.createValue(strValue, stringClass);
        Assert.assertEquals(strValue, result);

        Class<File> fileClass = File.class;
        File file = new File("testFile");
        File resultFile = (File) TypeHandler.createValue(file.getAbsolutePath(), fileClass);
        Assert.assertEquals(file, resultFile);

        Class<Date> dateClass = Date.class;
        Date date = new Date();
        Date resultDate = (Date) TypeHandler.createValue(date.toString(), dateClass);
        Assert.assertEquals(date, resultDate);

        Class<URL> urlClass = URL.class;
        URL url = new URL("http://test.com");
        URL resultUrl = (URL) TypeHandler.createValue(url.toString(), urlClass);
        Assert.assertEquals(url, resultUrl);

        Class<Number> numberClass = Number.class;
        Number number = 123;
        Number resultNumber = (Number) TypeHandler.createValue(number.toString(), numberClass);
        Assert.assertEquals(number, resultNumber);

        Class<Class<?>> classClass = Class.class;
        Class<?> resultClass = (Class<?>) TypeHandler.createValue("java.lang.String", classClass);
        Assert.assertEquals(String.class, resultClass);

        Class<FileInputStream> fileInputStreamClass = FileInputStream.class;
        File file = new File("testFile");
        FileInputStream resultFileInputStream = (FileInputStream) TypeHandler.createValue(file.getAbsolutePath(), fileInputStreamClass);
        Assert.assertNotNull(resultFileInputStream);

        Class<?> objectClass = Object.class;
        Object resultObject = TypeHandler.createValue("testObjectClass", objectClass);
        Assert.assertNotNull(resultObject);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidClass() throws ParseException
    {
        String strValue = "testValue";
        Class<Integer> intClass = Integer.class;
        TypeHandler.createValue(strValue, intClass.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassName() throws ParseException
    {
        String classname = "InvalidClassName";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumberString() throws ParseException
    {
        String strValue = "invalidNumberValue";
        TypeHandler.createNumber(strValue);
    }

    @Test(expected = ParseException.class)
    public void testCreateDate_InvalidDateString() throws ParseException
    {
        String strValue = "invalidDateValue";
        TypeHandler.createDate(strValue);
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFile_InvalidFile() throws ParseException, FileNotFoundException
    {
        String strValue = "invalidFileValue";
        TypeHandler.openFile(strValue);
    }
}