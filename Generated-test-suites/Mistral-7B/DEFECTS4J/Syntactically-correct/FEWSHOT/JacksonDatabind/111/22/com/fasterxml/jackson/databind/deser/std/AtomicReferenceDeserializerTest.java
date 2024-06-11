package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.util.TestUtils.constructObjectMapper;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserializerCreation() {
        JavaType fullType = new TypeReference<AtomicReference<Object>>() {}.getType();
        ValueInstantiator valueInstantiator = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deserializer = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, deserializer);
        assertNotNull(atomicReferenceDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        ObjectMapper mapper = constructObjectMapper();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        Object nullValue = atomicReferenceDeserializer.getNullValue(null);
        assertTrue(nullValue instanceof AtomicReference);
        assertEquals(null, nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
                null, null, null, null);
        Object emptyValue = atomicReferenceDeserializer.getEmptyValue(null);
        assertTrue(emptyValue instanceof AtomicReference);
        assertEquals(null, emptyValue.get());
    }

    @Test
    public void testReferenceValue() throws JsonMappingException {
        ObjectMapper mapper = constructObjectMapper();
        Object contents = new Object();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        AtomicReference<Object> referenceValue = atomicReferenceDeserializer.referenceValue(contents);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() throws JsonMappingException {
        ObjectMapper mapper = constructObjectMapper();
        AtomicReference<Object> atomicReference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        Object referencedObject = atomicReferenceDeserializer.getReferenced(atomicReference);
        assertEquals(atomicReference.get(), referencedObject);
    }

    @Test
    public void testUpdateReference() throws JsonMappingException {
        ObjectMapper mapper = constructObjectMapper();
        Object newValue = new Object();
        AtomicReference<Object> atomicReference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(atomicReference, newValue);
        assertEquals(updatedReference, atomicReference);
        assertEquals(newValue, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() throws JsonMappingException {
        ObjectMapper mapper = constructObjectMapper();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
    }

    @Test
    public void testDeserialize() throws JsonMappingException {
        ObjectMapper mapper = constructObjectMapper();
        JsonNode node = mapper.readTree("{\"foo\": 1}");
        Object typeId = mapper.readValue(node, Object.class);
        TypeDeserializer typeDeserializer = mapper.findValueDeserializer(typeId);
        JsonDeserializer<?> valueDeserializer = mapper.findRootValueDeserializer(Object.class);

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, typeDeserializer, valueDeserializer);

        ObjectNode objectNode = (ObjectNode) mapper.readTree("{\"@type\": \"com.fasterxml.jackson.databind.deser.std.AtomicReference\", \"value\": 1}");
        AtomicReference<Object> deserializedObject = mapper.treeToValue(objectNode, AtomicReference.class);

        AtomicReference<Object> expectedObject = new AtomicReference<Object>(1);
        assertEquals(expectedObject, deserializedObject);
        assertEquals(1, deserializedObject.get());
    }
}