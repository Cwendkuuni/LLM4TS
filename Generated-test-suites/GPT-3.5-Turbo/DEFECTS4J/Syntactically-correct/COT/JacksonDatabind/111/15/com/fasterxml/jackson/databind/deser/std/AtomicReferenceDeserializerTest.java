import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);

        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);

        assertSame(fullType, result._fullType);
        assertSame(inst, result._valueInstantiator);
        assertSame(typeDeser, result._valueTypeDeserializer);
        assertSame(valueDeser, result._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
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
        assertNull(((AtomicReference<?>) result).get());
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
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        reference.set(contents);

        Object result = deserializer.getReferenced(reference);

        assertSame(contents, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();

        AtomicReference<Object> result = deserializer.updateReference(reference, contents);

        assertSame(contents, reference.get());
        assertSame(reference, result);
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = new DeserializationConfig();

        Boolean result = deserializer.supportsUpdate(config);

        assertTrue(result);
    }
}

