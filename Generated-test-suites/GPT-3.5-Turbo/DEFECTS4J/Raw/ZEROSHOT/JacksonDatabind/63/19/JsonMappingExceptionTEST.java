import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testGetPath() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertEquals(0, exception.getPath().size());
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertEquals("", exception.getPathReference());
    }

    @Test
    public void testPrependPath() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        exception.prependPath("Referrer", "FieldName");
        assertEquals(1, exception.getPath().size());
        assertEquals("Referrer.FieldName", exception.getPath().get(0).getDescription());

        exception.prependPath("Referrer", 1);
        assertEquals(2, exception.getPath().size());
        assertEquals("Referrer.FieldName", exception.getPath().get(0).getDescription());
        assertEquals("Referrer[1]", exception.getPath().get(1).getDescription());
    }

    @Test
    public void testGetProcessor() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertNull(exception.getProcessor());
    }

    @Test
    public void testGetMessage() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertEquals("Test Exception", exception.getMessage());

        exception.prependPath("Referrer", "FieldName");
        assertEquals("Test Exception (through reference chain: Referrer.FieldName)", exception.getMessage());
    }

    @Test
    public void testToString() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test Exception", exception.toString());
    }
}