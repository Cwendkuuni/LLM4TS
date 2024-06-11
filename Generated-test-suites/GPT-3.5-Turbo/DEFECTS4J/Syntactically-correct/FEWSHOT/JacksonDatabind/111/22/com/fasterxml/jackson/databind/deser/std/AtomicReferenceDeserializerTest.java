
package com.fasterxml.jackson.databind.deser.std;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<?>();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);

        AtomicReferenceDeserializer resolvedDeserializer = deserializer.withResolved(typeDeser, deser);
        
        assertNotNull(resolvedDeserializer);
        assertEquals(fullType, resolvedDeserializer._fullType);
        assertEquals(inst, resolvedDeserializer._valueInstantiator);
        assertEquals(typeDeser, resolvedDeserializer._valueTypeDeserializer);
        assertEquals(deser, resolvedDeserializer._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        AtomicReference<Object> nullValue = deserializer.getNullValue(ctxt);
        
        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        Object emptyValue = deserializer.getEmptyValue(ctxt);
        
        assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);
        
        Object referenced = deserializer.getReferenced(reference);
        
        assertNotNull(referenced);
        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        
        AtomicReference<Object> reference = new AtomicReference<Object>();
        
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        
        Boolean supportsUpdate = deserializer.supportsUpdate(config);
        
        assertNotNull(supportsUpdate);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}
