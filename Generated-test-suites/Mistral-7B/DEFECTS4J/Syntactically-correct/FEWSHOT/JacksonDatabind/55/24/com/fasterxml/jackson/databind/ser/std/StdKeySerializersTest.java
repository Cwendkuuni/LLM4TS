package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

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

        // Test for null or Object.class
        Class<?> rawKeyType = null;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        rawKeyType = Object.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // Test for String
        rawKeyType = String.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        // Test for primitive types or Number
        rawKeyType = int.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        rawKeyType = Double.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // Test for Class
        rawKeyType = Class.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        // Test for Date
        rawKeyType = Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for Calendar
        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for UUID
        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for other JDK types that convert properly with 'toString()'
        rawKeyType = java.sql.Timestamp.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for Enum
        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for custom class
        rawKeyType = MyCustomClass.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        // Test for null or Object.class
        Class<?> rawKeyType = null;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = Object.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for String
        rawKeyType = String.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test for primitive types or Number
        rawKeyType = int.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = Double.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for Class
        rawKeyType = Class.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        // Test for Date
        rawKeyType = Date.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for Calendar
        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for UUID
        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for Enum
        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

        // Test for custom class
        rawKeyType = MyCustomClass.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetDefault() {
        // Deprecated method test
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getDefault());
    }
}