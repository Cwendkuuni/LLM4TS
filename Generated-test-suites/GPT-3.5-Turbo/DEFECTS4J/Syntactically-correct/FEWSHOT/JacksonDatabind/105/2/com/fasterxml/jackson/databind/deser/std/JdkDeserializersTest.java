package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_withValidClassNames_shouldReturnDeserializer() {
        assertNull(JdkDeserializers.find(Integer.class, "java.lang.Integer"));
        assertNull(JdkDeserializers.find(Long.class, "java.lang.Long"));
        assertNull(JdkDeserializers.find(Float.class, "java.lang.Float"));
        assertNull(JdkDeserializers.find(Double.class, "java.lang.Double"));
        assertNull(JdkDeserializers.find(Byte.class, "java.lang.Byte"));
        assertNull(JdkDeserializers.find(Short.class, "java.lang.Short"));
        assertNull(JdkDeserializers.find(Character.class, "java.lang.Character"));
        assertNull(JdkDeserializers.find(Boolean.class, "java.lang.Boolean"));
        assertNull(JdkDeserializers.find(String.class, "java.lang.String"));
        assertNull(JdkDeserializers.find(Object.class, "java.lang.Object"));
        
        assertEquals(UUIDDeserializer.class, JdkDeserializers.find(UUID.class, "java.util.UUID").getClass());
        assertEquals(AtomicBooleanDeserializer.class, JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean").getClass());
        assertEquals(StackTraceElementDeserializer.class, JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement").getClass());
        assertEquals(ByteBufferDeserializer.class, JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer").getClass());
    }
    
    @Test
    public void testFind_withInvalidClassNames_shouldReturnNull() {
        assertNull(JdkDeserializers.find(Integer.class, "java.lang.Integer1"));
        assertNull(JdkDeserializers.find(Long.class, "java.lang.Long1"));
        assertNull(JdkDeserializers.find(Float.class, "java.lang.Float1"));
        assertNull(JdkDeserializers.find(Double.class, "java.lang.Double1"));
        assertNull(JdkDeserializers.find(Byte.class, "java.lang.Byte1"));
        assertNull(JdkDeserializers.find(Short.class, "java.lang.Short1"));
        assertNull(JdkDeserializers.find(Character.class, "java.lang.Character1"));
        assertNull(JdkDeserializers.find(Boolean.class, "java.lang.Boolean1"));
        assertNull(JdkDeserializers.find(String.class, "java.lang.String1"));
        assertNull(JdkDeserializers.find(Object.class, "java.lang.Object1"));
    }
    
    @Test
    public void testFind_withNullClassName_shouldReturnNull() {
        assertNull(JdkDeserializers.find(Integer.class, null));
        assertNull(JdkDeserializers.find(Long.class, null));
        assertNull(JdkDeserializers.find(Float.class, null));
        assertNull(JdkDeserializers.find(Double.class, null));
        assertNull(JdkDeserializers.find(Byte.class, null));
        assertNull(JdkDeserializers.find(Short.class, null));
        assertNull(JdkDeserializers.find(Character.class, null));
        assertNull(JdkDeserializers.find(Boolean.class, null));
        assertNull(JdkDeserializers.find(String.class, null));
        assertNull(JdkDeserializers.find(Object.class, null));
    }
}