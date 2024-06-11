package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

/**
 * Test class for DefaultPrettyPrinter
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeEndObject();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes());
        JsonParser jsonParser = new JsonFactory().createParser(byteArrayInputStream);
        assertEquals("{\"name\":\"John Doe\"}", jsonParser.getValueAsString());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeEndObject();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes());
        JsonParser jsonParser = new JsonFactory().createParser(byteArrayInputStream);
        assertEquals("--{\"name\":\"John Doe\"}", jsonParser.getValueAsString());
    }

    @Test
    public void testIndentArraysWith() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        prettyPrinter.indentArraysWith(indenter);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue("John");
        jsonGenerator.writeValue("Doe");
        jsonGenerator.writeEndArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes());
        JsonParser jsonParser = new JsonFactory().createParser(byteArrayInputStream);
        assertEquals("[  \"John\",  \"Doe\"]", jsonParser.getValueAsString());
    }

    @Test
    public void testIndentObjectsWith() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        prettyPrinter.indentObjectsWith(indenter);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeEndObject();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes());
        JsonParser jsonParser = new JsonFactory().createParser(byteArrayInputStream);
        assertEquals("{\"name\":\"John Doe\"}", jsonParser.getValueAsString());
    }

    @Test
    public void testWithSpacesInObjectEntries() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinterWithSpaces);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeEndObject();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes());
        JsonParser jsonParser = new JsonFactory().createParser(byteArrayInputStream);
        String jsonString = jsonParser.getValueAsString();

        assertTrue(jsonString.contains(" : "));

        JsonGenerator jsonGeneratorWithoutSpaces = new JsonFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);
        jsonGeneratorWithoutSpaces.writeStartObject();
        jsonGeneratorWithoutSpaces.writeFieldName("name");
        jsonGeneratorWithoutSpaces.writeValue("John Doe");
        jsonGeneratorWithoutSpaces.writeEndObject();

        ByteArrayInputStream byteArrayInputStreamWithoutSpaces = new ByteArrayInputStream(jsonGeneratorWithoutSpaces.getOutputBuffer().getBytes());
        JsonParser jsonParserWithoutSpaces = new JsonFactory().createParser(byteArrayInputStreamWithoutSpaces);
        String jsonStringWithoutSpaces = jsonParserWithoutSpaces.getValueAsString();

        assertFalse(jsonStringWithoutSpaces.contains(" : "));
    }
}