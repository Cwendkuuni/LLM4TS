package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        ClassType uuidType = ClassType.constructUnsafe(UUID.class);
        JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidType.rawClass(), UUID.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        ClassType atomicBooleanType = ClassType.constructUnsafe(AtomicBoolean.class);
        JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanType.rawClass(), AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        ClassType stackTraceElementType = ClassType.constructUnsafe(StackTraceElement.class);
        JsonDeserializer<?> deserializer = JdkDeserializers.find(stackTraceElementType.rawClass(), StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        ClassType byteBufferType = ClassType.constructUnsafe(ByteBuffer.class);
        JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferType.rawClass(), ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_UnknownClass() {
        ClassType unknownClassType = ClassType.constructUnsafe(String.class);
        JsonDeserializer<?> deserializer = JdkDeserializers.find(unknownClassType.rawClass(), "UnknownClass.class".getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        ClassType stringType = ClassType.constructUnsafe(String.class);
        JsonDeserializer<?> deserializer = JdkDeserializers.find(stringType.rawClass(), String.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof FromStringDeserializer);
    }
}