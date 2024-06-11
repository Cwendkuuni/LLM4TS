package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Test writeRootValueSeparator
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(null);

        // Test writeStartObject
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{\n", jsonGenerator.getOutputString());

        // Test writeObjectFieldValueSeparator and writeObjectEntrySeparator
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(" :\n", jsonGenerator.getOutputString().substring(0, 3));
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("\n{\n", jsonGenerator.getOutputString().substring(3));

        // Test writeEndObject
        prettyPrinter.writeEndObject(jsonGenerator, 0);
        assertEquals("}\n", jsonGenerator.getOutputString());

        // Test writeStartArray
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputString());

        // Test writeArrayValueSeparator and writeEndArray
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",\n", jsonGenerator.getOutputString().substring(0, 2));
        prettyPrinter.writeEndArray(jsonGenerator, 0);
        assertEquals("]\n", jsonGenerator.getOutputString());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Test writeRootValueSeparator
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw("--");

        // Test writeStartObject, writeObjectFieldValueSeparator, writeObjectEntrySeparator, and writeEndObject
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("--{\n", jsonGenerator.getOutputString());
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(" :\n", jsonGenerator.getOutputString().substring(0, 3));
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("\n--{\n", jsonGenerator.getOutputString().substring(3));
        prettyPrinter.writeEndObject(jsonGenerator, 0);
        assertEquals("}\n--", jsonGenerator.getOutputString());

        // Test writeStartArray, writeArrayValueSeparator, and writeEndArray
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputString());
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",\n", jsonGenerator.getOutputString().substring(0, 2));
        prettyPrinter.writeEndArray(jsonGenerator, 0);
        assertEquals("]\n--", jsonGenerator.getOutputString());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorAndIndenters() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        Indenter arrayIndenter = mock(Indenter.class);
        Indenter objectIndenter = mock(Indenter.class);

        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.indentObjectsWith(objectIndenter);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Test writeRootValueSeparator
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw("--");

        // Test writeStartObject, writeObjectFieldValueSeparator, writeObjectEntrySeparator, and writeEndObject
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("--{\n", jsonGenerator.getOutputString());
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(" :\n", jsonGenerator.getOutputString().substring(0, 3));
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("\n--{\n", jsonGenerator.getOutputString().substring(3));
        prettyPrinter.writeEndObject(jsonGenerator, 0);
        assertEquals("}\n--", jsonGenerator.getOutputString());

        verify(arrayIndenter, times(1)).writeIndentation(jsonGenerator, 1);
        verify(objectIndenter, times(1)).writeIndentation(jsonGenerator, 2);

        // Test writeStartArray, writeArrayValueSeparator, and writeEndArray
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputString());
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",\n", jsonGenerator.getOutputString().substring(0, 2));
        prettyPrinter.writeEndArray(jsonGenerator, 0);
        assertEquals("]\n--", jsonGenerator.getOutputString());

        verify(arrayIndenter, times(1)).writeIndentation(jsonGenerator, 1);
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Test writeObjectFieldValueSeparator and writeObjectEntrySeparator
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(" : \n", jsonGenerator.getOutputString().substring(0, 4));
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("\n", jsonGenerator.getOutputString().substring(0, 1));
    }

    @Test
    public void testDefaultPrettyPrinterWithSeparators() {
        Separators separators = new Separators();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSeparators(separators);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Test writeObjectFieldValueSeparator
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(separators.getObjectFieldValueSeparator(), jsonGenerator.getOutputString().substring(0, 1));

        // Test writeObjectEntrySeparator
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals(separators.getObjectEntrySeparator(), jsonGenerator.getOutputString().substring(0, 1));

        // Test writeArrayValueSeparator
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(separators.getArrayValueSeparator(), jsonGenerator.getOutputString().substring(0, 1));
    }
}