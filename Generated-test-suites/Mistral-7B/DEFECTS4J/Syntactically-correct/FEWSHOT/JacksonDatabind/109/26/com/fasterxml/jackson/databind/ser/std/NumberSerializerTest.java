package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test cases for {@link NumberSerializer}.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testSerializationInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator gen = createJsonGenerator();

        Integer number = 123;
        serializer.serialize(number, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(number, parser.readValueAs(Integer.class));
    }

    @Test
    public void testSerializationLong() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator gen = createJsonGenerator();

        Long number = 456L;
        serializer.serialize(number, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(number, parser.readValueAs(Long.class));
    }

    @Test
    public void testSerializationFloat() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator gen = createJsonGenerator();

        Float number = 1.123F;
        serializer.serialize(number, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(number, parser.readValueAs(Float.class));
    }

    @Test
    public void testSerializationDouble() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator gen = createJsonGenerator();

        Double number = 1.234;
        serializer.serialize(number, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(number, parser.readValueAs(Double.class));
    }

    @Test
    public void testSerializationBigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator gen = createJsonGenerator();

        BigInteger number = BigInteger.valueOf(567L);
        serializer.serialize(number, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(number, parser.readValueAs(BigInteger.class));
    }

    @Test
    public void testSerializationBigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator gen = createJsonGenerator();

        BigDecimal number = new BigDecimal("123.45");
        serializer.serialize(number, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(number, parser.readValueAs(BigDecimal.class));
    }

    @Test
    public void testSerializationBigDecimalString() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator gen = createJsonGenerator();

        BigDecimal number = new BigDecimal("123.45");
        String numberString = number.toString();
        serializer.serialize(numberString, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(number, parser.readValueAs(BigDecimal.class));
    }

    @Test
    public void testSerializationUnknownNumberType() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator gen = createJsonGenerator();

        Object number = new Object() {
            @Override
            public String toString() {
                return "456";
            }
        };
        serializer.serialize(number, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(456, parser.readValueAs(Integer.class));
    }

    @Test
    public void testSerializationContextualSerializer() throws Exception {
        JsonFormat format = JsonFormat.value()
                .withShape(JsonFormat.Shape.STRING)
                .build();

        NumberSerializer serializer = new NumberSerializer(Number.class);

        JsonSerializer<Number> contextualSerializer = serializer.createContextual(new SerializerProvider(), null);

        JsonGenerator gen = createJsonGenerator();

        BigDecimal number = new BigDecimal("123.45");
        contextualSerializer.serialize(number, gen, new SerializerProvider());

        JsonParser parser = createJsonParser(serializer.getSchema(null, null));
        assertEquals(number, parser.readValueAs(BigDecimal.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializationShouldFailForNull() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator gen = createJsonGenerator();

        serializer.serialize(null, gen, new SerializerProvider());
    }

    private JsonGenerator createJsonGenerator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createGenerator();
    }

    private JsonParser createJsonParser(JsonNode schema) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readerForTree(new StringReader(schema.toString()));
    }
}