package com.fasterxml.jackson.databind.deser.std;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFindDeserializer_UUID() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertSame(UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializer_AtomicBoolean() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertSame(AtomicBooleanDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializer_StackTraceElement() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertSame(StackTraceElementDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializer_ByteBuffer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertSame(ByteBufferDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializer_Other() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, Object.class.getName());
        Assert.assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_FromStringDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> deserializer = JdkDeserializers.find(Integer.class, FromStringDeserializer.class.getName());
        Assert.assertNotNull(deserializer);
        Assert.assertSame(FromStringDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializer_Null() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(null, Object.class.getName());
        Assert.assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_UnknownClass() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(List.class, Object.class.getName());
        Assert.assertNull(deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializer_MismatchedType() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(Integer.class, UUID.class.getName());
        Assert.assertNull(deserializer);
    }

    @Test
    public void testClassNames() {
        Set<String> expected = new HashSet<>(Arrays.asList(
                UUID.class.getName(),
                AtomicBoolean.class.getName(),
                StackTraceElement.class.getName(),
                ByteBuffer.class.getName(),
                "java.util.List",
                "java.util.ArrayList",
                "com.fasterxml.jackson.databind.JsonDeserializer"
        ));
        Set<String> actual = JdkDeserializers._classNames;
        Assert.assertEquals(expected, actual);
    }
}