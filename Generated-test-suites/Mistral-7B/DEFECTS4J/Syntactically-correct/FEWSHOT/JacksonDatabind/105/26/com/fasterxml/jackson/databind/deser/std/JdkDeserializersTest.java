package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> uuidClass = UUID.class;
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, UUID.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_UnknownClass() {
        Class<String> stringClass = String.class;
        JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, Object.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        Class<Number> numberClass = Number.class;
        JsonDeserializer<Number> deserializer = JdkDeserializers.find(numberClass, FromStringDeserializer.NUMBER_CLASS_NAME);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof FromStringDeserializer);
    }

    @Test
    public void testFind_NullInput() {
        Class<?> rawType = null;
        String clsName = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_EmptyClassName() {
        Class<?> rawType = Object.class;
        String clsName = "";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_EmptyClassNameWithInvalidClassPrefix() {
        Class<?> rawType = Object.class;
        String clsName = "invalidPrefix" + StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_ClassWithInvalidSuffix() {
        Class<List<String>> listStringClass = new TypeReference<List<String>>() {}.getType();
        String clsName = UUID.class.getName() + "List";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(listStringClass, clsName);
        assertNull(deserializer);
    }
}