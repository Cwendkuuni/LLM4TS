package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeValue("John Doe");
            jsonGenerator.writeEndObject();
            String result = new String(jsonGenerator.getOutputBuffer().toByteArray());

            assertEquals("{\n" +
                    "  \"name\": \"John Doe\"\n" +
                    "}", result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(": "));
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeValue("John Doe");
            jsonGenerator.writeEndObject();
            String result = new String(jsonGenerator.getOutputBuffer().toByteArray());

            assertEquals("{\n" +
                    "  :  name:\n" +
                    "  :  \"John Doe\"\n" +
                    "}", result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter arrayIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("    ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentArraysWith(arrayIndenter);
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
            jsonGenerator.writeStartArray();
            jsonGenerator.writeValue("apple");
            jsonGenerator.writeValue("banana");
            jsonGenerator.writeValue("orange");
            jsonGenerator.writeEndArray();
            String result = new String(jsonGenerator.getOutputBuffer().toByteArray());

            assertEquals("[
n" +
                    "  \"apple\"\n" +
                    "  \"banana\"\n" +
                    "  \"orange\"\n" +
                    "]", result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentObjectsWith(objectIndenter);
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeValue("John Doe");
            jsonGenerator.writeFieldName("age");
            jsonGenerator.writeValue(30);
            jsonGenerator.writeEndObject();
            String result = new String(jsonGenerator.getOutputBuffer().toByteArray());

            assertEquals("{\n" +
                    "  \"name\": \"John Doe\"\n" +
                    "  \"age\": 30\n" +
                    "}", result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withSpacesInObjectEntries();
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeValue("John Doe");
            jsonGenerator.writeFieldName("age");
            jsonGenerator.writeValue(30);
            jsonGenerator.writeEndObject();
            String result = new String(jsonGenerator.getOutputBuffer().toByteArray());

            assertEquals("{\n" +
                    "  \"name\": \"John Doe\"\n" +
                    "  \"age\": 30\n" +
                    "}", result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        DefaultPrettyPrinter prettyPrinter2 = new DefaultPrettyPrinter();
        prettyPrinter2 = prettyPrinter2.withoutSpacesInObjectEntries();
        JsonGenerator jsonGenerator2 = null;
        try {
            jsonGenerator2 = new JsonFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter2);
            jsonGenerator2.writeStartObject();
            jsonGenerator2.writeFieldName("name");
            jsonGenerator2.writeValue("John Doe");
            jsonGenerator2.writeFieldName("age");
            jsonGenerator2.writeValue(30);
            jsonGenerator2.writeEndObject();
            String result2 = new String(jsonGenerator2.getOutputBuffer().toByteArray());

            assertEquals("{\n" +
                    "  \"name\":\"John Doe\"\n" +
                    "  \"age\":30\n" +
                    "}", result2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator2 != null) {
                try {
                    jsonGenerator2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}