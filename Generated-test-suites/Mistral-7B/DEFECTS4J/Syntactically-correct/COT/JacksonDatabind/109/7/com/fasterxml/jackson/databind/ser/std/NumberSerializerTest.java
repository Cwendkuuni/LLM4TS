package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test cases for NumberSerializer class
 */
public class NumberSerializerTest {

    private static final ObjectMapper mapper = new JsonMapper();

    @Test
    public void testSerializeNumber() {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();

        serializeNumber(serializer, gen, new Integer(1));
        serializeNumber(serializer, gen, new Long(1));
        serializeNumber(serializer, gen, new Float(1.0f));
        serializeNumber(serializer, gen, new Double(1.0));
        serializeNumber(serializer, gen, new BigInteger("1"));
        serializeNumber(serializer, gen, new BigDecimal("1"));

        assertSerialization("1", gen.asString());
    }

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();

        serializeBigDecimal(serializer, gen, new BigDecimal("1.1"));
        serializeBigDecimal(serializer, gen, new BigDecimal("1.1E-2"));
        serializeBigDecimal(serializer, gen, new BigDecimal("1.123456789012345678901234567890"));

        assertSerialization("1.1", gen.asString());
        assertSerialization("1.1E-2", gen.asString());
        assertSerialization("1.123456789012345678901234567890", gen.asString());
    }

    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();

        serializeBigInteger(serializer, gen, new BigInteger("1"));
        serializeBigInteger(serializer, gen, new BigInteger("1E10"));
        serializeBigInteger(serializer, gen, new BigInteger("123456789012345678901234567890"));

        assertSerialization("1", gen.asString());
        assertSerialization("1E10", gen.asString());
        assertSerialization("123456789012345678901234567890", gen.asString());
    }

    @Test
    public void testSerializeJsonFormat() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectReader reader = mapper.readerForValueTypeOf(Number.class);

        JsonParser parser = createJsonParser("{\"number\":1}");
        Number number = reader.readValue(parser);
        parser.nextToken(); // consume '}'

        JsonParser parserWithFormat = createJsonParser("{\"number\":{\"format\":\"%!d\"}}");
        Number numberWithFormat = reader.readValue(parserWithFormat);
        parserWithFormat.nextToken(); // consume '}'

        assertEquals(number.longValue(), numberWithFormat.longValue());

        parser = createJsonParser("{\"number\":\"1.1\"}");
        number = reader.readValue(parser);
        parser.nextToken(); // consume '}'

        JsonParser parserWithFormatDouble = createJsonParser("{\"number\":{\"format\":\"%!f\"}}");
        Number numberWithFormatDouble = reader.readValue(parserWithFormatDouble);
        parserWithFormatDouble.nextToken(); // consume '}'

        assertEquals(number.doubleValue(), numberWithFormatDouble.doubleValue(), 0.00001);
    }

    @Test(expected = JsonProcessingException.class)
    public void testFailSerializeNull() {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();

        serializer.serialize(null, gen, null);
    }

    @Test(expected = JsonProcessingException.class)
    public void testFailSerializeString() {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();

        serializer.serialize("1", gen, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testFailCreateContextualJsonMappingException() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        ObjectReader reader = mapper.readerForValueTypeOf(Number.class);

        JsonParser parser = createJsonParser("{}");
        Number number = null;
        try {
            number = reader.readValue(parser);
        } catch (JsonProcessingException e) {
            // expected
        }

        ObjectReader readerWithContextual = mapper.readerForValueTypeOf(Number.class)
                .withContextualSerializer(NumberSerializer.class);

        JsonParser parserWithContextual = createJsonParser("{}");
        Number numberWithContextual = null;
        try {
            numberWithContextual = readerWithContextual.readValue(parserWithContextual);
        } catch (JsonMappingException e) {
            // expected
        }

        assertNull(number);
        assertNull(numberWithContextual);
    }

    @Test(expected = JsonMappingException.class)
    public void testFailCreateContextualJsonMappingExceptionWithBigDecimalFormat() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        ObjectReader reader = mapper.readerForValueTypeOf(Number.class);

        JsonParser parser = createJsonParser("{\"number\":{\"format\":\"%!d\"}}");
        Number number = null;
        try {
            number = reader.readValue(parser);
        } catch (JsonProcessingException e) {
            // expected
        }

        ObjectReader readerWithContextual = mapper.readerForValueTypeOf(Number.class)
                .withContextualSerializer(NumberSerializer.class);

        JsonParser parserWithContextual = createJsonParser("{\"number\":{\"format\":\"%!d\",\"value\":\"1.1\"}}");
        Number numberWithContextual = null;
        try {
            numberWithContextual = readerWithContextual.readValue(parserWithContextual);
        } catch (JsonMappingException e) {
            // expected
        }

        assertNull(number);
        assertNull(numberWithContextual);
    }

    // Helper methods

    private JsonGenerator createJsonGenerator() throws IOException {
        return mapper.getFactory().createGenerator(System.out, null);
    }

    private JsonParser createJsonParser(String content) throws IOException {
        return mapper.getFactory().createParser(new StringReader(content));
    }

    private void assertSerialization(String expected, String actual) {
        assertEquals(expected, actual);
    }

    private void serializeNumber(NumberSerializer serializer, JsonGenerator gen, Number number) throws IOException {
        serializer.serialize(number, gen, null);
    }

    private void serializeBigDecimal(NumberSerializer serializer, JsonGenerator gen, BigDecimal bigDecimal) throws IOException {
        serializer.serialize(bigDecimal, gen, null);
    }

    private void serializeBigInteger(NumberSerializer serializer, JsonGenerator gen, BigInteger bigInteger) throws IOException {
        serializer.serialize(bigInteger, gen, null);
    }
}