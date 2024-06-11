package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
    
    @Test
    public void testWithResolved() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);
        
        assertEquals(fullType, result.getFullType());
        assertEquals(inst, result.getValueInstantiator());
        assertEquals(typeDeser, result.getTypeDeserializer());
        assertEquals(valueDeser, result.getValueDeserializer());
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator());
        
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        
        assertNull(result.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator());
        
        Object result = deserializer.getEmptyValue(ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator());
        
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator());
        
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        
        Object result = deserializer.getReferenced(reference);
        
        assertEquals(contents, result);
    }
    
    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator());
        
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator());
        
        Boolean result = deserializer.supportsUpdate(new DeserializationConfig());
        
        assertEquals(Boolean.TRUE, result);
    }
}