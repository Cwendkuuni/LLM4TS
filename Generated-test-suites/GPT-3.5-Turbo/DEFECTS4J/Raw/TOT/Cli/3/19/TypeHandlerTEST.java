import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringValue() {
        String str = "test";
        String type = PatternOptionBuilder.STRING_VALUE;
        
        Object result = TypeHandler.createValue(str, type);
        
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValueWithInvalidType() {
        String str = "test";
        String type = "invalid";
        
        Object result = TypeHandler.createValue(str, type);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateObjectWithValidClassName() {
        String className = "java.lang.String";
        
        Object result = TypeHandler.createObject(className);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
    }
    
    @Test
    public void testCreateObjectWithInvalidClassName() {
        String className = "invalid";
        
        Object result = TypeHandler.createObject(className);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateNumberWithValidNumber() {
        String number = "10";
        
        Number result = TypeHandler.createNumber(number);
        
        assertNotNull(result);
        assertEquals(10, result.intValue());
    }
    
    @Test
    public void testCreateNumberWithInvalidNumber() {
        String number = "invalid";
        
        Number result = TypeHandler.createNumber(number);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateClassWithValidClassName() {
        String className = "java.lang.String";
        
        Class result = TypeHandler.createClass(className);
        
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateClassWithInvalidClassName() {
        String className = "invalid";
        
        Class result = TypeHandler.createClass(className);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateDateWithValidDateString() {
        String dateString = "2021-01-01";
        
        Date result = TypeHandler.createDate(dateString);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateDateWithInvalidDateString() {
        String dateString = "invalid";
        
        Date result = TypeHandler.createDate(dateString);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateURLWithValidURLString() {
        String urlString = "http://www.example.com";
        
        URL result = TypeHandler.createURL(urlString);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateURLWithInvalidURLString() {
        String urlString = "invalid";
        
        URL result = TypeHandler.createURL(urlString);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateFileWithValidLocation() {
        String location = "test.txt";
        
        File result = TypeHandler.createFile(location);
        
        assertNotNull(result);
        assertEquals(location, result.getPath());
    }
    
    @Test
    public void testCreateFileWithInvalidLocation() {
        String location = "invalid";
        
        File result = TypeHandler.createFile(location);
        
        assertNotNull(result);
        assertFalse(result.exists());
    }
    
    @Test
    public void testCreateFilesWithValidPaths() {
        String paths = "file1.txt,file2.txt";
        
        File[] result = TypeHandler.createFiles(paths);
        
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("file1.txt", result[0].getPath());
        assertEquals("file2.txt", result[1].getPath());
    }
    
    @Test
    public void testCreateFilesWithInvalidPaths() {
        String paths = "invalid";
        
        File[] result = TypeHandler.createFiles(paths);
        
        assertNull(result);
    }
}