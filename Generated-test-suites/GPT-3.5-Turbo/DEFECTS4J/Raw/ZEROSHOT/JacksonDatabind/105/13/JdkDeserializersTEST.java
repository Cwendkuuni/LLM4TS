package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test for UUID
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof UUIDDeserializer);

        // Test for AtomicBoolean
        rawType = AtomicBoolean.class;
        clsName = AtomicBoolean.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        // Test for StackTraceElement
        rawType = StackTraceElement.class;
        clsName = StackTraceElement.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        // Test for ByteBuffer
        rawType = ByteBuffer.class;
        clsName = ByteBuffer.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        // Test for other classes
        rawType = String.class;
        clsName = String.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(null, deserializer);
    }
}