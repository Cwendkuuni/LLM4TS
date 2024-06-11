package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"some-uuid\"";
        JsonParser parser = mapper.getFactory().createParser(new TextNode(json).asText());
        DeserializationContext ctxt = new DeserializationContext(parser, null, mapper);
        Class<UUID> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, clsName);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
        UUID result = mapper.readValue(parser, UUID.class);
        assertInstanceOf(UUID.class, result);
    }

    @Test
    public void testFind_UUID_NullInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(null);
        DeserializationContext ctxt = new DeserializationContext(parser, null, mapper);
        Class<UUID> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_AtomicBoolean() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = "false";
        JsonParser parser = mapper.getFactory().createParser(new TextNode(json).asText());
        DeserializationContext ctxt = new DeserializationContext(parser, null, mapper);
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        String clsName = rawType.getName();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(rawType, clsName);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
        AtomicBoolean result = mapper.readValue(parser, AtomicBoolean.class);
        assertInstanceOf(AtomicBoolean.class, result);
    }

    @Test
    public void testFind_AtomicBoolean_NullInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(null);
        DeserializationContext ctxt = new DeserializationContext(parser, null, mapper);
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        String clsName = rawType.getName();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_StackTraceElement() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"className\": \"SomeClass\", \"methodName\": \"someMethod\", \"file\": \"SomeFile.java\", \"lineNumber\": 42, \"columnNumber\": 123}";
        JsonParser parser = mapper.getFactory().createParser(new TextNode(json).asText());
        DeserializationContext ctxt = new DeserializationContext(parser, null, mapper);
        Class<StackTraceElement> rawType = StackTraceElement.class;
        String clsName = rawType.getName();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(rawType, clsName);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
        StackTraceElement result = mapper.readValue(parser, StackTraceElement[].class)[0];
        assertInstanceOf(StackTraceElement.class, result);
    }

    @Test
    public void testFind_StackTraceElement_NullInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(null);
        DeserializationContext ctxt = new DeserializationContext(parser, null, mapper);
        Class<StackTraceElement> rawType = StackTraceElement.class;
        String clsName = rawType.getName();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_ByteBuffer() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "base64EncodedByteBuffer";
        JsonParser parser = mapper.getFactory().createParser(new TextNode(json).asText());
        DeserializationContext ctxt = new DeserializationContext(parser, null, mapper);
        Class<ByteBuffer> rawType = ByteBuffer.class;
        String clsName = rawType.getName();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(rawType, clsName);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);
        ByteBuffer result = mapper.readValue(parser, ByteBuffer.class);
        // ByteBuffer cannot be directly compared for equality due to internal state
        byte[] expectedBytes = Arrays.copyOfRange(json.getBytes(), 0, json.length() - 2);
        byte[] resultBytes = result.array();
        assertArrayEquals(expectedBytes, resultBytes);
    }

    @Test
    public void testFind_ByteBuffer_NullInput() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(null);
        DeserializationContext ctxt = new DeserializationContext(parser, null, mapper);
        Class<ByteBuffer> rawType = ByteBuffer.class;
        String clsName = rawType.getName();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_UnknownType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Class<Integer> rawType = Integer.class;
        String clsName = String.class.getName();
        JsonDeserializer<Integer> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}