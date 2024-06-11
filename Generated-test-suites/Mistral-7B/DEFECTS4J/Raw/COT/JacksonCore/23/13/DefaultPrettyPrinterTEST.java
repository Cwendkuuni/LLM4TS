package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private ByteArrayOutputStream byteStream;
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() {
        prettyPrinter = new DefaultPrettyPrinter();
        byteStream = new ByteArrayOutputStream();
        jsonGenerator = prettyPrinter.getFactory().createGenerator(byteStream);
    }

    @After
    public void tearDown() throws IOException {
        jsonGenerator.close();
        byteStream.reset();
    }

    // Testing the constructor

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, defaultPrettyPrinter.getRootSeparator());
    }

    @Test
    public void testStringRootSeparatorConstructor() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter("rootSeparator");
        assertEquals("rootSeparator", defaultPrettyPrinter.getRootSeparator().asString());
    }

    @Test
    public void testSerializableStringRootSeparatorConstructor() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter(new SerializedString("rootSeparator"));
        assertEquals("rootSeparator", defaultPrettyPrinter.getRootSeparator().asString());
    }

    @Test
    public void testCopyConstructor() {
        DefaultPrettyPrinter originalPrettyPrinter = new DefaultPrettyPrinter("rootSeparator");
        DefaultPrettyPrinter copiedPrettyPrinter = new DefaultPrettyPrinter(originalPrettyPrinter);
        assertEquals(originalPrettyPrinter.getRootSeparator(), copiedPrettyPrinter.getRootSeparator());
    }

    // Testing the methods for configuring the pretty printer

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withRootSeparator(new SerializedString("rootSeparator"));
        assertEquals("rootSeparator", prettyPrinter.getRootSeparator().asString());
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("customIndentation");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentArraysWith(indenter);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeObjectEntrySeparator();
        jsonGenerator.writeString("test");
        jsonGenerator.writeArrayValueSeparator();
        jsonGenerator.writeString("value");
        jsonGenerator.writeEndArray();
        assertEquals("customIndentation{" + '\n' +
                "  \"test\":\n" +
                "  \"value\"\n" +
                "}", byteStream.toString());
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("customIndentation");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentObjectsWith(indenter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldName("test");
        jsonGenerator.writeObjectFieldValueSeparator();
        jsonGenerator.writeString("value");
        jsonGenerator.writeObjectEntrySeparator();
        jsonGenerator.writeObjectFieldName("nested");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldName("nestedField");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeString("nestedValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        assertEquals("customIndentation{\n" +
                "  \"test\":\n" +
                "    \"value\"\n" +
                ",\n" +
                "  \"nested\":\n" +
                "  {\n" +
                "    \"nestedField\":\n" +
                "    \"nestedValue\"\n" +
                "  }\n" +
                "}", byteStream.toString());
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldName("test");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeString("value");
        jsonGenerator.writeObjectEntrySeparator();
        jsonGenerator.writeObjectFieldName("nested");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldName("nestedField");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeString("nestedValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        assertEquals("{\n" +
                "  \"test\":\n" +
                "    \"value\"\n" +
                ",\n" +
                "  \"nested\":\n" +
                "  {\n" +
                "    \"nestedField\":\n" +
                "    \"nestedValue\"\n" +
                "  }\n" +
                "}", byteStream.toString());
    }

    @Test
    public void testWithoutSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withoutSpacesInObjectEntries();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldName("test");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeString("value");
        jsonGenerator.writeObjectEntrySeparator();
        jsonGenerator.writeObjectFieldName("nested");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldName("nestedField");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeString("nestedValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        assertEquals("{\"test\":\"value\",\"nested\":{\"nestedField\":\"nestedValue\"}}", byteStream.toString());
    }

    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators() {
            @Override
            public String getArrayValueSeparator() {
                return ";";
            }

            @Override
            public String getObjectEntrySeparator() {
                return " | ";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return ":";
            }
        };
        prettyPrinter.withSeparators(separators);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString("item1");
        jsonGenerator.writeArrayValueSeparator();
        jsonGenerator.writeString("item2");
        jsonGenerator.writeEndArray();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldName("field1");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeString("value1");
        jsonGenerator.writeObjectEntrySeparator();
        jsonGenerator.writeObjectFieldName("field2");
        jsonGenerator.writeObjectValueSeparator();
        jsonGenerator.writeString("value2");
        jsonGenerator.writeEndObject();
        assertEquals("item1;\n" +
                "  | field1:\n" +
                "  |   value1\n" +
                ",\n" +
                "  | field2:\n" +
                "  |   value2", byteStream.toString());
    }
}