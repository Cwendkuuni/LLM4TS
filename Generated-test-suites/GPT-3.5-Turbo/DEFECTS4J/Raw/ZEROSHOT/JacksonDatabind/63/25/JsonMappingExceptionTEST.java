import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class JsonMappingExceptionTest {

    @Test
    public void testGetPath() {
        JsonMappingException exception = new JsonMappingException("test");
        assertNull(exception.getPath());
        exception.prependPath("referrer", "fieldName");
        List<JsonMappingException.Reference> path = exception.getPath();
        assertNotNull(path);
        assertEquals(1, path.size());
        assertEquals("referrer.fieldName", path.get(0).toString());
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException exception1 = new JsonMappingException("test");
        assertEquals("", exception1.getPathReference());

        JsonMappingException exception2 = new JsonMappingException("test");
        exception2.prependPath("referrer", "fieldName");
        assertEquals("referrer.fieldName", exception2.getPathReference());
    }

    @Test
    public void testPrependPath() {
        JsonMappingException exception = new JsonMappingException("test");
        assertNull(exception.getPath());
        exception.prependPath("referrer", "fieldName");
        List<JsonMappingException.Reference> path = exception.getPath();
        assertNotNull(path);
        assertEquals(1, path.size());
        assertEquals("referrer.fieldName", path.get(0).toString());
        exception.prependPath("referrer", 1);
        path = exception.getPath();
        assertNotNull(path);
        assertEquals(2, path.size());
        assertEquals("referrer[fieldName]->referrer[1]", exception.getPathReference());
    }
}