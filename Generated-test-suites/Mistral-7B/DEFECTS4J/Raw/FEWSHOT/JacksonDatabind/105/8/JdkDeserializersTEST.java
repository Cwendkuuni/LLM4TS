package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * Test cases for JdkDeserializers class.
 */
public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);
    }

    @Test
    public void testFind_Other() {
        Class<Object> rawType = Object.class;
        String clsName = String.class.getName();
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_Null() {
        Class<?> rawType = null;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_Unknown() {
        Class<Serializable> rawType = Serializable.class;
        String clsName = "com.example.UnknownClass".replace('.', '_').toUpperCase();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_UUID() {
        Class<UUID> rawType = UUID.class;
        JsonDeserializer<UUID> deserializer = FromStringDeserializer.findDeserializer(rawType);
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFindDeserializer_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        JsonDeserializer<AtomicBoolean> deserializer = FromStringDeserializer.findDeserializer(rawType);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_StackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        JsonDeserializer<StackTraceElement> deserializer = FromStringDeserializer.findDeserializer(rawType);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = FromStringDeserializer.findDeserializer(rawType);
        assertNull(deserializer);
    }
}