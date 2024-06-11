import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValueWithString() {
        String inputValue = "test string";
        Object expectedValue = inputValue;
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.STRING_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateValueWithObject() {
        String inputValue = "java.lang.String";
        Object expectedValue = TypeHandler.createObject(inputValue);
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.OBJECT_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateValueWithNumber() {
        String inputValue = "10";
        Object expectedValue = TypeHandler.createNumber(inputValue);
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.NUMBER_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateValueWithDate() {
        String inputValue = "2021-01-01";
        Object expectedValue = TypeHandler.createDate(inputValue);
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.DATE_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateValueWithClass() {
        String inputValue = "java.lang.String";
        Object expectedValue = TypeHandler.createClass(inputValue);
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.CLASS_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateValueWithFile() {
        String inputValue = "/path/to/file.txt";
        Object expectedValue = TypeHandler.createFile(inputValue);
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.FILE_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateValueWithExistingFile() {
        String inputValue = "/path/to/existing/file.txt";
        Object expectedValue = TypeHandler.createFile(inputValue);
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.EXISTING_FILE_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateValueWithFiles() {
        String inputValue = "/path/to/files/";
        Object expectedValue = TypeHandler.createFiles(inputValue);
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.FILES_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateValueWithURL() {
        String inputValue = "http://www.example.com";
        Object expectedValue = TypeHandler.createURL(inputValue);
        Object actualValue = TypeHandler.createValue(inputValue, PatternOptionBuilder.URL_VALUE);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
}