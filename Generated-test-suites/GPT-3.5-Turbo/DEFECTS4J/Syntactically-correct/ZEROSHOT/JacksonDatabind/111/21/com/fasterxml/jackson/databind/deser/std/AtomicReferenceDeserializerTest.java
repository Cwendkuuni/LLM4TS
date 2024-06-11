package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {
    
    @Test
    public void testWithResolved() {
        // Create test objects
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        
        // Execute the method being tested
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        
        // Verify the result
        assertNotNull(result);
        assertEquals(fullType, result._fullType);
        assertEquals(inst, result._valueInstantiator);
        assertEquals(typeDeser, result._typeDeserializer);
        assertEquals(valueDeser, result._valueDeserializer);
    }
    
    @Test
    public void testGetNullValue() {
        // Create test objects
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        
        // Execute the method being tested
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        
        // Verify the result
        assertNotNull(result);
        // Additional assertions can be made if needed
    }
    
    @Test
    public void testGetEmptyValue() {
        // Create test objects
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        
        // Execute the method being tested
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        
        // Verify the result
        assertNotNull(result);
        // Additional assertions can be made if needed
    }
    
    @Test
    public void testReferenceValue() {
        // Create test objects
        Object contents = new Object();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        
        // Execute the method being tested
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        
        // Verify the result
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        // Create test objects
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        
        // Execute the method being tested
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        
        // Verify the result
        assertNotNull(result);
        assertEquals(reference.get(), result);
    }
    
    @Test
    public void testUpdateReference() {
        // Create test objects
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        
        // Execute the method being tested
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        
        // Verify the result
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        // Create test objects
        DeserializationConfig config = new DeserializationConfig();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        
        // Execute the method being tested
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        
        // Verify the result
        assertTrue(result);
    }
}