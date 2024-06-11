import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer deserializer;

    @Before
    public void setUp() {
        JavaType fullType = mock(JavaType.class);
        ValueInstantiator inst = mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);

        AtomicReference<Object> result = deserializer.getNullValue(ctxt);

        assertNotNull(result);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Object result = deserializer.getEmptyValue(ctxt);

        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertNull(((AtomicReference<Object>) result).get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();

        AtomicReference<Object> result = deserializer.referenceValue(contents);

        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        reference.set(contents);

        Object result = deserializer.getReferenced(reference);

        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();

        AtomicReference<Object> result = deserializer.updateReference(reference, contents);

        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        Boolean result = deserializer.supportsUpdate(config);

        assertTrue(result);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeser = mock(JsonDeserializer.class);

        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);

        assertNotNull(result);
        assertEquals(typeDeser, result.getTypeDeserializer());
        assertEquals(valueDeser, result.getValueDeserializer());
    }

}