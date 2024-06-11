import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionWithMessage() {
        JsonMappingException exception = new JsonMappingException("Custom Message");
        assertEquals("Custom Message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithLocation() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Custom Message", new Location());
        JsonMappingException jsonMappingException = new JsonMappingException(jsonProcessingException);
        assertEquals(jsonProcessingException.getLocation(), jsonMappingException.getLocation());
    }

    @Test
    public void testJsonMappingExceptionWithRootCause() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Custom Message", new Location(), new IOException("IO Error"));
        JsonMappingException jsonMappingException = new JsonMappingException(jsonProcessingException);
        assertEquals(jsonProcessingException.getCause(), jsonMappingException.getCause());
    }

    @Test
    public void testJsonMappingExceptionWithProcessor() {
        Closeable processor = new Closeable() {
            @Override
            public void close() throws IOException {
            }
        };
        JsonMappingException exception = new JsonMappingException(processor, "Custom Message");
        assertEquals(processor, exception.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorAndMessage() {
        Closeable processor = new Closeable() {
            @Override
            public void close() throws IOException {
            }
        };
        JsonMappingException exception = new JsonMappingException(processor, "Custom Message");
        assertEquals(processor, exception.getProcessor());
        assertEquals("Custom Message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorMessageAndLocation() {
        Closeable processor = new Closeable() {
            @Override
            public void close() throws IOException {
            }
        };
        JsonMappingException exception = new JsonMappingException(processor, "Custom Message", new Location());
        assertEquals(processor, exception.getProcessor());
        assertEquals("Custom Message", exception.getMessage());
        assertEquals(exception.getLocation(), new Location());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorMessageLocationAndRootCause() {
        Closeable processor = new Closeable() {
            @Override
            public void close() throws IOException {
            }
        };
        JsonMappingException exception = new JsonMappingException(processor, "Custom Message", new Location(), new IOException("IO Error"));
        assertEquals(processor, exception.getProcessor());
        assertEquals("Custom Message", exception.getMessage());
        assertEquals(exception.getLocation(), new Location());
        assertEquals(exception.getCause(), new IOException("IO Error"));
    }
}