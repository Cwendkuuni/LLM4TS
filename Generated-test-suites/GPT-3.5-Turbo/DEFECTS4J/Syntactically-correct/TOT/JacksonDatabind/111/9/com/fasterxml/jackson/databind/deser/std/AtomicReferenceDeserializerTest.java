import static org.junit.Assert.*;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReferenceDeserializer resolvedDeserializer = deserializer.withResolved(null, null);
        assertNotSame(deserializer, resolvedDeserializer);
    }

    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);
        assertNotNull(nullValue);
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object emptyValue = deserializer.getEmptyValue(null);
        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object referenced = deserializer.getReferenced(reference);
        assertNotNull(referenced);
        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        assertNotNull(supportsUpdate);
        assertTrue(supportsUpdate);
    }
}