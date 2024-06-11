import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testConstructorWithMessageAndRootCause() {
        Throwable cause = new Throwable("Root cause");
        JsonMappingException exception = new JsonMappingException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndLocation() {
        JsonLocation location = new JsonLocation("test source", 1, 1, 1);
        JsonMappingException exception = new JsonMappingException("Test message", location);
        assertEquals("Test message", exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @Test
    public void testConstructorWithMessageLocationAndRootCause() {
        JsonLocation location = new JsonLocation("test source", 1, 1, 1);
        Throwable cause = new Throwable("Root cause");
        JsonMappingException exception = new JsonMappingException("Test message", location, cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioException = new IOException("IO Exception");
        JsonMappingException exception = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type java.io.IOException): IO Exception", exception.getMessage());
    }

    @Test
    public void testWrapWithPathWithObjectAndFieldName() {
        Throwable src = new Throwable("Source exception");
        Object refFrom = new Object();
        String fieldName = "testField";
        JsonMappingException exception = JsonMappingException.wrapWithPath(src, refFrom, fieldName);
        assertEquals("Source exception", exception.getCause().getMessage());
        assertEquals("testField", exception.getPathReference());
    }

    @Test
    public void testWrapWithPathWithObjectAndIndex() {
        Throwable src = new Throwable("Source exception");
        Object refFrom = new Object();
        int index = 1;
        JsonMappingException exception = JsonMappingException.wrapWithPath(src, refFrom, index);
        assertEquals("Source exception", exception.getCause().getMessage());
        assertEquals("1", exception.getPathReference());
    }

}