package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        ObjectNode node = Json.createObjectNode();
        node.putPOJO("foo", new Foo("bar"));
        node.put("baz", 123);

        node.writeValue(jsonGenerator);
        jsonGenerator.flush();

        String actual = new String(jsonGenerator.getOutputBuffer().toByteArray());
        String expected = "{\n" +
                "  \"foo\": {\n" +
                "    \"class\": \"Foo\",\n" +
                "    \"bar\": \"bar\"\n" +
                "  },\n" +
                "  \"baz\": 123\n" +
                "}\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        ObjectNode node = Json.createObjectNode();
        node.putPOJO("foo", new Foo("bar"));
        node.put("baz", 123);

        node.writeValue(jsonGenerator);
        jsonGenerator.flush();

        String actual = new String(jsonGenerator.getOutputBuffer().toByteArray());
        String expected = "--{\n" +
                "  \"foo\": {\n" +
                "    \"class\": \"Foo\",\n" +
                "    \"bar\": \"bar\"\n" +
                "  },\n" +
                "  \"baz\": 123\n" +
                "}\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorAndSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"), true);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        ObjectNode node = Json.createObjectNode();
        node.putPOJO("foo", new Foo("bar"));
        node.put("baz", 123);

        node.writeValue(jsonGenerator);
        jsonGenerator.flush();

        String actual = new String(jsonGenerator.getOutputBuffer().toByteArray());
        String expected = "--{\n" +
                "  \"foo\": {\n" +
                "    \"class\": \"Foo\",\n" +
                "    \"bar\": \"bar\"\n" +
                "  },\n" +
                "  \"baz\": 123\n" +
                "}\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomIndenters() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        Indenter arrayIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("****");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        Indenter objectIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("*****");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.indentObjectsWith(objectIndenter);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        ObjectNode node = Json.createObjectNode();
        node.putPOJO("foo", new Foo("bar"));
        node.put("baz", 123);

        node.writeValue(jsonGenerator);
        jsonGenerator.flush();

        String actual = new String(jsonGenerator.getOutputBuffer().toByteArray());
        String expected = "*****\n" +
                "*****\n" +
                "{\n" +
                "  \"foo\": {\n" +
                "    \"class\": \"Foo\",\n" +
                "    \"bar\": \"bar\"\n" +
                "  },\n" +
                "  \"baz\": 123\n" +
                "}\n" +
                "*****\n" +
                "*****";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        Separators separators = new Separators() {
            @Override
            public String getObjectEntrySeparator() {
                return "\n";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return "=";
            }

            @Override
            public String getArrayValueSeparator() {
                return "|";
            }
        };

        prettyPrinter.withSeparators(separators);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        ObjectNode node = Json.createObjectNode();
        node.putPOJO("foo", new Foo("bar"));
        node.put("baz", 123);

        node.writeValue(jsonGenerator);
        jsonGenerator.flush();

        String actual = new String(jsonGenerator.getOutputBuffer().toByteArray());
        String expected = "{\n" +
                "  \"foo\":=\n" +
                "  {\n" +
                "    \"class\": \"Foo\",\n" +
                "    \"bar\": \"bar\"\n" +
                "  },\n" +
                "  \"baz\": 123\n" +
                "}\n";

        assertEquals(expected, actual);
    }
}

class Foo {
    private String bar;

    public Foo(String bar) {
        this.bar = bar;
    }

    public String getBar() {
        return bar;
    }
}

class Indenter implements Serializable {
}

class Separators implements Serializable {
    public String getObjectEntrySeparator() {
        return "";
    }

    public String getObjectFieldValueSeparator() {
        return "";
    }

    public String getArrayValueSeparator() {
        return "";
    }
}