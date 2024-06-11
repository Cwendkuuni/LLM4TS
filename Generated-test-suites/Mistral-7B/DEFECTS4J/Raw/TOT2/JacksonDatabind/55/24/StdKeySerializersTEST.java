package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.*;

public class StdKeySerializersTest {

    @BeforeClass
    public static void setup() {
        // Set up any static initializations or configurations here if needed
    }

    @AfterClass
    public static void teardown() {
        // Clean up any static resources or configurations here if needed
    }

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> expectedSerializer = new StringKeySerializer();
        JsonSerializer<Object> actualSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(expectedSerializer, actualSerializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> actualSerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(new StringKeySerializer(), actualSerializer);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> actualSerializer = StdKeySerializers.getDefault();
        assertEquals(new StringKeySerializer(), actualSerializer);
    }

    // Edge case tests

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerWithNullConfig() {
        Class<?> rawKeyType = String.class;
    }

    @Test(expected = NullPointerException.class)
    public void testGetFallbackKeySerializerWithNullConfig() {
        Class<?> rawKeyType = String.class;
    }

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerWithNullRawKeyType() {
        SerializationConfig config = new SerializationConfig();
    }

    @Test(expected = NullPointerException.class)
    public void testGetFallbackKeySerializerWithNullRawKeyType() {
        SerializationConfig config = new SerializationConfig();
    }

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerWithRawKeyTypeAsObject() {
        SerializationConfig config = new SerializationConfig();
    }

    @Test(expected = NullPointerException.class)
    public void testGetFallbackKeySerializerWithRawKeyTypeAsObject() {
        SerializationConfig config = new SerializationConfig();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetStdKeySerializerWithJsonMappingException() {
        SerializationConfig config = new SerializationConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        Class<?> rawKeyType = String.class;
        try {
            StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testGetFallbackKeySerializerWithJsonMappingException() {
        SerializationConfig config = new SerializationConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        Class<?> rawKeyType = String.class;
        try {
            StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test(expected = IOException.class)
    public void testGetStdKeySerializerWithIOException() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        try {
            StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(expected = IOException.class)
    public void testGetFallbackKeySerializerWithIOException() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        try {
            StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}