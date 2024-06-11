package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtil.createParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test cases for {@link NumberSerializer}.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        // Test serialization of various types of Numbers
        testSerialize(serializer, new Integer(1), "1");
        testSerialize(serializer, new Long(1), "1");
        testSerialize(serializer, new Double(1.0), "1.0");
        testSerialize(serializer, new Float(1.0f), "1.0");
        testSerialize(serializer, new Byte((byte) 1), "1");
        testSerialize(serializer, new Short((short) 1), "1");
        testSerialize(serializer, new BigDecimal("1"), "1");
        testSerialize(serializer, new BigInteger("1"), "1");

        // Test serialization of BigDecimal and BigInteger with custom formats
        testSerializeWithCustomFormat(serializer, new BigDecimal("1.23"), "number", JsonParser.NumberType.FLOAT);
        testSerializeWithCustomFormat(serializer, new BigInteger("12345678901234567890"), "number", JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testDeserializeNumber() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        // Test deserialization of various types of Numbers from JSON strings
        Number number = MAPPER.readValue("1", Number.class);
        assertEquals(Integer.class, number.getClass());
        assertEquals(1, ((Number) number).intValue());

        number = MAPPER.readValue("1", Number.class);
        assertEquals(Long.class, number.getClass());
        assertEquals(1, ((Number) number).longValue());

        number = MAPPER.readValue("1.0", Number.class);
        assertEquals(Double.class, number.getClass());
        assertEquals(1.0, ((Number) number).doubleValue());

        number = MAPPER.readValue("1.0", Number.class);
        assertEquals(Float.class, number.getClass());
        assertEquals(1.0f, ((Number) number).floatValue());

        number = MAPPER.readValue("1", Number.class);
        assertEquals(Byte.class, number.getClass());
        assertEquals((byte) 1, ((Number) number).byteValue());

        number = MAPPER.readValue("1", Number.class);
        assertEquals(Short.class, number.getClass());
        assertEquals((short) 1, ((Number) number).shortValue());

        number = MAPPER.readValue("1", Number.class);
        assertEquals(BigDecimal.class, number.getClass());
        assertEquals(new BigDecimal("1"), number);

        number = MAPPER.readValue("1", Number.class);
        assertEquals(BigInteger.class, number.getClass());
        assertEquals(new BigInteger("1"), number);

        // Test deserialization of BigDecimal and BigInteger with custom formats
        Number numberWithCustomFormat = MAPPER.readValue(TEST_JSON_NUMBER_WITH_CUSTOM_FORMAT, Number.class);
        assertEquals(BigDecimal.class, numberWithCustomFormat.getClass());
        assertEquals(new BigDecimal("1.23"), numberWithCustomFormat);

        numberWithCustomFormat = MAPPER.readValue(TEST_JSON_BIG_INTEGER_WITH_CUSTOM_FORMAT, Number.class);
        assertEquals(BigInteger.class, numberWithCustomFormat.getClass());
        assertEquals(new BigInteger("12345678901234567890"), numberWithCustomFormat);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        // Test acceptJsonFormatVisitor method with various Number types
        JsonFormatVisitorWrapper visitor = createJsonFormatVisitor("number", JsonParser.NumberType.BIG_DECIMAL);
        serializer.acceptJsonFormatVisitor(visitor, Number.class.getType());

        if (serializer._isInt) {
            assertEquals(JsonNodeType.NUMBER, visitor.getFormatNode().getType());
            assertEquals("number", visitor.getFormatNode().getTextValue());
            assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.getNumberType());
        } else {
            // Need to test this when _isInt is false
            // For now, just check that the method doesn't throw an exception
            serializer.acceptJsonFormatVisitor(visitor, Number.class.getType());
        }
    }

    private void testSerialize(NumberSerializer serializer, Number number, String expectedJson) throws IOException {
        JsonGenerator generator = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), MAPPER.getSerializerProvider());
        serializer.serialize(number, generator, MAPPER.getSerializerProvider());
        generator.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) generator.getOutputStream();
        assertEquals(expectedJson, outputStream.toString());
    }

    private void testSerializeWithCustomFormat(NumberSerializer serializer, Number number, String format, JsonParser.NumberType numberType) throws IOException {
        String json = String.format("{\"@class\":\"%s\",\"numberFormat\":{\"format\":\"%s\",\"numberType\":\"%s\"}}",
            Number.class.getName(), format, numberType.name());

        JsonParser parser = createParser(json);
        ObjectReader reader = MAPPER.readerFor(Number.class);
        Number result = reader.readValue(parser);

        JsonNode formatNode = parser.readValueAsTree("/numberFormat");
        assertEquals(JsonNodeType.OBJECT, formatNode.getType());
        ObjectNode formatObject = (ObjectNode) formatNode;
        assertEquals("number", formatObject.get("format").getTextValue());
        assertEquals(numberType.name(), formatObject.get("numberType").getTextValue());

        testSerialize(serializer, number, json);
    }

    private JsonFormatVisitorWrapper createJsonFormatVisitor(String format, JsonParser.NumberType numberType) {
        return new JsonNumberFormatVisitor(format, numberType);
    }

    private static final String TEST_JSON_NUMBER_WITH_CUSTOM_FORMAT = "{\"@class\":\"java.math.BigDecimal\",\"numberFormat\":{\"format\":\"number\",\"numberType\":\"Float\"}}";
    private static final String TEST_JSON_BIG_INTEGER_WITH_CUSTOM_FORMAT = "{\"@class\":\"java.math.BigInteger\",\"numberFormat\":{\"format\":\"number\",\"numberType\":\"BigInteger\"}}";
}