
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.deserGeneratedValueDeserializers.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.deserGeneratedValueDeserializers.ByteBufferDeserializer;
import com.fasterxml.jackson.databind.deser.std.deserGeneratedValueDeserializers.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.deserGeneratedValueDeserializers.UUIDDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_validClassName() {
        JsonDeserializer<?> deserializer;
        
        deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
        
        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
        
        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
        
        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
    }
    
    @Test
    public void testFind_invalidClassName() {
        JsonDeserializer<?> deserializer;
        
        deserializer = JdkDeserializers.find(UUID.class, "InvalidClassName");
        assertNull(deserializer);
        
        deserializer = JdkDeserializers.find(AtomicBoolean.class, "InvalidClassName");
        assertNull(deserializer);
        
        deserializer = JdkDeserializers.find(StackTraceElement.class, "InvalidClassName");
        assertNull(deserializer);
        
        deserializer = JdkDeserializers.find(ByteBuffer.class, "InvalidClassName");
        assertNull(deserializer);
    }
}
