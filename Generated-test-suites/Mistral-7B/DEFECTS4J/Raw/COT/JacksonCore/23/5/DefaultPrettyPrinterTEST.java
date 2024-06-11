package com.fasterxml.jackson.core.util; // make sure the test class is in the same package as the class under test

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*; // assume we are using Mockito for mocking

import static org.junit.Assert.*; // assume we are using JUnit assertions

public class DefaultPrettyPrinterTest {

    // create a test instance and mocks
    DefaultPrettyPrinter prettyPrinter;
    JsonGenerator mockJsonGenerator;

    @Before
    public void setUp() {
        mockJsonGenerator = mock(JsonGenerator.class);
        prettyPrinter = new DefaultPrettyPrinter();
    }

    // test case for constructor with no arguments
    @Test
    public void testDefaultConstructor() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(" ");
    }

    // test case for constructor with root separator
    @Test
    public void testConstructorWithRootSeparator() throws IOException {
        String rootSeparator = "test";
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        pp.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(rootSeparator);
    }

    // test case for writeStartObject()
    @Test
    public void testWriteStartObject() throws IOException {
        prettyPrinter.writeStartObject(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("{");
    }

    // test case for writeEndObject()
    @Test
    public void testWriteEndObject() throws IOException {
        prettyPrinter.writeEndObject(mockJsonGenerator, 1);
        verify(mockJsonGenerator).writeRaw("}");
    }

    // test case for writeStartArray()
    @Test
    public void testWriteStartArray() throws IOException {
        prettyPrinter.writeStartArray(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("[");
    }

    // test case for writeEndArray()
    @Test
    public void testWriteEndArray() throws IOException {
        prettyPrinter.writeEndArray(mockJsonGenerator, 1);
        verify(mockJsonGenerator).writeRaw("]");
    }

    // test case for writeObjectFieldValueSeparator()
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        prettyPrinter.withRootSeparator(new SerializedString(""));
        prettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("");

        String separator = ": ";
        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withRootSeparator(null);
        prettyPrinter.withSeparators(new DefaultPrettyPrinter.Separators(""));
        prettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(separator);
    }

    // test case for writeArrayValueSeparator()
    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        prettyPrinter.withRootSeparator(new SerializedString(""));
        prettyPrinter.withSeparators(new DefaultPrettyPrinter.Separators(""));
        prettyPrinter.writeArrayValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("");
    }

    // test case for writeObjectEntrySeparator()
    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        String separator = "\n";
        prettyPrinter.withRootSeparator(null);
        prettyPrinter.withSeparators(new DefaultPrettyPrinter.Separators(separator));
        prettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(separator);
    }

    // test case for writeRootValueSeparator() with null root separator
    @Test
    public void testWriteRootValueSeparatorWithNullRootSeparator() throws IOException {
        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator, never()).writeRaw("");
    }

    // test case for writeRootValueSeparator() with empty root separator
    @Test
    public void testWriteRootValueSeparatorWithEmptyRootSeparator() throws IOException {
        prettyPrinter = new DefaultPrettyPrinter(new SerializedString(""));
        prettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator, never()).writeRaw("");
    }

    // test case for writeRootValueSeparator() with non-empty root separator
    @Test
    public void testWriteRootValueSeparatorWithNonEmptyRootSeparator() throws IOException {
        String rootSeparator = "test";
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        pp.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(rootSeparator);
    }

    // test case for writeStartObject() with indentation
    @Test
    public void testWriteStartObjectWithIndentation() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentObjectsWith(new DefaultPrettyPrinter.FixedSpaceIndenter());
        pp.writeStartObject(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("{\n");
    }

    // test case for writeStartArray() with indentation
    @Test
    public void testWriteStartArrayWithIndentation() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(new DefaultPrettyPrinter.FixedSpaceIndenter());
        pp.writeStartArray(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("[");
        pp.beforeArrayValues(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("\n");
    }

    // test case for writeObjectEntrySeparator() with indentation
    @Test
    public void testWriteObjectEntrySeparatorWithIndentation() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeObjectEntrySeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("\n");
    }

    // test case for writeArrayValueSeparator() with indentation
    @Test
    public void testWriteArrayValueSeparatorWithIndentation() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.beforeArrayValues(mockJsonGenerator);
        pp.writeArrayValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("\n");
    }

    // test case for writeObjectFieldValueSeparator() with spaces instead of colon
    @Test
    public void testWriteObjectFieldValueSeparatorWithSpaces() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.withSpacesInObjectEntries();
        pp.writeObjectFieldValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(" ");
    }

    // test case for writeObjectFieldValueSeparator() without spaces
    @Test(expected = IllegalArgumentException.class)
    public void testWriteObjectFieldValueSeparatorWithoutSpaces() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.withoutSpacesInObjectEntries();
        pp.writeObjectFieldValueSeparator(mockJsonGenerator);
        fail("Expected IllegalArgumentException");
    }
}