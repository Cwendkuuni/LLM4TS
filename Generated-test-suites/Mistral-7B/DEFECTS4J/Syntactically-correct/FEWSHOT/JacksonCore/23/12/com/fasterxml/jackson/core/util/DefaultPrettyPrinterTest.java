package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        assertEquals("--", prettyPrinter.getRootSeparator().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorString() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        assertEquals("--", prettyPrinter.getRootSeparator().toString());

        DefaultPrettyPrinter prettyPrinter2 = new DefaultPrettyPrinter("--");
        assertEquals("--", prettyPrinter2.getRootSeparator().toString());
    }

    @Test
    public void testDefaultPrettyPrinterCopyConstructor() {
        DefaultPrettyPrinter base = new DefaultPrettyPrinter();
        DefaultPrettyPrinter copy = new DefaultPrettyPrinter(base);

        assertArrayEquals(base._arrayIndenter, copy._arrayIndenter);
        assertArrayEquals(base._objectIndenter, copy._objectIndenter);
        assertEquals(base._spacesInObjectEntries, copy._spacesInObjectEntries);
        assertEquals(base._nesting, copy._nesting);
        assertEquals(base._separators, copy._separators);
        assertEquals(base._objectFieldValueSeparatorWithSpaces, copy._objectFieldValueSeparatorWithSpaces);
        assertEquals(base._rootSeparator, copy._rootSeparator);
    }

    @Test
    public void testWriteRootValueSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeValue(new Object());
        assertEquals("--", jsonGenerator.getOutputContent().toString().substring(0, 2));
    }

    @Test
    public void testWriteStartObject() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        assertEquals("{\n", jsonGenerator.getOutputContent().toString().substring(0, 4));
    }

    @Test
    public void testWriteEndObject() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();
        assertEquals("{\n}\n", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testWriteStartObjectWithSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        assertEquals("--{\n", jsonGenerator.getOutputContent().toString().substring(0, 5));
    }

    @Test
    public void testWriteEndObjectWithSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();
        assertEquals("--{\n}\n", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("value");
        assertEquals(": ", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().indexOf(": "), jsonGenerator.getOutputContent().toString().length()));
    }

    @Test
    public void testWriteObjectFieldValueSeparatorWithSpaces() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(" "));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("value");
        assertEquals(":\n", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().indexOf(":\n"), jsonGenerator.getOutputContent().toString().length()));
    }

    @Test
    public void testWriteObjectEntrySeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("name2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();
        assertEquals("\n,\n{\n\t\"name2\":\n\t\t\"value2\"\n}", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().indexOf("\n,\n"), jsonGenerator.getOutputContent().toString().length()));
    }

    @Test
    public void testWriteStartArray() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartArray();
        assertEquals("[", jsonGenerator.getOutputContent().toString().substring(0, 1));
    }

    @Test
    public void testWriteEndArray() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeEndArray();
        assertEquals("]", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().length() - 1, jsonGenerator.getOutputContent().toString().length()));
    }

    @Test
    public void testWriteStartArrayWithSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartArray();
        assertEquals("--[\n", jsonGenerator.getOutputContent().toString().substring(0, 5));
    }

    @Test
    public void testWriteEndArrayWithSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeEndArray();
        assertEquals("]\n", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().length() - 2, jsonGenerator.getOutputContent().toString().length()));
    }

    @Test
    public void testWriteArrayValueSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue(1);
        jsonGenerator.writeValue(2);
        jsonGenerator.writeValue(3);
        jsonGenerator.writeEndArray();
        assertEquals(",\n,\n3\n", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().indexOf(",\n"), jsonGenerator.getOutputContent().toString().length()));
    }

    @Test
    public void testWriteArrayValueSeparatorWithSpaces() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(" "));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue(1);
        jsonGenerator.writeValue(2);
        jsonGenerator.writeValue(3);
        jsonGenerator.writeEndArray();
        assertEquals(",\n\n3\n", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().indexOf(",\n\n"), jsonGenerator.getOutputContent().toString().length()));
    }

    @Test
    public void testWriteObjectFieldValueSeparatorWithCustomSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("::"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("value");
        assertEquals("::", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().indexOf("::"), jsonGenerator.getOutputContent().toString().length()));
    }

    @Test
    public void testWriteObjectEntrySeparatorWithCustomSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("name2");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndObject();
        assertEquals("\n--,\n{\n--\t\"name2\":\n--\t\t\"value2\"\n}", jsonGenerator.getOutputContent().toString().substring(jsonGenerator.getOutputContent().toString().indexOf("--,\n"), jsonGenerator.getOutputContent().toString().length()));
    }
}