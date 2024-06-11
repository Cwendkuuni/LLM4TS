package com.example.test; // replace with your test package name

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import static org.junit.Assert.assertEquals;



public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = null;
        NumberSerializers.addAll(allDeserializers);
        assertEquals(12, allDeserializers.size()); // check size
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllNullMap() {
        NumberSerializers.addAll(null);
    }

    @Test
    public void testShortSerializer() throws IOException {
        Short testValue = Short.MIN_VALUE;
        String expectedJson = "\"-32768\"";
        JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
        
        String json = mapper.writeValueAsString(testValue);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        Integer testValue = Integer.MAX_VALUE;
        String expectedJson = "\"2147483647\"";
        JsonSerializer<Object> intSerializer = NumberSerializers.IntegerSerializer.instance;

        String json = mapper.writeValueAsString(testValue);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        Long testValue = Long.MAX_VALUE;
        String expectedJson = "\"9223372036854775807\"";
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        String json = mapper.writeValueAsString(testValue);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testLongSerializer() throws IOException {
        Long testValue = Long.MIN_VALUE;
        String expectedJson = "\"-9223372036854775808\"";
        JsonSerializer<Object> longSerializer = NumberSerializers.LongSerializer.instance;

        String json = mapper.writeValueAsString(testValue);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        Float testValue = Float.MAX_VALUE;
        String expectedJson = "\"3.4028235E38\"";
        JsonSerializer<Object> floatSerializer = NumberSerializers.FloatSerializer.instance;

        String json = mapper.writeValueAsString(testValue);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        Double testValue = Double.MAX_VALUE;
        String expectedJson = "\"1.79769313486231E308\"";
        JsonSerializer<Object> doubleSerializer = NumberSerializers.DoubleSerializer.instance;

        String json = mapper.writeValueAsString(testValue);
        assertEquals(expectedJson, json);
    }
}
