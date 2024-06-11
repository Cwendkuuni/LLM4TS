package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer.Std;

import java.util.*;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> rawType = UUID.class;
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, UUID.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer.deserialize(null, new ObjectCodec(), new UUID("test"), TypeReference.valueOf(UUID.class)));
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(rawType, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer.deserialize(null, new ObjectMapper(), new AtomicBoolean(true), AtomicBoolean.class));
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(rawType, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer.deserialize(null, new ObjectMapper(), new StackTraceElement("test", "test", "test", 1, null), StackTraceElement.class));
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(rawType, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer.deserialize(null, new ObjectMapper(), ByteBuffer.allocate(10), ByteBuffer.class));
    }

    @Test
    public void testFind_UnknownClass() {
        Class<String> rawType = String.class;
        JsonDeserializer<String> deserializer = JdkDeserializers.find(rawType, "UnknownClass.class.getName()");
        assertNull(deserializer);
    }

    @Test
    public void testFind_ArrayType() {
        Class<String[]> rawType = String[].class;
        JsonDeserializer<String[]> deserializer = JdkDeserializers.find(rawType, "java.lang.String.class.getName()");
        assertNotNull(deserializer);
        assertNotNull(deserializer.deserialize(null, new ObjectMapper(), new ObjectMapper().getValueFactory().constructValueArray(new String[]{"test1", "test2"})));
    }

    @Test
    public void testFind_FromStringDeserializer() {
        Class<Integer> rawType = Integer.class;
        String clsName = Std.StringDeserializer.class.getName();
        JsonDeserializer<Integer> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
    }
}