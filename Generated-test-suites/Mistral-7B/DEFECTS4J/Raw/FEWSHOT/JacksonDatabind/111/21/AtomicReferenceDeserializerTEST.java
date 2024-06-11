package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserializerConstruction() {
        AtomicReferenceType type = new AtomicReferenceType(AtomicReference.class, Object.class);
        ValueInstantiator valueInstantiator = new StdScalarValueInstantiator();
        TypeDeserializer typeDeserializer = new SimpleTypeDeserializer(type);
        JsonDeserializer<?> valueDeserializer = new StdScalarDeserializer(Object.class);

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(type, valueInstantiator, typeDeserializer, valueDeserializer);

        assertNotNull(atomicReferenceDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AtomicReference<Object> nullValue = AtomicReferenceDeserializer.getNullValue(ctxt);

        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object emptyValue = AtomicReferenceDeserializer.getEmptyValue(ctxt);

        assertEquals(AtomicReference.class, emptyValue.getClass());
        assertNotNull(emptyValue);
        assertNull(((AtomicReference<Object>) emptyValue).get());
    }

    @Test
    public void testReferenceValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object contents = new Object();
        AtomicReference<Object> referenceValue = AtomicReferenceDeserializer.referenceValue(contents);

        assertEquals(AtomicReference.class, referenceValue.getClass());
        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testUpdateReference() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newContents = new Object();

        AtomicReference<Object> updatedReference = AtomicReferenceDeserializer.updateReference(reference, newContents);

        assertEquals(reference, updatedReference);
        assertEquals(newContents, reference.get());
    }

    @Test
    public void testSupportsUpdate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(new AtomicReferenceType(AtomicReference.class, Object.class), new StdScalarValueInstantiator(), new SimpleTypeDeserializer(new AtomicReferenceType(AtomicReference.class, Object.class)), new StdScalarDeserializer(Object.class));

        assertTrue(atomicReferenceDeserializer.supportsUpdate(mapper.getDeserializationConfig()));
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": 123}";
        JsonNode node = mapper.readTree(json);
        TypeReference<AtomicReference<Number>> typeReference = new TypeReference<AtomicReference<Number>>() {};

        AtomicReference<Number> deserializedValue = mapper.readValue(node, typeReference);

        assertEquals(AtomicReference.class, deserializedValue.getClass());
        assertEquals(Number.class, deserializedValue.get().getClass());
        assertEquals(123, deserializedValue.get().intValue());
    }
}