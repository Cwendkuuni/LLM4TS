
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.JsonDeserializer;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test case 1: Test deserialization of UUID type
        JsonDeserializer<?> deserializer1 = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer1);
        assertTrue(deserializer1 instanceof UUIDDeserializer);

        // Test case 2: Test deserialization of StackTraceElement type
        JsonDeserializer<?> deserializer2 =
            JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer2);
        assertTrue(deserializer2 instanceof StackTraceElementDeserializer);

        // Test case 3: Test deserialization of AtomicBoolean type
        JsonDeserializer<?> deserializer3 = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer3);
        assertTrue(deserializer3 instanceof AtomicBooleanDeserializer);

        // Test case 4: Test deserialization of ByteBuffer type
        JsonDeserializer<?> deserializer4 = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer4);
        assertTrue(deserializer4 instanceof ByteBufferDeserializer);

        // Test case 5: Test deserialization of unsupported type
        JsonDeserializer<?> deserializer5 = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializer5);
    }
}
