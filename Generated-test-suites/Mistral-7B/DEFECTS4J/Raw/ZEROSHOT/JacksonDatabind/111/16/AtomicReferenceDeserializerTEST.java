package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testEmptyDeserialization() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(type, null, new None(), null);
        AtomicReference<Object> result = deserializer.deserialize(null, null);
        assertNull(result);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(type, null, new None(), null);
        AtomicReference<Object> result = deserializer.deserialize(null, new DeserializationContext(null));
        assertEquals(AtomicReference.class, result.getClass());
        assertNull(result.get());
    }

    @Test
    public void testDeserializationWithContent() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, String.class);
        AtomicReference<String> expectedResult = new AtomicReference<>("testContent");
        ObjectNode node = mapper.createObjectNode();
        node.put("_class", "com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer");
        node.put("value", "testContent");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(type, null, new None(), null);
        AtomicReference<Object> result = deserializer.deserialize(node, new DeserializationContext(mapper.getFactory()));
        assertEquals(expectedResult, result);
        assertEquals("testContent", result.get());
    }

    @Test
    public void testDeserializationWithTypeDeserializer() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, String.class);
        ValueInstantiator instantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws IOException {
                return new AtomicReference<>("testContent");
            }
        };
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(type, instantiator, null, null);
        AtomicReference<Object> result = deserializer.deserialize(null, new DeserializationContext(mapper.getFactory()));
        assertEquals(AtomicReference.class, result.getClass());
        assertNull(result.get());

        ObjectNode node = mapper.createObjectNode();
        node.put("_class", "com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer");
        node.put("value", "testContent");
        result = deserializer.deserialize(node, new DeserializationContext(mapper.getFactory()));
        assertEquals("testContent", result.get());
    }

    @Test
    public void testDeserializationWithValueDeserializer() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, String.class);
        JsonDeserializer<String> valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "testContent";
            }
        };
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(type, null, null, valueDeserializer);
        AtomicReference<Object> result = deserializer.deserialize(null, new DeserializationContext(mapper.getFactory()));
        assertEquals(AtomicReference.class, result.getClass());
        assertNull(result.get());

        ObjectNode node = mapper.createObjectNode();
        node.put("_class", "com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer");
        result = deserializer.deserialize(node, new DeserializationContext(mapper.getFactory()));
        assertEquals("testContent", result.get());
    }

    @Test
    public void testUpdateReference() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, String.class);
        AtomicReference<String> expectedResult = new AtomicReference<>("testContent");
        ObjectNode node = mapper.createObjectNode();
        node.put("_class", "com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer");
        AtomicReference<Object> result = deserializer.deserialize(node, new DeserializationContext(mapper.getFactory()));
        assertEquals(expectedResult.getClass(), result.getClass());
        assertNull(result.get());

        String newContent = "newContent";
        result = deserializer.updateReference(result, newContent);
        assertEquals(expectedResult.getClass(), result.getClass());
        assertEquals(newContent, result.get());
    }

    @Test(expected = JsonMappingException.class)
    public void testUpdateWithNullReference() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, String.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(type, null, null, null);
        AtomicReference<Object> result = deserializer.updateReference(null, "testContent");
    }

    @Test
    public void testSupportsUpdate() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, String.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(type, null, null, null);
        assertTrue(deserializer.supportsUpdate(null));
    }
}