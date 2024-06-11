import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValueWithObject() {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testCreateValueWithNumber() {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof Number);
    }
    
    @Test
    public void testCreateValueWithDate() {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }
    
    @Test
    public void testCreateValueWithClass() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof Class);
    }
    
    @Test
    public void testCreateValueWithFile() {
        String str = "/path/to/file";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }
    
    @Test
    public void testCreateValueWithExistingFile() {
        String str = "/path/to/file";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }
    
    @Test
    public void testCreateValueWithFiles() {
        String str = "/path/to/file1,/path/to/file2";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof File[]);
    }
    
    @Test
    public void testCreateValueWithURL() {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof URL);
    }
}