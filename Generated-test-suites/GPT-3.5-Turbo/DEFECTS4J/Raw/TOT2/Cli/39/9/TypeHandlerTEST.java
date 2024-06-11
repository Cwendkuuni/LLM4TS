import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithClass() {
        // Typical use case
        Object obj = TypeHandler.createValue("123", Long.class);
        assertEquals(Long.class, obj.getClass());
        
        // Edge case
        try {
            obj = TypeHandler.createValue("abc", Long.class);
            fail("Expected a ParseException to be thrown");
        } catch (ParseException e) {
            // Expected
        }
    }
    
    @Test
    public void testCreateValueWithString() {
        // Typical use case
        Object obj = TypeHandler.createValue("test", String.class);
        assertEquals("test", obj);
        
        // Edge case
        obj = TypeHandler.createValue("test", Object.class);
        assertEquals("test", obj);
    }
    
    @Test
    public void testCreateFile() {
        // Typical use case
        File file = TypeHandler.createFile("test.txt");
        assertEquals("test.txt", file.getName());
        
        // Error scenario
        try {
            file = TypeHandler.createFile("non_existing_file.txt");
            fail("Expected a ParseException to be thrown");
        } catch (ParseException e) {
            // Expected
        }
    }
    
    @Test
    public void testCreateURL() {
        // Typical use case
        URL url = TypeHandler.createURL("http://www.example.com");
        assertEquals("http://www.example.com", url.toString());
        
        // Error scenario
        try {
            url = TypeHandler.createURL("invalid_url");
            fail("Expected a ParseException to be thrown");
        } catch (ParseException e) {
            // Expected
        }
    }
    
    @Test
    public void testCreateObject() {
        // Typical use case
        Object obj = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, obj.getClass());
        
        // Error scenario
        try {
            obj = TypeHandler.createObject("non_existing_class");
            fail("Expected a ParseException to be thrown");
        } catch (ParseException e) {
            // Expected
        }
    }
    
}