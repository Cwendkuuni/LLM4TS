package com.fasterxml.jackson.core.base; // adjust the package if necessary

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class GeneratorBaseTest {
    private JsonGenerator generator;
    private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

    @Before
    public void setUp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        generator = writer.withRootValue(new Object()).getGenerator();
        generator.setOutputStrategies(new JsonGenerator.OutputStrategies());
        generator.setWriterFeatures(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        generator.setCodec(mapper.getFactory());
    }

    @Test
    public void testIsEnabled_Features() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null, null);
        assertTrue(generatorBase.isEnabled(Feature.AUTO_CLOSE_TARGET));
        assertFalse(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));
        generatorBase.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));
        generatorBase.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testIsEnabled_DerivedFeatures() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null, null);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("Hello World!");
        String result = generator.getOutputStrategies().getSerializerFor(String.class).serializeValue(generator, "Hello World!");
        assertEquals("\"Hello World!\"" + System.lineSeparator(), result);
    }

    @Test
    public void testWriteRawValue() throws IOException {
        generator.writeRawValue("Hello World!");
        String result = generator.getOutputStrategies().getSerializerFor(String.class).serializeValue(generator, "Hello World!");
        assertEquals("\"Hello World!\"", result);
    }

    @Test
    public void testWriteRawValue_WithOffsetAndLength() throws IOException {
        String text = "Hello World!";
        generator.writeRawValue(text, 0, text.length());
        String result = generator.getOutputStrategies().getSerializerFor(String.class).serializeValue(generator, text);
        assertEquals("\"Hello World!\"" + System.lineSeparator(), result);
    }

    @Test
    public void testWriteRawValue_WithCharArray() throws IOException {
        char[] text = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        generator.writeRawValue(text, 0, text.length);
        String result = generator.getOutputStrategies().getSerializerFor(String.class).serializeValue(generator, new String(text));
        assertEquals("\"Hello World!\"", result);
    }

    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(42);
        String result = generator.getOutputStrategies().getSerializerFor(JsonGenerator.NumberSerializer.class).serializeValue(generator, 42);
        assertEquals("42", result);
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        String result = generator.getOutputStrategies().getSerializerFor(Boolean.class).serializeValue(generator, true);
        assertEquals("true", result);
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        String result = generator.getOutputStrategies().getSerializerFor(NullSerializer.class).serializeValue(generator, null);
        assertEquals("null", result);
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteInvalidValue() throws IOException {
        generator.writeRawValue("This is an invalid test case!");
    }

    @Test
    public void testWriteObject() throws IOException {
        Object testObject = new Object() {{
            @SuppressWarnings("unchecked")
            String testString = (String) getClass().getDeclaredField("testString").get(this);
            put("testString", testString);
        }};
        String testString = "Test String";
        setFieldValue(testObject, "testString", testString);
        generator.writeObject(testObject);
        String result = generator.getOutputStrategies().getSerializerFor(Object.class).serializeValue(generator, testObject);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(byteStream.toByteArray()));
        assertTrue(parser.readValueAsTree().equals(mapper.readTree(result)));
    }
}