package com.fasterxml.jackson.databind.ser.std.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

/**
 * Test file for NumberSerializers class
 */
public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Integer value = 123;
        Base<Object> serializer = NumberSerializers.IntegerSerializer.instance;

        String serializedValue = testSerialization(serializer, value);
        assertEquals("123", serializedValue);
    }

    @Test
    public void testShortSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Short value = 127;
        Base<Object> serializer = NumberSerializers.ShortSerializer.instance;

        String serializedValue = testSerialization(serializer, value);
        assertEquals("127", serializedValue);
    }

    @Test
    public void testIntLikeSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Number value = 128;
        Base<Object> serializer = NumberSerializers.IntLikeSerializer.instance;

        String serializedValue = testSerialization(serializer, value);
        assertEquals("128", serializedValue);
    }

    @Test
    public void testLongSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Long value = 123L;
        Base<Object> serializer = NumberSerializers.LongSerializer.instance;

        String serializedValue = testSerialization(serializer, value);
        assertEquals("123", serializedValue);
    }

    @Test
    public void testFloatSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Float value = 123.45f;
        Base<Object> serializer = NumberSerializers.FloatSerializer.instance;

        String serializedValue = testSerialization(serializer, value);
        assertEquals("123.45", serializedValue);
    }

    @Test
    public void testDoubleSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Double value = 123.45d;
        Base<Object> serializer = NumberSerializers.DoubleSerializer.instance;

        String serializedValue = testSerialization(serializer, value);
        assertEquals("123.45", serializedValue);
    }

    @Test
    public void testIntegerSerializerWithJsonFormatAnnotation() throws JsonMappingException, JsonProcessingException, IOException {
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        Integer value = 123;
        Base<Object> serializer = new IntegerSerializer(Integer.class);
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(null, mapper.getTypeFactory()), null);
        serializer.setFormatter(jsonFormat);

        String serializedValue = testSerialization(serializer, value);
        assertEquals("\"123\"", serializedValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValueWithIntegerSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Base<Object> serializer = NumberSerializers.IntegerSerializer.instance;
        serializer.serialize(null, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testNegativeValueWithIntegerSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Base<Object> serializer = NumberSerializers.IntegerSerializer.instance;
        Integer value = -123;
        serializer.serialize(value, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testLongValueWithIntegerSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Base<Object> serializer = NumberSerializers.IntegerSerializer.instance;
        Long value = 123L;
        serializer.serialize(value, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testFloatValueWithIntegerSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Base<Object> serializer = NumberSerializers.IntegerSerializer.instance;
        Float value = 123.45f;
        serializer.serialize(value, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDoubleValueWithIntegerSerializer() throws JsonMappingException, JsonProcessingException, IOException {
        Base<Object> serializer = NumberSerializers.IntegerSerializer.instance;
        Double value = 123.45d;
        serializer.serialize(value, null, null);
    }

    private String testSerialization(Base<Object> serializer, Object value)
            throws JsonMappingException, JsonProcessingException, IOException {
        StringWriter stringWriter = new StringWriter(new StringBuilder());
        JsonGenerator generator = mapper.getFactory()
                .createGenerator(stringWriter);
        SerializerProvider provider = mapper.getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.close();
        return stringWriter.toString();
    }

}