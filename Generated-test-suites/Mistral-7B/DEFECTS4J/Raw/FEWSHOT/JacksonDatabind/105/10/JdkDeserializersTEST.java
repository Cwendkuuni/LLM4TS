package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(rawType, clsName);
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_Other() {
        Class<String> rawType = String.class;
        String clsName = Object.class.getName();
        JsonDeserializer<String> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        Class<Integer> rawType = Integer.class;
        String clsName = Integer.class.getName();
        JsonDeserializer<Integer> deserializer = FromStringDeserializer.findDeserializer(rawType);
        assertEquals(NumberDeserializers.IntegerDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_Null() {
        Class<Object> rawType = Object.class;
        String clsName = null;
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_UnknownClass() {
        Class<Object> rawType = Object.class;
        String clsName = "com.example.UnknownClass".getName();
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void test_ClassNames_Contains() {
        Class<?>[] types = new Class<?>[] {
                UUID.class,
                AtomicBoolean.class,
                StackTraceElement.class,
                ByteBuffer.class
        };
        for (Class<?> cls : types) {
            String clsName = cls.getName();
            assertTrue(JdkDeserializers._classNames.contains(clsName));
        }
    }

    @Test
    public void test_ClassNames_FromStringDeserializer() {
        for (Class<?> cls : FromStringDeserializer.types()) {
            String clsName = cls.getName();
            assertTrue(JdkDeserializers._classNames.contains(clsName));
        }
    }

    @Test
    public void test_ClassNames_EmptyString() {
        String emptyString = "";
        assertFalse(JdkDeserializers._classNames.contains(emptyString));
    }

    @Test
    public void testFromStringDeserializer_Deserialize_UUID() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"01234567-89ab-cdef-1234-567890abcdef\"";
        UUID uuid = mapper.readValue(json, UUID.class);
        assertEquals(UUID.fromString("01234567-89ab-cdef-1234-567890abcdef"), uuid);
    }

    @Test
    public void testFromStringDeserializer_Deserialize_AtomicBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonTrue = "true";
        String jsonFalse = "false";
        AtomicBoolean atomicBooleanTrue = mapper.readValue(jsonTrue, AtomicBoolean.class);
        AtomicBoolean atomicBooleanFalse = mapper.readValue(jsonFalse, AtomicBoolean.class);
        assertTrue(atomicBooleanTrue.get());
        assertFalse(atomicBooleanFalse.get());
    }

    @Test
    public void testFromStringDeserializer_Deserialize_StackTraceElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"MyClass[123, /path/to/file:32]\":25";
        StackTraceElement stackTraceElement = mapper.readValue(json, StackTraceElement.class);
        assertEquals("MyClass", stackTraceElement.getClassName());
        assertEquals(123, stackTraceElement.getLineNumber());
        assertEquals("/path/to/file", stackTraceElement.getFileName());
        assertEquals(25, stackTraceElement.getMethodName());
    }

    @Test
    public void testFromStringDeserializer_Deserialize_ByteBuffer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "base64:VG8gZXJyIGlzIGh1bWFuLCBmaWxlIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICAgIHJlc3BvbnNlIGlmIHNlcnZpY2Uu";
        ByteBuffer byteBuffer = mapper.readValue(json, ByteBuffer.class);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(new byte[]{(byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07, (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c, (byte)0x0d, (byte)0x0e, (byte)0x0f, (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13, (byte)0x14, (byte)0x15});
        assertEquals(expectedByteBuffer, byteBuffer);
    }
}