package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertEquals(new UUIDDeserializer(), deserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertEquals(new AtomicBooleanDeserializer(), deserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertEquals(new StackTraceElementDeserializer(), deserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertEquals(new ByteBufferDeserializer(), deserializer);
    }

    @Test
    public void testFind_OtherClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "java.lang.String");
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(Integer.class, "java.lang.Integer");
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(List.class, "java.util.List");
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String[].class, "java.lang.String[]");
        assertEquals(FromStringDeserializer.findDeserializer(String[].class), deserializer);
    }

    @Test
    public void testFind_Null() {
        assertNull(JdkDeserializers.find(null, null));
    }

    @Test
    public void testFind_UnknownClass() {
        assertNull(JdkDeserializers.find(Object.class, "java.util.Map"));
    }

    @Test
    public void testFromStringDeserializer_UUID() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<UUID> deserializer = new UUIDDeserializer();
        mapper.registerDeserializer(UUID.class, deserializer);

        String json = "\"123e4567-1234-4567-a123-456789abcdef\"";
        UUID uuid = mapper.readValue(json, UUID.class);
        assertEquals(uuid, UUID.fromString(json));
    }

    @Test
    public void testFromStringDeserializer_AtomicBoolean() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<AtomicBoolean> deserializer = new AtomicBooleanDeserializer();
        mapper.registerDeserializer(AtomicBoolean.class, deserializer);

        String jsonTrue = "true";
        AtomicBoolean atomicBooleanTrue = mapper.readValue(jsonTrue, AtomicBoolean.class);
        assertTrue(atomicBooleanTrue.get());

        String jsonFalse = "false";
        AtomicBoolean atomicBooleanFalse = mapper.readValue(jsonFalse, AtomicBoolean.class);
        assertFalse(atomicBooleanFalse.get());
    }

    @Test
    public void testFromStringDeserializer_StackTraceElement() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<StackTraceElement> deserializer = new StackTraceElementDeserializer();
        mapper.registerDeserializer(StackTraceElement.class, deserializer);

        String json = "java.lang.StackTr
 election[1]:myMethod(org.example.MyClass.java:123)";
        StackTraceElement stackTraceElement = mapper.readValue(json, StackTraceElement.class);
        assertEquals(StackTraceElement.class, stackTraceElement.getClass());
        assertEquals("myMethod", stackTraceElement.getMethodName());
        assertEquals("org.example.MyClass.java", stackTraceElement.getClassName());
        assertEquals(123, stackTraceElement.getLineNumber());
    }

    @Test
    public void testFromStringDeserializer_ByteBuffer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<ByteBuffer> deserializer = new ByteBufferDeserializer();
        mapper.registerDeserializer(ByteBuffer.class, deserializer);

        String json = "base64-encoded-byte-buffer";
        ByteBuffer byteBuffer = mapper.readValue(json, ByteBuffer.class);
        assertEquals(TextNode.class, byteBuffer.getClass().getComponentType());
        assertEquals(byteBuffer.capacity(), byteBuffer.limit());
        assertEquals(byteBuffer.limit(), byteBuffer.position());
    }
}