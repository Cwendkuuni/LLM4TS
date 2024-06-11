package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, int[].class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, java.lang.Enum.class);
        assertEquals(new StdKeySerializers.Dynamic(), serializer);
    }

    @Test
    public void testDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        try {
            mapper.writeValueAsString(new Object());
        } catch (JsonProcessingException e) {
            // expected
        }

        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), serializer);
    }

    @Test
    public void testDynamicKeySerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        PropertySerializerMap serializers = PropertySerializerMap.emptyForProperties();

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Object[].class, false);
        assertEquals(new StdKeySerializers.Dynamic(), serializer);

        try {
            mapper.writeValueAsString(new Object[]{"foo", "bar"});
        } catch (JsonProcessingException e) {
            // expected
        }

        serializer = StdKeySerializers.getStdKeySerializer(config, int[].class, false);
        assertEquals(new StdKeySerializers.Dynamic(), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, java.lang.Enum[].class, false);
        assertEquals(new StdKeySerializers.Dynamic(), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, MyClass[].class, false);
        assertEquals(new StdKeySerializers.Dynamic(), serializer);

        serializer = StdKeySerializers._findAndAddDynamic(serializers, MyClass.class, mapper.getSerializerProvider());
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, MyClass.class), serializer);

        try {
            mapper.writeValueAsString(new MyClass[]{new MyClass(), new MyClass()});
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testStringKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getStringKeySerializer();
        assertEquals(StdKeySerializers.STRING_KEY_SERIALIZER, serializer);

        try {
            mapper.writeValueAsString(new String[]{"foo", "bar"});
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    private static class MyClass {
        // empty class
    }
}