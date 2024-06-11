package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() throws IOException {
        Class<UUID> uuidClass = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFind_AtomicBoolean() throws IOException {
        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
    }

    @Test
    public void testFind_StackTraceElement() throws IOException {
        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
    }

    @Test
    public void testFind_ByteBuffer() throws IOException {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);
    }

    @Test
    public void testFind_UnknownClass() throws IOException {
        Class<Object> unknownClass = Object.class;
        String clsName = Integer.class.getName();
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(unknownClass, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() throws IOException {
        Class<String> stringClass = String.class;
        String clsName = String.class.getName();
        JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, clsName);
        assertInstanceOf(FromStringDeserializer.class, deserializer);
    }

    @Test
    public void testFind_Null() {
        Class<?> nullClass = null;
        String clsName = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(nullClass, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_SameInstance() throws IOException {
        Class<UUID> uuidClass = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<UUID> deserializer1 = JdkDeserializers.find(uuidClass, clsName);
        JsonDeserializer<UUID> deserializer2 = JdkDeserializers.find(uuidClass, clsName);
        assertSame(deserializer1, deserializer2);
    }

    @Test
    public void testFind_DifferentTypes() throws IOException {
        Class<UUID> uuidClass = UUID.class;
        Class<Integer> intClass = Integer.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<UUID> deserializer1 = JdkDeserializers.find(uuidClass, clsName);
        JsonDeserializer<Integer> deserializer2 = JdkDeserializers.find(intClass, clsName);
        assertNotSame(deserializer1, deserializer2);
    }

    @Test
    public void testFind_FromStringDeserializer_ForOtherType() throws IOException {
        Class<String> stringClass = String.class;
        Class<UUID> uuidClass = UUID.class;
        String clsName = String.class.getName();
        JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, clsName);
        assertInstanceOf(FromStringDeserializer.class, deserializer);
        assertFalse(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_NullClassName() {
        Class<?> nullClass = null;
        String clsName = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(nullClass, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_EmptyClassName() {
        Class<?> nullClass = null;
        String clsName = "";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(nullClass, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_ClassNameWithoutDot() {
        Class<?> nullClass = null;
        String clsName = "UUID";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(nullClass, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_ClassNameWithWrongPackage() throws IOException {
        Class<?> nullClass = null;
        String clsName = "other.package.UUID";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(nullClass, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_PrimitiveType() throws IOException {
        Class<Byte> byteClass = Byte.TYPE;
        String clsName = Byte.TYPE.getName();
        JsonDeserializer<Byte> deserializer = JdkDeserializers.find(byteClass, clsName);
        assertInstanceOf(FromStringDeserializer.class, deserializer);
    }

    @Test
    public void testFind_ArrayType() throws IOException {
        Class<byte[]> byteArrayClass = byte[].class;
        String clsName = byte[].class.getName();
        JsonDeserializer<byte[]> deserializer = JdkDeserializers.find(byteArrayClass, clsName);
        assertInstanceOf(FromArrayDeserializer.class, deserializer);
    }

    @Test
    public void testFind_CollectionType() throws IOException {
        TypeReference<List<Byte>> listType = new TypeReference<List<Byte>>() {};
        String clsName = List.class.getName() + "<B>";
        JsonDeserializer<List<Byte>> deserializer = JdkDeserializers.find(listType.getType(), clsName);
        assertInstanceOf(CollectionDeserializer.class, deserializer);
    }
}