package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        Class<?> rawKeyType = Object.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);

        rawKeyType = null;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);

        rawKeyType = String.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        rawKeyType = int.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = Class.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        rawKeyType = Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        Class<?> rawKeyType = Object.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = null;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = String.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        rawKeyType = int.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = Class.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        rawKeyType = Date.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testDynamicKeySerialization() throws IOException {
        SerializationConfig config = new SerializationConfig();
        StdKeySerializers serializers = new StdKeySerializers();

        Object obj = new Object() {
            @Override
            public String toString() {
                return "dynamicKey";
            }
        };

        PropertySerializerMap propertySerializerMap = PropertySerializerMap.emptyForProperties();
        JsonSerializer<Object> serializer = serializers._findAndAddDynamic(propertySerializerMap, obj.getClass(), config);
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        serializer.serialize(obj, generator, config);
        assertEquals("dynamicKey", generator.getOutputBuffer().toString());
    }
}