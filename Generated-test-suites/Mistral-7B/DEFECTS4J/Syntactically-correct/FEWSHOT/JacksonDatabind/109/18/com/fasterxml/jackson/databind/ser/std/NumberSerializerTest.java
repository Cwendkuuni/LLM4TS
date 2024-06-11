package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * Test cases for NumberSerializer class
 */
@JacksonStdImpl
public class NumberSerializerTest {

    /**
     * Test for static instance creation
     */
    @Test
    public void testSerializationForNumber() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        assertNotNull(serializer);
    }

    /**
     * Test serialization for Integer, Long, Float, Double, BigDecimal and BigInteger
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testSerializeNumber() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();

        Number numberList[] = {new Integer(1), new Long(2), new Float(3.5f), new Double(4.5), new BigDecimal("5.6"), new BigInteger("7")};

        for (Number number : numberList) {
            String json = mapper.writeValueAsString(number);
            JsonParser parser = mapper.getFactory().createParser(json);
            Number deserialized = mapper.readValue(parser, Number.class);

            assertEquals(number, deserialized);
        }
    }

    /**
     * Test serialization for BigDecimal with custom JsonFormat
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public void testSerializeBigDecimalWithJsonFormat() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BigDecimal bd = new BigDecimal("1234567890.1234567890");
        String json = mapper.writeValueAsString(bd);
        JsonParser parser = mapper.getFactory().createParser(json);
        BigDecimal deserialized = mapper.readValue(parser, BigDecimal.class);
        assertEquals(bd, deserialized);
    }

    /**
     * Test serialization for BigInteger with custom JsonFormat
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public void testSerializeBigIntegerWithJsonFormat() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BigInteger bi = new BigInteger("12345678901234567890");
        String json = mapper.writeValueAsString(bi);
        JsonParser parser = mapper.getFactory().createParser(json);
        BigInteger deserialized = mapper.readValue(parser, BigInteger.class);
        assertEquals(bi, deserialized);
    }

    /**
     * Test serialization for Number with custom JsonFormat
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public void testSerializeNumberWithJsonFormat() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Number number = new BigDecimal("1234567890.1234567890");
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialization for Number using createContextual method
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testSerializeNumberUsingContextual() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Number number = new BigDecimal("1234567890.1234567890");

        JsonSerializer<Number> contextualSerializer = mapper.getSerializerProvider().findValueSerializer(Number.class, null);
        assertNotNull(contextualSerializer);

        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(number, deserialized);
    }
}