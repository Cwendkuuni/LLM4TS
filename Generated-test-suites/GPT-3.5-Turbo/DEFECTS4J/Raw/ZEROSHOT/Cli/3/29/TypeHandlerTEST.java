import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithObject() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof String);
        Assert.assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testCreateValueWithNumber() {
        Object result = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Number);
        Assert.assertEquals(123, ((Number) result).intValue());
    }

    @Test
    public void testCreateValueWithDate() {
        Object result = TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
        Assert.assertNotNull(result);
        // Add assertions for Date parsing
        //Assert.assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValueWithClass() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Class);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValueWithFile() {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertEquals("test.txt", ((File) result).getName());
    }

    @Test
    public void testCreateValueWithExistingFile() {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertEquals("test.txt", ((File) result).getName());
    }

    @Test
    public void testCreateValueWithFiles() {
        Object result = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        Assert.assertEquals(2, files.length);
        Assert.assertEquals("file1.txt", files[0].getName());
        Assert.assertEquals("file2.txt", files[1].getName());
    }

    @Test
    public void testCreateValueWithURL() {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals("http://www.example.com", result.toString());
    }
}