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
    public void setup() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;

        deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);
        
        assertNotNull(result);
        assertEquals(AtomicReferenceDeserializer.class, result.getClass());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;

        AtomicReference<Object> result = deserializer.getNullValue(ctxt);

        assertNotNull(result);
        assertEquals(AtomicReference.class, result.getClass());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;

        Object result = deserializer.getEmptyValue(ctxt);

        assertNotNull(result);
        assertEquals(AtomicReference.class, result.getClass());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();

        AtomicReference<Object> result = deserializer.referenceValue(contents);

        assertNotNull(result);
        assertEquals(AtomicReference.class, result.getClass());
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>(new Object());

        Object result = deserializer.getReferenced(reference);

        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
        assertEquals(reference.get(), result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object contents = new Object();

        AtomicReference<Object> result = deserializer.updateReference(reference, contents);

        assertNotNull(result);
        assertEquals(AtomicReference.class, result.getClass());
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;
        
        Boolean result = deserializer.supportsUpdate(config);

        assertNotNull(result);
        assertTrue(result);
    }
}