import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> atomicReference = deserializer.getNullValue(null);
        
        assertNull(atomicReference.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> atomicReference = deserializer.getEmptyValue(null);
        
        assertNull(atomicReference.get());
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> atomicReference = deserializer.referenceValue("test");
        
        assertEquals("test", atomicReference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> atomicReference = new AtomicReference<>("test");
        
        assertEquals("test", deserializer.getReferenced(atomicReference));
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> atomicReference = new AtomicReference<>(null);
        Object contents = "test";
        
        deserializer.updateReference(atomicReference, contents);
        
        assertEquals("test", atomicReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = null; // Mock DeserializationConfig
        
        assertEquals(Boolean.TRUE, deserializer.supportsUpdate(config));
    }
}