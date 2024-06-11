import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {

    private JsonMappingException jsonMappingException;

    @Before
    public void setup() {
        jsonMappingException = new JsonMappingException("Test message");
    }

    @Test
    public void testGetPath() {
        assertEquals("Expected empty list", jsonMappingException.getPath().size(), 0);
    }

    @Test
    public void testGetPathReference() {
        assertEquals("Expected empty string", jsonMappingException.getPathReference(), "");
    }

    @Test
    public void testPrependPath_ObjectString() {
        jsonMappingException.prependPath("object", "field");
        assertEquals("Expected prepend path size 1", jsonMappingException.getPath().size(), 1);
    }

    @Test
    public void testPrependPath_ObjectInt() {
        jsonMappingException.prependPath("object", 0);
        assertEquals("Expected prepend path size 1", jsonMappingException.getPath().size(), 1);
    }

    @Test
    public void testGetLocalizedMessage() {
        assertEquals("Expected test message", jsonMappingException.getLocalizedMessage(), "Test message");
    }

    @Test
    public void testGetMessage() {
        assertEquals("Expected test message", jsonMappingException.getMessage(), "Test message");
    }

    @Test
    public void testToString() {
        assertEquals("Expected class name and test message", jsonMappingException.toString(), "JsonMappingExceptionTest: Test message");
    }
}