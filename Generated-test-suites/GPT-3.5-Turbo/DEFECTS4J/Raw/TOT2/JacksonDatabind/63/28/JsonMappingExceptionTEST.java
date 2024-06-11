import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;

public class JsonMappingExceptionTest {

    private JsonMappingException jsonMappingException;

    @Before
    public void setup() {
        jsonMappingException = new JsonMappingException("Test Message");
    }

    @Test
    public void testGetMessage() {
        assertEquals("Test Message", jsonMappingException.getMessage());
    }

    @Test
    public void testWrapWithPath() {
        JsonMappingException nestedException = new JsonMappingException("Nested Message");
        Reference ref = new Reference(this, "fieldName");
        JsonMappingException wrappedException = JsonMappingException.wrapWithPath(nestedException, ref);

        assertEquals("Nested Message", wrappedException.getMessage());
        assertEquals(ref, wrappedException.getPath().get(0));
    }

    @Test
    public void testAppendPath() {
        Reference ref1 = new Reference(this, "field1");
        Reference ref2 = new Reference(this, "field2");
        jsonMappingException.prependPath(ref1);
        jsonMappingException.prependPath(ref2);

        assertEquals(2, jsonMappingException.getPath().size());
        assertEquals(ref2, jsonMappingException.getPath().get(0));
        assertEquals(ref1, jsonMappingException.getPath().get(1));
    }

    @Test
    public void testGetPathReference() {
        Reference ref1 = new Reference(this, "field1");
        Reference ref2 = new Reference(this, 0);
        jsonMappingException.prependPath(ref1);
        jsonMappingException.prependPath(ref2);

        assertEquals("[?]->field1", jsonMappingException.getPathReference());
    }
}