package com.example.junit.test.jackson;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NumberSerializerTest {

    private ObjectMapper mapper;

    public NumberSerializerTest() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerialize_Integer() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = 1;
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = "1";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_BigDecimal() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = new BigDecimal("3.14");
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = "3.14";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_BigInteger() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = new BigInteger("12345678901234567890");
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = "12345678901234567890";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_Long() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = 1L;
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = "1";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_Double() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = 1.5;
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = "1.5";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_Float() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = 1.5f;
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = "1.5";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = IOException.class)
    public void testSerialize_NegativeNumber() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = -1;

        try {
            numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testSerialize_Zero() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = 0;
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = "0";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_NaN() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = Double.NaN;
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = "NaN";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = IOException.class)
    public void testSerialize_InfiniteNumber() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = Double.POSITIVE_INFINITY;

        try {
            numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testSerialize_MinMaxIntegerValues() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = Integer.MIN_VALUE;
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        String expectedJson = Integer.MIN_VALUE + "";
        String actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);

        number = Integer.MAX_VALUE;
        numberSerializer.serialize(number, gen, mapper.getSerializerProvider());

        expectedJson = Integer.MAX_VALUE + "";
        actualJson = gen.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = IOException.class)
    public void testSerialize_NullValue() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        Number number = null;

        try {
            numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testGetSchema() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);

        JsonNode expectedSchema = mapper.readTree("{\"type\": \"number\", \"description\": \"Number\"}");
        JsonNode actualSchema = numberSerializer.getSchema(mapper.getSerializerProvider(), Integer.TYPE);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);

        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdSerializer.NumberSerializer(mapper.getSerializerProvider());
        numberSerializer.acceptJsonFormatVisitor(visitor, Integer.TYPE);

        // TODO: Add more test cases based on the expectedJsonFormatVisitor methods
    }
}