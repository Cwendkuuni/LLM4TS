package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        // Test for null and Object.class
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for String
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test for primitive types and Number
        serializer = StdKeySerializers.getStdKeySerializer(config, boolean.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, double.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Number.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for Class
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CLASS, Class.class), serializer);

        // Test for Date
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_DATE, Date.class), serializer);

        // Test for Calendar
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CALENDAR, Calendar.class), serializer);

        // Test for java.util.UUID
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        // Test for other JDK types that convert properly with 'toString()'
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, true);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        Class<?> rawKeyType = Enum.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.new Dynamic(), serializer);

        rawKeyType = String.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        rawKeyType = int.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = double.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = boolean.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = Number.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = java.util.Date.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_DATE, java.util.Date.class), serializer);

        rawKeyType = java.util.Calendar.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CALENDAR, java.util.Calendar.class), serializer);

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        rawKeyType = null;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetDefault() {
        SerializationConfig config = new SerializationConfig();

        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }
}