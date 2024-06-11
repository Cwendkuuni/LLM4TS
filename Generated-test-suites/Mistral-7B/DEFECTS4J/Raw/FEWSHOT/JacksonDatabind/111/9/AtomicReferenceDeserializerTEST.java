package com.fasterxml.jackson.databind.deser.std;

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
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                return new AtomicReference<Object>();
            }
        };
        TypeDeserializer typeDeserializer = new SimpleTypeDeserializer();
        JsonDeserializer<?> valueDeserializer = new StdDeserializer<Object>() {};
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);
        assertNotNull(atomicReferenceDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<Object>>() {}.getType(), null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        assertTrue(result instanceof AtomicReference);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<Object>>() {}.getType(), null, null, null);
        Object result = atomicReferenceDeserializer.getEmptyValue(null);
        assertTrue(result instanceof AtomicReference);
        assertNull(result.get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<Object>>() {}.getType(), null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        Object value = new Object();
        AtomicReference<Object> atomicReference = new AtomicReference<Object>(value);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<Object>>() {}.getType(), null, null, null);
        Object result = atomicReferenceDeserializer.getReferenced(atomicReference);
        assertEquals(value, result);
    }

    @Test
    public void testUpdateReference() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<Object>>() {}.getType(), null, null, null);
        Object newValue = new Object();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", newValue);
        JsonNode jsonNode = mapper.readTree(mapper.writerWithRootValue(node).writeValueAsString());
        atomicReferenceDeserializer.updateReference(atomicReference, newValue);
        assertEquals(newValue, atomicReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<Object>>() {}.getType(), null, null, null);
        assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
    }
}