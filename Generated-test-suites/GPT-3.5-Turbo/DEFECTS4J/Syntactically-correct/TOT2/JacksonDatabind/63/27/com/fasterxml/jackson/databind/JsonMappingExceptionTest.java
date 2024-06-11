import org.junit.Test;
import static org.junit.Assert.*;
import java.io.Closeable;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstructorWithMessage() {
        JsonMappingException exception = new JsonMappingException("Test Message");
        assertNotNull(exception);
        assertEquals("Test Message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithMessageAndRootCause() {
        Throwable rootCause = new Throwable("Root Cause");
        JsonMappingException exception = new JsonMappingException("Test Message", rootCause);
        assertNotNull(exception);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(rootCause, exception.getCause());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithMessageAndLocation() {
        JsonLocation location = new JsonLocation("source", 1, 1, 1);
        JsonMappingException exception = new JsonMappingException("Test Message", location);
        assertNotNull(exception);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithProcessorAndMessage() {
        Closeable processor = new Closeable() {
            @Override
            public void close() {}
        };
        JsonMappingException exception = new JsonMappingException(processor, "Test Message");
        assertNotNull(exception);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(processor, exception.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionConstructorWithProcessorAndMessageAndProblem() {
        Closeable processor = new Closeable() {
            @Override
            public void close() {}
        };
        Throwable problem = new Throwable("Problem");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", problem);
        assertNotNull(exception);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(processor, exception.getProcessor());
        assertEquals(problem, exception.getCause());
    }

    @Test
    public void testWrapWithPath() {
        Throwable src = new Throwable("Source");
        Object refFrom = new Object();
        JsonMappingException exception = JsonMappingException.wrapWithPath(src, refFrom, "fieldName");
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("fieldName"));
    }

    @Test
    public void testPrependPathWithRefFieldName() {
        JsonMappingException exception = new JsonMappingException("Test Message");
        Object refFrom = new Object();
        exception.prependPath(refFrom, "fieldName");
        assertNotNull(exception);
        assertTrue(exception.getPathReference().contains("fieldName"));
    }

    @Test
    public void testPrependPathWithRefIndex() {
        JsonMappingException exception = new JsonMappingException("Test Message");
        Object refFrom = new Object();
        exception.prependPath(refFrom, 1);
        assertNotNull(exception);
        assertTrue(exception.getPathReference().contains("1"));
    }
}