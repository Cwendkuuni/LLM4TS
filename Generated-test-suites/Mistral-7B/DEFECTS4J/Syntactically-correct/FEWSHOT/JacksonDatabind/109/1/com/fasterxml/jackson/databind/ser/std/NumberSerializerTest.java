package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

/**
 * Test cases for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = 123;
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123", jsonGenerator.getOutputContentAsString());

        number = 123L;
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123", jsonGenerator.getOutputContentAsString());

        number = 123.0;
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123.0", jsonGenerator.getOutputContentAsString());

        number = 123.0f;
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123.0", jsonGenerator.getOutputContentAsString());

        number = 123.0d;
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123.0", jsonGenerator.getOutputContentAsString());

        number = new BigDecimal("123.45");
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123.45", jsonGenerator.getOutputContentAsString());

        number = new BigInteger("12345678901234567890");
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("12345678901234567890", jsonGenerator.getOutputContentAsString());

        number = new Integer(123);
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123", jsonGenerator.getOutputContentAsString());

        number = new Byte((byte) 123);
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123", jsonGenerator.getOutputContentAsString());

        number = (short) 123;
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123", jsonGenerator.getOutputContentAsString());
    }

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = new BigDecimal("123.45");
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("123.45", jsonGenerator.getOutputContentAsString());
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = new BigInteger("12345678901234567890");
        numberSerializer.serialize(number, jsonGenerator, new ObjectMapper().getSerializationConfig());
        assertEquals("12345678901234567890", jsonGenerator.getOutputContentAsString());
    }

    @Test
    public void testContextualSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<?> jsonSerializer = mapper.findValueSerializer(BigDecimal.class);
        assertNotNull(jsonSerializer);
        assertFalse(jsonSerializer instanceof NumberSerializer);

        JsonSerializer<?> jsonSerializerForNumber = mapper.findValueSerializer(Number.class);
        assertTrue(jsonSerializerForNumber instanceof NumberSerializer);
    }

    @Test
    public void testDeserializeNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jsonParser = mapper.getFactory().createParser("123");
        Number number = mapper.readValue(jsonParser, Number.class);
        assertEquals(123, number.intValue());

        jsonParser = mapper.getFactory().createParser("123.0");
        number = mapper.readValue(jsonParser, Number.class);
        assertEquals(123.0, number.doubleValue());

        jsonParser = mapper.getFactory().createParser("123.45");
        number = mapper.readValue(jsonParser, Number.class);
        assertEquals(new BigDecimal("123.45"), number);

        jsonParser = mapper.getFactory().createParser("12345678901234567890");
        number = mapper.readValue(jsonParser, Number.class);
        assertEquals(new BigInteger("12345678901234567890"), number);

        jsonParser = mapper.getFactory().createParser("123.45e-5");
        number = mapper.readValue(jsonParser, Number.class);
        assertEquals(new BigDecimal("0.00012345"), number);

        jsonParser = mapper.getFactory().createParser("1.23e5");
        number = mapper.readValue(jsonParser, Number.class);
        assertEquals(new BigInteger("1230000"), number);
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonProcessingException, JsonParseException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jsonParser = mapper.getFactory().createParser("123.45");
        Number number = mapper.readValue(jsonParser, Number.class);
        assertEquals(new BigDecimal("123.45"), number);

        jsonParser = mapper.getFactory().createParser("123.45e-5");
        number = mapper.readValue(jsonParser, Number.class);
        assertEquals(new BigDecimal("0.00012345"), number);

        jsonParser = mapper.getFactory().createParser("1.23e5");
        number = mapper.readValue(jsonParser, Number.class);
        assertEquals(new BigDecimal("1230000"), number);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonProcessingException, JsonParseException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jsonParser = mapper.getFactory().createParser("12345678901234567890");
        Number number = mapper.readValue(jsonParser, Number.class);
        assertEquals(new BigInteger("12345678901234567890"), number);
    }

    @Test
    public void testDeserializeNumberWithJsonFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jsonParser = mapper.getFactory().createParser("{\"number\":123}");
        Number number = mapper.readValue(jsonParser, new NumberDeserializers.NumberDeserializer());
        assertEquals(123, number.intValue());

        jsonParser = mapper.getFactory().createParser("{\"number\":123.45}");
        number = mapper.readValue(jsonParser, new NumberDeserializers.NumberDeserializer());
        assertEquals(new BigDecimal("123.45"), number);

        jsonParser = mapper.getFactory().createParser("{\"number\":12345678901234567890}");
        number = mapper.readValue(jsonParser, new NumberDeserializers.NumberDeserializer());
        assertEquals(new BigInteger("12345678901234567890"), number);

        JsonFormat numberFormat = new JsonFormat.NumberFormat();
        numberFormat.setShape(JsonFormat.Shape.INT);
        JsonParser jsonParserWithFormat = mapper.getFactory().createParser("{\"number\":123.45}");
        try {
            number = mapper.readValue(jsonParserWithFormat, new NumberDeserializers.NumberDeserializer());
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Success
        }

        JsonParser jsonParserWithInvalidFormat = mapper.getFactory().createParser("{\"number\":foo}");
        try {
            number = mapper.readValue(jsonParserWithInvalidFormat, new NumberDeserializers.NumberDeserializer());
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Success
        }
    }

    @Test
    public void testDeserializeBigDecimalWithJsonFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jsonParser = mapper.getFactory().createParser("{\"number\":123.45}");
        Number number = mapper.readValue(jsonParser, new NumberDeserializers.NumberDeserializer());
        assertEquals(new BigDecimal("123.45"), number);

        JsonFormat numberFormat = new JsonFormat.NumberFormat();
        numberFormat.setShape(JsonFormat.Shape.NUMBER);
        JsonParser jsonParserWithFormat = mapper.getFactory().createParser("{\"number\":123.45}");
        try {
            number = mapper.readValue(jsonParserWithFormat, new NumberDeserializers.NumberDeserializer());
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Success
        }

        JsonParser jsonParserWithInvalidFormat = mapper.getFactory().createParser("{\"number\":foo}");
        try {
            number = mapper.readValue(jsonParserWithInvalidFormat, new NumberDeserializers.NumberDeserializer());
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Success
        }
    }

    @Test
    public void testDeserializeBigIntegerWithJsonFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jsonParser = mapper.getFactory().createParser("{\"number\":12345678901234567890}");
        Number number = mapper.readValue(jsonParser, new NumberDeserializers.NumberDeserializer());
        assertEquals(new BigInteger("12345678901234567890"), number);

        JsonFormat numberFormat = new JsonFormat.NumberFormat();
        numberFormat.setShape(JsonFormat.Shape.INT);
        JsonParser jsonParserWithFormat = mapper.getFactory().createParser("{\"number\":12345678901234567890}");
        try {
            number = mapper.readValue(jsonParserWithFormat, new NumberDeserializers.NumberDeserializer());
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Success
        }

        JsonParser jsonParserWithInvalidFormat = mapper.getFactory().createParser("{\"number\":foo}");
        try {
            number = mapper.readValue(jsonParserWithInvalidFormat, new NumberDeserializers.NumberDeserializer());
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Success
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializationConfig(), new Object());

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class.getType());

        assertEquals("number", visitor.getCurrentName());
        assertTrue(visitor.isExpectedStartVector());
        assertFalse(visitor.isExpectedEndVector());
    }
}