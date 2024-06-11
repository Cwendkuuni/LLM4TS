import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;

public class JsonMappingExceptionTest {

    @Test
    public void testReferenceConstructor() {
        JsonMappingException.Reference ref = new JsonMappingException.Reference("test");
        assertEquals("test", ref.getFrom());
        assertNull(ref.getFieldName());
        assertEquals(-1, ref.getIndex());

        JsonMappingException.Reference ref2 = new JsonMappingException.Reference("test", "field");
        assertEquals("test", ref2.getFrom());
        assertEquals("field", ref2.getFieldName());
        assertEquals(-1, ref2.getIndex());

        JsonMappingException.Reference ref3 = new JsonMappingException.Reference("test", 1);
        assertEquals("test", ref3.getFrom());
        assertNull(ref3.getFieldName());
        assertEquals(1, ref3.getIndex());
    }

    @Test
    public void testPathMethods() {
        JsonMappingException exception = new JsonMappingException("Test");
        assertEquals(0, exception.getPath().size());
        assertEquals("", exception.getPathReference());

        JsonMappingException.Reference ref = new JsonMappingException.Reference("test");
        exception.prependPath(ref);

        assertEquals(1, exception.getPath().size());
        assertEquals("test", exception.getPathReference());
    }

    @Test
    public void testProcessorAccessors() {
        JsonMappingException exception = new JsonMappingException("Test");
        assertNull(exception.getProcessor());

        Closeable processor = new Closeable() {
            @Override
            public void close() {}
        };
        exception = new JsonMappingException(processor, "Test");
        assertNotNull(exception.getProcessor());
    }

    @Test
    public void testMessageOverride() {
        JsonMappingException exception = new JsonMappingException("Test");
        assertEquals("Test", exception.getMessage());

        JsonMappingException.Reference ref = new JsonMappingException.Reference("test");
        exception.prependPath(ref);
        assertEquals("Test (through reference chain: test)", exception.getMessage());
    }
}