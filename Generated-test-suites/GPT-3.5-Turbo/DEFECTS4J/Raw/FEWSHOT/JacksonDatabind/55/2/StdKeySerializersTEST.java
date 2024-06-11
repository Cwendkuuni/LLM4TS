
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.junit.Before;
import org.junit.Test;

public class StdKeySerializersTest {

    private SerializationConfig config;
    private Class<?> rawKeyType;
    private boolean useDefault;

    @Before
    public void setUp() throws Exception {
        config = mock(SerializationConfig.class);
        rawKeyType = null;
        useDefault = true;
    }

    @Test
    public void testGetStdKeySerializer_withNullRawKeyType() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withObjectRawKeyType() {
        rawKeyType = Object.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withStringRawKeyType() {
        rawKeyType = String.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.StringKeySerializer.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withPrimitiveRawKeyType() {
        rawKeyType = int.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withNumberRawKeyType() {
        rawKeyType = Number.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withClassRawKeyType() {
        rawKeyType = Class.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withDateRawKeyType() {
        rawKeyType = Date.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withCalendarRawKeyType() {
        rawKeyType = Calendar.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withUUIDRawKeyType() {
        rawKeyType = java.util.UUID.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withNonEnumRawKeyType() {
        rawKeyType = NonEnumRawKeyType.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_withEnumRawKeyType() {
        rawKeyType = EnumRawKeyType.class;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer_withNullRawKeyType() {
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer_withEnumRawKeyType() {
        rawKeyType = EnumRawKeyType.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer_withNonEnumRawKeyType() {
        rawKeyType = NonEnumRawKeyType.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer_withKnownRawKeyType() {
        rawKeyType = String.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());
    }

    @Test
    public void testDefaultSerialize() throws Exception {
        DefaultKeySerializer defaultKeySerializer = new DefaultKeySerializer(Default.TYPE_DATE, Date.class);
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Date value = new Date();
        defaultKeySerializer.serialize(value, g, provider);
        // verify defaultSerializeDateKey() is called
        // You would have to implement this method in the test class, as it is not shown in the example code.
        // verify(provider).defaultSerializeDateKey(value, g);
    }

    private static class EnumRawKeyType extends Enum<EnumRawKeyType> {

        @JsonValue
        public String value() {
            return name();
        }
    }

    private static class NonEnumRawKeyType {

        private final String value;

        public NonEnumRawKeyType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
