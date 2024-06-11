import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_StringObject() throws ParseException
    {
        String str = "test";
        Object obj = new Object();
        Object returnedObj = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, returnedObj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringWrongObjectType() throws ParseException
    {
        String str = "test";
        int obj = 1;
        TypeHandler.createValue(str, obj.getClass());
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_PatternOptionBuilder_STRING_VALUE() throws ParseException
    {
        String str = "test";
        Object returnedObj = TypeHandler.createValue(str, String.class);
        assertEquals(str, returnedObj);
    }

    @Test
    public void testCreateValue_PatternOptionBuilder_OBJECT_VALUE() throws ParseException
    {
        String className = "java.util.Date";
        Object returnedObj = TypeHandler.createValue(className, Date.class);
        assertTrue(returnedObj instanceof Date);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_PatternOptionBuilder_WRONG_OBJECT_VALUE() throws ParseException
    {
        String className = "java.util.Date";
        Object returnedObj = TypeHandler.createValue(className, int.class);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_PatternOptionBuilder_NUMBER_VALUE() throws ParseException
    {
        String str = "123";
        Number returnedNum = TypeHandler.createValue(str, int.class);
        assertEquals((int) 123, returnedNum.intValue());

        String strWithDecimal = "123.45";
        Double returnedDouble = TypeHandler.createValue(strWithDecimal, Double.class);
        assertEquals(123.45, returnedDouble.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_PatternOptionBuilder_WRONG_NUMBER_VALUE() throws ParseException
    {
        String str = "test";
        Number returnedNum = TypeHandler.createValue(str, int.class);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_PatternOptionBuilder_DATE_VALUE() throws ParseException
    {
        String dateString = "2022-03-15";
        Date returnedDate = TypeHandler.createValue(dateString, Date.class);
        assertEquals(new Date(2022, 2, 15), returnedDate);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_PatternOptionBuilder_DATE_VALUE_UNSUPPORTED() throws ParseException
    {
        String dateString = "test";
        Date returnedDate = TypeHandler.createValue(dateString, Date.class);
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testCreateValue_PatternOptionBuilder_CLASS_VALUE() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.util.Date";
        Class<?> clazz = Class.forName(className);
        Object returnedObj = TypeHandler.createValue(className, clazz);
        assertTrue(returnedObj instanceof Date);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_PatternOptionBuilder_CLASS_VALUE_WRONG() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.util.ArrayList";
        Class<?> clazz = Class.forName(className);
        Object returnedObj = TypeHandler.createValue(className, Date.class);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateFile()
    {
        String filePath = "test.txt";
        File returnedFile = TypeHandler.createFile(filePath);
        assertTrue(new File(filePath).exists());
        assertTrue(returnedFile.equals(new File(filePath)));
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_FileNotFoundException()
    {
        String filePath = "test.notexists.txt";
        File returnedFile = TypeHandler.createFile(filePath);
        fail("Expected FileNotFoundException");
    }

    @Test
    public void testCreateURL() throws ParseException
    {
        String urlString = "http://www.example.com";
        URL returnedURL = TypeHandler.createURL(urlString);
        assertEquals(new URL("http://www.example.com"), returnedURL);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_MalformedURLException() throws ParseException
    {
        String urlString = "http://www.example.com/test";
        URL returnedURL = TypeHandler.createURL(urlString + "%");
        fail("Expected ParseException");
    }
}