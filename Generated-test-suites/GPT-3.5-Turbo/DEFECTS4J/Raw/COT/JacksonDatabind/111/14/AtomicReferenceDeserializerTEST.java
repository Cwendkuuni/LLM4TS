
import static org.junit.Assert.*;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        // Arrange
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);

        // Act
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);

        // Assert
        assertNotNull(result);
        assertEquals(fullType, result._fullType);
        assertEquals(inst, result._valueInstantiator);
        assertEquals(typeDeser, result._valueTypeDeserializer);
        assertEquals(valueDeser, result._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Act
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);

        // Assert
        assertNotNull(result);
        assertNull(result.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        // Arrange
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Act
        AtomicReference<Object> result = deserializer.getEmptyValue(ctxt);

        // Assert
        assertNotNull(result);
        assertNull(result.get());
    }
    
    @Test
    public void testReferenceValue() {
        // Arrange
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Act
        AtomicReference<Object> result = deserializer.referenceValue(contents);

        // Assert
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        // Arrange
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Act
        Object result = deserializer.getReferenced(reference);

        // Assert
        assertNotNull(result);
        assertEquals(reference.get(), result);
    }
    
    @Test
    public void testUpdateReference() {
        // Arrange
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Act
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);

        // Assert
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        // Arrange
        DeserializationConfig config = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Act
        Boolean result = deserializer.supportsUpdate(config);

        // Assert
        assertNotNull(result);
        assertTrue(result);
    }
}
