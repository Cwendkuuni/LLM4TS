
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType javaType = JavaType.class;
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = TypeDeserializer.class;
        JsonDeserializer<?> valueDeser = JsonDeserializer.class;
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaType, inst,
                typeDeser, valueDeser);
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        assertNotNull(result);
        assertEquals(javaType, result._fullType);
        assertEquals(inst, result._valueInstantiator);
        assertEquals(typeDeser, result._valueTypeDeserializer);
        assertEquals(valueDeser, result._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertNull(((AtomicReference<?>) result).get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertNotNull(result);
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(null);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        assertNotNull(result);
        assertTrue(result);
    }
}

