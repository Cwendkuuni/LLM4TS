import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValue() throws ParseException {
        Object result = TypeHandler.createValue("hello", String.class);
        assertEquals("hello", result);
        
        result = TypeHandler.createValue("java.lang.Object", Class.class);
        assertEquals(Object.class, result);
        
        result = TypeHandler.createValue("10", Number.class);
        assertEquals(10, result);
        
        result = TypeHandler.createValue("2019-01-01", Date.class);
        assertNotNull(result);
        
        result = TypeHandler.createValue("java.lang.String", Class.class);
        assertEquals(String.class, result);
        
        result = TypeHandler.createValue("/path/to/file", File.class);
        assertNotNull(result);
        
        result = TypeHandler.createValue("/path/to/existing/file", FileInputStream.class);
        assertNotNull(result);
        
        result = TypeHandler.createValue("/path/to/files", File[].class);
        assertNotNull(result);
        
        result = TypeHandler.createValue("http://www.example.com", URL.class);
        assertNotNull(result);
        
        result = TypeHandler.createValue("invalid class", Class.class);
        assertNull(result);
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
        
        result = TypeHandler.createObject("invalid.class");
        assertNull(result);
    }
    
    @Test
    public void testCreateNumber() throws ParseException {
        Number result = TypeHandler.createNumber("10");
        assertEquals(10, result);
        
        result = TypeHandler.createNumber("10.5");
        assertEquals(10.5, result);
        
        try {
            result = TypeHandler.createNumber("invalid");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);
        
        try {
            result = TypeHandler.createClass("invalid.class");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2019-01-01");
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
        
        try {
            result = TypeHandler.createURL("invalid url");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
    
    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("/path/to/file");
        assertNotNull(result);
    }
    
    @Test
    public void testOpenFile() throws ParseException {
        FileInputStream result = TypeHandler.openFile("/path/to/existing/file");
        assertNotNull(result);
        
        try {
            result = TypeHandler.openFile("/path/to/nonexisting/file");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("/path/to/files");
    }
}