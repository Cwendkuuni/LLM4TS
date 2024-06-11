
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertySerializerMap;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializerWithTypeNullAndUseDefaultTrue() {
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, true));
    }

    @Test
    public void testGetStdKeySerializerWithTypeNullAndUseDefaultFalse() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, null, false));
    }

    @Test
    public void testGetStdKeySerializerWithStringTypeAndUseDefaultFalse() {
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, String.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithNumberTypeAndUseDefaultFalse() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Double.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithTypeDateAndUseDefaultFalse() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Date.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithTypeCalendarAndUseDefaultFalse() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Calendar.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithUtilUUIDTypeAndUseDefaultFalse() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithUseDefaultTrue() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Object.class, true));
    }

    @Test
    public void testGetStdKeySerializerWithUseDefaultFalse() {
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetFallbackKeySerializerWithRawKeyTypeNotNull() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, Object.class));
    }

    @Test
    public void testGetFallbackKeySerializerWithRawKeyTypeNull() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, null));
    }

    @Test
    public void testDefaultSerialize() throws IOException {
        JsonSerializer<Object> defaultSerializer = new Default(Default.TYPE_DATE, Date.class);
        JsonGenerator g = null;
        SerializerProvider provider = null;

        // TODO: Add assertions
        defaultSerializer.serialize(new Date(), g, provider);
    }

    @Test
    public void testDynamicSerialize() throws IOException {
        JsonSerializer<Object> dynamicSerializer = new Dynamic();
        JsonGenerator g = null;
        SerializerProvider provider = null;

        // TODO: Add assertions
        dynamicSerializer.serialize("test", g, provider);
    }

    @Test
    public void testStringKeySerialize() throws IOException {
        JsonSerializer<Object> stringKeySerializer = new StringKeySerializer();
        JsonGenerator g = null;
        SerializerProvider provider = null;

        // TODO: Add assertions
        stringKeySerializer.serialize("test", g, provider);
    }

    @Test
    public void testDynamicReadResolve() throws Exception {
        Dynamic dynamic = new Dynamic();
        dynamic.readResolve();

        PropertySerializerMap dynamicSerializers = dynamic.getClass().getDeclaredField("_dynamicSerializers").get(dynamic);
        assertNotNull(dynamicSerializers);
        assertEquals(PropertySerializerMap.emptyForProperties(), dynamicSerializers);
    }
}
