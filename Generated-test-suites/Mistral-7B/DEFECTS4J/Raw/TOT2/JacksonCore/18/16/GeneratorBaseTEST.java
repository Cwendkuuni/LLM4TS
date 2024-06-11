package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.StandardSerializers;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    private JsonGenerator generator;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getEncoder();
        generator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        assertEquals("true", generator.getOutputBuffer().toString());

        generator.writeBoolean(false);
        assertEquals("false", generator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        assertEquals("null", generator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(123);
        assertEquals("123", generator.getOutputBuffer().toString());

        generator.writeNumber(456L);
        assertEquals("456", generator.getOutputBuffer().toString());

        generator.writeNumber(1.23f);
        assertEquals("1.23", generator.getOutputBuffer().toString());

        generator.writeNumber(new BigDecimal("3.1415926535897932384626433832795"));
        assertEquals("3.1415926535897932384626433832795", generator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("test");
        assertEquals("test", generator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        generator.writeRawValue("test");
        assertEquals("test", generator.getOutputBuffer().toString());

        generator.writeRawValue("test\n");
        assertEquals("test\n", generator.getOutputBuffer().toString());

        generator.writeRawValue("\uD800\uDBFF\uDC00\uDFFF"); // Surrogate pair
        assertEquals("\uD800\uDBFF\uDC00\uDFFF", generator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObject() throws IOException {
        generator.writeObject(new Object());

        JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(generator.getOutputBuffer().toString().getBytes()));
        assertNotNull(parser.getValueAsObject());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteObjectWithError() throws IOException {
        generator.writeObject(new Object());
        generator.writeNumber(123); // Should throw JsonGenerationException
    }

    @Test
    public void testWriteBinary() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.BINARY_OUTPUT.getMask(), objectCodec, generator.getOutputContext());
        generator.writeBinary(JsonGenerator.Base64Variant.STANDARD, new ByteArrayInputStream("data".getBytes()), "data".getBytes().length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(generator.getOutputBuffer().toString().getBytes());

        Object codec = objectMapper.readValue(inputStream, Object.class);
        assertTrue(codec instanceof byte[]);
        assertEquals("data", new String((byte[]) codec));
    }

    @Test
    public void testWriteStringWithEscapeNonAscii() throws IOException {
        generator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), objectCodec);
        generator.writeString("test\u00E9"); // é
        assertEquals("test\\u00E9", generator.getOutputBuffer().toString());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        generator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec);
        generator.useDefaultPrettyPrinter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator.setOutput(outputStream);

        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeString("test");
        generator.writeFieldName("num");
        generator.writeNumber(123);
        generator.writeEndObject();

        JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(outputStream.toByteArray()));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("test", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("num", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(123, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    public void testDisableFeatures() throws IOException {
        generator = new GeneratorBase(0, objectCodec);

        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueWithError() throws IOException {
        generator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec);
        generator.writeValue("not a valid json");
    }

    @Test(expected = IOException.class)
    public void testWriteValueWithIOException() throws IOException {
        generator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec);
        generator.writeValue(new IOException("test error"));
    }
}