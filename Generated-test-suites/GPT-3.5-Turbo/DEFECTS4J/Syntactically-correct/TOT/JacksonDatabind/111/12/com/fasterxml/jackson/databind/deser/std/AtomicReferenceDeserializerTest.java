
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
    private DeserializationContext context = null; //initialize context

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> result = deserializer.getNullValue(context);
        assertNotNull(result);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        Object result = deserializer.getEmptyValue(context);
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertNull(((AtomicReference<?>) result).get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = "Test string";
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = "Test string";
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = deserializer.getReferenced(reference);
        assertNotNull(result);
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        Object contents = "Test string";
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object newContents = "Updated string";
        deserializer.updateReference(reference, newContents);
        assertEquals(newContents, reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        assertTrue(deserializer.supportsUpdate(null));
    }
}

