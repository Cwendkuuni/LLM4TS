import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws JsonMappingException {
        // Given
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.empty(), null, null, null);

        // When
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);

        // Then
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        // Given
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.empty(), null, null, null);

        // When
        Object emptyValue = deserializer.getEmptyValue(null);

        // Then
        assertTrue(emptyValue instanceof AtomicReference);
        assertNull(((AtomicReference) emptyValue).get());
    }

    @Test
    public void testReferenceValue() {
        // Given
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.empty(), null, null, null);
        Object contents = "test";

        // When
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);

        // Then
        assertNotNull(referenceValue.get());
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        // Given
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.empty(), null, null, null);
        Object contents = "test";
        AtomicReference<Object> reference = new AtomicReference<>(contents);

        // When
        Object referencedValue = deserializer.getReferenced(reference);

        // Then
        assertNotNull(referencedValue);
        assertEquals(contents, referencedValue);
    }

    @Test
    public void testUpdateReference() {
        // Given
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.empty(), null, null, null);
        Object contents = "test";
        AtomicReference<Object> reference = new AtomicReference<>(null);

        // When
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);

        // Then
        assertNotNull(updatedReference.get());
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        // Given
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.empty(), null, null, null);
        DeserializationConfig config = null;

        // When
        Boolean supportsUpdate = deserializer.supportsUpdate(config);

        // Then
        assertTrue(supportsUpdate);
    }
}