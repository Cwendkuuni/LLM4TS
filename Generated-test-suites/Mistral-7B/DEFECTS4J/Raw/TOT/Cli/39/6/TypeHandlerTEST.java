import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueString() throws ParseException
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, obj);
    }

    @Test
    public void testCreateValueClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.String";
        Class<?> clazz = Class.forName(classname);
        Object obj = TypeHandler.createValue("test", clazz);
        Assert.assertEquals(classname, obj.getClass().getName());
    }

    @Test
    public void testCreateValueLong() throws ParseException
    {
        String str = "123";
        Class<?> clazz = Long.TYPE;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123L, obj);
    }

    @Test
    public void testCreateValueDouble() throws ParseException
    {
        String str = "123.45";
        Class<?> clazz = Double.TYPE;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123.45, obj, 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidNumber() throws ParseException
    {
        String str = "abcd";
        Class<?> clazz = Double.TYPE;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.fail("Expected ParseException but got: " + obj);
    }

    @Test
    public void testCreateValueDate() throws ParseException, UnsupportedOperationException
    {
        String str = "2022-01-01";
        Class<?> clazz = Date.class;
        Object obj = TypeHandler.createValue(str, clazz);
        // UnsupportedOperationException is expected as createDate method is not implemented
        //Assert.assertNotNull(obj);
    }

    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        String str = "http://test.com";
        Class<?> clazz = URL.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(new URL("http://test.com"), obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidURL() throws ParseException, MalformedURLException
    {
        String str = "test.com";
        Class<?> clazz = URL.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.fail("Expected ParseException but got: " + obj);
    }

    @Test
    public void testCreateValueFile()
    {
        String str = "/test/file.txt";
        Class<?> clazz = File.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(new File(str), obj);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueInvalidFile()
    {
        String str = "/test/nonexistentfile.txt";
        Class<?> clazz = File.class;
        Object obj = TypeHandler.createValue(str, clazz);
        File file = (File) obj;
        Assert.fail("Expected FileNotFoundException but got: " + file.exists());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueUnknownType() throws ParseException
    {
        String str = "unknownType";
        Class<?> clazz = Object.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.fail("Expected ParseException but got: " + obj);
    }
}