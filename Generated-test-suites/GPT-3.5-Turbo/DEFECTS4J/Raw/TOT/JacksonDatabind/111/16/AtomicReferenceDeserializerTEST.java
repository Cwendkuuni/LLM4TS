import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> atomicReference = deserializer.getNullValue(null);
        
        assertNotNull(atomicReference);
        assertNull(atomicReference.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object emptyValue = deserializer.getEmptyValue(null);
        
        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
        assertNull(((AtomicReference<Object>) emptyValue).get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> atomicReference = deserializer.referenceValue(contents);
        
        assertNotNull(atomicReference);
        assertEquals(contents, atomicReference.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> atomicReference = new AtomicReference<>(contents);
        
        Object referenced = deserializer.getReferenced(atomicReference);
        
        assertEquals(contents, referenced);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> atomicReference = new AtomicReference<>();
        
        AtomicReference<Object> updatedReference = deserializer.updateReference(atomicReference, contents);
        
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}