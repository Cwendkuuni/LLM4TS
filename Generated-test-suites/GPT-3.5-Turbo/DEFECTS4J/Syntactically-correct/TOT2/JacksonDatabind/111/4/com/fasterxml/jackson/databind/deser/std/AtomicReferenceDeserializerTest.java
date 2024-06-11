import static org.junit.Assert.*;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    AtomicReferenceDeserializer deserializer;

    @Test
    public void testWithResolved() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(null, null);
        assertNotNull(newDeserializer);
    }

    @Test
    public void testGetNullValue() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        try {
            AtomicReference<Object> nullValue = deserializer.getNullValue(null);
            assertNotNull(nullValue);
            assertNull(nullValue.get());
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testGetEmptyValue() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> emptyValue = (AtomicReference<Object>) deserializer.getEmptyValue(null);
        assertNotNull(emptyValue);
        assertNull(emptyValue.get());
    }

    @Test
    public void testReferenceValue() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object obj = new Object();
        AtomicReference<Object> referenceValue = deserializer.referenceValue(obj);
        assertNotNull(referenceValue);
        assertEquals(obj, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object obj = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(obj);
        Object referenced = deserializer.getReferenced(reference);
        assertEquals(obj, referenced);
    }

    @Test
    public void testUpdateReference() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object obj1 = new Object();
        Object obj2 = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(obj1);
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, obj2);
        assertNotNull(updatedReference);
        assertEquals(obj2, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        assertTrue(deserializer.supportsUpdate(null));
    }
}