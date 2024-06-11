package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer.FromStringDeserializerType;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.SimpleFromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.ByteBufferDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.UUIDDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFindDeserializerForUUID() {
        Class<?> rawType = UUID.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, UUID.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFindDeserializerForAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFindDeserializerForStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFindDeserializerForByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFindDeserializerForOtherClass() {
        Class<?> rawType = Integer.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, Integer.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerForFromStringDeserializer() {
        ClassIdType idType = new ClassIdType(String.class);
        JsonDeserializer<?> rawDeserializer = new SimpleFromStringDeserializer(String.class);
        FromStringDeserializerType type = new FromStringDeserializerType(idType, String.class);
        Class<?> cls = type.getRawClass();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(cls, type.getClassIdentifier().toString());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof SimpleFromStringDeserializer);
    }
}