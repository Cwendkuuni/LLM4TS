package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = jsonGenerator.getPrettyPrinter();

        // Test empty JSON
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 0);
        String result = jsonGenerator.getOutputText();
        assertEquals("{}", result);

        // Test root separator
        SerializedString rootSeparator = new SerializedString("--");
        prettyPrinter = jsonGenerator.getPrettyPrinter();
        prettyPrinter.withRootSeparator(rootSeparator);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 0);
        result = jsonGenerator.getOutputText();
        assertEquals("--{}", result);

        // Test indenters
        Indenter arrayIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        Indenter objectIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("    ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter = jsonGenerator.getPrettyPrinter();
        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.indentObjectsWith(objectIndenter);
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        result = jsonGenerator.getOutputText();
        assertEquals("[ \n    ]", result);

        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeFieldName("name");
        prettyPrinter.writeValue("John Doe");
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        result = jsonGenerator.getOutputText();
        assertEquals("{\n    \"name\": \"John Doe\"\n}", result);

        // Test spaces in object entries
        prettyPrinter = jsonGenerator.getPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeFieldName("name");
        prettyPrinter.writeValue("John Doe");
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        result = jsonGenerator.getOutputText();
        assertEquals("{\n    \"name\": \"John Doe\"\n}", result);

        prettyPrinter = jsonGenerator.getPrettyPrinter();
        prettyPrinter.withoutSpacesInObjectEntries();
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeFieldName("name");
        prettyPrinter.writeValue("John Doe");
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        result = jsonGenerator.getOutputText();
        assertEquals("{\"name\":\"John Doe\"}", result);

        // Test separators
        Separators separators = new Separators() {
            @Override
            public String getObjectFieldValueSeparator() {
                return "|";
            }

            @Override
            public String getObjectEntrySeparator() {
                return "|";
            }

            @Override
            public String getArrayValueSeparator() {
                return "|";
            }
        };
        prettyPrinter = jsonGenerator.getPrettyPrinter();
        prettyPrinter.withSeparators(separators);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeFieldName("name");
        prettyPrinter.writeValue("John Doe");
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        result = jsonGenerator.getOutputText();
        assertEquals("{\"name\":\"John Doe\"}", result);
    }

    @Test
    public void testNullArguments() throws JsonProcessingException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = jsonGenerator.getPrettyPrinter();

        // Test null JsonGenerator
        assertNull(prettyPrinter = jsonGenerator.getPrettyPrinter());

        // Test writeFieldName with null value
        try {
            prettyPrinter.writeFieldName(null);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test writeValue with null value
        try {
            prettyPrinter.writeValue(null);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test writeStartArray with null value
        try {
            prettyPrinter.writeStartArray(null);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test writeStartObject with null value
        try {
            prettyPrinter.writeStartObject(null);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test writeEndArray with negative value
        try {
            prettyPrinter.writeEndArray(jsonGenerator, -1);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test writeEndObject with negative value
        try {
            prettyPrinter.writeEndObject(jsonGenerator, -1);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}