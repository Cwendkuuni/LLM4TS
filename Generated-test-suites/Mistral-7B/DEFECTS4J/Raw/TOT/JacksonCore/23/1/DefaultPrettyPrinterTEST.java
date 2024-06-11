package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    private JsonFactory jsonFactory;
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() throws Exception {
        jsonFactory = new JsonFactory();
        jsonGenerator = jsonFactory.createGenerator(new StringWriter());
    }

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        jsonGenerator.writeStartObject();
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        jsonGenerator.writeFieldName("key1");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeValue("value1");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.writeFieldName("key2");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();
        assertEquals("{\n" +
                "  \"key1\": \"value1\",\n" +
                "  \"key2\": \"value2\"\n" +
                "}", jsonGenerator.getOutputContent());
    }

    @Test
    public void testWithRootSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(": "));
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        jsonGenerator.writeStartObject();
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        jsonGenerator.writeFieldName("key1");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeValue("value1");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.writeFieldName("key2");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();
        assertEquals("{\n" +
                "  : key1:\n" +
                "  : \n{\n" +
                "    \"key1\": \"value1\",\n" +
                "    \"key2\": \"value2\"\n" +
                "  }\n" +
                "}", jsonGenerator.getOutputContent());
    }

    @Test
    public void testWithArrayIndenter() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(new FixedSpaceIndenter());
        jsonGenerator.writeStartArray();
        prettyPrinter.beforeArrayValues(jsonGenerator);
        jsonGenerator.writeValue(1);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        jsonGenerator.writeValue(2);
        jsonGenerator.writeEndArray();
        assertEquals("[ 1,\n" +
                "  \" \":\n" +
                "  2]", jsonGenerator.getOutputContent());
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeNestingLevel() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = jsonFactory.createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(g);
        prettyPrinter.writeEndObject(g, -1);
    }
}