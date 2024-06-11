package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(new StringWriter());
        generator.setPrettyPrinter(new DefaultPrettyPrinter());

        // Test root-level separator
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("value1");
        generator.writeFieldName("field2");
        generator.writeValue("value2");
        generator.writeEndObject();
        String result = generator.getOutput().getString();
        assertEquals("{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}", result);

        // Test object and array indentation
        generator.reset();
        generator.writeStartObject();
        generator.writeFieldName("obj1");
        generator.writeStartArray();
        generator.writeValue(1);
        generator.writeValue(2);
        generator.writeEndArray();
        generator.writeValue("value1");
        generator.writeEndObject();
        result = generator.getOutput().getString();
        assertEquals("{\n" +
                "  \"obj1\": [\n" +
                "    1,\n" +
                "    2\n" +
                "  ],\n" +
                "  \"value1\"\n" +
                "}", result);

        // Test spaces in object entries
        generator.reset();
        generator.setPrettyPrinter(new DefaultPrettyPrinter().withSpacesInObjectEntries());
        generator.writeStartObject();
        generator.writeFieldName("obj1");
        generator.writeFieldName("field1");
        generator.writeValue("value1");
        generator.writeFieldName("field2");
        generator.writeValue("value2");
        generator.writeEndObject();
        result = generator.getOutput().getString();
        assertEquals("{\n" +
                "  \"obj1\": {\n" +
                "    \"field1\": \"value1\",\n" +
                "    \"field2\": \"value2\"\n" +
                "  }\n" +
                "}", result);

        // Test root-level separator with null value
        generator.reset();
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeNull();
        generator.writeFieldName("field2");
        generator.writeValue("value2");
        generator.writeEndObject();
        result = generator.getOutput().getString();
        assertEquals("{\n" +
                "  \"field1\": null,\n" +
                "  \"field2\": \"value2\"\n" +
                "}", result);

        // Test custom root-level separator
        generator.reset();
        generator.setPrettyPrinter(new DefaultPrettyPrinter("custom separator"));
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("value1");
        generator.writeFieldName("field2");
        generator.writeValue("value2");
        generator.writeEndObject();
        result = generator.getOutput().getString();
        assertEquals("{\n" +
                "  custom separator\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new DefaultPrettyPrinter(null);
    }
}