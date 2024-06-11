import org.junit.Test;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReferenceDeserializer resolvedDeserializer = deserializer.withResolved(null, null);
        assertNotNull(resolvedDeserializer);
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);
        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object emptyValue = deserializer.getEmptyValue(null);
        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
        assertNull(((AtomicReference) emptyValue).get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object referencedObject = deserializer.getReferenced(reference);
        assertNotNull(referencedObject);
        assertEquals(reference.get(), referencedObject);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object newContents = new Object();
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newContents);
        assertNotNull(updatedReference);
        assertEquals(newContents, updatedReference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        assertNotNull(supportsUpdate);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}