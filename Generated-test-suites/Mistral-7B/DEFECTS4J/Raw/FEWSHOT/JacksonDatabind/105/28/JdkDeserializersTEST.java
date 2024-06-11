package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);
    }

    @Test
    public void testFind_UnknownClass() {
        Class<String> rawType = String.class;
        String clsName = Object.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_NullRawType() {
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(null, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_NullClassName() {
        Class<UUID> rawType = UUID.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, null);
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() throws JsonProcessingException {
        Class<String> rawType = String.class;
        String clsName = StringDeserializer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(FromStringDeserializer.class, deserializer);
    }

    @Test
    public void testFind_Null() {
        Class<UUID> rawType = UUID.class;
        String clsName = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}