package com.yourpackage.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@JacksonStdImpl
public class NumberSerializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);

        Number number = 123;
        numberSerializer.serialize(number, jsonGenerator, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("123"));
        Number deserializedNumber = numberSerializer.deserialize(jsonParser, Number.class, null);

        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);

        BigDecimal bigDecimal = new BigDecimal("123.45");
        numberSerializer.serialize(bigDecimal, jsonGenerator, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("123.45"));
        Number deserializedNumber = numberSerializer.deserialize(jsonParser, Number.class, null);

        assertEquals(bigDecimal, deserializedNumber);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);

        BigInteger bigInteger = BigInteger.valueOf(1234567890123456789L);
        numberSerializer.serialize(bigInteger, jsonGenerator, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("1234567890123456789"));
        Number deserializedNumber = numberSerializer.deserialize(jsonParser, Number.class, null);

        assertEquals(bigInteger, deserializedNumber);
    }

    @Test
    public void testSerializeOtherNumberTypes() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);

        Integer intNumber = 123;
        Long longNumber = 1234567890123456789L;
        Double doubleNumber = 123.45;
        Float floatNumber = 123.45F;
        Byte byteNumber = (byte) 123;
        Short shortNumber = (short) 123;

        numberSerializer.serialize(intNumber, jsonGenerator, null);
        numberSerializer.serialize(longNumber, jsonGenerator, null);
        numberSerializer.serialize(doubleNumber, jsonGenerator, null);
        numberSerializer.serialize(floatNumber, jsonGenerator, null);
        numberSerializer.serialize(byteNumber, jsonGenerator, null);
        numberSerializer.serialize(shortNumber, jsonGenerator, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("123")); // int
        Number deserializedIntNumber = numberSerializer.deserialize(jsonParser, Number.class, null);
        assertEquals((int) intNumber, deserializedIntNumber.intValue());

        jsonParser = objectMapper.getFactory().createParser(new StringReader("1234567890123456789")); // long
        Number deserializedLongNumber = numberSerializer.deserialize(jsonParser, Number.class, null);
        assertEquals(longNumber, deserializedLongNumber);

        jsonParser = objectMapper.getFactory().createParser(new StringReader("123.45")); // double
        Number deserializedDoubleNumber = numberSerializer.deserialize(jsonParser, Number.class, null);
        assertEquals(doubleNumber, deserializedDoubleNumber);

        jsonParser = objectMapper.getFactory().createParser(new StringReader("123.45")); // float
        Number deserializedFloatNumber = numberSerializer.deserialize(jsonParser, Number.class, null);
        assertEquals((float) floatNumber, deserializedFloatNumber, 0.01f);

        jsonParser = objectMapper.getFactory().createParser(new StringReader("123")); // byte
        Number deserializedByteNumber = numberSerializer.deserialize(jsonParser, Number.class, null);
        assertEquals((byte) byteNumber, deserializedByteNumber.intValue());

        jsonParser = objectMapper.getFactory().createParser(new StringReader("123")); // short
        Number deserializedShortNumber = numberSerializer.deserialize(jsonParser, Number.class, null);
        assertEquals((short) shortNumber, deserializedShortNumber.intValue());
    }

    @Test(expected = IOException.class)
    public void testSerializeString() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);

        String stringNumber = "123.45";
        numberSerializer.serialize(stringNumber, jsonGenerator, null);
    }

    @Test
    public void testGetSchema() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonNode schema = numberSerializer.getSchema(null, null);

        assertEquals("number", schema.getClass().getSimpleName());
        assertTrue(schema.isObject());
        assertTrue(schema.has("type"));
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.has("properties"));
        assertTrue(schema.get("properties").isObject());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = null;

        if (numberSerializer._isInt) {
            jsonFormatVisitorWrapper = new IntJsonFormatVisitor(null, null);
        } else {
            Class<?> handledType = numberSerializer.handledType();
            if (handledType == BigDecimal.class) {
                jsonFormatVisitorWrapper = new FloatJsonFormatVisitor(null, null);
            } else {
                // Unhandled types should not throw an exception
                // as it is mentioned in the code comment: "bit unclear what to call..."
            }
        }

        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }
}