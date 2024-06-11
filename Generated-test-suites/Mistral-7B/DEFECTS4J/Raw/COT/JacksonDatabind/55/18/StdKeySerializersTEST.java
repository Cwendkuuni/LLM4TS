package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;

import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        rawKeyType = int.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        rawKeyType = java.util.Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
        // To test this, we need a mocked JsonGenerator

        rawKeyType = java.util.Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
        // To test this, we need a mocked JsonGenerator and Calendar instance

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);

        rawKeyType = Class.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
        // To test this, we need a mocked JsonGenerator and Class instance

        rawKeyType = boolean.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);

        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

        rawKeyType = Object.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);

        rawKeyType = null;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        rawKeyType = int.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        rawKeyType = java.util.Date.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        // To test this, we need a mocked JsonGenerator

        rawKeyType = java.util.Calendar.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        // To test this, we need a mocked JsonGenerator and Calendar instance

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);

        rawKeyType = Class.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        // To test this, we need a mocked JsonGenerator and Class instance

        rawKeyType = boolean.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = Object.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = null;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testDynamicSerializationNullMap() throws IOException {
        SerializationConfig config = new SerializationConfig();
        Object value = new Object();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        dynamicSerializer._dynamicSerializers = map;
        serializer = (JsonSerializer<Object>) dynamicSerializer.serialize(value, new JsonGenerator(null), config);
    }

    @Test(expected = JsonMappingException.class)
    public void testDynamicSerializationNullType() throws IOException {
        SerializationConfig config = new SerializationConfig();
        Object value = new Object();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        dynamicSerializer._dynamicSerializers = map;
        serializer = (JsonSerializer<Object>) dynamicSerializer.serialize(value, new JsonGenerator(null), config);
        Class<?> clazz = null;
        serializer.serialize(clazz, new JsonGenerator(null), config);
    }

    @Test
    public void testDynamicSerialization() throws IOException {
        SerializationConfig config = new SerializationConfig();
        Object value = new Object();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        dynamicSerializer._dynamicSerializers = map;
        serializer = (JsonSerializer<Object>) dynamicSerializer.serialize(value, new JsonGenerator(null), config);
        Class<?> clazz = String.class;
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(clazz, generator, config);
        String result = generator.getOutputContent();
        assertTrue(result.contains("\"class\""));
    }

    @Test
    public void testStringSerialization() throws IOException {
        SerializationConfig config = new SerializationConfig();
        Object value = "test";
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultStringSerializer();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(value, generator, config);
        String result = generator.getOutputContent();
        String expected = "\"test\"";
        assertEquals(expected, result);
    }
}