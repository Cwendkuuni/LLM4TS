package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        String json = "[1, 2, 3, 4]";
        JsonNode node = mapper.readTree(json);

        String prettyJson = prettyPrinter.writeValueAsString(node);
        assertEquals(json, prettyJson);

        json = "{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"]}";
        node = mapper.readTree(json);

        prettyJson = prettyPrinter.writeValueAsString(node);
        assertEquals(json, prettyJson);
    }

    @Test
    public void testRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        String json = "rootValue";
        JsonNode node = mapper.valueToTree(json);

        String prettyJson = prettyPrinter.writeValueAsString(node);
        String expected = "rootValue";
        if (prettyPrinter._rootSeparator != null) {
            expected = prettyPrinter._rootSeparator.getStringValue() + "rootValue";
        }
        assertEquals(expected, prettyJson);

        prettyPrinter = new DefaultPrettyPrinter(new SerializedString(" separator "));
        prettyJson = prettyPrinter.writeValueAsString(node);
        assertEquals(" separator rootValue", prettyJson);
    }

    @Test
    public void testIndentArraysWith() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("indented ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        prettyPrinter.indentArraysWith(indenter);

        String json = "[1, 2, 3, 4]";
        JsonNode node = mapper.readTree(json);

        String prettyJson = prettyPrinter.writeValueAsString(node);
        assertEquals("indented \nindented \nindented 1,\nindented \nindented 2,\nindented \nindented 3,\nindented \nindented 4,\n", prettyJson);
    }

    @Test
    public void testIndentObjectsWith() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("indented ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        prettyPrinter.indentObjectsWith(indenter);

        String json = "{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"]}";
        JsonNode node = mapper.readTree(json);

        String prettyJson = prettyPrinter.writeValueAsString(node);
        assertEquals("{\nindented \"name\": \"John\",\nindented \nindented \"age\": 30,\nindented \nindented \"hobbies\": [\nindented \t\"reading\",\nindented \t\"swimming\"\nindented ]\n}", prettyJson);
    }

    @Test
    public void testSpacesInObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        String json = "{\"name\": John, \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"]}";
        JsonNode node = mapper.readTree(json);

        String prettyJson = prettyPrinter.writeValueAsString(node);
        assertEquals(json, prettyJson);

        prettyPrinter = new DefaultPrettyPrinter().withSpacesInObjectEntries();

        prettyJson = prettyPrinter.writeValueAsString(node);
        assertEquals("{\n\"name\": John,\n\t\"age\": 30,\n\thobbies:\n\t[\n\t\t\"reading\",\n\t\t\"swimming\"\n\t]\n}", prettyJson);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNode() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeValueAsString(null);
    }

    private static ObjectMapper mapper = new ObjectMapper();

}

class Indenter {
    // empty class
}