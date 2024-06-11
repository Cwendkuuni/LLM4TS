package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind_NullRawType() {
        assertNull(JdkDeserializers.find(null, "com.class.name"));
    }

    @Test
    public void testFind_InvalidClassName() {
        assertNull(JdkDeserializers.find(Integer.class, "com.invalid.className"));
    }

    @Test
    public void testFind_FoundByFromStringDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNotNull(deserializer);
        assertNotNull(JdkDeserializers.find(String.class, FromStringDeserializer.class.getName()));
    }

    @Test
    public void testFind_FoundByClass() {
        assertNotNull(JdkDeserializers.find(UUID.class, UUID.class.getName()));
    }

    @Test
    public void testFind_NotFound() {
        assertNull(JdkDeserializers.find(Object.class, Object.class.getName()));
    }

    @Test
    public void testFind_UUID() {
        assertNotNull(JdkDeserializers.find(UUID.class, UUID.class.getName()));
        assertNull(JdkDeserializers.find(UUID.class, String.class.getName()));
    }

    @Test
    public void testFind_AtomicBoolean() {
        assertNotNull(JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()));
        assertNull(JdkDeserializers.find(AtomicBoolean.class, String.class.getName()));
    }

    @Test
    public void testFind_StackTraceElement() {
        assertNotNull(JdkDeserializers.find(StackTraceElement[].class, StackTraceElement[].class.getName()));
        assertNull(JdkDeserializers.find(StackTraceElement[].class, String.class.getName()));
    }

    @Test
    public void testFind_ByteBuffer() {
        assertNotNull(JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()));
        assertNull(JdkDeserializers.find(ByteBuffer.class, String.class.getName()));
    }

    @Test(expected = NullPointerException.class)
    public void testFind_NullClassName() {
        JdkDeserializers.find(UUID.class, null);
    }
}