package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        Class<String> rawKeyType = String.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);

        rawKeyType = Integer.class;
        result = StdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);

        rawKeyType = Class.class;
        result = StdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultResult = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, defaultResult._typeId);
        assertEquals(rawKeyType, defaultResult._handledType);

        rawKeyType = java.util.UUID.class;
        result = StdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        defaultResult = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultResult._typeId);
        assertEquals(rawKeyType, defaultResult._handledType);

        rawKeyType = java.util.Date.class;
        result = StdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        defaultResult = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_DATE, defaultResult._typeId);
        assertEquals(rawKeyType, defaultResult._handledType);

        rawKeyType = java.util.Calendar.class;
        result = StdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        defaultResult = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, defaultResult._typeId);
        assertEquals(rawKeyType, defaultResult._handledType);

        rawKeyType = Object.class;
        result = StdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
        StdKeySerializers.Dynamic dynamicResult = (StdKeySerializers.Dynamic) result;

        rawKeyType = null;
        result = StdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
        dynamicResult = (StdKeySerializers.Dynamic) result;
    }

    @Test
    public void testGetFallbackKeySerializer() {
        Class<String> rawKeyType = String.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);

        rawKeyType = Enum.class;
        result = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
        StdKeySerializers.Dynamic dynamicResult = (StdKeySerializers.Dynamic) result;

        rawKeyType = EnumForTest.class;
        result = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultResult = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, defaultResult._typeId);
        assertEquals(rawKeyType, defaultResult._handledType);

        rawKeyType = null;
        result = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    // Need a custom enum type for testing
    public enum EnumForTest {
        VALUE1, VALUE2
    }
}