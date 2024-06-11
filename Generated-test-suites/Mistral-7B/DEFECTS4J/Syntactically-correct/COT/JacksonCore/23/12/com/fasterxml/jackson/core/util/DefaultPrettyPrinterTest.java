package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    DefaultPrettyPrinter prettyPrinter;
    JsonFactory jsonFactory;

    @Before
    public void setup() {
        jsonFactory = new JsonFactory();
        prettyPrinter = new DefaultPrettyPrinter();
    }

    // Test for DefaultPrettyPrinter constructor
    @Test
    public void testDefaultConstructor() {
        prettyPrinter = new DefaultPrettyPrinter();
        assertNull(prettyPrinter.getRootSeparator());
    }

    @Test
    public void testStringRootSeparatorConstructor() {
        prettyPrinter = new DefaultPrettyPrinter("Root Separator");
        SerializedString rootSeparator = prettyPrinter.getRootSeparator();
        assertEquals("Root Separator", rootSeparator.toString());
    }

    @Test
    public void testSerializedStringRootSeparatorConstructor() {
        prettyPrinter = new DefaultPrettyPrinter(new SerializedString("Root Separator"));
        SerializedString rootSeparator = prettyPrinter.getRootSeparator();
        assertEquals("Root Separator", rootSeparator.toString());
    }

    @Test
    public void testDefaultPrettyPrinterCopierConstructor() {
        DefaultPrettyPrinter prettyPrinter1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter2 = new DefaultPrettyPrinter(prettyPrinder1);
        assertNotSame(prettyPrinter1, prettyPrinter2);
        assertEquals(prettyPrinter1.getRootSeparator(), prettyPrinter2.getRootSeparator());
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSeparator = prettyPrinter.withRootSeparator("Root Separator");
        SerializedString rootSeparator = prettyPrinterWithSeparator.getRootSeparator();
        assertEquals("Root Separator", rootSeparator.toString());
    }

    // Test for setters
    @Test
    public void testSetArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Custom Indentation");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentArraysWith(indenter);
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartArray(jsonGenerator);
        jsonGenerator.flush();
        assertEquals("Custom IndentationCustom Indentation", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSetObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Custom Indentation");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentObjectsWith(indenter);
        ObjectNode objectNode = jsonFactory.createObjectNode();
        objectNode.put("Key", jsonFactory.valueOf("Value"));
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeObject(objectNode, jsonGenerator);
        jsonGenerator.flush();
        assertEquals("{\n" +
                     "  \"Key\":\n" +
                     "  \"Value\"\n" +
                     "}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Custom Indentation");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter prettyPrinterWithIndenter = prettyPrinter.withArrayIndenter(indenter);
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinterWithIndenter.writeStartArray(jsonGenerator);
        jsonGenerator.flush();
        assertEquals("Custom IndentationCustom Indentation", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Custom Indentation");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter prettyPrinterWithIndenter = prettyPrinter.withObjectIndenter(indenter);
        ObjectNode objectNode = jsonFactory.createObjectNode();
        objectNode.put("Key", jsonFactory.valueOf("Value"));
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinterWithIndenter.writeObject(objectNode, jsonGenerator);
        jsonGenerator.flush();
        assertEquals("{\n" +
                     "  \"Key\":\n" +
                     "  \"Value\"\n" +
                     "}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();
        ObjectNode objectNode = jsonFactory.createObjectNode();
        objectNode.put("Key", jsonFactory.valueOf("Value"));
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinterWithSpaces.writeObject(objectNode, jsonGenerator);
        jsonGenerator.flush();
        assertEquals("{\n" +
                     "  \"Key\": \"Value\"\n" +
                     "}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWithoutSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithoutSpaces = prettyPrinter.withoutSpacesInObjectEntries();
        ObjectNode objectNode = jsonFactory.createObjectNode();
        objectNode.put("Key", jsonFactory.valueOf("Value"));
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinterWithoutSpaces.writeObject(objectNode, jsonGenerator);
        jsonGenerator.flush();
        assertEquals("{\"Key\":\"Value\"}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators() {
            @Override
            public String getObjectEntrySeparator() {
                return ":";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return "=";
            }

            @Override
            public String getArrayValueSeparator() {
                return ",";
            }
        };
        DefaultPrettyPrinter prettyPrinterWithSeparators = prettyPrinter.withSeparators(separators);
        ObjectNode objectNode = jsonFactory.createObjectNode();
        objectNode.put("Key", jsonFactory.valueOf("Value"));
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinterWithSeparators.writeObject(objectNode, jsonGenerator);
        jsonGenerator.flush();
        assertEquals("{\n" +
                     "  \"Key\": Value\n" +
                     "}", jsonGenerator.getOutputBuffer().toString());
    }
}