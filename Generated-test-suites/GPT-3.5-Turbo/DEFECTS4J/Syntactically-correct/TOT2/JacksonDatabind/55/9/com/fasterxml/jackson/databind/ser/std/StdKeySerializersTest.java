package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        assertEquals(StringKeySerializer.class, StdKeySerializers.getStdKeySerializer(null, String.class, false).getClass());
        assertEquals(Default.class, StdKeySerializers.getStdKeySerializer(null, Date.class, false).getClass());
        assertEquals(Default.class, StdKeySerializers.getStdKeySerializer(null, Calendar.class, false).getClass());
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        assertEquals(Default.class, StdKeySerializers.getFallbackKeySerializer(null, Enum.class).getClass());
        assertEquals(Default.class, StdKeySerializers.getFallbackKeySerializer(null, EnumTest.class).getClass());
        assertEquals(Default.class, StdKeySerializers.getFallbackKeySerializer(null, null).getClass());
    }

    @Test
    public void testGetDefault() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getDefault());
    }

    @Test
    public void testDefaultSerializer() {
        Default defaultSerializer = new Default(Default.TYPE_DATE, Date.class);
        //TODO: Add test case for serialize method of Default
    }

    @Test
    public void testDynamicSerializer() {
        Dynamic dynamicSerializer = new Dynamic();
        //TODO: Add test case for serialize method of Dynamic
    }

    @Test
    public void testStringKeySerializer() {
        StringKeySerializer stringKeySerializer = new StringKeySerializer();
        //TODO: Add test case for serialize method of StringKeySerializer
    }
}