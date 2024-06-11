import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstructor() {
        JsonMappingException exception = new JsonMappingException("Test Message");

        assertEquals("Test Message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithRootCause() {
        Throwable rootCause = new Throwable("Root Cause");
        JsonMappingException exception = new JsonMappingException("Test Message", rootCause);

        assertEquals("Test Message", exception.getMessage());
        assertEquals(rootCause, exception.getCause());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithLocation() {
        JsonLocation location = new JsonLocation("test", 1, 1, 1);
        JsonMappingException exception = new JsonMappingException("Test Message", location);

        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithLocationAndRootCause() {
        JsonLocation location = new JsonLocation("test", 1, 1, 1);
        Throwable rootCause = new Throwable("Root Cause");
        JsonMappingException exception = new JsonMappingException("Test Message", location, rootCause);

        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertEquals(rootCause, exception.getCause());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOE() {
        IOException ioException = new IOException("IO Exception");
        JsonMappingException exception = JsonMappingException.fromUnexpectedIOE(ioException);

        assertEquals("Unexpected IOException (of type java.io.IOException): IO Exception", exception.getMessage());
    }

    @Test
    public void testWrapWithPath() {
        Exception source = new Exception("Source Exception");
        Reference reference = new Reference("Test Reference");

        JsonMappingException exception = JsonMappingException.wrapWithPath(source, reference);

        assertEquals("Source Exception", exception.getMessage());
        assertEquals(reference, exception.getPath().get(0));
    }

    @Test
    public void testPrependPath() {
        JsonMappingException exception = new JsonMappingException("Test Message");
        Reference reference = new Reference("Test Reference");

        exception.prependPath(reference);

        assertEquals(1, exception.getPath().size());
        assertEquals(reference, exception.getPath().get(0));
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException exception = new JsonMappingException("Test Message");
        Reference reference = new Reference("Test Reference");
        exception.prependPath(reference);

        String pathReference = exception.getPathReference();

        assertEquals("[Test Reference]", pathReference);
    }

}