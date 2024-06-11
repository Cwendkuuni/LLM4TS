package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

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
        JsonDeserializer<?> valueDeser = new JsonDeserializer<?>();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);

        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);

        assertEquals(typeDeser, result.getTypeDeserializer());
        assertEquals(valueDeser, result.getDeserializer());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        AtomicReference<Object> result = deserializer.getNullValue(ctxt);

        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        Object result = deserializer.getEmptyValue(ctxt);

        assertNotNull(result);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        AtomicReference<Object> result = deserializer.referenceValue(contents);

        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        Object result = deserializer.getReferenced(reference);

        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        AtomicReference<Object> result = deserializer.updateReference(reference, contents);

        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        Boolean result = deserializer.supportsUpdate(config);

        assertEquals(Boolean.TRUE, result);
    }
}