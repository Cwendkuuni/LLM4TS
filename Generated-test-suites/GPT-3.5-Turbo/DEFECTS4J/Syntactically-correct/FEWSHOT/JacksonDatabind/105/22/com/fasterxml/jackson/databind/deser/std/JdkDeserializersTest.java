package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.HashSet;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(String.class, "java.lang.String"));
        assertNull(JdkDeserializers.find(Integer.class, "java.lang.Integer"));
        assertNull(JdkDeserializers.find(Double.class, "java.lang.Double"));
        
        JsonDeserializer<?> d = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertEquals(UUIDDeserializer.class, d.getClass());
        
        d = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertEquals(AtomicBooleanDeserializer.class, d.getClass());
        
        d = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertEquals(StackTraceElementDeserializer.class, d.getClass());
        
        d = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertEquals(ByteBufferDeserializer.class, d.getClass());
    }
}