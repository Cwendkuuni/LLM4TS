package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.JSONPassthru;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserializer() {
        AtomicReference<Object> expected = new AtomicReference<Object>("initialValue");
        JavaType fullType = JsonTypeFactory.bareType(AtomicReference.class, Object.class);
        ValueInstantiator inst = new StdValueInstantiator(fullType);
        TypeDeserializer typeDeser = new BaseTypeDeserializer.BaseValueDeserializer<>();
        JsonDeserializer<Object> valueDeser = new StandardDeserializer<Object>(Object.class);

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        Object result = deserializer.referencedValue(expected);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getNullValue(null);

        assertNotNull(result);
        assertEquals(new AtomicReference<Object>(), result);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getEmptyValue(null);

        assertNotNull(result);
        assertEquals(new AtomicReference<Object>(), result);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.referenceValue(contents);

        assertNotNull(result);
        assertEquals(new AtomicReference<Object>(contents), result);
    }

    @Test
    public void testGetReferenced() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>("initialValue");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getReferenced(reference);

        assertNotNull(result);
        assertEquals("initialValue", result);
    }

    @Test
    public void testUpdateReference() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>("initialValue");
        Object newValue = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
        Object updatedValue = deserializer.getReferenced(updatedReference);

        assertEquals(updatedReference, reference);
        assertEquals(newValue, updatedValue);
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean result = deserializer.supportsUpdate(null);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testDeserialize() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"value\":{\"@class\":\"java.util.concurrent.atomic.AtomicReference\",\"value\":\"newValue\"}}";
        JsonNode node = mapper.readTree(json);

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.deserialize(null, null, null, node, null);

        assertNotNull(result);
        assertEquals(new AtomicReference<Object>("newValue"), result);
    }

    @Test
    public void testDeserializeWithTypeHint() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"value\":{\"@class\":\"java.util.concurrent.atomic.AtomicReference\",\"value\":\"newValue\"}}";
        TypeDeserializer typeDeserializer = new BaseTypeDeserializer.BaseValueDeserializer<>();
        JavaType targetType = TypeFactory.defaultInstance().constructType(AtomicReference.class, String.class);

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(targetType, null, typeDeserializer, null);
        AtomicReference<String> result = (AtomicReference<String>) deserializer.deserialize(null, null, null, JSONPassthru.parseMap(json), null);

        assertNotNull(result);
        assertEquals(new AtomicReference<String>("newValue"), result);
    }
}