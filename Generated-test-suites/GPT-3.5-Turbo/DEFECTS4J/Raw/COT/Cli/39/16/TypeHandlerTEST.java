import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() throws ParseException {
        String input = "test";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testCreateValueWithObject() throws ParseException {
        String input = "java.lang.String";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals(input, result.getClass().getName());
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String input = "123";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Number);
        Assert.assertEquals(Long.valueOf(input), result);
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        String input = "2021-01-01";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.DATE_VALUE);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithClass() throws ParseException {
        String input = "java.lang.String";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.CLASS_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testCreateValueWithFile() throws ParseException {
        String input = "/path/to/file";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.FILE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertEquals(input, ((File) result).getPath());
    }

    @Test
    public void testCreateValueWithExistingFile() throws ParseException {
        String input = "/path/to/existing/file";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertEquals(input, ((File) result).getPath());
    }

    @Test
    public void testCreateValueWithFiles() throws ParseException {
        String input = "/path/to/files";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.FILES_VALUE);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithURL() throws ParseException, MalformedURLException {
        String input = "https://www.example.com";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.URL_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals(input, ((URL) result).toString());
    }

    @Test
    public void testCreateValueWithUnknownType() throws ParseException {
        String input = "unknown";
        Object result = TypeHandler.createValue(input, PatternOptionBuilder.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String input = "java.lang.String";
        Object result = TypeHandler.createObject(input);
        Assert.assertNotNull(result);
        Assert.assertEquals(input, result.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithUnknownClass() throws ParseException {
        String input = "unknown";
        TypeHandler.createObject(input);
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        String input = "123";
        Object result = TypeHandler.createNumber(input);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(Long.valueOf(input), result);
    }

    @Test
    public void testCreateNumberWithDouble() throws ParseException {
        String input = "123.45";
        Object result = TypeHandler.createNumber(input);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Double);
        Assert.assertEquals(Double.valueOf(input), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidInput() throws ParseException {
        String input = "invalid";
        TypeHandler.createNumber(input);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String input = "java.lang.String";
        Object result = TypeHandler.createClass(input);
        Assert.assertNotNull(result);
        Assert.assertEquals(input, result.getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithUnknownClass() throws ParseException {
        String input = "unknown";
        TypeHandler.createClass(input);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException {
        String input = "2021-01-01";
        TypeHandler.createDate(input);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        String input = "https://www.example.com";
        Object result = TypeHandler.createURL(input);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals(input, ((URL) result).toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        String input = "invalid-url";
        TypeHandler.createURL(input);
    }

    @Test
    public void testCreateFile() {
        String input = "/path/to/file";
        Object result = TypeHandler.createFile(input);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertEquals(input, ((File) result).getPath());
    }

    @Test
    public void testCreateFiles() {
        String input = "/path/to/files";
        Object result = TypeHandler.createFiles(input);
        Assert.assertNull(result);
    }
}

