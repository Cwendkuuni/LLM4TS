package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_NullInput() {
        assertNull(JdkDeserializers.find(null, null));
    }

    @Test
    public void testFind_EmptyClassName() {
        assertNull(JdkDeserializers.find(Object.class, ""));
    }

    @Test
    public void testFind_UnknownClassName() {
        assertNull(JdkDeserializers.find(Object.class, "com.example.MyClass"));
    }

    @Test
    public void testFind_UUID() {
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(Object.class, UUID.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(Object.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(Object.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(Object.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        ClassIdType idType = new ClassIdType();
        Class<?> rawType = idType.resolvedClassId(UUID.class.getName(), UUID.class);
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, "com.fasterxml.jackson.databind.deser.std.FromStringDeserializer");
        assertNotNull(deserializer);
        assertInstanceOf(FromStringDeserializer.class, deserializer);
    }
}