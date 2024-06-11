package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializerWithNullType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithStringType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.StringKeySerializer.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithBooleanType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Boolean.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithNumberType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Integer.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithClassType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithDateType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithCalendarType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithUUIDType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, UUID.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithEnumType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithUnknownType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, MyCustomClass.class, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializerWithEnumType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializerWithUnknownType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, MyCustomClass.class);
        assertNull(serializer);
    }

    @Test
    public void testSerializeDefault() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PropertySerializerMap serializers = new PropertySerializerMap();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();

        String json = mapper.writerWithDefaultPrettyPrinter()
                .withSerializerProviders(new SerializerProvider() {
                    @Override
                    public JsonSerializer<?> findValueSerializer(Class<?> type)
                            throws JsonMappingException {
                        return serializer;
                    }
                }).writeValueAsString(new Object());

        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        Object readValue = parser.parseValueAsObject(json);

        JsonGenerator gen = factory.createGenerator(new ByteArrayOutputStream());
        serializer.serialize(readValue, gen, serializers);

        assertEquals("", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDynamic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PropertySerializerMap serializers = new PropertySerializerMap();
        Object value = new MyCustomClass();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
                value.getClass(), false);
        serializers.put(value.getClass(), serializer);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .withSerializerProviders(new SerializerProvider() {
                    @Override
                    public JsonSerializer<?> findValueSerializer(Class<?> type)
                            throws JsonMappingException {
                        return serializers.serializerFor(type);
                    }
                }).writeValueAsString(new Object());

        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        Object readValue = parser.parseValueAsObject(json);

        JsonGenerator gen = factory.createGenerator(new ByteArrayOutputStream());
        serializer.serialize(readValue, gen, serializers);

        assertEquals("myCustomClass", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDefaultEnum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PropertySerializerMap serializers = new PropertySerializerMap();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();

        String json = mapper.writerWithDefaultPrettyPrinter()
                .withSerializerProviders(new SerializerProvider() {
                    @Override
                    public JsonSerializer<?> findValueSerializer(Class<?> type)
                            throws JsonMappingException {
                        if (type == Enum.class) {
                            return serializer;
                        }
                        return null;
                    }
                }).writeValueAsString(MyEnum.VALUE1);

        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        Object readValue = parser.parseValueAsObject(json);

        JsonGenerator gen = factory.createGenerator(new ByteArrayOutputStream());
        serializer.serialize(readValue, gen, serializers);

        assertEquals("VALUE1", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDynamicEnum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PropertySerializerMap serializers = new PropertySerializerMap();
        Object value = MyEnum.VALUE1;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
                value.getClass(), false);
        serializers.put(value.getClass(), serializer);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .withSerializerProviders(new SerializerProvider() {
                    @Override
                    public JsonSerializer<?> findValueSerializer(Class<?> type)
                            throws JsonMappingException {
                        if (type == value.getClass()) {
                            return serializer;
                        }
                        return null;
                    }
                }).writeValueAsString(value);

        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        Object readValue = parser.parseValueAsObject(json);

        JsonGenerator gen = factory.createGenerator(new ByteArrayOutputStream());
        serializer.serialize(readValue, gen, serializers);

        assertEquals("VALUE1", gen.getOutputBuffer().toString());
    }

    private static class MyCustomClass {}

    private enum MyEnum {
        VALUE1,
        VALUE2
    }
}