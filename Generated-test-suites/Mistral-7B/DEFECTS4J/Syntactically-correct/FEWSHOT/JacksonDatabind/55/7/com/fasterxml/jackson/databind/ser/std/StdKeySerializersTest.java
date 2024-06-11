package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.Dynamic;
import com.fasterxml.jackson.databind.ser.std.Default;
import com.fasterxml.jackson.databind.ser.std.StringKeySerializer;

import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        Class<?> rawKeyType = Object.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);

        rawKeyType = String.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(StringKeySerializer.class, serializer);

        rawKeyType = java.util.Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.util.Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Integer.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Boolean.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Double.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Long.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Class.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Enum.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = null;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertInstanceOf(Dynamic.class, serializer);

        rawKeyType = new Object();
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType.getClass(), true);
        assertInstanceOf(Dynamic.class, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        Class<?> rawKeyType = Object.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.util.Date.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.util.Calendar.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Integer.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Boolean.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Double.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Long.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = java.lang.Class.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertInstanceOf(Default.class, serializer);

        rawKeyType = null;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = new Object();
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType.getClass());
        assertNull(serializer);
    }
}