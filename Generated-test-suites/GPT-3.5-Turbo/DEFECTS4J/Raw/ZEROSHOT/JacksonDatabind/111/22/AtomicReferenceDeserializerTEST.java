package com.fasterxml.jackson.databind.deser.std;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
  
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
  
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);

        assertEquals(typeDeser, result.getTypeDeserializer());
        assertEquals(valueDeser, result.getValueDeserializer());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
  
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
  
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference<?>);
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
  
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(null);
        
        assertNotNull(result);
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>();
        reference.set("test");

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
  
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        
        assertNotNull(result);
        assertEquals("test", result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
  
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, "test");
        
        assertNotNull(result);
        assertEquals("test", result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
  
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        
        assertTrue(result);
    }
}