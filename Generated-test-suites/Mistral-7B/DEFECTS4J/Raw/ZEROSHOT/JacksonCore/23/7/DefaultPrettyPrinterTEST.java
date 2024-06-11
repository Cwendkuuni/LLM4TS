package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter_DefaultConstruction() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();

        JsonParser jsonParser = new JsonFactory().createParser(stringWriter.toString());
        String actual = jsonParser.getText();

        String expected = "{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinter_WithRootSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(":");
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();

        JsonParser jsonParser = new JsonFactory().createParser(stringWriter.toString());
        String actual = jsonParser.getText();

        String expected = ":{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinter_WithRootSeparatorAndSpaces() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(" : ");
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();

        JsonParser jsonParser = new JsonFactory().createParser(stringWriter.toString());
        String actual = jsonParser.getText();

        String expected = " : {\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinter_WithArrayIndenter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndArray();

        JsonParser jsonParser = new JsonFactory().createParser(stringWriter.toString());
        String actual = jsonParser.getText();

        String expected = "[\n" +
                "  \"value1\",\n" +
                "  \"value2\"\n" +
                "]";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinter_WithObjectIndenter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new DefaultPrettyPrinter.Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();

        JsonParser jsonParser = new JsonFactory().createParser(stringWriter.toString());
        String actual = jsonParser.getText();

        String expected = "{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinter_WithSpacesInObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();

        JsonParser jsonParser = new JsonFactory().createParser(stringWriter.toString());
        String actual = jsonParser.getText();

        String expected = "{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinter_WithWithoutSpacesInObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withoutSpacesInObjectEntries();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();

        JsonParser jsonParser = new JsonFactory().createParser(stringWriter.toString());
        String actual = jsonParser.getText();

        String expected = "{\"field1\":\"value1\",\"field2\":\"value2\"}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinter_WithSeparators() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators(",", ":", "[", "]", "{", "}");
        prettyPrinter.withSeparators(separators);
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();

        JsonParser jsonParser = new JsonFactory().createParser(stringWriter.toString());
        String actual = jsonParser.getText();

        String expected = "{,\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}";

        assertEquals(expected, actual);
    }

}