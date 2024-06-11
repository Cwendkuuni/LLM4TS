package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testNewInstance() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;

        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        assertNotNull(serializer);
    }

    @Test
    public void testWithResolved() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;

        AtomicReferenceDeserializer serializer1 = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        TypeDeserializer typeDeser1 = new StdTypeDeserializer(null, null);
        JsonDeserializer<?> valueDeser1 = null;

        AtomicReferenceDeserializer serializer2 = serializer1.withResolved(typeDeser1, valueDeser1);
        assertNotSame(serializer1, serializer2);
        assertEquals(serializer1.getClass(), serializer2.getClass());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;

        AtomicReference<Object> nullValue = AtomicReferenceDeserializer.getNullValue(ctxt);
        assertNotNull(nullValue);
        assertEquals(null, nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;

        AtomicReference<Object> emptyValue = AtomicReferenceDeserializer.getEmptyValue(ctxt);
        assertNotNull(emptyValue);
        assertEquals(null, emptyValue.get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> atomicReference = serializer.referenceValue(contents);
        assertEquals(contents, atomicReference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object referenced = serializer.getReferenced(reference);
        assertEquals(reference.get(), referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newValue = new Object();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> updatedReference = serializer.updateReference(reference, newValue);
        assertEquals(reference, updatedReference);
        assertEquals(newValue, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;

        Boolean supportsUpdate = AtomicReferenceDeserializer.supportsUpdate(config);
        assertTrue(supportsUpdate);
    }
}