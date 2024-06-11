
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {

    private JsonMappingException exception;

    @Before
    public void setUp() {
        exception = new JsonMappingException("Test Exception");
    }

    @Test
    public void testGetMessage() {
        assertEquals("Test Exception", exception.getMessage());
    }

    @Test
    public void testGetLocalizedMessage() {
        assertEquals("Test Exception", exception.getLocalizedMessage());
    }

    @Test
    public void testGetPath() {
        assertEquals(0, exception.getPath().size());
    }

    @Test
    public void testGetPathReference() {
        assertEquals("", exception.getPathReference());
    }

    @Test
    public void testToString() {
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test Exception", exception.toString());
    }
}
