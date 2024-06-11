package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.StdHandlerInstantiator;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    private AtomicReference<Object> atomicReference;
    private DeserializationContext ctxt;
    private AtomicReferenceDeserializer deserializer;
    private TypeDeserializer typeDeserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeReference<AtomicReference<Object>> typeRef;

    @Before
    public void setUp() {
        atomicReference = new AtomicReference<Object>();
        ctxt = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
        valueInstantiator = new StdHandlerInstantiator();
        valueDeserializer = mock(JsonDeserializer.class);
        typeRef = new TypeReference<AtomicReference<Object>>() {};

        when(valueDeserializer.deserialize(any(DeserializationContext.class), any(TypeDeserializer.class), any(JsonParser.class), anyInt()))
                .thenReturn(atomicReference.get());

        deserializer = new AtomicReferenceDeserializer(typeRef, valueInstantiator, typeDeserializer, valueDeserializer);
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeserializer, valueDeserializer);
        assertEquals(deserializer, newDeserializer);
    }

    @Test
    public void testGetNullValue() {
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        assertEquals(atomicReference, result);
    }

    @Test
    public void testGetEmptyValue() {
        Object result = deserializer.getEmptyValue(ctxt);
        assertEquals(atomicReference, result);
    }

    @Test
    public void testReferenceValue() {
        Object content = new Object();
        AtomicReference<Object> result = deserializer.referenceValue(content);
        assertEquals(atomicReference, result);
        assertEquals(content, result.get());
    }

    @Test
    public void testGetReferenced() {
        Object result = deserializer.getReferenced(atomicReference);
        assertEquals(atomicReference.get(), result);
    }

    @Test
    public void testUpdateReference() throws Exception {
        Object newContent = new Object();
        AtomicReference<Object> updatedReference = deserializer.updateReference(atomicReference, newContent);
        assertEquals(atomicReference, updatedReference);
        assertEquals(newContent, atomicReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        assertTrue(deserializer.supportsUpdate(null));
    }
}