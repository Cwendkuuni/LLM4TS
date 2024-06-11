package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class StdKeySerializersTest {


    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Integer.class, true) instanceof StdKeySerializers.Dynamic);
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Long.class, true) instanceof StdKeySerializers.Default);
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Double.class, true) instanceof StdKeySerializers.Default);
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Boolean.class, true) instanceof StdKeySerializers.Default);
        assertTrue(StdKeySerializers.getStdKeySerializer(config, String.class, true) instanceof StdKeySerializers.StringKeySerializer);
    }


    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        assertTrue(StdKeySerializers.getFallbackKeySerializer(config, Integer.class) instanceof StdKeySerializers.Default);
        assertTrue(StdKeySerializers.getFallbackKeySerializer(config, Long.class) instanceof StdKeySerializers.Default);
        assertTrue(StdKeySerializers.getFallbackKeySerializer(config, Double.class) instanceof StdKeySerializers.Default);
        assertTrue(StdKeySerializers.getFallbackKeySerializer(config, Boolean.class) instanceof StdKeySerializers.Default);
        assertTrue(StdKeySerializers.getFallbackKeySerializer(config, String.class) instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetDefault() {
        assertTrue(StdKeySerializers.getDefault() instanceof StdKeySerializers.Default);
    }



    @Test
    public void testDefaultSerialization() throws IOException {
        JsonSerializer<Object> serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.serialize(new Date(), generator, provider);
        generator.flush();
    }
}