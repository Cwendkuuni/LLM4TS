package com.fasterxml.jackson.databind.deser.std;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext context = new DeserializationContext(null, null);
        AtomicReference<Object> result = deserializer.getNullValue(context);
        AtomicReference<Object> expected = new AtomicReference<Object>();
        assertEquals(expected, result);
    }

    @Test
    public void testGetEmptyValue() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext context = new DeserializationContext(null, null);
        Object result = deserializer.getEmptyValue(context);
        AtomicReference<Object> expected = new AtomicReference<Object>();
        assertEquals(expected, result);
    }

    @Test
    public void testReferenceValue() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        AtomicReference<Object> expected = new AtomicReference<Object>(contents);
        assertEquals(expected, result);
    }

    @Test
    public void testGetReferenced() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object result = deserializer.getReferenced(reference);
        Object expected = reference.get();
        assertEquals(expected, result);
    }

    @Test
    public void testUpdateReference() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        reference.set(contents);
        AtomicReference<Object> expected = reference;
        assertEquals(expected, result);
    }

    @Test
    public void testSupportsUpdate() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = new DeserializationConfig(null, null);
        Boolean result = deserializer.supportsUpdate(config);
        Boolean expected = Boolean.TRUE;
        assertEquals(expected, result);
    }
}