import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        // Typical use case
        Object result = TypeHandler.createValue("123", String.class);
        assertEquals("123", result);
        
        // Edge case
        try {
            TypeHandler.createValue("123", Void.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringClass() {
        // Typical use case
        Object result = TypeHandler.createValue("123", String.class);
        assertEquals("123", result);
        
        // Edge case
        try {
            TypeHandler.createValue("123", Void.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateObject() {
        // Typical use case
        Object result = TypeHandler.createObject("java.lang.String");
        assertTrue(result instanceof String);
        
        // Error scenario
        try {
            TypeHandler.createObject("invalid.Class");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateNumber() {
        // Typical use case
        Number result = TypeHandler.createNumber("123");
        assertEquals(123L, result);
        
        // Edge case
        result = TypeHandler.createNumber("123.456");
        assertEquals(123.456, result);
        
        // Error scenario
        try {
            TypeHandler.createNumber("abc");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateClass() {
        // Typical use case
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);
        
        // Error scenario
        try {
            TypeHandler.createClass("invalid.Class");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateDate() {
        // Edge case
        try {
            TypeHandler.createDate("2022-13-45");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testCreateURL() {
        // Typical use case
        URL result = TypeHandler.createURL("http://www.example.com");
        assertEquals("http://www.example.com", result.toString());
        
        // Error scenario
        try {
            TypeHandler.createURL("invalid_url");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFile() {
        // Typical use case
        File result = TypeHandler.createFile("/path/to/file.txt");
        assertEquals("/path/to/file.txt", result.getPath());
    }

    @Test
    public void testCreateFiles() {
        // Edge case
        try {
            TypeHandler.createFiles("/path/to/files");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}