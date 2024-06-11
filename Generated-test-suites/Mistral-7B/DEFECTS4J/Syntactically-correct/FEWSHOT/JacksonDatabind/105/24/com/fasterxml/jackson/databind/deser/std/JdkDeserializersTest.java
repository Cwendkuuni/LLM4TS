package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<UUID> rawType = UUID.class;
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(rawType, UUID.class.getName());
        assertNotNull(deserializer);
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(rawType, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(rawType, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(rawType, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_OtherClass() {
        Class<String> rawType = String.class;
        JsonDeserializer<String> deserializer = JdkDeserializers.find(rawType, Object.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_Null() {
        Class<?> rawType = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, UUID.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_WrongClassName() {
        Class<Integer> rawType = Integer.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, UUID.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_UUID() {
        Class<UUID> rawType = UUID.class;
        JsonDeserializer<UUID> deserializer = FromStringDeserializer.findDeserializer(rawType);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_AtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        JsonDeserializer<AtomicBoolean> deserializer = FromStringDeserializer.findDeserializer(rawType);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_UUID_Present() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<UUID> deserializer = mapper.findValueSerializer(UUID.class);
        assertNotNull(deserializer);
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializer_ByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(rawType, ByteBuffer.class.getName());
        JsonDeserializer<ByteBuffer> deserializerFromString = FromStringDeserializer.findDeserializer(rawType);
        assertNotNull(deserializer);
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
        assertNull(deserializerFromString);
    }

    @Test
    public void testFindDeserializer_NotPresent() {
        Class<String> rawType = String.class;
        JsonDeserializer<String> deserializer = JdkDeserializers.find(rawType, String.class.getName());
        JsonDeserializer<String> deserializerFromString = FromStringDeserializer.findDeserializer(rawType);
        assertNull(deserializer);
        assertNotNull(deserializerFromString);
    }

    @Test
    public void testClassNames() {
        Set<String> classNames = JdkDeserializers._classNames;
        assertTrue(classNames.containsAll(Arrays.asList(
                UUID.class.getName(),
                AtomicBoolean.class.getName(),
                StackTraceElement.class.getName(),
                ByteBuffer.class.getName()
        )));
    }

    @Test
    public void testFromStringDeserializerTypes() {
        List<Class<?>> types = Collections.list(FromStringDeserializer.types());
        assertTrue(types.contains(UUID.class));
        assertTrue(types.contains(AtomicBoolean.class));
        // other tests for FromStringDeserializer.types() can be added here
    }
}