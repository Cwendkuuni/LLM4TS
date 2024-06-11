package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.util.TestUtil.convertValueToObject;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstruction() {
        JavaType fullType = new TypeReference<AtomicReference<Object>>() {}.getType();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, null, null, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
        AtomicReference<Object> result = deserializer.getNullValue(null);
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getEmptyValue(null);
        assertNotNull(result);
        assertEquals(AtomicReference.class, result.getClass());
    }

    @Test
    public void testReferenceValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
        Object contents = convertValueToObject(ObjectNode.class, "{ \"value\": 1 }");
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        assertNotNull(referenceValue);
        assertEquals(1, referenceValue.get());
    }

    @Test
    public void testGetReferenced() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
        AtomicReference<String> atomicReference = new AtomicReference<>("initialValue");
        Object referenced = deserializer.getReferenced(atomicReference);
        assertNotNull(referenced);
        assertEquals("initialValue", referenced);
    }

    @Test
    public void testUpdateReference() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
        AtomicReference<Object> atomicReference = new AtomicReference<Object>(1);
        Object newValue = convertValueToObject(ObjectNode.class, "{ \"newValue\": 2 }");
        AtomicReference<Object> updatedAtomicReference = deserializer.updateReference(atomicReference, newValue);
        assertEquals(updatedReference, atomicReference);
        assertEquals(2, updatedAtomicReference.get());
    }

    @Test
    public void testSupportsUpdate() throws JsonProcessingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        assertTrue(deserializer.supportsUpdate(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionOnInvalidUpdate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
        AtomicReference<Object> atomicReference = new AtomicReference<Object>(1);
        Object newValue = "not an Object";
        deserializer.updateReference(atomicReference, newValue);
    }
}