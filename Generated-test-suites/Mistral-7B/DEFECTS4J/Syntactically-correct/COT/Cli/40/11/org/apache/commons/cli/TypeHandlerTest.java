import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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
    public void testCreateValueStringString() throws ParseException
    {
        String str = "test";
        Class<String> clazz = String.class;
        String result = (String) TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassWithStringConstructor() throws ParseException
    {
        String className = "java.lang.String";
        String str = "test";
        Class<String> clazz = String.class;
        String result = (String) TypeHandler.createValue(str, Class.forName(className));
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassWithVoidConstructor() throws ParseException
    {
        String classname = "java.lang.Object";
        String str = "test";
        Class<?> clazz = Class.forName(classname);
        assertNull(TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateNumberLong() throws ParseException
    {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123L, result.longValue());
    }

    @Test
    public void testCreateNumberDouble() throws ParseException
    {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123.45, result.doubleValue(), 0.001);
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
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassInvalid() throws ParseException, ClassNotFoundException
    {
        String classname = "java.nonExistentClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() throws ParseException, UnsupportedOperationException
    {
        String str = "2023-03-15";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String str = "http://example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals("http://example.com", result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalid() throws ParseException, MalformedURLException
    {
        String str = "invalid_url";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile()
    {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertEquals(new File(str), result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileNonExistentFile() throws ParseException, FileNotFoundException
    {
        String str = "non_existent_file.txt";
        TypeHandler.openFile(str);
    }
}