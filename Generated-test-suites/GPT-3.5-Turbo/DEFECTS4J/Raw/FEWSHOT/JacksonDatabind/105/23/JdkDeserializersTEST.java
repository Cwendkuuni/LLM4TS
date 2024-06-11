package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdkDeserializersTest {
    
    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(String.class, "java.lang.String"));
        assertEquals( UUIDDeserializer.class, JdkDeserializers.find(UUID.class, "java.util.UUID").getClass());
        assertEquals( StackTraceElementDeserializer.class, JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement").getClass());
        assertEquals( AtomicBooleanDeserializer.class, JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean").getClass());
        assertEquals( ByteBufferDeserializer.class, JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer").getClass());
    }
}