package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

public class StdKeySerializersTest {

    @SuppressWarnings("unchecked")
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
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
        serializer = StdKeySerializers.getStdKeySerializer(config, double.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for Class
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(new Default(Default.TYPE_CLASS, Class.class), serializer);

        // Test for Date
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(new Default(Default.TYPE_DATE, Date.class), serializer);

        // Test for Calendar
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(new Default(Default.TYPE_CALENDAR, Calendar.class), serializer);

        // Test for UUID
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(new Default(Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        // Test for other JDK types
        serializer = StdKeySerializers.getStdKeySerializer(config, java.lang.Enum.class, false);
        assertEquals(new Dynamic(), serializer);
        serializer = StdKeySerializers.getStdKeySerializer(config, java.lang.Enum.class, true);
        assertEquals(new Default(Default.TYPE_ENUM, java.lang.Enum.class), serializer);

        // Test for custom classes
        MyClass myClass = new MyClass();
        Class<?> myClassType = myClass.getClass();
        serializer = StdKeySerializers.getStdKeySerializer(config, myClassType, false);
        assertEquals(new Dynamic(), serializer);
        serializer = StdKeySerializers.getStdKeySerializer(config, myClassType, true);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        // Test for null
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);

        // Test for custom classes
        MyClass myClass = new MyClass();
        Class<?> myClassType = myClass.getClass();
        serializer = StdKeySerializers.getFallbackKeySerializer(config, myClassType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testDynamicKeySerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        PropertySerializerMap map = PropertySerializerMap.emptyForProperties();

        MyClass myClass = new MyClass();
        Class<?> myClassType = myClass.getClass();

        // Test first time
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, myClassType, false);
        assertEquals(new Dynamic(), serializer);
        serializer.serialize(myClass, new JsonGenerator(), new SerializerProvider());
        Map<Class<?>, JsonSerializer<Object>> serializers = map.serializers();
        JsonSerializer<Object> cachedSerializer = serializers.get(myClassType);
        assertSame(serializer, cachedSerializer);

        // Test second time
        serializer = StdKeySerializers.getStdKeySerializer(config, myClassType, false);
        assertEquals(cachedSerializer, serializer);

        // Test third time with different map
        PropertySerializerMap newMap = PropertySerializerMap.emptyForProperties();
        JsonSerializer<Object> newSerializer = StdKeySerializers.getStdKeySerializer(config, myClassType, false);
        assertEquals(new Dynamic(), newSerializer);
        newSerializer.serialize(myClass, new JsonGenerator(), new SerializerProvider());
        serializers = newMap.serializers();
        JsonSerializer<Object> newCachedSerializer = serializers.get(myClassType);
        assertSame(newSerializer, newCachedSerializer);
        assertNotSame(cachedSerializer, newCachedSerializer);
    }

    static class MyClass {
    }
}