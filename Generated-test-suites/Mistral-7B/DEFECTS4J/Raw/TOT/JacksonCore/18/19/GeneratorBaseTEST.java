package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    @Test
    public void testDefaultConfiguration() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(null, null);

        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeString("hello");
        generator.writeEndObject();
        generator.flush();

        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertEquals("{\"field1\":\"hello\"}", result);

        generator.close();
        assertFalse(generator.isOpen());
    }

    @Test
    public void testWriteNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);

        generator.writeNumber(42);
        generator.writeNumber(3.14);
        generator.writeNumber(BigDecimal.valueOf(1.23456));

        generator.flush();
        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertEquals("42,3.14,1.23456", result);

        generator.close();
    }

    @Test
    public void testWriteBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(null, null);

        generator.writeBoolean(true);
        generator.writeBoolean(false);

        generator.flush();
        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertEquals("true,false", result);

        generator.close();
    }

    @Test
    public void testWriteNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(null, null);

        generator.writeNull();

        generator.flush();
        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertNull(result);

        generator.close();
    }

    @Test
    public void testWriteString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(null, null);

        generator.writeString("hello");

        generator.flush();
        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertEquals("hello", result);

        generator.close();
    }

    @Test
    public void testWriteRawValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(null, null);

        generator.writeRawValue("hello world");

        generator.flush();
        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertEquals("hello world", result);

        generator.close();
    }

    @Test
    public void testWriteBinary() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(null, null);

        byte[] data = "hello world".getBytes();

        generator.writeBinary(JsonWriteContext.Base64Variant.STANDARD_BASE64, new ByteArrayInputStream(data), data.length);

        generator.flush();
        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertEquals("aGVsbG8=", result);

        generator.close();
    }

    @Test
    public void testWriteObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(null, null);

        String json = "{ \"name\": \"John\", \"age\": 30 }";
        Object obj = mapper.readValue(new ByteArrayInputStream(json.getBytes()), Object.class);

        generator.writeObject(obj);

        generator.flush();
        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertEquals(json, result);

        generator.close();
    }

    @Test
    public void testDisableWriteNumbersAsStrings() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);

        generator.writeNumber(42);

        generator.flush();
        String result = mapper.readValue(new ByteArrayInputStream(generator.getOutputBuffer().getBytes()), String.class);
        assertEquals("42", result);

        generator.close();
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(null, null);

        generator.writeString("{abc:");
        generator.flush();

        generator.close();
    }
}