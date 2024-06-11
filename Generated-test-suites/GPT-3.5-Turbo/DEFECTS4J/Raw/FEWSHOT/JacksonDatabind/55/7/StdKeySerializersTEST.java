package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;

public class StdKeySerializersTest {
    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerNull() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = null;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerPrimitive() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = int.class;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerNumber() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Number.class;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerClass() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Class.class;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerDate() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Date.class;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerCalendar() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerUUID() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = UUID.class;
        boolean useDefault = true;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerUseDefaultFalse() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = String.class;
        boolean useDefault = false;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializerNotNull() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Enum.class;

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializerEnum() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Enum.class;

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializerNull() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = null;

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializerDate() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Date.class;

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializerUseDefaultFalse() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = null;

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testDefaultKeySerialize() throws Exception {
        StdKeySerializers.Default keySerializer = new StdKeySerializers.Default(0, Object.class);
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        keySerializer.serialize("test", g, provider);
        g.close();
    }

    @Test
    public void testDynamicSerialize() throws Exception {
        StdKeySerializers.Dynamic keySerializer = new StdKeySerializers.Dynamic();
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        keySerializer.serialize("test", g, provider);
        g.close();
    }

    @Test
    public void testStringKeySerialize() throws Exception {
        StdKeySerializers.StringKeySerializer keySerializer = new StdKeySerializers.StringKeySerializer();
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        keySerializer.serialize("test", g, provider);
        g.close();
    }
}