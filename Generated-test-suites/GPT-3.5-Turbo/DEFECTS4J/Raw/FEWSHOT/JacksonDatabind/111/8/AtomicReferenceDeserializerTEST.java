
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        
        assertNull(deserializer.withResolved(null, null));
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);

        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getEmptyValue(ctxt);

        assertNotNull(result);
    }

    @Test
    public void testReferenceValue() {
        Object contents = null;

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.referenceValue(contents);

        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = null;

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getReferenced(reference);

        assertNull(result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();

        Object contents = null;

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);

        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean result = deserializer.supportsUpdate(config);

        assertEquals(Boolean.TRUE, result);
    }

}
