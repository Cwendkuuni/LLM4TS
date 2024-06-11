package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>(){}, null, null, null);

        assertSame(new AtomicReference<Object>(), deserializer.getNullValue(null));
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>(){}, null, null, null);

        assertSame(new AtomicReference<Object>(), deserializer.getEmptyValue(null));
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>(){}, null, null, null);

        Object contents = new Object();

        AtomicReference<Object> reference = deserializer.referenceValue(contents);

        assertSame(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>(){}, null, null, null);

        Object referencedObject = deserializer.getReferenced(reference);

        assertSame(new Object(), referencedObject);
    }

    @Test
    public void testUpdateReference() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>(){}, null, null, null);

        Object updatedContents = new Object();

        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, updatedContents);

        assertSame(updatedContents, updatedReference.get());
        assertSame(reference, updatedReference);
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>(){}, null, null, null);

        assertSame(Boolean.TRUE, deserializer.supportsUpdate(null));
    }

    @Test
    public void testDeserialize() throws JsonMappingException {
        String jsonString = "{\"value\": 123}";

        JavaType targetType = TypeFactory.defaultInstance().constructType(AtomicReference.class, Object.class);

        AtomicReference<Object> expectedReference = new AtomicReference<Object>(123);

        ObjectMapper objectMapper = new ObjectMapper();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(targetType, null, null, null);

        AtomicReference<Object> actualReference = objectMapper.readValue(jsonString, targetType);

        assertSame(expectedReference, actualReference);
    }
}