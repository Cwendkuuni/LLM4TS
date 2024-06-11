package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;

import java.nio.ByteBuffer;
import java.util.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> rawType = UUID.class;
        assertNotNull(JdkDeserializers.find(rawType, UUID.class.getName()));
        assertNull(JdkDeserializers.find(rawType, String.class.getName()));
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        assertNotNull(JdkDeserializers.find(rawType, AtomicBoolean.class.getName()));
        assertNull(JdkDeserializers.find(rawType, String.class.getName()));
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        assertNotNull(JdkDeserializers.find(rawType, StackTraceElement.class.getName()));
        assertNull(JdkDeserializers.find(rawType, String.class.getName()));
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        assertNotNull(JdkDeserializers.find(rawType, ByteBuffer.class.getName()));
        assertNull(JdkDeserializers.find(rawType, String.class.getName()));
    }

    @Test
    public void testFind_FromStringDeserializer() {
        Class<?> rawType = String.class;
        String clsName = FromStringDeserializer.class.getName();
        assertNotNull(JdkDeserializers.find(rawType, clsName));
    }

    @Test
    public void testFind_Null() {
        Class<?> rawType = null;
        assertNull(JdkDeserializers.find(rawType, UUID.class.getName()));
    }

    @Test
    public void testFind_UnknownClass() {
        Class<?> rawType = Integer.class;
        assertNull(JdkDeserializers.find(rawType, UUID.class.getName()));
    }
}