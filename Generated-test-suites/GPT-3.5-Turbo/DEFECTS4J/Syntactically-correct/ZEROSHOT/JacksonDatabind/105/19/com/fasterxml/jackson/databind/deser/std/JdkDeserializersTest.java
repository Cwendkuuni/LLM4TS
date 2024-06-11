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
        JsonDeserializer<?> deserializer;

        // Test rawType = UUID.class
        deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertTrue(deserializer instanceof UUIDDeserializer);

        // Test rawType = AtomicBoolean.class
        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        // Test rawType = StackTraceElement.class
        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        // Test rawType = ByteBuffer.class
        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        // Test invalid rawType
        deserializer = JdkDeserializers.find(Integer.class, Integer.class.getName());
        assertNull(deserializer);
    }

    // Other test methods for FromStringDeserializer types can be added here

}