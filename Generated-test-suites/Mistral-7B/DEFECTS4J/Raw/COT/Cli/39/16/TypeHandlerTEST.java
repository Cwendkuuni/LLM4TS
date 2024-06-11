import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassNotFoundException;
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
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() throws ParseException
    {
        String str = "testString";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() throws ParseException
    {
        String str = "testString";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidStringNumberValue() throws ParseException
    {
        String str = "testString";
        TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test
    public void testCreateValueStringNumberValueLong() throws ParseException
    {
        String str = "12345";
        Long result = (Long) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(12345L, result);
    }

    @Test
    public void testCreateValueStringNumberValueDouble() throws ParseException
    {
        String str = "123.45";
        Double result = (Double) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(123.45, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidStringDateValue() throws ParseException
    {
        String str = "testString";
        TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringDateValue() throws ParseException
    {
        String str = "testString";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateValueStringClassValue() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String str = "org.apache.commons.cli.TypeHandlerTest";
        Class<?> clazz = Class.forName(str);
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getClass().equals(clazz));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidStringClassValue() throws ParseException
    {
        String str = "org.apache.commons.cli.TypeHandlerTestNonExisting";
        TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringURLValueMalformedURL() throws ParseException
    {
        String str = "http://test.com/test.asd";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateValueStringURLValueURL() throws ParseException
    {
        String str = "http://test.com/test.html";
        URL result = TypeHandler.createURL(str);
        Assert.assertNotNull(result);
        Assert.assertEquals("http://test.com/test.html", result.toString());
    }

    @Test
    public void testCreateFile()
    {
        String str = "/test/testFile.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertNotNull(result);
        Assert.assertTrue(new File(str).exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInvalidPath()
    {
        String str = "/test/testFile.nonExisting";
        File result = TypeHandler.createFile(str);
    }

    @Test
    public void testCreateFileInputStream() throws FileNotFoundException, IOException
    {
        String str = "/test/testFile.txt";
        File file = new File(str);
        FileInputStream inputStream = TypeHandler.createFileInputStream(str);
        Assert.assertNotNull(inputStream);
        Assert.assertSame(inputStream, new FileInputStream(file));
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStreamInvalidPath() throws FileNotFoundException
    {
        String str = "/test/testFile.nonExisting";
        TypeHandler.createFileInputStream(str);
    }

    @Test(expected = ParseException.class)
    public void testCreateFilesInvalidString() throws ParseException
    {
        String str = "testString";
        TypeHandler.createFiles(str);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() throws ParseException
    {
        String str = "test/testFile1.txt, test/testFile2.txt";
        TypeHandler.createFiles(str);
    }
}