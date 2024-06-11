package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.AtomicBoolean;
import java.util.StacktraceElement;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdkDeserializersTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testFind() throws Exception {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        JsonDeserializer<?> expectedDeserializer = new UUIDDeserializer();
        assertEquals(expectedDeserializer.getClass(), deserializer.getClass());

        rawType = StackTraceElement.class;
        clsName = rawType.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        JsonDeserializer<?> expectedDeserializer1 = new StackTraceElementDeserializer();
        assertEquals(expectedDeserializer1.getClass(), deserializer.getClass());

        rawType = AtomicBoolean.class;
        clsName = rawType.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        JsonDeserializer<?> expectedDeserializer2 = new AtomicBooleanDeserializer();
        assertEquals(expectedDeserializer2.getClass(), deserializer.getClass());

        rawType = ByteBuffer.class;
        clsName = rawType.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        JsonDeserializer<?> expectedDeserializer3 = new ByteBufferDeserializer();
        assertEquals(expectedDeserializer3.getClass(), deserializer.getClass());

        rawType = String.class; // Non-supported class
        clsName = rawType.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindWithNullRawType() {
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(null, clsName);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindWithNullClsName() {
        Class<?> rawType = UUID.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindWithInvalidClsName() {
        Class<?> rawType = UUID.class;
        String clsName = "InvalidClassName";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
    }

    @org.junit.Test
    public void testFindWithUnknownClass() throws Exception {
        Class<?> rawType = new Object().getClass();
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testFromStringDeserializerFindDeserializer() throws Exception {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testFindWhenClassNotFoundInHashSet() throws Exception {
        Class<?> rawType = BigDecimal.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        fail("Expected JsonMappingException");
    }

    @org.junit.Test
    public void testEdgeCase_FindDeserializerForNullContext() throws Exception {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        DeserializationContext context = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer2 = mapper.findValueDeserializer(context, UUID.class);
        assertEquals(deserializer.getClass(), deserializer2.getClass());
    }

    @org.junit.Test
    public void testEdgeCase_FindDeserializerForEmptyContext() throws Exception {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer2 = mapper.findValueDeserializer(context, UUID.class);
        assertEquals(deserializer.getClass(), deserializer2.getClass());
    }

    @org.junit.Test
    public void testEdgeCase_FindDeserializerForExistingDeserializer() throws Exception {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializerFromMapper = mapper.findValueDeserializer(context, UUID.class);
        JsonDeserializer<?> deserializerFromJdkDeserializers = new UUIDDeserializer();
        assertEquals(deserializerFromJdkDeserializers.getClass(), deserializer.getClass());
        assertEquals(deserializerFromJdkDeserializers.getClass(), deserializerFromMapper.getClass());
    }

    @org.junit.Test
    public void testEdgeCase_FindDeserializerForNonExistingClass() throws Exception {
        Class<?> rawType = UUID.class;
        String clsName = Integer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @org.junit.Test(expected = JsonProcessingException.class)
    public void testEdgeCase_DeserializeNullInputForUUIDDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new UUIDDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        UUID uuid = mapper.readValue("null", UUID.class);
        fail("Expected JsonProcessingException");
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeEmptyStringInputForUUIDDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new UUIDDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        UUID uuid = mapper.readValue("", UUID.class);
        assertTrue(uuid == null);
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeInvalidStringInputForUUIDDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new UUIDDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        String invalidUuidString = "InvalidUuidString";
        UUID uuid = mapper.readValue(invalidUuidString, UUID.class);
        assertTrue(uuid == null);
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeValidStringInputForUUIDDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new UUIDDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        String validUuidString = "01234567-89AB-CDEF-1234-567890123456";
        UUID uuid = mapper.readValue(validUuidString, UUID.class);
        assertNotNull(uuid);
        assertEquals(validUuidString, uuid.toString());
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeNullInputForAtomicBooleanDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new AtomicBooleanDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        AtomicBoolean atomicBoolean = mapper.readValue("null", AtomicBoolean.class);
        assertNull(atomicBoolean);
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeEmptyStringInputForAtomicBooleanDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new AtomicBooleanDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        AtomicBoolean atomicBoolean = mapper.readValue("", AtomicBoolean.class);
        assertTrue(atomicBoolean.get() == false);
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeBooleanStringInputForAtomicBooleanDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new AtomicBooleanDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        String booleanString = "true";
        AtomicBoolean atomicBoolean = mapper.readValue(booleanString, AtomicBoolean.class);
        assertTrue(atomicBoolean.get() == true);
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeInvalidBooleanStringInputForAtomicBooleanDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new AtomicBooleanDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        String invalidBooleanString = "InvalidBooleanString";
        AtomicBoolean atomicBoolean = mapper.readValue(invalidBooleanString, AtomicBoolean.class);
        assertTrue(atomicBoolean.get() == false);
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeNullInputForByteBufferDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new ByteBufferDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        ByteBuffer byteBuffer = null;
        ByteBuffer deserializedByteBuffer = mapper.readValue(mapper.writeValueAsString(byteBuffer), ByteBuffer.class);
        assertNull(deserializedByteBuffer);
    }

    @org.junit.Test(expected = JsonProcessingException.class)
    public void testEdgeCase_DeserializeEmptyStringInputForByteBufferDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new ByteBufferDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        String emptyString = "";
        ByteBuffer byteBuffer = mapper.readValue(emptyString, ByteBuffer.class);
        fail("Expected JsonProcessingException");
    }

    @org.junit.Test
    public void testEdgeCase_DeserializeValidByteBufferInputForByteBufferDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = new ByteBufferDeserializer();
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ObjectMapper mapper = new ObjectMapper();
        byte[] byteArray = new byte[] { 1, 2, 3, 4, 5 };
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        String jsonString = mapper.writeValueAsString(byteBuffer);
        ByteBuffer deserializedByteBuffer = mapper.readValue(jsonString, ByteBuffer.class);
        assertEquals(byteBuffer.capacity(), deserializedByteBuffer.capacity());
        for (int i = 0; i < byteBuffer.limit(); i++) {
            byte b1 = byteBuffer.get(i);
            byte b2 = deserializedByteBuffer.get(i);
            assertEquals(b1, b2);
        }
    }
}