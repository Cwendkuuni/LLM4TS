package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() throws IOException {
        Class<UUID> uuidClass = UUID.class;
        assertInstanceOf(UUIDDeserializer.class, JdkDeserializers.find(uuidClass, UUIDDeserializer.class.getName()));
    }

    @Test
    public void testFind_AtomicBoolean() throws IOException {
        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        assertInstanceOf(AtomicBooleanDeserializer.class, JdkDeserializers.find(atomicBooleanClass, AtomicBooleanDeserializer.class.getName()));
    }

    @Test
    public void testFind_StackTraceElement() throws IOException {
        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        assertInstanceOf(StackTraceElementDeserializer.class, JdkDeserializers.find(stackTraceElementClass, StackTraceElementDeserializer.class.getName()));
    }

    @Test
    public void testFind_ByteBuffer() throws IOException {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        assertInstanceOf(ByteBufferDeserializer.class, JdkDeserializers.find(byteBufferClass, ByteBufferDeserializer.class.getName()));
    }

    @Test
    public void testFind_UnknownClass() throws IOException {
        Class<String> stringClass = String.class;
        assertNull(JdkDeserializers.find(stringClass, Object.class.getName()));
    }

    @Test
    public void testFind_FromStringDeserializer() throws IOException {
        Class<Integer> intClass = Integer.class;
        assertInstanceOf(FromStringDeserializer.class, JdkDeserializers.find(intClass, FromStringDeserializer.class.getName()));
    }

    @Test
    public void testFind_NullInput() {
        assertNull(JdkDeserializers.find(null, null));
    }

    @Test
    public void testFind_EmptyClassName() {
        assertNull(JdkDeserializers.find(String.class, ""));
    }

    @Test
    public void testFind_EmptyClassName_NullType() {
        assertNull(JdkDeserializers.find(null, ""));
    }

    @Test
    public void testFind_UnknownClassName() {
        assertNull(JdkDeserializers.find(String.class, "UnknownClass"));
    }

    @Test
    public void testFind_NullTypeName() {
        assertNull(JdkDeserializers.find(String.class, null));
    }

    @Test
    public void testFind_ClassAlreadyRegistered() throws IOException {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, ByteBufferDeserializer.class.getName());
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);

        JsonDeserializer<ByteBuffer> deserializer2 = JdkDeserializers.find(byteBufferClass, ByteBufferDeserializer.class.getName());
        assertInstanceOf(ByteBufferDeserializer.class, deserializer2);

        assertSame(deserializer, deserializer2);
    }

    @Test
    public void testFind_ClassNotRegistered() throws IOException {
        Class<List<String>> listStringClass = new TypeReference<List<String>>() {}.getType();
        assertNull(JdkDeserializers.find(listStringClass, Object.class.getName()));
    }

    @Test
    public void testFind_ClassWithCustomDeserializer() throws IOException {
        Class<JsonNode> jsonNodeClass = JsonNode.class;
        StdSerializer<JsonNode> stdSerializer = new StdSerializer<JsonNode>(JsonNode.class) {};
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSerializer(JsonNode.class, stdSerializer);

        JsonDeserializer<JsonNode> deserializer = JdkDeserializers.find(jsonNodeClass, JsonNode.class.getName());
        assertInstanceOf(stdSerializer.getClass(), deserializer.getClass());
    }
}