package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class NumberSerializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        BigDecimal bigDecimal = new BigDecimal("123.456");
        numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String serializedBigDecimal = mapper.readValue(System.out.toString(), String.class);
        assertEquals("123.456", serializedBigDecimal);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        BigInteger bigInteger = new BigInteger("987654321098765432109876543210");
        numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String serializedBigInteger = mapper.readValue(System.out.toString(), String.class);
        assertEquals("987654321098765432109876543210", serializedBigInteger);
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        Long longNumber = new Long(123);
        numberSerializer.serialize(longNumber, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String serializedLong = mapper.readValue(System.out.toString(), String.class);
        assertEquals("123", serializedLong);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        Double doubleNumber = new Double(123.45);
        numberSerializer.serialize(doubleNumber, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String serializedDouble = mapper.readValue(System.out.toString(), String.class);
        assertEquals("123.45", serializedDouble);
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        Integer integerNumber = new Integer(123);
        numberSerializer.serialize(integerNumber, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String serializedInteger = mapper.readValue(System.out.toString(), String.class);
        assertEquals("123", serializedInteger);
    }

    @Test
    public void testSerializeByte() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        Byte byteNumber = new Byte((byte) 127);
        numberSerializer.serialize(byteNumber, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String serializedByte = mapper.readValue(System.out.toString(), String.class);
        assertEquals("127", serializedByte);
    }

    @Test
    public void testSerializeShort() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        Short shortNumber = new Short((short) 123);
        numberSerializer.serialize(shortNumber, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String serializedShort = mapper.readValue(System.out.toString(), String.class);
        assertEquals("123", serializedShort);
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        Number nullNumber = null;
        numberSerializer.serialize(nullNumber, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String serializedNull = mapper.readValue(System.out.toString(), String.class);
        assertEquals("null", serializedNull);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidNumber() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out, null);
        jsonGenerator.setCodec(mapper);
        String invalidNumber = "abc";
        numberSerializer.serialize(invalidNumber, jsonGenerator, mapper.getSerializerProvider());
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer$NumberSerializerBase\",\"value\":123.456}";
        Number deserializedBigDecimal = objectMapper.readValue(jsonString, Number.class);
        assertEquals(new BigDecimal("123.456"), deserializedBigDecimal);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer$NumberSerializerBase\",\"value\":987654321098765432109876543210}";
        Number deserializedBigInteger = objectMapper.readValue(jsonString, Number.class);
        assertEquals(new BigInteger("987654321098765432109876543210"), deserializedBigInteger);
    }

    @Test
    public void testDeserializeLong() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer$NumberSerializerBase\",\"value\":123}";
        Number deserializedLong = objectMapper.readValue(jsonString, Number.class);
        assertEquals(new Long(123), deserializedLong);
    }

    @Test
    public void testDeserializeDouble() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer$NumberSerializerBase\",\"value\":123.45}";
        Number deserializedDouble = objectMapper.readValue(jsonString, Number.class);
        assertEquals(new Double(123.45), deserializedDouble);
    }

    @Test
    public void testDeserializeInteger() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer$NumberSerializerBase\",\"value\":123}";
        Number deserializedInteger = objectMapper.readValue(jsonString, Number.class);
        assertEquals(new Integer(123), deserializedInteger);
    }

    @Test
    public void testDeserializeByte() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer$NumberSerializerBase\",\"value\":127}";
        Number deserializedByte = objectMapper.readValue(jsonString, Number.class);
        assertEquals(new Byte((byte) 127), deserializedByte);
    }

    @Test
    public void testDeserializeShort() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer$NumberSerializerBase\",\"value\":123}";
        Number deserializedShort = objectMapper.readValue(jsonString, Number.class);
        assertEquals(new Short((short) 123), deserializedShort);
    }

    @Test
    public void testDeserializeNull() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer$NumberSerializerBase\",\"value\":null}";
        Number deserializedNull = objectMapper.readValue(jsonString, Number.class);
        assertEquals(null, deserializedNull);
    }
}