package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {
    
    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertTrue(deserializer instanceof UUIDDeserializer);
        
        deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
        
        deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
        
        deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertTrue(deserializer instanceof ByteBufferDeserializer);
        
        deserializer = JdkDeserializers.find(ArrayList.class, "java.util.ArrayList");
        assertNull(deserializer);
    }
}