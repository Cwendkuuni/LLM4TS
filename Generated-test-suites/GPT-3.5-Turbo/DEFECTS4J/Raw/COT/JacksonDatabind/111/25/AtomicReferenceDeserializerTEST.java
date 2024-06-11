import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        // Create test objects
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        
        // Create AtomicReferenceDeserializer object
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        
        // Call the method
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        
        // Assert the result
        assertNotNull(result);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        // Create test objects
        DeserializationContext ctxt = new DeserializationContext();
        
        // Create AtomicReferenceDeserializer object
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        
        // Call the method
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        
        // Assert the result
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        // Create test objects
        DeserializationContext ctxt = new DeserializationContext();
        
        // Create AtomicReferenceDeserializer object
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        
        // Call the method
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        
        // Assert the result
        assertNotNull(result);
    }
    
    @Test
    public void testReferenceValue() {
        // Create test objects
        Object contents = new Object();
        
        // Create AtomicReferenceDeserializer object
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        
        // Call the method
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        
        // Assert the result
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        // Create test objects
        AtomicReference<Object> reference = new AtomicReference<>();
        
        // Create AtomicReferenceDeserializer object
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        
        // Call the method
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        
        // Assert the result
        assertNotNull(result);
    }

    @Test
    public void testUpdateReference() {
        // Create test objects
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        
        // Create AtomicReferenceDeserializer object
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        
        // Call the method
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        
        // Assert the result
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        // Create test objects
        DeserializationConfig config = new DeserializationConfig();
        
        // Create AtomicReferenceDeserializer object
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        
        // Call the method
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        
        // Assert the result
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result);
    }
}

