
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_WithValidClassName_ReturnsDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof FromStringDeserializer);
    }

    @Test
    public void testFind_WithInvalidClassName_ReturnsNull() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "invalid.class.name");
        assertNull(deserializer);
    }

    @Test
    public void testFind_WithValidClassNameAndUUID_ReturnsUUIDDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_WithValidClassNameAndStackTraceElement_ReturnsStackTraceElementDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_WithValidClassNameAndAtomicBoolean_ReturnsAtomicBooleanDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_WithValidClassNameAndByteBuffer_ReturnsByteBufferDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
}
