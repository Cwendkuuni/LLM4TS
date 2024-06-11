package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test when rawType is UUID
        JsonDeserializer<?> deserializerUUID = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertTrue(deserializerUUID instanceof UUIDDeserializer);

        // Test when rawType is StackTraceElement
        JsonDeserializer<?> deserializerStackTraceElement = JdkDeserializers.find(StackTraceElement.class,
                StackTraceElement.class.getName());
        assertTrue(deserializerStackTraceElement instanceof StackTraceElementDeserializer);

        // Test when rawType is AtomicBoolean
        JsonDeserializer<?> deserializerAtomicBoolean = JdkDeserializers.find(AtomicBoolean.class,
                AtomicBoolean.class.getName());
        assertTrue(deserializerAtomicBoolean instanceof AtomicBooleanDeserializer);

        // Test when rawType is ByteBuffer
        JsonDeserializer<?> deserializerByteBuffer = JdkDeserializers.find(ByteBuffer.class,
                ByteBuffer.class.getName());
        assertTrue(deserializerByteBuffer instanceof ByteBufferDeserializer);

        // Test when rawType is not a JDK type
        JsonDeserializer<?> deserializerNotJDKType = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializerNotJDKType);
    }
}