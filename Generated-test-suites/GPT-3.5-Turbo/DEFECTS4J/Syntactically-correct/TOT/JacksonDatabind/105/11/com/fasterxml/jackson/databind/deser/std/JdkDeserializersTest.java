
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidClassName_andUUID_shouldReturnUUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String className = UUID.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withValidClassName_andStackTraceElement_shouldReturnStackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String className = StackTraceElement.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_withValidClassName_andAtomicBoolean_shouldReturnAtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String className = AtomicBoolean.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_withValidClassName_andByteBuffer_shouldReturnByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String className = ByteBuffer.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_withInvalidClassName_shouldReturnNull() {
        Class<?> rawType = UUID.class;
        String className = "InvalidClassName";

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        assertNull(deserializer);
    }

    @Test
    public void testFind_withInvalidRawType_shouldReturnNull() {
        Class<?> rawType = Calendar.class;
        String className = Calendar.class.getName();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);

        assertNull(deserializer);
    }
}
