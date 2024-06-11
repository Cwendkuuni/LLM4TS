package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;

        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        assertNotNull(ard);
    }

    @Test
    public void testWithResolved() throws JsonMappingException {
        AtomicReferenceDeserializer ard1 = new AtomicReferenceDeserializer(null, null, null, null);
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;

        AtomicReferenceDeserializer ard2 = ard1.withResolved(typeDeser, valueDeser);
        assertNotNull(ard2);
        assertNotSame(ard1, ard2);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = null;

        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = ard.getNullValue(ctxt);

        assertNotNull(result);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;

        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = ard.getEmptyValue(ctxt);

        assertNotNull(result);
        assertInstanceOf(AtomicReference.class, result);
        assertNull(((AtomicReference<Object>) result).get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> result = ard.referenceValue(contents);
        assertNotNull(result);
        assertSame(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        Object result = ard.getReferenced(reference);
        assertNotNull(result);
        assertSame(reference.get(), result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newContent = new Object();
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> updatedReference = ard.updateReference(reference, newContent);
        assertSame(reference, updatedReference);
        assertSame(newContent, reference.get());
    }

    @Test(expected = JsonMappingException.class)
    public void testSupportsUpdateWithInvalidConfig() {
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = null;

        Boolean result = ard.supportsUpdate(config);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testSupportsUpdateWithValidConfig() {
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = new DeserializationConfig();

        Boolean result = ard.supportsUpdate(config);
        assertTrue(result.booleanValue());
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;

        AtomicReference<Object> expected = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        String json = mapper.writerWithRootValue(expected).writeValueAsString(expected);
        Object result = mapper.readValue(json, ard.getType());

        assertNotNull(result);
        assertInstanceOf(AtomicReference.class, result);
        assertSame(expected, result);
    }

    @Test
    public void testDeserializeNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;

        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        Object result = mapper.readValue(node, ard.getType());
        assertNotNull(result);
        assertInstanceOf(AtomicReference.class, result);
        assertNull(((AtomicReference<Object>) result).get());
    }

    @Test
    public void testDeserializeEmptyValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode();

        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        Object result = mapper.readValue(node, ard.getType());
        assertNotNull(result);
        assertInstanceOf(AtomicReference.class, result);
        assertNull(((AtomicReference<Object>) result).get());
    }

    @Test
    public void testDeserializeWithContents() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        Object contents = new Object();

        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        String json = mapper.writerWithRootValue(contents).writeValueAsString(contents);
        node = mapper.readTree(json);

        Object result = mapper.readValue(node, ard.getType());
        assertNotNull(result);
        assertInstanceOf(AtomicReference.class, result);
        assertSame(contents, ((AtomicReference<Object>) result).get());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;

        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        String json = "{ \"foo\": \"bar\" }";
        Object result = mapper.readValue(json, ard.getType());

        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeWithNullType() {
        AtomicReferenceDeserializer ard = new AtomicReferenceDeserializer(null, null, null, null);

        try {
            ard.deserialize(null, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}