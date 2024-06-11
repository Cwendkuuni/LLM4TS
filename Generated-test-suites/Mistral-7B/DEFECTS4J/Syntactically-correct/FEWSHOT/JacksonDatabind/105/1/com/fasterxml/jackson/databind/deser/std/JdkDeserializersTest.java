package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        String clsName = rawType.getName();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        String clsName = rawType.getName();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        String clsName = rawType.getName();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);
    }

    @Test
    public void testFind_Null() {
        Class<String> rawType = String.class;
        String clsName = null;
        JsonDeserializer<String> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_DifferentClass() {
        Class<Integer> rawType = Integer.class;
        String clsName = Double.class.getName();
        JsonDeserializer<Integer> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        Class<String> rawType = String.class;
        String clsName = String.class.getName();
        JsonDeserializer<String> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(FromStringDeserializer.class, deserializer);
    }

    @Test
    public void testFind_UUID_WithCustomName() {
        Class<UUID> rawType = UUID.class;
        String clsName = "CustomUUIDClass";
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_UnknownClass() {
        Class<Object> rawType = Object.class;
        String clsName = "UnknownClass";
        JsonDeserializer<Object> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_WithTypeReference() {
        TypeReference<UUID> typeRef = new TypeReference<UUID>() {
        };
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(typeRef.getType(), UUID.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFind_EmptyList() {
        Class<List<String>> rawType = new TypeReference<List<String>>() {}.getType();
        JsonDeserializer<List<String>> deserializer = JdkDeserializers.find(rawType, Collections.emptyList().getClass().getName());
        assertNotNull(deserializer);
        assertInstanceOf(ListDeserializer.class, deserializer);
    }

    @Test
    public void testFind_EmptyMap() {
        Class<Map<String, String>> rawType = new TypeReference<Map<String, String>>() {}.getType();
        JsonDeserializer<Map<String, String>> deserializer = JdkDeserializers.find(rawType, Collections.emptyMap().getClass().getName());
        assertNotNull(deserializer);
        assertInstanceOf(MapDeserializer.class, deserializer);
    }

    @Test
    public void testFind_CustomCollection() {
        Class<LinkedHashSet<String>> rawType = LinkedHashSet.class;
        JsonDeserializer<LinkedHashSet<String>> deserializer = JdkDeserializers.find(rawType, LinkedHashSet.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(CollectionDeserializer.class, deserializer);
    }
}