import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
    AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Test
    public void testConstructor() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        assertNotNull(atomicReferenceDeserializer);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        assertNotNull(result);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = null;
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        assertNotNull(result);
    }

    @Test
    public void testReferenceValue() {
        Object contents = null;
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertNotNull(result);
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = null;
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertNull(result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = null;
        Object contents = null;
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        assertNotNull(result);
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result);
    }
}