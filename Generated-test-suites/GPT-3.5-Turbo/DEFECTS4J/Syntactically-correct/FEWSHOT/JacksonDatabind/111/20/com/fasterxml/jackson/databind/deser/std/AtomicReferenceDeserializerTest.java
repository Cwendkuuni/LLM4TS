package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer atomicReferenceDeserializer;
    private DeserializationContext deserializationContext;
    private DeserializationConfig deserializationConfig;

    @Before
    public void setup() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.bogusType(), ValueInstantiator.bogusInstance(),
                TypeDeserializer.bogusTypeDeserializer(), JsonDeserializer.bogusCreator());
        deserializationContext = DeserializationContext.bogusInstance();
        deserializationConfig = DeserializationConfig.bogusInstance();
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(TypeDeserializer.bogusTypeDeserializer(), JsonDeserializer.bogusCreator());
        // TODO: assert the result
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(deserializationContext);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        Object result = atomicReferenceDeserializer.getEmptyValue(deserializationContext);
        assertEquals(AtomicReference.class, result.getClass());
    }

    @Test
    public void testReferenceValue() {
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue("contents");
        assertEquals("contents", result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>("contents");
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertEquals("contents", result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, "contents");
        assertEquals("contents", result.get());
    }

    @Test
    public void testSupportsUpdate() {
        Boolean result = atomicReferenceDeserializer.supportsUpdate(deserializationConfig);
        assertEquals(Boolean.TRUE, result);
    }
}