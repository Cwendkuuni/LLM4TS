package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

public class StdKeySerializersTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final SerializationConfig CONFIG = MAPPER.getSerializationConfig();
    private static final DefaultSerializerProvider SERIALIZER_PROVIDER = new DefaultSerializerProvider.Impl();
    
    @Test
    public void testGetStdKeySerializer() {
        // Test for rawKeyType = null
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(CONFIG, null, true);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);

        // Test for rawKeyType = Object.class
        serializer = StdKeySerializers.getStdKeySerializer(CONFIG, Object.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);

        // Test for rawKeyType = String.class
        serializer = StdKeySerializers.getStdKeySerializer(CONFIG, String.class, true);
        assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);

        // Test for rawKeyType = int
        serializer = StdKeySerializers.getStdKeySerializer(CONFIG, int.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test for rawKeyType = Class.class
        serializer = StdKeySerializers.getStdKeySerializer(CONFIG, Class.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test for rawKeyType = Date.class
        serializer = StdKeySerializers.getStdKeySerializer(CONFIG, Date.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test for rawKeyType = Calendar.class
        serializer = StdKeySerializers.getStdKeySerializer(CONFIG, Calendar.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test for rawKeyType = java.util.UUID.class
        serializer = StdKeySerializers.getStdKeySerializer(CONFIG, java.util.UUID.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test for rawKeyType = Unknown.class
        serializer = StdKeySerializers.getStdKeySerializer(CONFIG, Unknown.class, true);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test for rawKeyType = null
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(CONFIG, null);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test for rawKeyType = Object.class
        serializer = StdKeySerializers.getFallbackKeySerializer(CONFIG, Object.class);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test for rawKeyType = Enum.class
        serializer = StdKeySerializers.getFallbackKeySerializer(CONFIG, Enum.class);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);

        // Test for rawKeyType = EnumSubclass.class
        serializer = StdKeySerializers.getFallbackKeySerializer(CONFIG, EnumSubclass.class);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }

    @Test
    public void testDefaultSerializer() throws Exception {
        // Test for TYPE_DATE with Date
        Default serializer = new Default(Default.TYPE_DATE, Date.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = SERIALIZER_PROVIDER.createInstance(CONFIG, null);
        serializer.serialize(new Date(), generator, provider);
        JsonNode node = MAPPER.readTree(generator);
        assertTrue(node.isTextual());

        // Test for TYPE_CALENDAR with Calendar
        serializer = new Default(Default.TYPE_CALENDAR, Calendar.class);
        generator = new JsonFactory().createGenerator(System.out);
        provider = SERIALIZER_PROVIDER.createInstance(CONFIG, null);
        serializer.serialize(Calendar.getInstance(), generator, provider);
        node = MAPPER.readTree(generator);
        assertTrue(node.isNumber());

        // Test for TYPE_CLASS with Class
        serializer = new Default(Default.TYPE_CLASS, Class.class);
        generator = new JsonFactory().createGenerator(System.out);
        provider = SERIALIZER_PROVIDER.createInstance(CONFIG, null);
        serializer.serialize(String.class, generator, provider);
        node = MAPPER.readTree(generator);
        assertTrue(node.isTextual());

        // Test for TYPE_ENUM with Enum
        serializer = new Default(Default.TYPE_ENUM, Enum.class);
        generator = new JsonFactory().createGenerator(System.out);
        provider = SERIALIZER_PROVIDER.createInstance(CONFIG, null);
        serializer.serialize(Enum.TEST, generator, provider);
        node = MAPPER.readTree(generator);
        assertTrue(node.isTextual());

        // Test for TYPE_TO_STRING with UUID
        serializer = new Default(Default.TYPE_TO_STRING, java.util.UUID.class);
        generator = new JsonFactory().createGenerator(System.out);
        provider = SERIALIZER_PROVIDER.createInstance(CONFIG, null);
        serializer.serialize(java.util.UUID.randomUUID(), generator, provider);
        node = MAPPER.readTree(generator);
        assertTrue(node.isTextual());
    }

    @Test
    public void testStringKeySerializer() throws Exception {
        StdKeySerializers.StringKeySerializer serializer = new StdKeySerializers.StringKeySerializer();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = SERIALIZER_PROVIDER.createInstance(CONFIG, null);
        serializer.serialize("test", generator, provider);
        JsonNode node = MAPPER.readTree(generator);
        assertTrue(node.isTextual());
    }

    @Test
    public void testDynamicSerializer() throws Exception {
        StdKeySerializers.Dynamic serializer = new StdKeySerializers.Dynamic();
        
        // Test for String key
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = SERIALIZER_PROVIDER.createInstance(CONFIG, null);
        serializer.serialize("test", generator, provider);
        JsonNode node = MAPPER.readTree(generator);
        assertTrue(node.isTextual());

        // Test for Integer key
        generator = new JsonFactory().createGenerator(System.out);
        provider = SERIALIZER_PROVIDER.createInstance(CONFIG, null);
        serializer.serialize(123, generator, provider);
        node = MAPPER.readTree(generator);
        assertTrue(node.isNumber());
    }

    private static class Unknown {
        // Dummy class for testing
    }

    private static enum Enum {
        TEST
    }

    private static enum EnumSubclass {
        TEST
    }

}