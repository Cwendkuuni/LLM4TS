package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, null, null, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testGetNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, null, null, null);
        AtomicReference<Object> nullValue = deserializer.getNullValue(mapper.getDeserializationContext());
        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, null, null, null);
        Object emptyValue = deserializer.getEmptyValue(null);
        AtomicReference<Object> atomicEmptyValue = (AtomicReference<Object>) emptyValue;
        assertNotNull(atomicEmptyValue);
        assertEquals(0, atomicEmptyValue.get().hashCode());
    }

    @Test
    public void testReferenceValue() {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, null, null, null);
        Object getReferenced = deserializer.getReferenced(reference);
        assertEquals(contents, getReferenced);
    }

    @Test
    public void testUpdateReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object newContents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, null, null, null);
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newContents);
        assertEquals(reference, updatedReference);
        assertEquals(newContents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, null, null, null);
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        assertTrue(supportsUpdate.booleanValue());
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"value\":1}";
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(AtomicReference.class);
        JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(Integer.class, null);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, typeDeserializer, valueDeserializer, null);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        AtomicReference<Object> deserialized = mapper.treeToValue(node, deserializer, null);
        AtomicReference<Object> expected = new AtomicReference<Object>(1);
        assertEquals(expected, deserialized);
    }
}