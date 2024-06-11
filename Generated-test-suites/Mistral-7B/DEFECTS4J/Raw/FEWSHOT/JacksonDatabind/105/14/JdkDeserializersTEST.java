package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> uuidClass = UUID.class;
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, UUID.class.getName());
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, AtomicBoolean.class.getName());
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, StackTraceElement.class.getName());
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, byteBufferClass.getName());
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_UnknownClass() {
        Class<Object> unknownClass = Object.class;
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(unknownClass, "UnknownClass".getClass().getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        Class<String> stringClass = String.class;
        JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, String.class.getName());
        assertEquals(FromStringDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_IntegerArray() {
        Class<int[]> intArrayClass = int[].class;
        JsonDeserializer<int[]> deserializer = JdkDeserializers.find(intArrayClass, int[].class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_ListOfUUID() {
        TypeReference<List<UUID>> listUuidTypeRef = new TypeReference<List<UUID>>() {};
        JsonDeserializer<List<UUID>> deserializer = JdkDeserializers.find(listUuidTypeRef.getType(), listUuidTypeRef.getType().getName());
        assertNull(deserializer);
    }
}