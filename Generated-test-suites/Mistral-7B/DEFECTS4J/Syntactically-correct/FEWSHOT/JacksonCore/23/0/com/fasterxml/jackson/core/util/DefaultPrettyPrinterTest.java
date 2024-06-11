package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        // Test default root value separator
        prettyPrinter.writeRootValueSeparator(new TestJsonGenerator());
        assertEquals("", prettyPrinter.getRootSeparator().getValue());

        // Test default array separator and value separator
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeString("foo");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeString("bar");
        prettyPrinter.writeEndArray(jsonGenerator);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("[, \"foo\", , \"bar\"]", result);

        // Test custom root value separator
        prettyPrinter = new DefaultPrettyPrinter(new SerializedString("---"));
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("---", result);

        // Test indenting objects and arrays
        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new SpacesIndenter());
        prettyPrinter.indentArraysWith(new TabsIndenter());
        jsonGenerator.reset();
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", mapper.valueToTree(1));
        node.put("bar", mapper.valueToTree(2));
        prettyPrinter.writeTree(node, jsonGenerator);
        result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\n" +
                "  \"foo\": 1,\n" +
                "  \"bar\": 2\n" +
                "}", result);

        jsonGenerator.reset();
        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add(mapper.valueToTree(1));
        arrayNode.add(mapper.valueToTree(2));
        prettyPrinter.writeTree(arrayNode, jsonGenerator);
        result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("[  1,\n" +
                "   2]", result);
    }

    private static class SpacesIndenter implements DefaultPrettyPrinter.Indenter {
        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            for (int i = 0; i < 2 * level; i++) {
                g.writeRaw(" ");
            }
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }

    private static class TabsIndenter implements DefaultPrettyPrinter.Indenter {
        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            for (int i = 0; i < level; i++) {
                g.writeRaw("\t");
            }
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }

    private static class TestJsonGenerator extends JsonGenerator {
        TestJsonGenerator() {
            super(new StringWriter());
        }
    }
}