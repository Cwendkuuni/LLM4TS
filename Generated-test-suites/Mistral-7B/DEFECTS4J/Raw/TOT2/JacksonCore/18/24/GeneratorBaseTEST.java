package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class GeneratorBaseTest {

    private JsonGenerator generator;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        if (generator != null) {
            generator.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testIsEnabled_WriteNumbersAsStrings() throws IOException {
        JsonGenerator generator = new GeneratorBase(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.close();
    }

    @Test
    public void testDisable_WriteNumbersAsStrings() throws IOException {
        JsonGenerator generator = new GeneratorBase(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        generator.disable(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.close();
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeStartObject();
        generator.writeString("Hello");
        generator.writeEndObject();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals("Hello", parser.getValueAsString());
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertNull(parser.getValueAsString());
    }

    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(123);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals(123, parser.getValueAsInt());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals(true, parser.getValueAsBoolean());
    }

    @Test
    public void testWriteObject() throws IOException, JsonMappingException {
        MyClass obj = new MyClass();
        generator.writeObject(obj);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectMapper mapper = new ObjectMapper();
        MyClass parsedObj = mapper.readValue(inputStream, MyClass.class);
        assertEquals(obj, parsedObj);
    }

    @Test(expected = IOException.class)
    public void testWriteArray_NullArray() throws IOException {
        generator.writeStartArray();
        generator.writeNull();
        generator.writeEndArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        parser.nextToken(); // Consume start array
        parser.nextToken(); // Consume null value
        parser.nextToken(); // Consume end array
    }

    @Test(expected = IOException.class)
    public void testWriteObject_NullObject() throws IOException {
        generator.writeObject(null);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        parser.nextToken(); // Consume start object
        parser.nextToken(); // Consume null value
        parser.nextToken(); // Consume end object
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteNumber_BigDecimalOutOfLimit() throws IOException {
        generator.writeNumber(new BigDecimal("1.0E+307"));
    }

    @Test
    public void testWriteRawValue_String() throws IOException {
        generator.writeRawValue("Hello World!");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals("Hello World!", parser.getValueAsString());
    }

    @Test
    public void testWriteRawValue_ByteArray() throws IOException {
        byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        generator.writeRawValue(data, 0, data.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals("Hello", parser.getValueAsString());
    }

    @Test
    public void testWriteRawValue_ByteArray_WithBOM() throws IOException {
        byte[] dataWithBOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x48, 0x65, 0x6C, 0x6C, 0x6F};
        generator.writeRawValue(dataWithBOM, 0, dataWithBOM.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals("Hello", parser.getValueAsString());
    }

    @Test(expected = IOException.class)
    public void testWriteRawValue_StringWithInvalidUTF8() throws IOException {
        generator.writeRawValue("\uD800\uDC00");
    }

    @Test(expected = IOException.class)
    public void testWriteRawValue_ByteArrayWithInvalidUTF8() throws IOException {
        byte[] dataWithInvalidUTF8 = {(byte) 0xD8, (byte) 0x00, (byte) 0xDC, (byte) 0x00};
        generator.writeRawValue(dataWithInvalidUTF8, 0, dataWithInvalidUTF8.length);
    }

    @Test
    public void testWriteRawValue_StringWithSurrogate() throws IOException {
        generator.writeRawValue("\uD801\uDC00");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals("\uD801\uDC00", parser.getValueAsString());
    }

    @Test
    public void testWriteRawValue_ByteArrayWithSurrogate() throws IOException {
        byte[] dataWithSurrogate = {(byte) 0xD8, (byte) 0x01, (byte) 0xDC, (byte) 0x00};
        generator.writeRawValue(dataWithSurrogate, 0, dataWithSurrogate.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals("\uD801\uDC00", parser.getValueAsString());
    }

    @Test
    public void testWriteRawValue_BigDecimal() throws IOException {
        BigDecimal number = new BigDecimal("1.23456789");
        generator.writeRawValue(number.toString());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        assertEquals("1.23456789", parser.getValueAsString());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        generator.useDefaultPrettyPrinter();
        assertEquals(JsonGenerationSchema.Feature.PRETTY_PRINT.getMask(), generator.getFeatures());
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        generator.setCodec(mapper.getFactory().createGeneratorFactory());
        assertEquals(mapper.getFactory().getGeneratorFactoryClass(), generator.getCodec().getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetCodec_NullCodec() throws IOException {
        generator.setCodec(null);
    }

    @Test
    public void testClose() throws IOException {
        generator.close();
        assertFalse(generator.isOpen());
    }

    @Test
    public void testIsOpen() throws IOException {
        generator.close();
        assertFalse(generator.isOpen());

        generator = new GeneratorBase(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isOpen());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteValue_AfterClose() throws IOException {
        generator.close();
        generator.writeString("Hello");
    }

    private static class MyClass {
        // Empty class for testing writeObject() method
    }
}