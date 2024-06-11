import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstructor() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertEquals("Test message", exception.getMessage());
    }
    
    @Test
    public void testJsonMappingExceptionWithPath() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.prependPath("referrer", "fieldName");
        assertEquals("Test message (through reference chain: referrer['fieldName'])", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionPrependPathIndex() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.prependPath("referrer", 5);
        assertEquals("Test message (through reference chain: referrer[5])", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionSerializationDescription() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference("from", "fieldName");
        assertEquals("from['fieldName']", reference.getDescription());
    }

    @Test
    public void testJsonMappingExceptionSerializationDescriptionArray() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference("from", 2);
        assertEquals("from[2]", reference.getDescription());
    }

}