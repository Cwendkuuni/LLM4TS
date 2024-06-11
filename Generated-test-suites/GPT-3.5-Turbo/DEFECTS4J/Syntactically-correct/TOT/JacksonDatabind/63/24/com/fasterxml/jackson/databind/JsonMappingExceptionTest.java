import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testPathReference() {
        JsonMappingException exception = new JsonMappingException("Test exception");
        assertEquals("", exception.getPathReference());
        
        exception.prependPath("ref", "field");
        assertEquals("ref.field", exception.getPathReference());
        
        exception.prependPath("obj", 2);
        assertEquals("obj[2].ref.field", exception.getPathReference());
    }
    
    @Test
    public void testMessage() {
        JsonMappingException exception = new JsonMappingException("Test exception");
        assertEquals("Test exception", exception.getMessage());
        
        exception.prependPath("ref", "field");
        assertEquals("Test exception (through reference chain: ref.field)", exception.getMessage());
        
        exception.prependPath("obj", 2);
        assertEquals("Test exception (through reference chain: obj[2].ref.field)", exception.getMessage());
    }
    
    @Test
    public void testToString() {
        JsonMappingException exception = new JsonMappingException("Test exception");
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test exception", exception.toString());
        
        exception.prependPath("ref", "field");
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test exception (through reference chain: ref.field)", 
                     exception.toString());
    }
    
}