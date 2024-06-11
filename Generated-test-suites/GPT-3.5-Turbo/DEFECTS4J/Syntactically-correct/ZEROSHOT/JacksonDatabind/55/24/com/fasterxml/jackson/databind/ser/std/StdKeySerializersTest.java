package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        StdKeySerializers stdKeySerializers = new StdKeySerializers();
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(null, null, false);
        assertEquals(null,result);
        result = stdKeySerializers.getStdKeySerializer(null, null, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,result);
        result = stdKeySerializers.getStdKeySerializer(null, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER,result);
        result = stdKeySerializers.getStdKeySerializer(null, String.class, true);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER,result);
        result = stdKeySerializers.getStdKeySerializer(null, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,result);
        result = stdKeySerializers.getStdKeySerializer(null, int.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,result);
        result = stdKeySerializers.getStdKeySerializer(null, Class.class, false);
        assertEquals(Default.class,result);
        result = stdKeySerializers.getStdKeySerializer(null, Class.class, true);
        assertEquals(Default.class,result);
        result = stdKeySerializers.getStdKeySerializer(null, Date.class, false);
        assertEquals(Default.class,result);
        result = stdKeySerializers.getStdKeySerializer(null, Date.class, true);
        assertEquals(Default.class,result);
        result = stdKeySerializers.getStdKeySerializer(null, Calendar.class, false);
        assertEquals(Default.class,result);
        result = stdKeySerializers.getStdKeySerializer(null, Calendar.class, true);
        assertEquals(Default.class,result);
        result = stdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false);
        assertEquals(Default.class,result);
        result = stdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true);
        assertEquals(Default.class,result);
        result = stdKeySerializers.getStdKeySerializer(null, Object.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,result);
        result = stdKeySerializers.getStdKeySerializer(null, Object.class, true);
        assertEquals(null,result);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        StdKeySerializers stdKeySerializers = new StdKeySerializers();
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(null, null);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,result);
        result = stdKeySerializers.getFallbackKeySerializer(null, Enum.class);
        assertEquals(Dynamic.class,result);
        result = stdKeySerializers.getFallbackKeySerializer(null, null);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,result);
        result = stdKeySerializers.getFallbackKeySerializer(null, Enum.class);
        assertEquals(Dynamic.class,result);
    }

    @Test
    public void testGetDefault() {
        StdKeySerializers stdKeySerializers = new StdKeySerializers();
        JsonSerializer<Object> result = stdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,result);
    }

    @Test
    public void testDefaultSerialize() throws IOException{
        Default def = new Default(Default.TYPE_DATE, Date.class);
        Object value = new Date();
        JsonGenerator g = null;
        SerializerProvider provider = null;
        def.serialize(value, g, provider);
    }

    @Test
    public void testDynamicSerialize() throws IOException{
        Dynamic dynamic = new Dynamic();
        Object value = new Object();
        JsonGenerator g = null;
        SerializerProvider provider = null;
        dynamic.serialize(value, g, provider);
    }

    @Test
    public void testStringKeySerialize() throws IOException{
        StringKeySerializer stringKeySerializer = new StringKeySerializer();
        Object value = "string";
        JsonGenerator g = null;
        SerializerProvider provider = null;
        stringKeySerializer.serialize(value, g, provider);
    }

    @Test
    public void testDefaultSerializeObjectField() throws IOException{
        Default def = new Default(Default.TYPE_TO_STRING, Object.class);
        Object value = new Object();
        JsonGenerator g = null;
        SerializerProvider provider = null;
        def.serialize(value, g, provider);
    }
}