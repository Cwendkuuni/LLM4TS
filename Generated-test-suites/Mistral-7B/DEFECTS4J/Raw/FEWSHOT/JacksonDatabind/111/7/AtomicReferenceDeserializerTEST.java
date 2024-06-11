package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest extends TestBase {

    private static final TypeReference<AtomicReference<Object>> TYPE_REF = new TypeReference<AtomicReference<Object>>() {};

    @Test
    public void testAtomicReferenceDeserializer() throws IOException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, null, null, null);
        Object value = new Object();
        AtomicReference<Object> expected = new AtomicReference<>(value);
        Object deserialized = deserializer.deserialize(value, null, null);
        assertTrue(deserialized instanceof AtomicReference);
        assertEquals(expected, deserialized);
    }

    @Test
    public void testGetNullValue() throws IOException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, null, null, null);
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);
        assertNotNull(nullValue);
        assertEquals(0, nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, null, null, null);
        Object emptyValue = deserializer.getEmptyValue(null);
        assertNotNull(emptyValue);
        assertEquals(new AtomicReference<Object>(), emptyValue);
    }

    @Test
    public void testReferenceValue() throws IOException {
        Object value = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, null, null, null);
        AtomicReference<Object> expected = new AtomicReference<>(value);
        Object deserialized = deserializer.referenceValue(value);
        assertTrue(deserialized instanceof AtomicReference);
        assertEquals(expected, deserialized);
    }

    @Test
    public void testGetReferenced() throws IOException {
        Object value = new Object();
        AtomicReference<Object> atomicReference = new AtomicReference<>(value);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, null, null, null);
        Object deserialized = deserializer.getReferenced(atomicReference);
        assertEquals(value, deserialized);
    }

    @Test
    public void testUpdateReference() throws IOException {
        Object newValue = new Object();
        AtomicReference<Object> atomicReference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, null, null, null);
        deserializer.updateReference(atomicReference, newValue);
        assertEquals(newValue, atomicReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, null, null, null);
        assertTrue(deserializer.supportsUpdate(null));
    }

    @Test
    public void testCreateDeserializer() throws IOException {
        ObjectValueFactory objFactory = new ObjectValueFactory();
        StdSerializer<Object> objSerializer = new StdSerializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, objFactory, null, objSerializer);
        AtomicReference<Object> expected = new AtomicReference<>(new Object());
        Object deserialized = mapper.readValue("{\"value\": \"foo\"}", TYPE_REF);
        assertEquals(expected, deserializer.deserialize(deserialized, null, null));
    }

    @Test
    public void testCreateDeserializerWithTypeDeserializer() throws IOException {
        ObjectValueFactory objFactory = new ObjectValueFactory();
        StdSerializer<Object> objSerializer = new StdSerializer<Object>(Object.class);
        TypeDeserializer typeDeserializer = new StringDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, objFactory, typeDeserializer, objSerializer);
        AtomicReference<Object> expected = new AtomicReference<>(new Object());
        Object deserialized = mapper.readValue("{\"value\": \"foo\"}", new TypeReference<AtomicReference<String>>() {});
        assertEquals(expected, deserializer.deserialize(deserialized, null, null));
    }

    @Test
    public void testCreateDeserializerWithValueDeserializer() throws IOException {
        ObjectValueFactory objFactory = new ObjectValueFactory();
        StringDeserializer stringDeserializer = new StringDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TYPE_REF, null, null, stringDeserializer);
        AtomicReference<String> expected = new AtomicReference<>("foo");
        Object deserialized = mapper.readValue("{\"value\": \"foo\"}", new TypeReference<AtomicReference<String>>() {});
        assertEquals(expected, deserializer.deserialize(deserialized, null, null));
    }
}