package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        // Test for null and Object type
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, true);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for String type
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, true);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test for primitive and Number types
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, double.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for Class type
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, true);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class), serializer);

        // Test for Date type
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, true);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class), serializer);

        // Test for Calendar type
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, true);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), serializer);

        // Test for UUID type
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, true);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        // Test for custom class types
        MyCustomClass customClass = new MyCustomClass();
        Class<?> customClassType = customClass.getClass();
        serializer = StdKeySerializers.getStdKeySerializer(config, customClassType, true);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, customClassType, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        // Test for null type
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);

        // Test for custom class types
        MyCustomClass customClass = new MyCustomClass();
        Class<?> customClassType = customClass.getClass();
        serializer = StdKeySerializers.getFallbackKeySerializer(config, customClassType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for Enum type
        MyCustomEnum customEnum = MyCustomEnum.FIRST;
        Class<?> enumType = customEnum.getClass();
        serializer = StdKeySerializers.getFallbackKeySerializer(config, enumType);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, enumType), serializer);
    }
}

class MyCustomClass {
    // Add your custom class implementation here
}

enum MyCustomEnum {
    FIRST,
    SECOND
}