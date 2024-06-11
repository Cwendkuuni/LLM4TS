package com.fasterxml.jackson.databind.deser.std;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Before
    public void setUp() {
        JavaType fullType = new JavaType(AtomicReference.class);
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        AtomicReferenceDeserializer deserializer = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);

        assertNotNull(deserializer);
        assertEquals(atomicReferenceDeserializer.getFullType(), deserializer.getFullType());
        assertSame(atomicReferenceDeserializer.getValueInstantiator(), deserializer.getValueInstantiator());
        assertSame(typeDeser, deserializer.getTypeDeserializer());
        assertSame(valueDeser, deserializer.getValueDeserializer());
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReference<Object> nullValue = atomicReferenceDeserializer.getNullValue(ctxt);

        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        Object emptyValue = atomicReferenceDeserializer.getEmptyValue(ctxt);

        assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> referenceValue = atomicReferenceDeserializer.referenceValue(contents);

        assertNotNull(referenceValue);
        assertSame(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);

        Object referenced = atomicReferenceDeserializer.getReferenced(reference);

        assertSame(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();

        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, contents);

        assertSame(reference, updatedReference);
        assertSame(contents, reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();

        Boolean supportsUpdate = atomicReferenceDeserializer.supportsUpdate(config);

        assertNotNull(supportsUpdate);
        assertTrue(supportsUpdate);
    }
}