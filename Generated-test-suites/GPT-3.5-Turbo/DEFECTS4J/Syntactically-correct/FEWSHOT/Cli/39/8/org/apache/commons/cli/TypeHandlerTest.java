import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;

public class TypeHandlerTest {
    @Test
    public void testCreateValueOfString() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValueOfObject() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateValueOfNumber() throws ParseException {
        Object longResult = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertNotNull(longResult);
        Assert.assertEquals(Long.class, longResult.getClass());

        Object doubleResult = TypeHandler.createValue("123.45", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertNotNull(doubleResult);
        Assert.assertEquals(Double.class, doubleResult.getClass());
    }

    @Test
    public void testCreateValueOfDate() {
        // Not yet implemented
    }

    @Test
    public void testCreateValueOfClass() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValueOfFile() throws ParseException {
        Object result = TypeHandler.createValue("/path/to/file", PatternOptionBuilder.FILE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals(File.class, result.getClass());
    }

    @Test
    public void testCreateValueOfExistingFile() throws ParseException {
        Object result = TypeHandler.createValue("/path/to/file", PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals(File.class, result.getClass());
    }

    @Test
    public void testCreateValueOfFiles() {
        // Not yet implemented
    }

    @Test
    public void testCreateValueOfURL() throws ParseException {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals(URL.class, result.getClass());
    }
}