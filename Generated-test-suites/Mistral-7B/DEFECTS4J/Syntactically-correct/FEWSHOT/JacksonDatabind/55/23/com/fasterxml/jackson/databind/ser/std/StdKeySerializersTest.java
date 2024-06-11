package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonProcessingException.fromThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testGetStdKeySerializer() {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();

        // Test null rawKeyType
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // Test String rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        // Test primitive types
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // Test Class rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        // Test Date rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        // Test Calendar rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        // Test UUID rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        // Test enum rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, MyEnum.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

        // Test unsupported rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, CustomObject.class, false);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, MyEnum.class);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

        // Test null rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // Test CustomObject rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, CustomObject.class);
        assertNull(serializer);
    }

    @Test
    public void testDynamicSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, CustomObject.class, true);
        assertNull(serializer);

        PropertySerializerMap dynamicSerializers = PropertySerializerMap.emptyForProperties();
        JsonSerializer<Object> dynamicSerializer = mapper.findValueSerializer(CustomObject.class);
        dynamicSerializers.setSerializer(CustomObject.class, dynamicSerializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, CustomObject.class, true);
        assertEquals(dynamicSerializer.getClass(), serializer.getClass());
    }

    @Test
    public void testSerializeDefault() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        Object key = new Object() {
        };

        StdKeySerializers.DEFAULT_KEY_SERIALIZER.serialize(key, gen, config);
        assertEquals("", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        String key = "test";

        StdKeySerializers.DEFAULT_STRING_SERIALIZER.serialize(key, gen, config);
        assertEquals("\"test\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeInt() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        int key = 1;

        StdKeySerializers.DEFAULT_KEY_SERIALIZER.serialize(key, gen, config);
        assertEquals("1", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeClass() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        Class<?> key = String.class;

        StdKeySerializers.Default serializer = (StdKeySerializers.Default) StdKeySerializers.getStdKeySerializer(config, key.getClass(), false);
        serializer.serialize(key, gen, config);
        assertEquals("java.lang.String", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        Date key = new Date();

        StdKeySerializers.Default serializer = (StdKeySerializers.Default) StdKeySerializers.getStdKeySerializer(config, key.getClass(), false);
        serializer.serialize(key, gen, config);
        assertEquals("\"1970-01-01T00:00:00.000Z\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeCalendar() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        Calendar key = Calendar.getInstance();

        StdKeySerializers.Default serializer = (StdKeySerializers.Default) StdKeySerializers.getStdKeySerializer(config, key.getClass(), false);
        serializer.serialize(key, gen, config);
        assertEquals("\"1970-01-01T00:00:00.000Z\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeUUID() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        java.util.UUID key = java.util.UUID.randomUUID();

        StdKeySerializers.Default serializer = (StdKeySerializers.Default) StdKeySerializers.getStdKeySerializer(config, key.getClass(), false);
        serializer.serialize(key, gen, config);
        assertEquals("\"5c2d5a1e-7c23-481d-98f4-2e63b72d622f\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeEnum() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        MyEnum key = MyEnum.ONE;

        StdKeySerializers.Default serializer = (StdKeySerializers.Default) StdKeySerializers.getStdKeySerializer(config, key.getClass(), false);
        serializer.serialize(key, gen, config);
        assertEquals("\"ONE\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDynamic() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.createGenerator();
        SerializationConfig config = mapper.getSerializationConfig();

        CustomObject key = new CustomObject();

        PropertySerializerMap dynamicSerializers = PropertySerializerMap.emptyForProperties();
        JsonSerializer<Object> dynamicSerializer = mapper.findValueSerializer(CustomObject.class);
        dynamicSerializers.setSerializer(CustomObject.class, dynamicSerializer);

        StdKeySerializers.Dynamic dynamic = (StdKeySerializers.Dynamic) StdKeySerializers.getStdKeySerializer(config, CustomObject.class, true);
        dynamic.serialize(key, gen, config);

        assertEquals("{\"name\":\"test\"}", gen.getOutputBuffer().toString());
    }

    private static class CustomObject {
        String name;
    }

    private enum MyEnum { ONE, TWO }
}