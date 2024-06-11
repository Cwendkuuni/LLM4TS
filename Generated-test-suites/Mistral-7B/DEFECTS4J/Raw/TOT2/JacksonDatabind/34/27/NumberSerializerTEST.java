// Your complete test file should look like this, with all the test cases included
package com.example; // Replace with your package name

import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.math.*;

public class NumberSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerialize_Integer() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        Integer number = 42;
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        JsonNode expectedJson = MAPPER.readTree(generator.getOutputStream());

        JsonNode actualJson = MAPPER.readTree(MAPPER.writeValueAsString(number));
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        BigDecimal number = new BigDecimal("123.45");
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        JsonNode expectedJson = MAPPER.readTree(generator.getOutputStream());

        JsonNode actualJson = MAPPER.readTree(MAPPER.writeValueAsString(number));
        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = IOException.class)
    public void testSerialize_NullInput() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        Number number = null;
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        BigInteger number = BigInteger.valueOf(1234567890123456789L);
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        JsonNode expectedJson = MAPPER.readTree(generator.getOutputStream());

        JsonNode actualJson = MAPPER.readTree(MAPPER.writeValueAsString(number));
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_Long() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        Long number = Long.MAX_VALUE;
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        JsonNode expectedJson = MAPPER.readTree(generator.getOutputStream());

        JsonNode actualJson = MAPPER.readTree(MAPPER.writeValueAsString(number));
        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = IOException.class)
    public void testSerialize_NegativeNumber() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        Integer number = -42;
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
    }

    @Test
    public void testSerialize_CustomNumber() throws IOException {
        // Add test case for a custom Number class
    }

    @Test
    public void testSerialize_NaN() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        Double number = Double.NaN;
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        JsonNode expectedJson = MAPPER.readTree(generator.getOutputStream());

        JsonNode actualJson = MAPPER.readTree(MAPPER.writeValueAsString(number));
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_Infinity() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        Double number = Double.POSITIVE_INFINITY;
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        JsonNode expectedJson = MAPPER.readTree(generator.getOutputStream());

        JsonNode actualJson = MAPPER.readTree(MAPPER.writeValueAsString(number));
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_EmptyString() throws IOException {
        NumberSerializer serializer = new NumberSerializer(String.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        String number = "";
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        JsonNode expectedJson = MAPPER.readTree(generator.getOutputStream());

        JsonNode actualJson = MAPPER.readTree(MAPPER.writeValueAsString(number));
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_EmptyArray() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number[].class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();

        Number[] numbers = {};
        serializer.serialize(numbers, generator, MAPPER.getSerializerProvider());
        JsonNode expectedJson = MAPPER.readTree(generator.getOutputStream());

        JsonNode actualJson = MAPPER.readTree(MAPPER.writeValueAsString(numbers));
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_ClosedJsonGenerator() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();
        generator.close();

        Integer number = 42;
        try {
            serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(expected = IOException.class)
    public void testSerialize_IOException() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = MAPPER.getFactory().createGenerator();
        ObjectWriter writer = MAPPER.getWriterWithView(JsonTestingSerializer.class);

        try {
            writer.writeValue(generator, new Object());
        } catch (JsonProcessingException e) {
            throw new IOException(e);
        }

        Number number = 42;
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
    }
}