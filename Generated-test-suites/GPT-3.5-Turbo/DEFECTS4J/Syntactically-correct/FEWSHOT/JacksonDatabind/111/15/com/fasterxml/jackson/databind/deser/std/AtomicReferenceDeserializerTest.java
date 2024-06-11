package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst,
                typeDeser, valueDeser);

        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);

        assertEquals(fullType, result.getFullType());
        assertEquals(inst, result.getValueInstantiator());
        assertEquals(typeDeser, result.getTypeDeserializer());
        assertEquals(valueDeser, result.getValueDeserializer());
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);

        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);

        assertNotNull(result);
    }

    @Test
    public void testReferenceValue() {
        AtomicReference<Object> reference = new AtomicReference<>(null);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Object contents = null;
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);

        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>(null);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Object result = atomicReferenceDeserializer.getReferenced(reference);

        assertNull(result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>(null);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Object contents = null;
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);

        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);

        assertTrue(result);
    }
}