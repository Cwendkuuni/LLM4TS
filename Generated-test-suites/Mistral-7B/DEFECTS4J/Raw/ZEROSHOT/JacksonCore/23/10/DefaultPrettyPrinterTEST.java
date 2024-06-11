package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    private PrettyPrinter prettyPrinter;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        jsonFactory = new JsonFactory();
        prettyPrinter = jsonFactory.createGenerator(new StringWriter(), DefaultPrettyPrinter.class);
    }

    @After
    public void tearDown() throws IOException {
        prettyPrinter.close();
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        JsonGenerator generatedJson = prettyPrinter;
        String rootValue = "{\"key\":\"value\"}";
        generatedJson.writeRaw(rootValue);
        generatedJson.close();
        String serializedJson = prettyPrinter.getOutputBuffer().toString();
        assertEquals(rootValue, serializedJson);
    }

    @Test
    public void testConstructorWithRootSeparator() throws IOException {
        String rootSeparator = "separator: ";
        JsonGenerator generatedJson = prettyPrinter.withRootSeparator(new SerializedString(rootSeparator));
        String rootValue = "{\"key\":\"value\"}";
        generatedJson.writeRaw(rootValue);
        generatedJson.close();
        String serializedJson = prettyPrinter.getOutputBuffer().toString();
        assertEquals(rootSeparator + rootValue, serializedJson);
    }

    @Test
    public void testArrayIndenter() throws IOException {
        Indenter arrayIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Array level " + level);
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        JsonGenerator generatedJson = prettyPrinter.indentArraysWith(arrayIndenter);

        String jsonData = "[1,2,3,4]";
        generatedJson.writeRaw(jsonData);
        generatedJson.close();
        String serializedJson = prettyPrinter.getOutputBuffer().toString();
        String expectedResult = "Array level 0\n" +
                "Array level 1 \n" +
                "1,\n" +
                "Array level 1 \n" +
                "2,\n" +
                "Array level 1 \n" +
                "3,\n" +
                "Array level 1 \n" +
                "4,\n";
        assertEquals(expectedResult, serializedJson);
    }

    @Test
    public void testObjectIndenter() throws IOException {
        Indenter objectIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Object level " + level);
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        JsonGenerator generatedJson = prettyPrinter.indentObjectsWith(objectIndenter);

        String jsonData = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
        generatedJson.writeObjectStart();
        generatedJson.writeFieldName("key1");
        generatedJson.writeValue(new SerializedString("value1"));
        generatedJson.writeFieldName("key2");
        generatedJson.writeValue(new SerializedString("value2"));
        generatedJson.writeEndObject();
        generatedJson.close();
        String serializedJson = prettyPrinter.getOutputBuffer().toString();
        String expectedResult = "Object level 0\n" +
                "{\n" +
                "  \"key1\": \"value1\",\n" +
                "  \"key2\": \"value2\"\n" +
                "}\n";
        assertEquals(expectedResult, serializedJson);
    }
}