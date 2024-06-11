
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
        
        assertEquals(typeDeser, result.typeDeser);
        assertEquals(valueDeser, result.valueDeser);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        Object result = deserializer.getEmptyValue(ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        reference.set(contents);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        Object result = deserializer.getReferenced(reference);
        
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        
        assertEquals(contents, reference.get());
        assertEquals(reference, result);
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        Boolean result = deserializer.supportsUpdate(config);
        
        assertEquals(Boolean.TRUE, result);
    }
}

