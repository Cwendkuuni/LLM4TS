package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Object result = new ObjectMapper().readValue("\"123e4567-123e-4567-89ab-123456789012\"", new TypeReference<UUID>() {});
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(result.getClass(), UUID.class.getName());
        UUID uuid = new ObjectMapper().readerForUpdating(new UUID(1L)).readValue(new ByteArrayInputStream(result.toString().getBytes()));
        assertEquals(deserializer.getClass(), UUIDDeserializer.class);
        assertEquals(deserializer.deserialize(new StringReader("\"123e4567-123e-4567-89ab-123456789012\""), null), uuid);
    }

    @Test
    public void testFind_AtomicBoolean() {
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertEquals(deserializer.getClass(), AtomicBooleanDeserializer.class);
        AtomicBoolean atomicBoolean = new ObjectMapper().readerForUpdating(new AtomicBoolean(true)).readValue(new ByteArrayInputStream("true.toString().getBytes()".getBytes()));
        assertEquals(deserializer.deserialize(new StringReader("true"), null), atomicBoolean);
    }

    @Test
    public void testFind_StackTraceElement() {
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement[].class, StackTraceElement.class.getName());
        assertEquals(deserializer.getClass(), StackTraceElementDeserializer.class);
        String stackTraceElementJson = "{\"fileName\":\"Test.java\",\"lineNumber\":123,\"columnNumber\":45,\"methodName\":\"testMethod\",\"className\":\"Test\",\"nativeMethod\":false}";
        StackTraceElement[] stackTraceElements = new ObjectMapper().readValue(new ByteArrayInputStream(stackTraceElementJson.getBytes()), new TypeReference<StackTraceElement[]>() {});
        assertEquals(deserializer.deserialize(new StringReader(stackTraceElementJson), null), stackTraceElements[0]);
    }

    @Test
    public void testFind_ByteBuffer() {
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertEquals(deserializer.getClass(), ByteBufferDeserializer.class);
        byte[] byteBufferAsBytes = new byte[10];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteBufferAsBytes);
        assertEquals(deserializer.deserialize(new StringReader("\"base64encodedbytebuffer\""), null).asIntBuffer().array(), byteBuffer.asIntBuffer().array());
    }

    @Test
    public void testFind_UnknownClass() {
        assertNull(JdkDeserializers.find(String.class, Object.class.getName()));
    }

    @Test
    public void testFind_FromStringDeserializer() {
        JsonDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertEquals(deserializer.getClass(), StringDeserializer.class);
        assertEquals(deserializer.deserialize(new StringReader("\"Hello World\""), null), "Hello World");
    }
}