package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
    
    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeser = mock(JsonDeserializer.class);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);
        
        assertEquals(typeDeser, result._typeDeserializer);
        assertEquals(valueDeser, result._valueDeserializer);
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        
        assertEquals(new AtomicReference<Object>(), result);
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object result = deserializer.getEmptyValue(ctxt);
        
        assertEquals(new AtomicReference<Object>(), result);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        
        assertEquals(new AtomicReference<Object>(contents), result);
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object result = deserializer.getReferenced(reference);
        
        assertEquals(reference.get(), result);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>();
        Object contents = new Object();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        
        assertEquals(reference, result);
        assertEquals(contents, reference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Boolean result = deserializer.supportsUpdate(config);
        
        assertEquals(Boolean.TRUE, result);
    }
}