package com.fasterxml.jackson.databind.deser.std;

import org.junit.*;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof UUIDDeserializer);

        // Edge cases
        rawType = StackTraceElement.class;
        clsName = StackTraceElement.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        rawType = AtomicBoolean.class;
        clsName = AtomicBoolean.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        rawType = ByteBuffer.class;
        clsName = ByteBuffer.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        // Error scenario
        rawType = Integer.class;
        clsName = Integer.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

}