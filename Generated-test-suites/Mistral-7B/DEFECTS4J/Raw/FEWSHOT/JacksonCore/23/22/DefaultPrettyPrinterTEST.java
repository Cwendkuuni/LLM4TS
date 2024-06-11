package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testInstanceCreation() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(defaultPrettyPrinter);

        DefaultPrettyPrinter defaultPrettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString("rootSeparator"));
        assertNotNull(defaultPrettyPrinterWithRootSeparator);
    }

    @Test
    public void testDefaultRootValueSeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter defaultPrettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString("rootSeparator"));
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinterWithRootSeparator.writeRootValueSeparator(jsonGenerator);
        assertEquals("rootSeparator", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{\n", jsonGenerator.getOutputString());

        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = new DefaultPrettyPrinter();
        prettyPrinterWithArrayIndenter.indentObjectsWith(new FixedSpaceIndenter());
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{\n", jsonGenerator.getOutputString());

        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = new DefaultPrettyPrinter();
        prettyPrinterWithObjectIndenter.indentObjectsWith(new DefaultIndenter.SystemLineFeedInstance());
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{\n", jsonGenerator.getOutputString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(":", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithSpaces = new DefaultPrettyPrinter();
        prettyPrinterWithSpaces.withSpacesInObjectEntries();
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(" :\n", jsonGenerator.getOutputString());

        DefaultPrettyPrinter prettyPrinterWithCustomSeparator = new DefaultPrettyPrinter();
        prettyPrinterWithCustomSeparator.withSeparators(new Separators("", "", ""));
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());
        prettyPrinterWithCustomSeparator.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectEntrySeparatorWithSpaces() throws IOException {
        DefaultPrettyPrinter prettyPrinterWithSpaces = new DefaultPrettyPrinter();
        prettyPrinterWithSpaces.withSpacesInObjectEntries();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();
        prettyPrinterWithSpaces.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(" :\n", jsonGenerator.getOutputString());
        prettyPrinterWithSpaces.writeObjectEntrySeparator(jsonGenerator);
        assertEquals(" \n", jsonGenerator.getOutputString());
    }

    @Test
    public void testWriteObjectEntries() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();

        defaultPrettyPrinter.writeStartObject(jsonGenerator);
        defaultPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        defaultPrettyPrinter.writeObjectFieldName("field1");
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value1");
        defaultPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        defaultPrettyPrinter.writeObjectFieldName("field2");
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value2");
        defaultPrettyPrinter.writeEndObject(jsonGenerator, 2);

        assertEquals("{\n" +
                " \"field1\":\n" +
                " \"value1\",\n" +
                " \"field2\":\n" +
                " \"value2\"\n" +
                "}", jsonGenerator.getOutputString());

        DefaultPrettyPrinter prettyPrinterWithSpaces = new DefaultPrettyPrinter();
        prettyPrinterWithSpaces.withSpacesInObjectEntries();
        JsonGenerator jsonGeneratorWithSpaces = new TestJsonFactory().createGenerator();

        prettyPrinterWithSpaces.writeStartObject(jsonGeneratorWithSpaces);
        prettyPrinterWithSpaces.writeObjectEntrySeparator(jsonGeneratorWithSpaces);
        prettyPrinterWithSpaces.writeObjectFieldName("field1");
        prettyPrinterWithSpaces.writeObjectFieldValueSeparator(jsonGeneratorWithSpaces);
        jsonGeneratorWithSpaces.writeRaw("value1");
        prettyPrinterWithSpaces.writeObjectEntrySeparator(jsonGeneratorWithSpaces);
        prettyPrinterWithSpaces.writeObjectFieldName("field2");
        prettyPrinterWithSpaces.writeObjectFieldValueSeparator(jsonGeneratorWithSpaces);
        jsonGeneratorWithSpaces.writeRaw("value2");
        prettyPrinterWithSpaces.writeEndObject(jsonGeneratorWithSpaces, 2);

        assertEquals("{\n" +
                " \"field1\":\n" +
                " \"value1\",\n" +
                " \"field2\":\n" +
                " \"value2\"\n" +
                "}", jsonGeneratorWithSpaces.getOutputString());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[\n", jsonGenerator.getOutputString());

        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = new DefaultPrettyPrinter();
        prettyPrinterWithArrayIndenter.indentArraysWith(new FixedSpaceIndenter());
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[\n", jsonGenerator.getOutputString());

        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = new DefaultPrettyPrinter();
        prettyPrinterWithObjectIndenter.indentArraysWith(new DefaultIndenter.SystemLineFeedInstance());
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[\n", jsonGenerator.getOutputString());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithSpaces = new DefaultPrettyPrinter();
        prettyPrinterWithSpaces.withSpacesInObjectEntries();
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",\n", jsonGenerator.getOutputString());

        DefaultPrettyPrinter prettyPrinterWithCustomSeparator = new DefaultPrettyPrinter();
        prettyPrinterWithCustomSeparator.withSeparators(new Separators("", "", ""));
        jsonGenerator = new TestJsonFactory().createGenerator();
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());
        prettyPrinterWithCustomSeparator.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteArrayValueSeparatorWithSpaces() throws IOException {
        DefaultPrettyPrinter prettyPrinterWithSpaces = new DefaultPrettyPrinter();
        prettyPrinterWithSpaces.withSpacesInObjectEntries();
        JsonGenerator jsonGeneratorWithSpaces = new TestJsonFactory().createGenerator();

        prettyPrinterWithSpaces.writeStartArray(jsonGeneratorWithSpaces);
        prettyPrinterWithSpaces.writeArrayValueSeparator(jsonGeneratorWithSpaces);
        jsonGeneratorWithSpaces.writeRaw("value1");
        prettyPrinterWithSpaces.writeArrayValueSeparator(jsonGeneratorWithSpaces);
        jsonGeneratorWithSpaces.writeRaw("value2");
        prettyPrinterWithSpaces.writeEndArray(jsonGeneratorWithSpaces, 2);

        assertEquals("[\n" +
                " \"value1\",\n" +
                " \"value2\"\n" +
                "]", jsonGeneratorWithSpaces.getOutputString());
    }

    @Test
    public void testWriteArrayValues() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonFactory().createGenerator();

        defaultPrettyPrinter.writeStartArray(jsonGenerator);
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value1");
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value2");
        defaultPrettyPrinter.writeEndArray(jsonGenerator, 2);

        assertEquals("[\n" +
                " \"value1\",\n" +
                " \"value2\"\n" +
                "]", jsonGenerator.getOutputString());

        DefaultPrettyPrinter prettyPrinterWithSpaces = new DefaultPrettyPrinter();
        prettyPrinterWithSpaces.withSpacesInObjectEntries();
        JsonGenerator jsonGeneratorWithSpaces = new TestJsonFactory().createGenerator();

        prettyPrinterWithSpaces.writeStartArray(jsonGeneratorWithSpaces);
        prettyPrinterWithSpaces.writeArrayValueSeparator(jsonGeneratorWithSpaces);
        jsonGeneratorWithSpaces.writeRaw("value1");
        prettyPrinterWithSpaces.writeArrayValueSeparator(jsonGeneratorWithSpaces);
        jsonGeneratorWithSpaces.writeRaw("value2");
        prettyPrinterWithSpaces.writeEndArray(jsonGeneratorWithSpaces, 2);

        assertEquals("[\n" +
                " \"value1\",\n" +
                " \"value2\"\n" +
                "]", jsonGeneratorWithSpaces.getOutputString());
    }

    private static class TestJsonFactory extends JsonFactory {
        @Override
        public JsonGenerator createGenerator() throws IOException {
            return new JsonGenerator(new StringWriter());
        }
    }
}