import org.junit.*;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValue_String_Class() {
        String str = "5";
        Class clazz = PatternOptionBuilder.NUMBER_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertTrue(result instanceof Number);
        assertEquals(5, ((Number)result).intValue());
    }
    
    @Test
    public void testCreateValue_String_Class_Invalid() {
        String str = "invalid";
        Class clazz = PatternOptionBuilder.NUMBER_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateObject() {
        String str = "java.util.Date";
        
        Object result = TypeHandler.createObject(str);
        
        assertTrue(result instanceof java.util.Date);
    }
    
    @Test
    public void testCreateObject_Invalid() {
        String str = "invalid.Object";
        
        Object result = TypeHandler.createObject(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateNumber_Integer() {
        String str = "10";
        
        Number result = TypeHandler.createNumber(str);
        
        assertTrue(result instanceof Integer);
        assertEquals(10, result.intValue());
    }
    
    @Test
    public void testCreateNumber_Long() {
        String str = "100000000000";
        
        Number result = TypeHandler.createNumber(str);
        
        assertTrue(result instanceof Long);
        assertEquals(100000000000L, result.longValue());
    }
    
    @Test
    public void testCreateNumber_Double() {
        String str = "3.14";
        
        Number result = TypeHandler.createNumber(str);
        
        assertTrue(result instanceof Double);
        assertEquals(3.14, result.doubleValue(), 0.001);
    }
    
    @Test
    public void testCreateNumber_Invalid() {
        String str = "invalid";
        
        Number result = TypeHandler.createNumber(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateClass() {
        String str = "java.util.Date";
        
        Class result = TypeHandler.createClass(str);
        
        assertEquals(java.util.Date.class, result);
    }
    
    @Test
    public void testCreateClass_Invalid() {
        String str = "invalid.Class";
        
        Class result = TypeHandler.createClass(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateDate_Valid() {
        String str = "2021-01-01";
        
        Date result = TypeHandler.createDate(str);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateDate_Invalid() {
        String str = "invalid-date";
        
        Date result = TypeHandler.createDate(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateURL_Valid() {
        String str = "https://www.example.com";
        
        URL result = TypeHandler.createURL(str);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateURL_Invalid() {
        String str = "invalid-url";
        
        URL result = TypeHandler.createURL(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateFile() {
        String str = "test.txt";
        
        File result = TypeHandler.createFile(str);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateFiles_Valid() {
        String str = "file1.txt,file2.txt";
        
        File[] result = TypeHandler.createFiles(str);
        
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("file1.txt", result[0].getName());
        assertEquals("file2.txt", result[1].getName());
    }
    
    @Test
    public void testCreateFiles_Invalid() {
        String str = "file1.txt,file2.txt";
        
        File[] result = TypeHandler.createFiles(str);
        
        assertNull(result);
    }
}