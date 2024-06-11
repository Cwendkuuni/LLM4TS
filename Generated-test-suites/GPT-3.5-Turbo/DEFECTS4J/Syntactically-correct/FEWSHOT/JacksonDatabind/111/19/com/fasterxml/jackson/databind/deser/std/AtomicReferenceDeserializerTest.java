package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReferenceDeserializer result = deserializer.withResolved(null, null);
        assertEquals(deserializer.getClass(), result.getClass());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getNullValue(null);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getEmptyValue(null);
        assertNull(result.get());
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = deserializer.getReferenced(reference);
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = null;
        assertEquals(Boolean.TRUE, deserializer.supportsUpdate(config));
    }
}