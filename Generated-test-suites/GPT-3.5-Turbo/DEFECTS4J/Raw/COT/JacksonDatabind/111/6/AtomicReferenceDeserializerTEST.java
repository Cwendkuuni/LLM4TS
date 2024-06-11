
import org.junit.Test;

import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getNullValue(null);
        assertNotNull(result);
        assertNull(result.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getEmptyValue(null);
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertNull(((AtomicReference) result).get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        assertNotNull(result);
        assertSame(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = deserializer.getReferenced(reference);
        assertSame(contents, result);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        assertSame(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean result = deserializer.supportsUpdate(null);
        assertTrue(result);
    }
}
