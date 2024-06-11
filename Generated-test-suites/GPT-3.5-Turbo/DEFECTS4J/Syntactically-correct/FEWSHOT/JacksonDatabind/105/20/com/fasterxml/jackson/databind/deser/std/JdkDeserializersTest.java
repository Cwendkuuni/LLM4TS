package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind_uuidClass_nameInClassNames_returnUuidDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_atomicBooleanClass_nameInClassNames_returnAtomicBooleanDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_stackTraceElementClass_nameInClassNames_returnStackTraceElementDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_byteBufferClass_nameInClassNames_returnByteBufferDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_unsupportedClass_returnNull() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ArrayList.class, "java.util.ArrayList");
        Assert.assertNull(deserializer);
    }
}