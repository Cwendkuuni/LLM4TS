package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class JdkDeserializersTest {
    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(UUIDDeserializer.class, deserializer.getClass());

        rawType = StackTraceElement.class;
        clsName = StackTraceElement.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());

        rawType = AtomicBoolean.class;
        clsName = AtomicBoolean.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());

        rawType = ByteBuffer.class;
        clsName = ByteBuffer.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());

        rawType = String.class;
        clsName = String.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);

        rawType = mock(Class.class);
        clsName = rawType.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}