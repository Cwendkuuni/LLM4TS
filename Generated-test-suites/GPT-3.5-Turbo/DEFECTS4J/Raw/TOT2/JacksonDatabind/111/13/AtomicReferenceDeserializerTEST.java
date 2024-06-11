package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType fullType = new JavaType(Object.class);
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        assertNotNull(ard);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer();
        
        AtomicReference<Object> result = ard.getNullValue(ctxt);
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer();
        
        Object result = ard.getEmptyValue(ctxt);
        assertNotNull(result);
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer();
        
        AtomicReference<Object> reference = ard.referenceValue("test");
        assertEquals("test", reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer();
        
        AtomicReference<Object> reference = new AtomicReference<>("test");
        Object result = ard.getReferenced(reference);
        assertEquals("test", result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer();
        
        AtomicReference<Object> reference = new AtomicReference<>("original");
        ard.updateReference(reference, "updated");
        assertEquals("updated", reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer();
        
        Boolean result = ard.supportsUpdate(config);
        assertTrue(result);
    }
}