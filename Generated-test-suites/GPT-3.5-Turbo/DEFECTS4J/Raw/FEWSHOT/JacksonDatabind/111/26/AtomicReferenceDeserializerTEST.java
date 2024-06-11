package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
    @Test
    public void testWithResolved() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, deser);

        assertEquals(deserializer._fullType, result._fullType);
        assertEquals(deserializer._valueInstantiator, result._valueInstantiator);
        assertEquals(typeDeser, result._valueDeserializer);
        assertEquals(deser, result._valueTypeDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = deserializer.getNullValue(new DeserializationContext());

        assertEquals(new AtomicReference<Object>(), reference);
    }


    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object emptyValue = deserializer.getEmptyValue(new DeserializationContext());

        assertEquals(new AtomicReference<Object>(), emptyValue);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(contents);

        assertEquals(new AtomicReference<Object>(contents), reference);
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);

        Object referenced = deserializer.getReferenced(reference);

        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();

        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);

        assertEquals(new AtomicReference<Object>(contents), updatedReference);
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = new DeserializationConfig();

        Boolean supportsUpdate = deserializer.supportsUpdate(config);

        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}