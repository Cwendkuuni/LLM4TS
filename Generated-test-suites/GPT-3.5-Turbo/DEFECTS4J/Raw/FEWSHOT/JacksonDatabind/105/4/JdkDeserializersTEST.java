package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test UUID
        assertNull(JdkDeserializers.find(UUID.class, "java.util.UUID"));
        
        // Test AtomicBoolean
        assertNull(JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean"));
        
        // Test StackTraceElement
        assertNull(JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement"));
        
        // Test ByteBuffer
        assertNull(JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer"));
        
        // Test other classes
        assertNull(JdkDeserializers.find(ArrayList.class, "java.util.ArrayList"));
        assertNull(JdkDeserializers.find(HashMap.class, "java.util.HashMap"));
    }
}