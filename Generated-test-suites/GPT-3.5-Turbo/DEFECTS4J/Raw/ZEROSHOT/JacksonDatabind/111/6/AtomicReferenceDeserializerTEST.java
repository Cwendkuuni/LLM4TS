package com.fasterxml.jackson.databind.deser.std;

import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer deserializer;

    @Before
    public void setUp() {
        JavaType fullType = new JavaType(new AtomicReference<Object>(){}.getClass());
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);
        
        assertNotNull(result);
        assertEquals(typeDeser, result.getTypeDeserializer());
        assertEquals(valueDeser, result.getValueDeserializer());
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = deserializer.getEmptyValue(ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        
        Object result = deserializer.getReferenced(reference);
        
        assertEquals(reference.get(), result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        
        assertEquals(contents, reference.get());
        assertEquals(reference, result);
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        
        Boolean result = deserializer.supportsUpdate(config);
        
        assertTrue(result);
    }
}