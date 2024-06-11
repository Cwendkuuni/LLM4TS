package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.FixedSpaceIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.NopIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Separators;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Test cases for DefaultPrettyPrinter class
 */
@RunWith(JUnitParamsRunner.class)
public class DefaultPrettyPrinterTest {

    private JsonGenerator mockJsonGenerator;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mockJsonGenerator = Mockito.mock(JsonGenerator.class);
    }

    @Test
    public void testDefaultConstructor() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);

        // Test default behavior of writeRootValueSeparator
        prettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw("");

        // Test default behavior of writeStartObject
        prettyPrinter.writeStartObject(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw("{");

        // Test default behavior of writeObjectFieldValueSeparator
        prettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw(": ");

        // Test default behavior of writeObjectEntrySeparator
        prettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw(": ");

        // Test default behavior of writeEndObject
        prettyPrinter.writeEndObject(mockJsonGenerator, 0);
        Mockito.verify(mockJsonGenerator).writeRaw("}");

        // Test default behavior of writeStartArray
        prettyPrinter.writeStartArray(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw("[");

        // Test default behavior of writeArrayValueSeparator
        prettyPrinter.writeArrayValueSeparator(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw(", ");

        // Test default behavior of writeEndArray
        prettyPrinter.writeEndArray(mockJsonGenerator, 0);
        Mockito.verify(mockJsonGenerator).writeRaw("]");
    }

    @Test
    public void testConstructorWithRootSeparator() throws IOException, JsonProcessingException {
        SerializedString rootSeparator = new SerializedString("test");
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        assertNotNull(prettyPrinter);

        // Test writeRootValueSeparator
        prettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw(rootSeparator.getValue());
    }

    @Test
    public void testConfigureObjectIndenter() throws IOException, JsonProcessingException {
        Indenter objectIndenter = Mockito.mock(Indenter.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        // Test writeStartObject
        prettyPrinter.indentObjectsWith(objectIndenter);
        prettyPrinter.writeStartObject(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw("{");

        // Test writeObjectFieldValueSeparator
        prettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        Mockito.verify(objectIndenter).writeIndentation(mockJsonGenerator, 1);
        Mockito.verify(mockJsonGenerator).writeRaw(": ");

        // Test writeObjectEntrySeparator
        prettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);
        Mockito.verify(objectIndenter).writeIndentation(mockJsonGenerator, 1);
        Mockito.verify(mockJsonGenerator).writeRaw(": ");

        // Test writeEndObject
        prettyPrinter.writeEndObject(mockJsonGenerator, 0);
        Mockito.verify(mockJsonGenerator).writeRaw("}");

        // Test indentObjectsWith with null
        DefaultPrettyPrinter prettyPrinter2 = new DefaultPrettyPrinter();
        prettyPrinter2.indentObjectsWith(null);
        prettyPrinter2.writeStartObject(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw("{");
    }

    @Test
    public void testConfigureArrayIndenter() throws IOException, JsonProcessingException {
        Indenter arrayIndenter = Mockito.mock(Indenter.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        // Test writeStartArray
        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.writeStartArray(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw("[");

        // Test writeArrayValueSeparator
        prettyPrinter.writeArrayValueSeparator(mockJsonGenerator);
        Mockito.verify(arrayIndenter).writeIndentation(mockJsonGenerator, 1);
        Mockito.verify(mockJsonGenerator).writeRaw(", ");

        // Test writeEndArray
        prettyPrinter.writeEndArray(mockJsonGenerator, 0);
        Mockito.verify(mockJsonGenerator).writeRaw("]");

        // Test indentArraysWith with null
        DefaultPrettyPrinter prettyPrinter2 = new DefaultPrettyPrinter();
        prettyPrinter2.indentArraysWith(null);
        prettyPrinter2.writeStartArray(mockJsonGenerator);
        Mockito.verify(mockJsonGenerator).writeRaw("[");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureObjectIndenterWithNull() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureArrayIndenterWithNull() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(null);
    }

    @Test
    @Parameters(method = "createTestCasesForWriteObjectFieldValueSeparator")
    public void testWriteObjectFieldValueSeparatorWithDifferentValues(boolean spacesInObjectEntries, String expectedSeparator)
            throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        if (spacesInObjectEntries) {
            prettyPrinter.withSpacesInObjectEntries();
        } else {
            prettyPrinter.withoutSpacesInObjectEntries();
        }

        // Test writeObjectFieldValueSeparator
        prettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        String separator = prettyPrinter._objectFieldValueSeparatorWithSpaces;
        if (spacesInObjectEntries) {
            assertEquals(expectedSeparator, separator);
        } else {
            assertEquals(Separators.DEFAULT_OBJECT_FIELD_VALUE_SEPARATOR, separator);
        }
    }

    private Object[] createTestCasesForWriteObjectFieldValueSeparator() {
        return new Object[] {
            new Object[] {true, Separators.SPACE},
            new Object[] {false, Separators.COMMA}
        };
    }
}