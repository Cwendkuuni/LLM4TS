package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

import org.junit.Before;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private JsonGenerator jsonGeneratorMock;

    @Before
    public void setUp() {
        jsonGeneratorMock = mock(JsonGenerator.class);
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() {
        String rootSeparator = "root";
        prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinter.writeRootValueSeparator(jsonGeneratorMock);
        verify(jsonGeneratorMock).writeRaw(rootSeparator);
    }

    @Test
    public void testWriteStartObject() {
        prettyPrinter.writeStartObject(jsonGeneratorMock);
        verify(jsonGeneratorMock).writeRaw('{');
    }

    @Test
    public void testBeforeObjectEntries() {
        prettyPrinter._objectIndenter = mock(Indenter.class);
        prettyPrinter.beforeObjectEntries(jsonGeneratorMock);
        verify(prettyPrinter._objectIndenter).writeIndentation(jsonGeneratorMock, 0);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        String separator = " : ";
        prettyPrinter._separators = mock(Separators.class);
        prettyPrinter._separators.getObjectFieldValueSeparator = () -> separator;
        prettyPrinter._spacesInObjectEntries = false;
        prettyPrinter.writeObjectFieldValueSeparator(jsonGeneratorMock);
        verify(jsonGeneratorMock).writeRaw(separator);
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        String separator = ",";
        prettyPrinter._separators = mock(Separators.class);
        prettyPrinter._separators.getObjectEntrySeparator = () -> separator;
        prettyPrinter.writeObjectEntrySeparator(jsonGeneratorMock);
        verify(jsonGeneratorMock).writeRaw(separator);
    }

    @Test
    public void testWriteEndObject() {
        int nrOfEntries = 5;
        prettyPrinter.writeEndObject(jsonGeneratorMock, nrOfEntries);
        if (!prettyPrinter._objectIndenter.isInline()) {
            assertEquals(0, prettyPrinter._nesting);
        }
        if (nrOfEntries > 0) {
            verify(jsonGeneratorMock).writeRaw(" " + repeatString(" ", nrOfEntries + 1));
        } else {
            verify(jsonGeneratorMock).writeRaw(' ');
        }
        verify(jsonGeneratorMock).writeRaw('}');
    }

    @Test
    public void testWriteStartArray() {
        prettyPrinter.writeStartArray(jsonGeneratorMock);
        verify(jsonGeneratorMock).writeRaw('[');
    }

    @Test
    public void testBeforeArrayValues() {
        prettyPrinter._arrayIndenter = mock(Indenter.class);
        prettyPrinter.beforeArrayValues(jsonGeneratorMock);
        verify(prettyPrinter._arrayIndenter).writeIndentation(jsonGeneratorMock, 0);
    }

    @Test
    public void testWriteArrayValueSeparator() {
        String separator = ",";
        prettyPrinter._separators = mock(Separators.class);
        prettyPrinter._separators.getArrayValueSeparator = () -> separator;
        prettyPrinter.writeArrayValueSeparator(jsonGeneratorMock);
        verify(jsonGeneratorMock).writeRaw(separator);
    }

    @Test
    public void testWriteEndArray() {
        int nrOfValues = 5;
        prettyPrinter.writeEndArray(jsonGeneratorMock, nrOfValues);
        if (!prettyPrinter._arrayIndenter.isInline()) {
            assertEquals(0, prettyPrinter._nesting);
        }
        if (nrOfValues > 0) {
            verify(jsonGeneratorMock).writeRaw(" " + repeatString(" ", nrOfValues + 1));
        } else {
            verify(jsonGeneratorMock).writeRaw(' ');
        }
        verify(jsonGeneratorMock).writeRaw(']');
    }

    private String repeatString(String str, int numberOfTimes) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numberOfTimes; i++) {
            result.append(str);
        }
        return result.toString();
    }
}