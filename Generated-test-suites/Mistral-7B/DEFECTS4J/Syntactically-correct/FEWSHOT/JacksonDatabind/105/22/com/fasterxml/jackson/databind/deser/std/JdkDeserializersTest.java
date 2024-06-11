package com.fasterxml.jackson.databind.deser.std;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;

import static org.junit.Assert.*;

public class JdkDeserializersTest {

    private static Set<String> _classNames;

    @BeforeClass
    public static void initialize() {
        _classNames = new HashSet<String>();
        Class<?>[] types = new Class<?>[] {
                UUID.class,
                AtomicBoolean.class,
                StackTraceElement.class,
                ByteBuffer.class
        };
        for (Class<?> cls : types) { _classNames.add(cls.getName()); }
        for (Class<?> cls : FromStringDeserializer.types()) { _classNames.add(cls.getName()); }
    }

    @Test
    public void testFind_UUID_Class() {
        assertEquals(UUIDDeserializer.class, JdkDeserializers.find(UUID.class, UUID.class.getName()));
    }

    @Test
    public void testFind_UUID_String() {
        assertEquals(UUIDDeserializer.class, JdkDeserializers.find(UUID.class, "java.util.UUID"));
    }

    @Test
    public void testFind_AtomicBoolean_Class() {
        assertEquals(AtomicBooleanDeserializer.class, JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()));
    }

    @Test
    public void testFind_AtomicBoolean_String() {
        assertEquals(AtomicBooleanDeserializer.class, JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean"));
    }

    @Test
    public void testFind_StackTraceElement_Class() {
        assertEquals(StackTraceElementDeserializer.class, JdkDeserializers.find(StackTraceElement[].class, StackTraceElement.class.getName()));
    }

    @Test
    public void testFind_StackTraceElement_String() {
        assertEquals(StackTraceElementDeserializer.class, JdkDeserializers.find(StackTraceElement[].class, "java.lang.StackTraceElement"));
    }

    @Test
    public void testFind_ByteBuffer_Class() {
        assertEquals(ByteBufferDeserializer.class, JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()));
    }

    @Test
    public void testFind_ByteBuffer_String() {
        assertEquals(ByteBufferDeserializer.class, JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer"));
    }

    @Test
    public void testFind_Null_Class() {
        assertNull(JdkDeserializers.find(null, "ClassName"));
    }

    @Test
    public void testFind_Null_String() {
        assertNull(JdkDeserializers.find(Object.class, null));
    }

    @Test
    public void testFind_Unknown_Class() {
        assertNull(JdkDeserializers.find(String.class, "java.lang.Integer"));
    }

    @Test
    public void testFind_Unknown_String() {
        assertNull(JdkDeserializers.find(Object.class, "java.lang.UnknownClass"));
    }

    @Test
    public void testFromStringDeserializer_Found() {
        Class<?> rawType = FromStringDeserializer.findDeserializer(UUID.class).getType();
        assertEquals(UUID.class, rawType);
    }

    @Test
    public void testFromStringDeserializer_Null() {
        assertNull(JdkDeserializers.find(Object.class, "java.lang.Object"));
    }

    @Test
    public void testFromStringDeserializer_WrongClass() {
        assertNull(JdkDeserializers.find(int.class, "java.util.UUID"));
    }
}