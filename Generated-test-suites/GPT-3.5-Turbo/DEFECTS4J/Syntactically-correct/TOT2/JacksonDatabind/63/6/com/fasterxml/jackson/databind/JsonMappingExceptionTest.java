import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testConstructor() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testPrependPath() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.prependPath(new Object(), "fieldName");
        assertEquals(1, exception.getPath().size());
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertEquals("", exception.getPathReference());
    }

    @Test
    public void testFromUnexpectedIOE() {
        JsonMappingException exception = JsonMappingException.fromUnexpectedIOE(new IOException("Test IO Exception"));
        assertEquals("Unexpected IOException (of type java.io.IOException): Test IO Exception", exception.getMessage());
    }

    @Test
    public void testWrapWithPath() {
        Exception src = new Exception("Source exception");
        JsonMappingException exception = JsonMappingException.wrapWithPath(src, new Object(), "fieldName");
        assertEquals(1, exception.getPath().size());
    }

}