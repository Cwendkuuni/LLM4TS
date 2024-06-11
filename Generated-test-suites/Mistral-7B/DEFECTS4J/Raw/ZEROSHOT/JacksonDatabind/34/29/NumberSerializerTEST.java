package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test cases for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerializeInteger() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new SerializerProvider().findSerializerProvider();
        JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out, null);
        provider.setCurrentOutput(generator);

        Number number = 1;
        serializer.serialize(number, provider);
        generator.flush();

        assertEquals("1", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new SerializerProvider().findSerializerProvider();
        JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out, null);
        provider.setCurrentOutput(generator);

        Number number = 1L;
        serializer.serialize(number, provider);
        generator.flush();

        assertEquals("1", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new SerializerProvider().findSerializerProvider();
        JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out, null);
        provider.setCurrentOutput(generator);

        Number number = BigInteger.valueOf(12345);
        serializer.serialize(number, provider);
        generator.flush();

        assertEquals("12345", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new SerializerProvider().findSerializerProvider();
        JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out, null);
        provider.setCurrentOutput(generator);

        Number number = new BigDecimal("123.45");
        serializer.serialize(number, provider);
        generator.flush();

        assertEquals("123.45", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeOtherNumberTypes() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new SerializerProvider().findSerializerProvider();
        JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out, null);
        provider.setCurrentOutput(generator);

        Number number = (byte) 1;
        serializer.serialize(number, provider);
        generator.flush();

        assertEquals("1", generator.getOutputContent().toString());

        number = (short) 123;
        serializer.serialize(number, provider);
        generator.flush();

        assertEquals("123", generator.getOutputContent().toString());

        number = 3.14;
        serializer.serialize(number, provider);
        generator.flush();

        assertEquals("3.14", generator.getOutputContent().toString());

        number = 1.23F;
        serializer.serialize(number, provider);
        generator.flush();

        assertEquals("1.23", generator.getOutputContent().toString());
    }

    @Test
    public void testDeserialize() throws IOException, JsonMappingException {
        SerializerProvider provider = new SerializerProvider().findSerializerProvider();
        JsonParser parser = MAPPER.getFactory().createParser("123");
        provider.setCurrentInput(parser);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Number number = mapper.readValue(provider, Number.class);

        assertEquals(123, number.intValue());
    }

    @Test
    public void testDeserializeBigInteger() throws IOException, JsonMappingException {
        SerializerProvider provider = new SerializerProvider().findSerializerProvider();
        JsonParser parser = MAPPER.getFactory().createParser("12345");
        provider.setCurrentInput(parser);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Number number = mapper.readValue(provider, Number.class);

        assertEquals(new BigInteger("12345"), number);
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException, JsonMappingException {
        SerializerProvider provider = new SerializerProvider().findSerializerProvider();
        JsonParser parser = MAPPER.getFactory().createParser("123.45");
        provider.setCurrentInput(parser);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Number number = mapper.readValue(provider, Number.class);

        assertEquals(new BigDecimal("123.45"), number);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = null; // should not be reached

        serializer.acceptJsonFormatVisitor(visitor, null);
    }
}