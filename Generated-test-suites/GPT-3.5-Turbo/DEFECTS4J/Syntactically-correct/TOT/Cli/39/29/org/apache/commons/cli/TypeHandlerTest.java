import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringType() throws ParseException {
        String str = "TestString";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObjectType() throws ParseException {
        String str = "org.apache.commons.cli.TypeHandler";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertEquals(TypeHandler.class, result.getClass());
    }

    @Test
    public void testCreateValueWithNumberType() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertEquals(Long.class, result.getClass());
        Assert.assertEquals(10L, result);
    }

    @Test
    public void testCreateValueWithDateType() throws ParseException {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithClassType() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValueWithFileType() throws ParseException {
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertEquals(File.class, result.getClass());
        Assert.assertTrue(((File) result).exists());
        Assert.assertTrue(((File) result).isFile());
        Assert.assertEquals("/path/to/file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValueWithExistingFileType() throws ParseException {
        String str = "/path/to/existing_file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertEquals(File.class, result.getClass());
        Assert.assertTrue(((File) result).exists());
        Assert.assertTrue(((File) result).isFile());
        Assert.assertEquals("/path/to/existing_file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValueWithFilesType() throws ParseException {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithURLType() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertEquals(URL.class, result.getClass());
        Assert.assertEquals("http://www.example.com", result.toString());
    }
}