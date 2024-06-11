import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class AtomicReferenceDeserializerTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private ValueInstantiator valueInstantiator;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private JsonDeserializer<?> jsonDeserializer;

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
            null, valueInstantiator, typeDeserializer, jsonDeserializer);
        AtomicReferenceDeserializer resolved = deserializer.withResolved(typeDeserializer, jsonDeserializer);
        assertNotNull(resolved);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
            null, valueInstantiator, typeDeserializer, jsonDeserializer);
        AtomicReference<Object> nullValue = deserializer.getNullValue(deserializationContext);
        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
            null, valueInstantiator, typeDeserializer, jsonDeserializer);
        AtomicReference<Object> emptyValue = deserializer.getEmptyValue(deserializationContext);
        assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
            null, valueInstantiator, typeDeserializer, jsonDeserializer);
        AtomicReference<Object> reference = deserializer.referenceValue("test");
        assertNotNull(reference);
        assertEquals("test", reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
            null, valueInstantiator, typeDeserializer, jsonDeserializer);
        AtomicReference<Object> reference = new AtomicReference<>("test");
        assertEquals("test", deserializer.getReferenced(reference));
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
            null, valueInstantiator, typeDeserializer, jsonDeserializer);
        AtomicReference<Object> reference = new AtomicReference<>("old");
        deserializer.updateReference(reference, "new");
        assertEquals("new", reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
            null, valueInstantiator, typeDeserializer, jsonDeserializer);
        DeserializationConfig config = null;
        assertTrue(deserializer.supportsUpdate(config));
    }
}