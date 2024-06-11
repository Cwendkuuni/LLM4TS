package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static com.fasterxml.jackson.databind.node.TextNode.value;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.core.io.SerializedString;

import org.junit.Test;

import java.io.IOException;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        ObjectNode rootNode = new ObjectMapper().readValue(new StringReader("{}"), ObjectNode.class);
        prettyPrinter.writeObject(jsonGenerator, rootNode);
        String actual = jsonGenerator.getOutputState().getWritable().toString();
        String expected = "{\n" +
                "}\n";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        ObjectNode rootNode = new ObjectMapper().readValue(new StringReader("{}"), ObjectNode.class);
        prettyPrinter.writeObject(jsonGenerator, rootNode);
        String actual = jsonGenerator.getOutputState().getWritable().toString();
        String expected = "--{\n" +
                "}\n";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectNode rootNode = new ObjectMapper().readValue(new StringReader("{" +
                "\"name\":\"John\",\n" +
                "\"age\":30" +
                "}"), ObjectNode.class);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeObject(jsonGenerator, rootNode);
        String actual = jsonGenerator.getOutputState().getWritable().toString();
        String expected = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30\n" +
                "}\n";
        assertThat(actual, is(equalTo(expected)));

        prettyPrinter = new DefaultPrettyPrinter() {
            @Override
            public void writeObjectFieldValueSeparator(JsonGenerator g) throws IOException {
                g.writeRaw("\t");
            }
        };
        jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        rootNode = new ObjectMapper().readValue(new StringReader("{" +
                "\"name\":\"John\",\n" +
                "\"age\":30" +
                "}"), ObjectNode.class);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeObject(jsonGenerator, rootNode);
        actual = jsonGenerator.getOutputState().getWritable().toString();
        expected = "{\n" +
                "  \"name\": \"John\",\t" +
                "  \"age\": 30\n" +
                "}\n";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectNode rootNode = new ObjectMapper().readValue(new StringReader("{" +
                "\"name\":{\"age\":30}," +
                "\"address\":{\"street\":\"Main St.\",\"city\":\"Anytown\"}" +
                "}"), ObjectNode.class);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeObject(jsonGenerator, rootNode);
        String actual = jsonGenerator.getOutputState().getWritable().toString();
        String expected = "{\n" +
                "  \"name\": {\n" +
                "    \"age\": 30\n" +
                "  },\n" +
                "  \"address\": {\n" +
                "    \"street\": \"Main St.\",\n" +
                "    \"city\": \"Anytown\"\n" +
                "  }\n" +
                "}\n";
        assertThat(actual, is(equalTo(expected)));

        prettyPrinter = new DefaultPrettyPrinter() {
            @Override
            public void writeObjectEntrySeparator(JsonGenerator g) throws IOException {
                g.writeRaw("\n");
            }
        };
        jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        rootNode = new ObjectMapper().readValue(new StringReader("{" +
                "\"name\":{\"age\":30}," +
                "\"address\":{\"street\":\"Main St.\",\"city\":\"Anytown\"}" +
                "}"), ObjectNode.class);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeObject(jsonGenerator, rootNode);
        actual = jsonGenerator.getOutputState().getWritable().toString();
        expected = "{\n" +
                "  \"name\": {\n" +
                "    \"age\": 30\n" +
                "  }\n" +
                ",\n" +
                "  \"address\": {\n" +
                "    \"street\": \"Main St.\",\n" +
                "    \"city\": \"Anytown\"\n" +
                "  }\n" +
                "}\n";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ArrayNode arrayNode = new ObjectMapper().readValue(new StringReader("[1,2,3]"), ArrayNode.class);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeArray(jsonGenerator, arrayNode);
        String actual = jsonGenerator.getOutputState().getWritable().toString();
        String expected = "[\n" +
                "  1,\n" +
                "  2,\n" +
                "  3\n" +
                "]\n";
        assertThat(actual, is(equalTo(expected)));

        prettyPrinter = new DefaultPrettyPrinter() {
            @Override
            public void writeArrayValueSeparator(JsonGenerator g) throws IOException {
                g.writeRaw("\n");
            }
        };
        jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        arrayNode = new ObjectMapper().readValue(new StringReader("[1,2,3]"), ArrayNode.class);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeArray(jsonGenerator, arrayNode);
        actual = jsonGenerator.getOutputState().getWritable().toString();
        expected = "[\n" +
                "  1,\n" +
                "]\n" +
                ",\n" +
                "  [\n" +
                "   2,\n" +
                "  ]\n" +
                ",\n" +
                "  [\n" +
                "    3\n" +
                "  ]\n" +
                "]\n";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testCustomIndenter() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectNode rootNode = new ObjectMapper().readValue(new StringReader("{" +
                "\"name\":{\"age\":30}," +
                "\"address\":{\"street\":\"Main St.\",\"city\":\"Anytown\"}" +
                "}"), ObjectNode.class);
        prettyPrinter.withObjectIndenter(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeObject(jsonGenerator, rootNode);
        String actual = jsonGenerator.getOutputState().getWritable().toString();
        String expected = "{\n" +
                "  \"name\": {\n" +
                "    \"age\": 30\n" +
                "  },\n" +
                "  \"address\": {\n" +
                "    \"street\": \"Main St.\",\n" +
                "    \"city\": \"Anytown\"\n" +
                "  }\n" +
                "}\n";
        assertThat(actual, is(equalTo(expected)));

        System.out.println(actual);
        System.out.println(expected);
        assertThat(actual, is(not(equalTo(expected))));
    }
}