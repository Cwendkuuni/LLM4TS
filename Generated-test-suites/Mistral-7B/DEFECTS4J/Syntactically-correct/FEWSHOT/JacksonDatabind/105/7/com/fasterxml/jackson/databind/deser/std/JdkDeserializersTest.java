package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);

        Object readValue = mapper.readValue("\"123e4567-123e-4567-a123-123456789abc\"", new TypeReference<UUID>() {});
        UUID uuid = deserializer.deserialize(mapper.readTree("{\"value\": \"123e4567-123e-4567-a123-123456789abc\"}"), UUID.class);
        assertEquals("123e4567-123e-4567-a123-123456789abc", readValue.toString());
        assertEquals("123e4567-123e-4567-a123-123456789abc", uuid.toString());
    }

    @Test
    public void testFind_AtomicBoolean() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);

        Object readValue = mapper.readValue("true", new TypeReference<AtomicBoolean>() {});
        AtomicBoolean atomicBoolean = deserializer.deserialize(mapper.readTree("{\"value\": true}"), AtomicBoolean.class);
        assertTrue(atomicBoolean.get());
        assertEquals(true, readValue.toString());
    }

    @Test
    public void testFind_StackTraceElement() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);

        Object readValue = mapper.readValue("[{\"fileName\": \"test.java\", \"lineNumber\": 42, \"columnNumber\": 5, \"methodName\": \"testMethod\", \"className\": \"TestClass\", \"nativeMethod\": false}]", new TypeReference<List<StackTraceElement>>() {});
        List<StackTraceElement> stackTraceElements = deserializer.deserialize(mapper.readTree("{\"elements\": [{\"fileName\": \"test.java\", \"lineNumber\": 42, \"columnNumber\": 5, \"methodName\": \"testMethod\", \"className\": \"TestClass\", \"nativeMethod\": false}]}"), new TypeReference<List<StackTraceElement>>() {});
        assertEquals(1, stackTraceElements.size());
        StackTraceElement stackTraceElement = stackTraceElements.get(0);
        assertEquals("test.java", stackTraceElement.getFileName());
        assertEquals(42, stackTraceElement.getLineNumber());
        assertEquals("testMethod", stackTraceElement.getMethodName());
        assertEquals("TestClass", stackTraceElement.getClassName());
        assertFalse(stackTraceElement.isNativeMethod());
    }

    @Test
    public void testFind_ByteBuffer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);

        byte[] data = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04};
        ByteBuffer byteBuffer = mapper.readValue("\"0001020304\"", ByteBuffer.class);
        ByteBuffer deserializedByteBuffer = deserializer.deserialize(mapper.readTree("{\"baseOffset\": 0, \"limit\": 5, \"capacity\": 5, \"array\": [0, 1, 2, 3, 4]}"), ByteBuffer.class);

        assertEquals(data, byteBuffer.array());
        assertEquals(deserializedByteBuffer, deserializedByteBuffer.duplicate());
    }

    @Test
    public void testFind_UnknownClass() {
        ObjectMapper mapper = new ObjectMapper();

        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "UnknownClass.class
name".getClass().getName());
        assertNull(deserializer);
    }
}