import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(TypeFactory.defaultInstance().constructType(Object.class), null, typeDeser, valueDeser);
        AtomicReferenceDeserializer resolvedDeserializer = deserializer.withResolved(typeDeser, valueDeser);
        assertNotNull(resolvedDeserializer);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(TypeFactory.defaultInstance().constructType(Object.class), null, null, null);
        AtomicReference<Object> nullValue = deserializer.getNullValue(ctxt);
        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(TypeFactory.defaultInstance().constructType(Object.class), null, null, null);
        AtomicReference<Object> emptyValue = deserializer.getEmptyValue(ctxt);
        assertNotNull(emptyValue);
        assertNull(emptyValue.get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(TypeFactory.defaultInstance().constructType(Object.class), null, null, null);
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(TypeFactory.defaultInstance().constructType(Object.class), null, null, null);
        Object referenced = deserializer.getReferenced(reference);
        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(TypeFactory.defaultInstance().constructType(Object.class), null, null, null);
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig(null, null, null, null);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(TypeFactory.defaultInstance().constructType(Object.class), null, null, null);
        Boolean supportsUpdate = deserializer.supportsUpdate(config);
        assertTrue(supportsUpdate);
    }
}