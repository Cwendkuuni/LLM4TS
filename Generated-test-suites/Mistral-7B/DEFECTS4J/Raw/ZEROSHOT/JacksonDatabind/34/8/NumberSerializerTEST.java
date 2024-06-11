package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind. TYPE_MAP_KEY;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer.java
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = new JsonFactory().createGenerator();
        NumberSerializer serializer = NumberSerializer.instance;

        // Test serialization for primitive numbers
        SerializerProvider provider = mapper.getSerializerProvider();
        serializer.serialize(1, generator, provider);
        serializer.serialize(1.0F, generator, provider);
        serializer.serialize(1.0D, generator, provider);
        generator.flush();
        String json = generator.getOutputContent();
        assertEquals("1.0", json);

        // Test serialization for BigDecimal and BigInteger
        BigDecimal bigDecimal = new BigDecimal("123.456");
        serializer.serialize(bigDecimal, generator, provider);
        generator.flush();
        json = generator.getOutputContent();
        assertEquals("123.456", json);

        BigInteger bigInteger = new BigInteger("9876543210");
        serializer.serialize(bigInteger, generator, provider);
        generator.flush();
        json = generator.getOutputContent();
        assertEquals("9876543210", json);
    }

    @Test
    public void testDeserializeNumber() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.getParser("\"123.456\"");

        // Test deserialization for primitive numbers
        TypeDeserializer numberDeserializer = mapper.findValueDeserializer(JsonParser.NumberType.NUMBER, null);
        Number number = (Number) numberDeserializer.deserialize(parser, JsonMappingContext.StdContext.instance(), null);
        assertEquals(123.456, number.doubleValue(), 0.0);

        // Test deserialization for BigDecimal and BigInteger
        TypeDeserializer bigDecimalDeserializer = mapper.findValueDeserializer(TYPE_MAP_KEY.Double, null);
        BigDecimal deserializedBigDecimal = (BigDecimal) bigDecimalDeserializer.deserialize(parser, JsonMappingContext.StdContext.instance(), null);
        assertEquals("123.456", deserializedBigDecimal.toString());

        TypeDeserializer bigIntegerDeserializer = mapper.findValueDeserializer(TYPE_MAP_KEY.BigInteger, null);
        BigInteger deserializedBigInteger = (BigInteger) bigIntegerDeserializer.deserialize(parser, JsonMappingContext.StdContext.instance(), null);
        assertEquals("9876543210", deserializedBigInteger.toString());
    }

    @Test
    public void testSerializationAndDeserializationRoundTrip() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test round trip for primitive numbers
        int intValue = 123;
        double floatValue = 123.456;
        double doubleValue = 123.456;
        int deserializedIntValue;
        float deserializedFloatValue;
        double deserializedDoubleValue;

        String json = mapper.writeValueAsString(intValue);
        deserializedIntValue = mapper.readValue(json, int.class);
        assertEquals(intValue, deserializedIntValue);

        json = mapper.writeValueAsString(floatValue);
        deserializedFloatValue = mapper.readValue(json, float.class);
        assertEquals(floatValue, deserializedFloatValue, 0.001f);

        json = mapper.writeValueAsString(doubleValue);
        deserializedDoubleValue = mapper.readValue(json, double.class);
        assertEquals(doubleValue, deserializedDoubleValue, 0.0001);

        // Test round trip for BigDecimal and BigInteger
        BigDecimal bigDecimal = new BigDecimal("123.456");
        String bigDecimalJson = mapper.writeValueAsString(bigDecimal);
        BigDecimal deserializedBigDecimal = mapper.readValue(bigDecimalJson, BigDecimal.class);
        assertEquals(bigDecimal, deserializedBigDecimal);

        BigInteger bigInteger = new BigInteger("9876543210");
        String bigIntegerJson = mapper.writeValueAsString(bigInteger);
        BigInteger deserializedBigInteger = mapper.readValue(bigIntegerJson, BigInteger.class);
        assertEquals(bigInteger, deserializedBigInteger);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.getParser("{\"numberFormat\":\"number\"}");

        NumberSerializer serializer = NumberSerializer.instance;
        serializer.acceptJsonFormatVisitor(parser, mapper.getTypeFactory().constructType(Number.class));
    }
}