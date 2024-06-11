package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use cases
        assertNotNull(JdkDeserializers.find(UUID.class, UUID.class.getName()));
        assertNotNull(JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()));
        assertNotNull(JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName()));
        assertNotNull(JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()));

        // Edge cases
        assertNull(JdkDeserializers.find(UUID.class, "random"));
        assertNull(JdkDeserializers.find(Void.class, "void"));

        // Exception handling
        assertNull(JdkDeserializers.find(null, ""));
    }
}