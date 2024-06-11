import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testReferenceConstructor() {
        Object obj = new Object();
        JsonMappingException.Reference reference = new JsonMappingException.Reference(obj);
        assertNotNull(reference);
        assertEquals(obj, reference.getFrom());
    }

    @Test
    public void testReferenceDescription() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference(Object.class);
        assertNotNull(reference.getDescription());
    }

    @Test
    public void testJsonMappingExceptionConstructor() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        Throwable cause = new Throwable("Cause");
        JsonMappingException.Reference reference = new JsonMappingException.Reference(Object.class);
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(cause, reference);
        assertNotNull(wrappedException);
        assertEquals("Cause", wrappedException.getCause().getMessage());
    }

    @Test
    public void testJsonMappingExceptionPath() {
        JsonMappingException exception = new JsonMappingException("Test message");
        exception.prependPath(new Object(), "fieldName");
        assertEquals("fieldName", exception.getPath().get(0).getFieldName());
    }

    @Test
    public void testJsonMappingExceptiontoString() {
        JsonMappingException exception = new JsonMappingException("Test message");
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test message", exception.toString());
    }
}