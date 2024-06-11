package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test for default key serializer
        JsonSerializer<Object> defaultKeySerializer = StdKeySerializers.getStdKeySerializer(null, String.class, false);
        assertNotNull(defaultKeySerializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test for fallback key serializer
        JsonSerializer<Object> fallbackKeySerializer = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
        assertNotNull(fallbackKeySerializer);
    }

    @Test
    public void testGetDefault() {
        // Test for default key serializer
        JsonSerializer<Object> defaultKeySerializer = StdKeySerializers.getDefault();
        assertNotNull(defaultKeySerializer);
    }

    @Test
    public void testDefaultSerializer() {
        // Test for Default key serializer with different types
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        assertNotNull(defaultSerializer);

        Date date = new Date();
        assertEquals("Date key serialization", date.toString(), defaultSerializer.serializeKey(date));
    }

    @Test
    public void testDynamicSerializer() {
        // Test for Dynamic key serializer
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        assertNotNull(dynamicSerializer);

        String str = "Test";
        assertEquals("Dynamic key serialization for String", str, dynamicSerializer.serializeKey(str));
    }

    @Test
    public void testStringKeySerializer() {
        // Test for String key serializer
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        assertNotNull(stringKeySerializer);

        String key = "Test Key";
        assertEquals("String key serialization", key, stringKeySerializer.serializeKey(key));
    }
}