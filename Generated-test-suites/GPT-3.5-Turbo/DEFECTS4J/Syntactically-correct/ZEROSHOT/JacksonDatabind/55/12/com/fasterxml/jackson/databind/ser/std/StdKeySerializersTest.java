package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Object.class;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        Assert.assertNotNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = objectMapper.getSerializationConfig();
        Class<?> rawKeyType = Object.class;

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        Assert.assertNotNull(serializer);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();

        Assert.assertNotNull(serializer);
    }
}