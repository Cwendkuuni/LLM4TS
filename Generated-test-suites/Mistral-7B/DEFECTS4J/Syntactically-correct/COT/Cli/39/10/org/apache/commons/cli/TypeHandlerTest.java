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

/**
 * Test class for TypeHandler
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueWithStringObject() throws ParseException
    {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithInvalidObject() throws ParseException
    {
        String str = "test";
        Object obj = new Object();
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, Integer.class));
    }

    @Test
    public void testCreateObject() throws ParseException
    {
        String className = String.class.getName();
        Object result = TypeHandler.createObject(className);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof String);
    }

    @Test
    public void testCreateObjectWithInvalidClass() throws ParseException
    {
        String className = Integer.class.getName();
        assertThrows(ParseException.class, () -> TypeHandler.createObject(className));
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateNumberWithInvalidString() throws ParseException
    {
        String str = "abc";
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateDate() throws ParseException
    {
        String str = "2023-03-31";
        Date result = TypeHandler.createDate(str);
        Assert.assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateWithInvalidString() throws ParseException
    {
        String str = "invalidDate";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException
    {
        String str = "http://example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidString() throws ParseException
    {
        String str = "invalidURL";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile()
    {
        String str = "path/to/file.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileWithInvalidPath()
    {
        String str = "nonExistentFile";
        File result = TypeHandler.createFile(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateFileInputStream() throws IOException, ParseException
    {
        String filePath = "path/to/existingFile.txt";
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        Object result = TypeHandler.createValue(filePath, FileInputStream.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof FileInputStream);
        Assert.assertEquals(inputStream.available(), result.available());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStreamWithInvalidPath() throws IOException, ParseException
    {
        String filePath = "nonExistentFile";
        Object result = TypeHandler.createValue(filePath, FileInputStream.class);
        Assert.assertNull(result);
    }
}