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
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
    
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);
    
        //Assert statements
    }
    
    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
    
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
    
        //Assert statements
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
    
        Object result = deserializer.getEmptyValue(ctxt);
    
        //Assert statements
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
    
        AtomicReference<Object> result = deserializer.referenceValue(contents);
    
        //Assert statements
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = new AtomicReference<>();
    
        Object result = deserializer.getReferenced(reference);
    
        //Assert statements
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
    
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
    
        //Assert statements
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        DeserializationConfig config = new DeserializationConfig();
    
        Boolean result = deserializer.supportsUpdate(config);
    
        //Assert statements
    }
}