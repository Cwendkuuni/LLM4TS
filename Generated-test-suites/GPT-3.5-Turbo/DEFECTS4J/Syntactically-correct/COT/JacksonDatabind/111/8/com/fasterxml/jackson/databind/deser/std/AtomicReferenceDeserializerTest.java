import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationConfig;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);
        assertEquals(fullType, result._fullType);
        assertEquals(inst, result._valueInstantiator);
        assertEquals(typeDeser, result._valueTypeDeserializer);
        assertEquals(valueDeser, result._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        AtomicReference<Object> result = deserializer.getNullValue(new DeserializationContext());
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        Object result = deserializer.getEmptyValue(new DeserializationContext());
        assertNotNull(result);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object result = deserializer.getReferenced(reference);
        assertNotNull(result);
        assertEquals(reference.get(), result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        DeserializationConfig config = new DeserializationConfig();
        Boolean result = deserializer.supportsUpdate(config);
        assertNotNull(result);
        assertTrue(result);
    }

}