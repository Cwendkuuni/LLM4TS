package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer.StdSerializerProvider;

public class AtomicReferenceDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testConstructor() {
        JavaType fullType = objectMapper.getTypeFactory().constructType(AtomicReference.class);
        TypeDeserializer typeDeser = new EmptyTypeDeserializer();
        JsonDeserializer<?> deser = new JsonCreator(AtomicReference.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, null, typeDeser, deser);
        assertNotNull(deserializer);
    }

    @Test
    public void testWithResolved() {
        JavaType fullType = objectMapper.getTypeFactory().constructType(AtomicReference.class);
        ValueInstantiator inst = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonMappingException, InstantiationException {
                return new AtomicReference<Object>();
            }
        };
        TypeDeserializer typeDeser = new EmptyTypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonCreator(AtomicReference.class);
        AtomicReferenceDeserializer deserializer1 = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        TypeDeserializer typeDeser2 = new EmptyTypeDeserializer();
        JsonDeserializer<?> valueDeser2 = new JsonCreator(AtomicReference.class);
        AtomicReferenceDeserializer deserializer2 = deserializer1.withResolved(typeDeser2, valueDeser2);
        assertEquals(deserializer1, deserializer2);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> result = new AtomicReferenceDeserializer(null, null, null, null).getNullValue(null);
        assertEquals(result.getClass(), AtomicReference.class);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReference<Object> result = new AtomicReferenceDeserializer(null, null, null, null).getEmptyValue(null);
        assertEquals(result.getClass(), AtomicReference.class);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> result = new AtomicReferenceDeserializer(null, null, null, null).referenceValue(contents);
        assertEquals(result.getClass(), AtomicReference.class);
        assertEquals(result.get(), contents);
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<String> reference = new AtomicReference<String>("test");
        Object result = new AtomicReferenceDeserializer(null, null, null, null).getReferenced(reference);
        assertEquals(result.getClass(), String.class);
        assertEquals(result, reference.get());
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<String> reference = new AtomicReference<String>("test");
        String newValue = "newTest";
        AtomicReference<String> result = new AtomicReferenceDeserializer(null, null, null, null).updateReference(reference, newValue);
        assertEquals(result.getClass(), AtomicReference.class);
        assertEquals(result.get(), newValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testUpdateReferenceWithJsonMappingException() throws JsonMappingException {
        AtomicReference<String> reference = new AtomicReference<String>("test");
        Object newValue = new Object(); // this should not be compatible with the reference type
        AtomicReference<String> result = new AtomicReferenceDeserializer(null, null, null, null).updateReference(reference, newValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdateReferenceWithUnsupportedOperationException() {
        AtomicReference<String> reference = new AtomicReference<String>("test");
        AtomicReference<Integer> newValue = new AtomicReference<Integer>(42); // AtomicReference<String> and AtomicReference<Integer> are different types
        AtomicReference<String> result = new AtomicReferenceDeserializer(null, null, null, null).updateReference(reference, newValue);
    }

    @Test
    public void testSupportsUpdate() {
        Boolean result = new AtomicReferenceDeserializer(null, null, null, null).supportsUpdate(null);
        assertEquals(result, Boolean.TRUE);
    }
}