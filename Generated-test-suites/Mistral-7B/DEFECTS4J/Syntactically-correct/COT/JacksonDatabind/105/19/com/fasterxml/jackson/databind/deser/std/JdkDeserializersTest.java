package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() throws IOException, JsonProcessingException {
        Class<UUID> uuidClass = UUID.class;
        String uuidClassName = uuidClass.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, uuidClassName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(UUIDDeserializer.class, deserializer.getClass());

        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        String stackTraceElementClassName = stackTraceElementClass.getName();
        JsonDeserializer<?> stackTraceElementDeserializer = JdkDeserializers.find(stackTraceElementClass, stackTraceElementClassName);
        Assert.assertNotNull(stackTraceElementDeserializer);
        Assert.assertEquals(StackTraceElementDeserializer.class, stackTraceElementDeserializer.getClass());

        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        String atomicBooleanClassName = atomicBooleanClass.getName();
        JsonDeserializer<?> atomicBooleanDeserializer = JdkDeserializers.find(atomicBooleanClass, atomicBooleanClassName);
        Assert.assertNotNull(atomicBooleanDeserializer);
        Assert.assertEquals(AtomicBooleanDeserializer.class, atomicBooleanDeserializer.getClass());

        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        String byteBufferClassName = byteBufferClass.getName();
        JsonDeserializer<?> byteBufferDeserializer = JdkDeserializers.find(byteBufferClass, byteBufferClassName);
        Assert.assertNotNull(byteBufferDeserializer);
        Assert.assertEquals(ByteBufferDeserializer.class, byteBufferDeserializer.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testFindNullRawType() {
        JdkDeserializers.find(null, "UUID.class");
    }

    @Test(expected = NullPointerException.class)
    public void testFindNullClassName() {
        Class<UUID> uuidClass = UUID.class;
        JdkDeserializers.find(uuidClass, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindUnknownClassName() {
        Class<UUID> uuidClass = UUID.class;
        String unknownClassName = "com.unknown.UnknownClass";
        JdkDeserializers.find(uuidClass, unknownClassName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindWrongClassNameType() {
        Class<Integer> integerClass = Integer.class;
        String uuidClassName = UUID.class.getName();
        JdkDeserializers.find(integerClass, uuidClassName);
    }

    @Test
    public void testFindFromStringDeserializer() throws IOException, JsonProcessingException {
        Class<String> stringClass = String.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, String.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(FromStringDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerNull() {
        Class<UUID> uuidClass = UUID.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, null);
        Assert.assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerUuid() throws IOException, JsonProcessingException {
        Class<UUID> uuidClass = UUID.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, null);
        Assert.assertNull(deserializer);
        deserializer = JdkDeserializers.find(uuidClass, UUID.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerAtomicBoolean() throws IOException, JsonProcessingException {
        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, null);
        Assert.assertNull(deserializer);
        deserializer = JdkDeserializers.find(atomicBooleanClass, AtomicBoolean.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerByteBuffer() throws IOException, JsonProcessingException {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, null);
        Assert.assertNull(deserializer);
        deserializer = JdkDeserializers.find(byteBufferClass, ByteBuffer.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerStackTraceElement() throws IOException, JsonProcessingException {
        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(stackTraceElementClass, null);
        Assert.assertNull(deserializer);
        deserializer = JdkDeserializers.find(stackTraceElementClass, StackTraceElement.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerFromString() throws IOException, JsonProcessingException {
        Class<String> stringClass = String.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, String.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(FromStringDeserializer.class, deserializer.getClass());
    }
}