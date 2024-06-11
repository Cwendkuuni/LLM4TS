package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test case for UUID
        assertNull(JdkDeserializers.find(UUID.class, "java.util.UUID"));
        
        // Test case for AtomicBoolean
        assertNull(JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean"));
        
        // Test case for StackTraceElement
        assertNull(JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement"));
        
        // Test case for ByteBuffer
        assertNull(JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer"));
    }
}