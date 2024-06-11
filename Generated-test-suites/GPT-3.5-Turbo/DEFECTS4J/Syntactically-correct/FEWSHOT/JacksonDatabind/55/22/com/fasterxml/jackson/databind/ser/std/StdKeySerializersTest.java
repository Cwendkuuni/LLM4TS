package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

@SuppressWarnings("serial")
public class StdKeySerializersTest {
    private StdKeySerializers _stdKeySerializers = new StdKeySerializers();

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        boolean useDefault = true;

        JsonSerializer<Object> result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
        
        rawKeyType = Object.class;
        result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());

        rawKeyType = String.class;
        result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);

        rawKeyType = Integer.TYPE;
        result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);

        rawKeyType = Class.class;
        result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());

        rawKeyType = Date.class;
        result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());

        rawKeyType = Calendar.class;
        result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());

        rawKeyType = java.util.UUID.class;
        result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());

        useDefault = false;
        result = _stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = null;

        JsonSerializer<Object> result = _stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
        
        rawKeyType = Enum.class;
        result = _stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());

        rawKeyType = EnumMap.class;
        result = _stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());

        rawKeyType = TestEnum.class;
        result = _stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.class, result.getClass());

        rawKeyType = String.class;
        result = _stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testDefaultSerialize() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        defaultKeySerializer.serialize(new Date(), jsonGenerator, provider);
    }

    @Test
    public void testDynamicSerialize() throws IOException {
        StdKeySerializers.Dynamic dynamicKeySerializer = new StdKeySerializers.Dynamic();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        dynamicKeySerializer.serialize(new TestEnum(), jsonGenerator, provider);
    }

    @Test
    public void testStringKeySerialize() throws IOException {
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        stringKeySerializer.serialize("test", jsonGenerator, provider);
    }

    private enum TestEnum { A, B, C }
}