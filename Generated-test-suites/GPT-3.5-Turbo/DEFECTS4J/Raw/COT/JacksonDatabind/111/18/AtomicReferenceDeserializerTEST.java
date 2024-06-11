
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, typeDeser, valueDeser);
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);
        assertNotNull(result);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getEmptyValue(ctxt);
        assertNotNull(result);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        assertNotNull(result);
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getReferenced(reference);
        assertNull(result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>();
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        assertNotNull(result);
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean result = deserializer.supportsUpdate(config);
        assertTrue(result);
    }

}
