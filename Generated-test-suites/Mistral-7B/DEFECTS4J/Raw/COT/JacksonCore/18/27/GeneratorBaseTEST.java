package com.fasterxml.jackson.core.base; // adjust the package if necessary

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    @Test
    public void testVersion() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        Version version = generator.version();
        assertTrue("Unexpected version: " + version, version.majorVersion() >= 2 && version.minorVersion() >= 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedWriteMethods() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        // Test methods with '//public void' comments are not implemented
        generator.writeStartArray();
        generator.writeEndArray();
        generator.writeStartObject();
        generator.writeEndObject();
    }

    @Test
    public void testWriteString() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John Doe\"}";
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();
        JsonParser jsonParser = mapper.getFactory().createParser(json.getBytes());
        assertEquals("name", jsonParser.getCurrentName());
        assertEquals("John Doe", jsonParser.getValueAsString());
        jsonGenerator.close();
        jsonParser.close();
    }

    @Test
    public void testWriteRawValue() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John Doe\", \"raw\":\"jackson\"}";
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeFieldName("raw");
        jsonGenerator.writeRawValue("jackson");
        jsonGenerator.writeEndObject();
        JsonParser jsonParser = mapper.getFactory().createParser(json.getBytes());
        assertEquals("name", jsonParser.getCurrentName());
        assertEquals("John Doe", jsonParser.nextValue().toString());
        assertEquals("raw", jsonParser.nextFieldName());
        assertEquals("jackson", jsonParser.nextTextValue());
        jsonGenerator.close();
        jsonParser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteBinaryUnsupported() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeBinary(null, null, 0);
    }

    @Test
    public void testWriteNumber() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.writeNumber(123);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputBuffer().toString());
        assertEquals(123, jsonParser.nextIntValue());
        jsonGenerator.close();
        jsonParser.close();
    }

    @Test
    public void testWriteBoolean() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.writeBoolean(true);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputBuffer().toString());
        assertTrue(jsonParser.nextBooleanValue());
        jsonGenerator.close();
        jsonParser.close();
    }

    @Test
    public void testWriteNull() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.writeNull();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputBuffer().toString());
        assertNull(jsonParser.nextValue());
        jsonGenerator.close();
        jsonParser.close();
    }

    @Test(expected = IOException.class)
    public void testWriteObjectUnsupported() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John Doe\"}";
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.writeObject(new Object());
        jsonGenerator.close();
    }

    @Test
    public void testWriteTree() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        Object tree = new Object();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.writeTree(tree);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputBuffer().toString());
        assertEquals(tree.getClass().getName(), jsonParser.getValueAsText());
        jsonGenerator.close();
        jsonParser.close();
    }

    @Test
    public void testClose() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.writeString("Testing");
        jsonGenerator.close();
        assertFalse(jsonGenerator.isClosed());
        jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.close();
        assertTrue(jsonGenerator.isClosed());
    }

    @Test
    public void testEnableFeatures() throws Exception {
        GeneratorBase generator = new GeneratorBase(GENERATOR_BASE_FEATURE_MASK, null);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        // Test features enabled by default
        assertTrue(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        // Test features explicitly enabled
        jsonGenerator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        // Test features explicitly disabled
        jsonGenerator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        jsonGenerator.close();
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        PrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.useDefaultPrettyPrinter();
        String json = jsonGenerator.getOutputBuffer().toString();
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonNode rootNode = jsonParser.readValueAsTree();
        assertEquals("{", rootNode.getClass().getName().substring(0, 1));
        jsonGenerator.close();
    }

    @Test
    public void testWriteValueAsString() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        String input = "{\"name\": \"John Doe\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(input);
        jsonGenerator.writeValueAsString(jsonParser);
        String output = jsonGenerator.getOutputBuffer().toString();
        assertEquals(input, output);
        jsonGenerator.close();
    }

    @Test
    public void testWriteValueAsStringWithPrettyPrinter() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), generator);
        jsonGenerator.useDefaultPrettyPrinter();
        String input = "{\"name\": \"John Doe\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(input);
        jsonGenerator.writeValueAsString(jsonParser);
        String output = jsonGenerator.getOutputBuffer().toString();
        JsonParser outputParser = mapper.getFactory().createParser(output);
        JsonNode rootNode = outputParser.readValueAsTree();
        assertEquals(input, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(input)));
        assertEquals(rootNode.getClass().getName().substring(0, 1), output.substring(0, 1));
        jsonGenerator.close();
    }
}