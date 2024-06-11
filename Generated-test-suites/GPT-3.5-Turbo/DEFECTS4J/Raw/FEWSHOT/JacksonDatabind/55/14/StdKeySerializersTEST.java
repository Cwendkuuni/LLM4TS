package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {
    @Test
    public void testGetStdKeySerializer() {
        // Test cases for rawKeyType being null or Object.class
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getStdKeySerializer(null, null, true).getClass());
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getStdKeySerializer(null, Object.class, true).getClass());

        // Test cases for rawKeyType being String.class
        assertEquals(StdKeySerializers.StringKeySerializer.class, StdKeySerializers.getStdKeySerializer(null, String.class, true).getClass());

        // Test cases for rawKeyType being primitive or Number class
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, int.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Integer.class, true).getClass());

        // Test cases for rawKeyType being Class, Date or Calendar
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Class.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Date.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Calendar.class, true).getClass());

        // Test case for rawKeyType being java.util.UUID
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true).getClass());

        // Test case for useDefault = false
        assertNull(StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test case for rawKeyType being null
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getFallbackKeySerializer(null, null).getClass());

        // Test case for rawKeyType being Enum
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getFallbackKeySerializer(null, Enum.class).getClass());

        // Test case for rawKeyType being Enum and isEnum() = true
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(null, Enum.class).getClass());
    }

    @Test
    public void testDefaultSerialize() throws IOException {
        StdKeySerializers.Default defaultKeySerializerDate = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        StdKeySerializers.Default defaultKeySerializerCalendar = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        StdKeySerializers.Default defaultKeySerializerClass = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        StdKeySerializers.Default defaultKeySerializerEnum = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);
        StdKeySerializers.Default defaultKeySerializerToString = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, Object.class);

        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;

        // Test cases for TYPE_DATE
        Date date = new Date(121, 10, 23);
        defaultKeySerializerDate.serialize(date, jsonGenerator, serializerProvider);

        // Test cases for TYPE_CALENDAR
        Calendar calendar = Calendar.getInstance();
        defaultKeySerializerCalendar.serialize(calendar, jsonGenerator, serializerProvider);

        // Test cases for TYPE_CLASS
        Class<StdKeySerializers> stdKeySerializersClass = StdKeySerializers.class;
        defaultKeySerializerClass.serialize(stdKeySerializersClass, jsonGenerator, serializerProvider);

        // Test cases for TYPE_ENUM
        Enum<?> enumValue = Enum.valueOf(StdKeySerializersTest.TestEnum.class, "ENUM_VALUE_ONE");
        defaultKeySerializerEnum.serialize(enumValue, jsonGenerator, serializerProvider);

        // Test cases for TYPE_TO_STRING
        String string = "testString";
        defaultKeySerializerToString.serialize(string, jsonGenerator, serializerProvider);
    }

    private enum TestEnum {
        ENUM_VALUE_ONE,
        ENUM_VALUE_TWO
    }
}