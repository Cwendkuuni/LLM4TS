package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> uuidClass = UUID.class;
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, UUID.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_UnknownClass() {
        Class<String> stringClass = String.class;
        JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, Object.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        Class<Integer> integerClass = Integer.class;
        JsonDeserializer<Integer> deserializer = JdkDeserializers.find(integerClass, FromStringDeserializer.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer instanceof FromStringDeserializer);
    }

    @Test
    public void testFind_Null() {
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(null, null);
        assertNull(deserializer);
    }

    @Test
    public void testFind_EmptyClassName() {
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(Object.class, "");
        assertNull(deserializer);
    }

    @Test
    public void testFind_EmptyTypeName() {
        Class<Object> objectClass = Object.class;
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(objectClass, "");
        assertNull(deserializer);
    }

    @Test
    public void testFind_UnknownTypeName() {
        Class<Object> objectClass = Object.class;
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(objectClass, "UnknownType.class");
        assertNull(deserializer);
    }

    @Test
    public void testClassNames_Contains() {
        Set<String> classNames = new HashSet<String>(JdkDeserializers._classNames);
        assert(classNames.contains(UUID.class.getName()));
        assert(classNames.contains(AtomicBoolean.class.getName()));
        assert(classNames.contains(StackTraceElement.class.getName()));
        assert(classNames.contains(ByteBuffer.class.getName()));
        assert(classNames.contains(String.class.getName())); // FromStringDeserializer
    }

    @Test
    public void testClassNames_Size() {
        Set<String> classNames = new HashSet<String>(JdkDeserializers._classNames);
        int size = classNames.size();
        assert(size == 6);
    }
}