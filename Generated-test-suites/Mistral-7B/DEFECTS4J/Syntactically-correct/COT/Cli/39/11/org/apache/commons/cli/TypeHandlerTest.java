import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassNotFoundException() throws ParseException
    {
        String str = "test.ClassToBeTested";
        Class<?> clazz = null;

        try
        {
            clazz = Class.forName(str);
        }
        catch (final ClassNotFoundException e)
        {
            // expected
        }

        try
        {
            Object result = TypeHandler.createValue(str, clazz);
            fail();
        }
        catch (final ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateValueStringClassInstantiationException() throws ParseException
    {
        String str = "test.ClassToBeTested";
        Class<?> clazz = null;

        try
        {
            clazz = Class.forName(str);
        }
        catch (final ClassNotFoundException e)
        {
            // expected
        }

        try
        {
            clazz.newInstance();
            fail();
        }
        catch (final InstantiationException | IllegalAccessException e)
        {
            // expected
        }

        try
        {
            Object result = TypeHandler.createValue(str, clazz);
            fail();
        }
        catch (final ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateValueStringPatternOptionBuilder_STRING_VALUE() throws ParseException
    {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringNumber() throws ParseException
    {
        String str = "123";
        Class<?> clazz = Number.class;
        Object result = TypeHandler.createValue(str, clazz);
        Number expected = 123L;
        Assert.assertEquals(expected.getClass(), result.getClass());
        if (result instanceof Long)
        {
            Assert.assertEquals(expected.longValue(), ((Long) result).longValue());
        }
        else if (result instanceof Double)
        {
            Assert.assertEquals(expected.doubleValue(), ((Double) result).doubleValue(), 0.0);
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringNumberFormatException() throws ParseException
    {
        String str = "test";
        Class<?> clazz = Number.class;
        Object result = TypeHandler.createValue(str, clazz);
        fail();
    }

    @Test
    public void testCreateValueStringClassWithEmptyConstructor() throws ParseException
    {
        String classname = "java.util.Date";
        Class<?> clazz = TypeHandler.createClass(classname);
        Object result = TypeHandler.createObject(classname);
        Assert.assertNotNull(result);
        Assert.assertEquals(clazz, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassNoEmptyConstructor() throws ParseException
    {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        fail();
    }

    @Test
    public void testCreateValueStringDate() throws ParseException
    {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        Date expected = new Date(2022, 11, 31);
        Assert.assertEquals(expected.getClass(), result.getClass());
        Assert.assertEquals(expected.getTime(), ((Date) result).getTime());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringDateUnsupportedOperationException() throws ParseException
    {
        String str = "test";
        Class<?> clazz = Date.class;
        Object result = TypeHandler.createDate(str);
        fail();
    }

    @Test
    public void testCreateValueStringURL() throws ParseException
    {
        String str = "http://www.example.com";
        Class<?> clazz = URL.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringURLMalformedURLException() throws ParseException
    {
        String str = "http://www.example.com/test:123";
        Class<?> clazz = URL.class;
        Object result = TypeHandler.createValue(str, clazz);
        fail();
    }

    @Test
    public void testCreateValueStringFile() throws ParseException, FileNotFoundException
    {
        String str = "test.txt";
        Class<?> clazz = File.class;
        Object result = TypeHandler.createValue(str, clazz);
        File expected = new File(str);
        Assert.assertEquals(expected.getAbsolutePath(), result.toString());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringFileNotFoundException() throws ParseException, FileNotFoundException
    {
        String str = "nonexistent.txt";
        Class<?> clazz = File.class;
        Object result = TypeHandler.createValue(str, clazz);
        fail();
    }

    @Test
    public void testCreateValueStringFileInputStream() throws ParseException, FileNotFoundException
    {
        String str = "test.txt";
        Class<?> clazz = FileInputStream.class;
        Object result = TypeHandler.createValue(str, clazz);
        File file = new File(str);
        FileInputStream expected = new FileInputStream(file);
        Assert.assertEquals(expected.getClass(), result.getClass());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringFileInputStreamFileNotFoundException() throws ParseException, FileNotFoundException
    {
        String str = "nonexistent.txt";
        Class<?> clazz = FileInputStream.class;
        Object result = TypeHandler.createValue(str, clazz);
        fail();
    }

    @Test
    public void testCreateValueStringFiles()
    {
        String str = "test1.txt, test2.txt";
        Class<?> clazz = File[].class;
        Object result = TypeHandler.createValue(str, clazz);
        File[] expected = new File[] { new File("test1.txt"), new File("test2.txt") };
        Assert.assertArrayEquals(expected, ((File[]) result));
    }
}