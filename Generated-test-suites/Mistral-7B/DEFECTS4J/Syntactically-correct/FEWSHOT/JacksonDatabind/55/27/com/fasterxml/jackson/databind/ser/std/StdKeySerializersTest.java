package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, true);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, true);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_DATE, Date.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, true);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CALENDAR, Calendar.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, true);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, true);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, true);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CLASS, Class.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, null, true);
        assertEquals(StdKeySerializers.new Dynamic(), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Object.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, int.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_DATE, Date.class), serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CALENDAR, Calendar.class), serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, java.util.UUID.class);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.new Dynamic(), serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Class.class);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CLASS, Class.class), serializer);
    }
}