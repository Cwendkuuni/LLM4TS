import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testConstructor1() {
        JsonMappingException exception = new JsonMappingException("Test Message");
        assertEquals("Test Message", exception.getMessage());
    }
    
    @Test
    public void testConstructor2() {
        Throwable rootCause = new Throwable("Root Cause");
        JsonMappingException exception = new JsonMappingException("Test Message", rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(rootCause, exception.getCause());
    }

    @Test
    public void testConstructor3() {
        JsonLocation location = new JsonLocation("source", 1, 5, 10);
        JsonMappingException exception = new JsonMappingException("Test Message", location);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @Test
    public void testConstructor4() {
        JsonLocation location = new JsonLocation("source", 1, 5, 10);
        Throwable rootCause = new Throwable("Root Cause");
        JsonMappingException exception = new JsonMappingException("Test Message", location, rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertEquals(rootCause, exception.getCause());
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioException = new IOException("IO Exception");
        JsonMappingException exception = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type java.io.IOException): IO Exception", exception.getMessage());
    }
}