package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class JdkDeserializersTest {
    @Test
    public void testFind() {
        assertEquals(UUIDDeserializer.class, JdkDeserializers.find(UUID.class, UUID.class.getName()).getClass());
        assertEquals(StackTraceElementDeserializer.class, JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName()).getClass());
        assertEquals(AtomicBooleanDeserializer.class, JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()).getClass());
        assertEquals(ByteBufferDeserializer.class, JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()).getClass());
        assertNull(JdkDeserializers.find(ArrayList.class, ArrayList.class.getName()));
    }
}