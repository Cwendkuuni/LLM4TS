package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;

public class JdkDeserializersTest {
    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(String.class, "java.lang.String"));
        assertNull(JdkDeserializers.find(Integer.class, "java.util.Date"));
        
        assertEquals(new UUIDDeserializer().getClass(), JdkDeserializers.find(UUID.class, "java.util.UUID").getClass());
        assertEquals(new StackTraceElementDeserializer().getClass(), JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement").getClass());
        assertEquals(new AtomicBooleanDeserializer().getClass(), JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean").getClass());
        assertEquals(new ByteBufferDeserializer().getClass(), JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer").getClass());
    }
}