
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertEquals("Test Exception", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testJsonMappingExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Test Cause");
        JsonMappingException exception = new JsonMappingException("Test Exception", cause);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    // Other public methods in the class can be tested similarly
}
