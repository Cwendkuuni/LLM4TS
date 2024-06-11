import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionEmptyMessage() {
        try {
            throw new JsonMappingException("");
        } catch (JsonMappingException e) {
            assertNotNull(e.getMessage());
            assertEquals("", e.getMessage());
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }

    @Test
    public void testJsonMappingExceptionWithMessage() {
        String message = "Test Message";
        try {
            throw new JsonMappingException(message);
        } catch (JsonMappingException e) {
            assertNotNull(e.getMessage());
            assertEquals(message, e.getMessage());
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }

    @Test
    public void testJsonMappingExceptionWithLocation() {
        String message = "Test Message";
        JsonLocation location = new JsonLocation(1, 1, "Test File");
        try {
            throw new JsonMappingException(message, location);
        } catch (JsonMappingException e) {
            assertNotNull(e.getMessage());
            assertEquals(message, e.getMessage());
            assertEquals(location, e.getLocation());
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }

    @Test
    public void testJsonMappingExceptionWithRootCause() {
        String message = "Test Message";
        IOException rootCause = new IOException("Root Cause Message");
        try {
            throw new JsonMappingException(message, rootCause);
        } catch (JsonMappingException e) {
            assertNotNull(e.getMessage());
            assertEquals(message, e.getMessage());
            assertNotNull(e.getRootCause());
            assertEquals(rootCause.getClass(), e.getRootCause().getClass());
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }

    @Test
    public void testJsonMappingExceptionWithProcessor() {
        Closeable processor = new ByteArrayInputSource(new byte[0]);
        String message = "Test Message";
        try {
            throw new JsonMappingException(processor, message);
        } catch (JsonMappingException e) {
            assertNotNull(e.getMessage());
            assertEquals(message, e.getMessage());
            assertNotNull(e.getProcessor());
            assertEquals(Closeable.class, e.getProcessor().getClass());
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }
}