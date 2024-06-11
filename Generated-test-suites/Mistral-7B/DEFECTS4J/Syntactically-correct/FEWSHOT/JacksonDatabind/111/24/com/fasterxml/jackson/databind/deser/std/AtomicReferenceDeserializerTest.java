package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToObject;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testInstanciation() throws InstantiationException, IllegalAccessException {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                ObjectType.valueOf("com.test.AtomicReferenceTest"), null, null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                ObjectType.valueOf("com.test.AtomicReferenceTest"), null, null, null);
        AtomicReference<Object> expected = new AtomicReference<>();
        Object actual = serializer.getNullValue(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                ObjectType.valueOf("com.test.AtomicReferenceTest"), null, null, null);
        AtomicReference<Object> expected = new AtomicReference<>();
        Object actual = serializer.getEmptyValue(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                ObjectType.valueOf("com.test.AtomicReferenceTest"), null, null, null);
        Object contents = new Object();
        AtomicReference<Object> expected = new AtomicReference<>(contents);
        AtomicReference<Object> actual = serializer.referenceValue(contents);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetReferenced() throws IOException {
        AtomicReference<Object> reference = new AtomicReference<>("initialValue");
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                ObjectType.valueOf("com.test.AtomicReferenceTest"), null, null, null);
        Object actual = serializer.getReferenced(reference);
        assertEquals("initialValue", actual);
    }

    @Test
    public void testUpdateReference() throws IOException {
        AtomicReference<Object> reference = new AtomicReference<>("initialValue");
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                ObjectType.valueOf("com.test.AtomicReferenceTest"), null, null, null);
        Object newValue = new Object();
        AtomicReference<Object> updatedReference = serializer.updateReference(reference, newValue);
        assertEquals(newValue, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                ObjectType.valueOf("com.test.AtomicReferenceTest"), null, null, null);
        assertTrue(serializer.supportsUpdate(null));
    }

    @Test
    public void testDeserializationWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"class\":\"com.test.AtomicReferenceTest\", \"value\":{\"foo\":1}}");
        JavaType javaType = mapper.getTypeFactory().constructType(AtomicReference.class, Integer.class);

        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                javaType, null, null, null);
        AtomicReference<Integer> actual = mapper.readerForUpdating(AtomicReference.class).readValue(node);

        AtomicReference<Integer> expected = new AtomicReference<>(1);
        assertEquals(expected, actual);
        assertEquals(1, expected.get());
    }

    @Test
    public void testDeserializationWithoutValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"class\":\"com.test.AtomicReferenceTest\", \"value\": null}");
        JavaType javaType = mapper.getTypeFactory().constructType(AtomicReference.class, String.class);

        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                javaType, null, null, null);
        AtomicReference<String> actual = mapper.readerForUpdating(AtomicReference.class).readValue(node);
        AtomicReference<String> expected = new AtomicReference<>();
        assertEquals(expected, actual);
        assertNull(expected.get());
    }
}

class ComTestAttributeReferenceTest {
    @JsonProperty("class")
    public static final String CLASS_NAME = "com.test.AtomicReferenceTest";

    @JsonCreator
    public static AtomicReferenceTest create(Object value) {
        return new AtomicReferenceTest(value);
    }
}

class AtomicReferenceTest {
    private AtomicReference<Object> reference;

    public AtomicReferenceTest(Object value) {
        reference = new AtomicReference<>(value);
    }

    public Object getValue() {
        return reference.get();
    }

    public void setValue(Object value) {
        reference.set(value);
    }
}