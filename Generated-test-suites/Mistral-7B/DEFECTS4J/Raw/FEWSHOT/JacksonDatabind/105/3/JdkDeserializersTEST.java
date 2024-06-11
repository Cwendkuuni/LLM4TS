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
    public void testFindUuidDeserializer() {
        Class<UUID> uuidClass = UUID.class;
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, UUID.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer.deserialize(new StringBuffer("UUID.fromString(\"random-uuid\")"), new ObjectContext(new StandardSerializer(new TypeReference<UUID>() {})), null));
    }

    @Test
    public void testFindAtomicBooleanDeserializer() {
        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer.deserialize(new StringBuffer("new AtomicBoolean(\"true\")"), new ObjectContext(new StandardSerializer(new TypeReference<AtomicBoolean>() {})), null));
    }

    @Test
    public void testFindStackTraceElementDeserializer() {
        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertNotNull(deserializer.deserialize(new StringBuffer("new StackTraceElement(\"className\", \"methodName\", \"file:lineNumber\", \"className:lineNumber\")"), new ObjectContext(new StandardSerializer(new TypeReference<StackTraceElement>() {})), null));
    }

    @Test
    public void testFindByteBufferDeserializer() {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        try {
            ByteBuffer byteBuffer = deserializer.deserialize(new StringBuffer("new ByteBuffer(\"R\")"), new ObjectContext(new StandardSerializer(new TypeReference<ByteBuffer>() {})), null);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testFindNullDeserializer() {
        Class<String> stringClass = String.class;
        JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, String.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFindClassNotFoundDeserializer() {
        Class<Object> objectClass = Object.class;
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(objectClass, "com.nonexistent.package.NonexistentClass".getClass().getName());
        assertNull(deserializer);
    }
}