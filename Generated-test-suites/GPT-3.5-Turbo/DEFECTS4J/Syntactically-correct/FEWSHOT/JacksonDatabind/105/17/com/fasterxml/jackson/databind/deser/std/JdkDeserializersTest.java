
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_ClassNotInList() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(Integer.class, Integer.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_NameNotInList() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, Object.class.getName());
        assertNull(deserializer);
    }
}
