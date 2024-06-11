package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test case for finding deserializer for UUID
        Class<?> rawTypeUUID = UUID.class;
        String clsNameUUID = UUID.class.getName();
        JsonDeserializer<?> deserializerUUID = JdkDeserializers.find(rawTypeUUID, clsNameUUID);
        assertTrue(deserializerUUID instanceof UUIDDeserializer);

        // Test case for finding deserializer for StackTraceElement
        Class<?> rawTypeStackTraceElement = StackTraceElement.class;
        String clsNameStackTraceElement = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializerStackTraceElement = JdkDeserializers.find(rawTypeStackTraceElement, clsNameStackTraceElement);
        assertTrue(deserializerStackTraceElement instanceof StackTraceElementDeserializer);

        // Test case for finding deserializer for AtomicBoolean
        Class<?> rawTypeAtomicBoolean = AtomicBoolean.class;
        String clsNameAtomicBoolean = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializerAtomicBoolean = JdkDeserializers.find(rawTypeAtomicBoolean, clsNameAtomicBoolean);
        assertTrue(deserializerAtomicBoolean instanceof AtomicBooleanDeserializer);

        // Test case for finding deserializer for ByteBuffer
        Class<?> rawTypeByteBuffer = ByteBuffer.class;
        String clsNameByteBuffer = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializerByteBuffer = JdkDeserializers.find(rawTypeByteBuffer, clsNameByteBuffer);
        assertTrue(deserializerByteBuffer instanceof ByteBufferDeserializer);
    }

}