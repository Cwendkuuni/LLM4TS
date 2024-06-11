package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private JsonGenerator jsonGeneratorMock;

    @Before
    public void setup() {
        jsonGeneratorMock = mock(JsonGenerator.class);
        prettyPrinter = new DefaultPrettyPrinter();
    }

    // Test constructor without arguments
    @Test
    public void testDefaultConstructor() {
        prettyPrinter.writeRootValueSeparator(jsonGeneratorMock);
        assertEquals("", jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test constructor with rootSeparator
    @Test
    public void testConstructorWithRootSeparator() {
        String rootSeparator = "Root: ";
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinterWithRootSeparator.writeRootValueSeparator(jsonGeneratorMock);
        assertEquals(rootSeparator, jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test writeRootValueSeparator method
    @Test
    public void testWriteRootValueSeparator() {
        String rootSeparator = "Root: ";
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        String expectedRootValueSeparator = rootSeparator;
        prettyPrinterWithRootSeparator.writeRootValueSeparator(jsonGeneratorMock);
        assertEquals(expectedRootValueSeparator, jsonGeneratorMock.getOutputBuffer().toString());

        prettyPrinter = new DefaultPrettyPrinter();
        String emptyRootValueSeparator = "";
        prettyPrinter.writeRootValueSeparator(jsonGeneratorMock);
        assertEquals(emptyRootValueSeparator, jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test writeStartObject method
    @Test
    public void testWriteStartObject() throws IOException, JsonProcessingException {
        prettyPrinter.writeStartObject(jsonGeneratorMock);
        assertEquals("{", jsonGeneratorMock.getOutputBuffer().toString());

        // Test writeStartObject with rootSeparator
        String rootSeparator = "Root: ";
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinterWithRootSeparator.writeStartObject(jsonGeneratorMock);
        assertEquals(rootSeparator + "{", jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test writeEndObject method
    @Test
    public void testWriteEndObject() throws IOException, JsonProcessingException {
        prettyPrinter.writeStartObject(jsonGeneratorMock);
        prettyPrinter.writeEndObject(jsonGeneratorMock, 0);
        assertEquals("}", jsonGeneratorMock.getOutputBuffer().toString());

        // Test writeEndObject with rootSeparator
        String rootSeparator = "Root: ";
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinterWithRootSeparator.writeStartObject(jsonGeneratorMock);
        prettyPrinterWithRootSeparator.writeEndObject(jsonGeneratorMock, 0);
        assertEquals(rootSeparator + "}", jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test writeStartArray method
    @Test
    public void testWriteStartArray() throws IOException, JsonProcessingException {
        prettyPrinter.writeStartArray(jsonGeneratorMock);
        assertEquals("[-", jsonGeneratorMock.getOutputBuffer().toString());

        // Test writeStartArray with rootSeparator
        String rootSeparator = "Root: ";
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinterWithRootSeparator.writeStartArray(jsonGeneratorMock);
        assertEquals(rootSeparator + "[-", jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test writeEndArray method
    @Test
    public void testWriteEndArray() throws IOException, JsonProcessingException {
        prettyPrinter.writeStartArray(jsonGeneratorMock);
        prettyPrinter.writeEndArray(jsonGeneratorMock, 0);
        assertEquals("]", jsonGeneratorMock.getOutputBuffer().toString());

        // Test writeEndArray with rootSeparator
        String rootSeparator = "Root: ";
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinterWithRootSeparator.writeStartArray(jsonGeneratorMock);
        prettyPrinterWithRootSeparator.writeEndArray(jsonGeneratorMock, 0);
        assertEquals(rootSeparator + "]", jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test writeObjectFieldValueSeparator method
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException, JsonProcessingException {
        String objectFieldValueSeparator = ": ";
        DefaultPrettyPrinter prettyPrinterWithObjectFieldValueSeparator = new DefaultPrettyPrinter();
        prettyPrinterWithObjectFieldValueSeparator.withSeparators(new Separators(objectFieldValueSeparator));
        prettyPrinterWithObjectFieldValueSeparator.writeObjectFieldValueSeparator(jsonGeneratorMock);
        assertEquals(objectFieldValueSeparator, jsonGeneratorMock.getOutputBuffer().toString());

        // Test writeObjectFieldValueSeparator without pretty-printing
        DefaultPrettyPrenter prettyPrinterWithoutPrettyPrinting = new DefaultPrettyPrinter();
        prettyPrinterWithoutPrettyPrinting.writeObjectFieldValueSeparator(jsonGeneratorMock);
        assertEquals(":", jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test writeObjectEntrySeparator method
    @Test
    public void testWriteObjectEntrySeparator() throws IOException, JsonProcessingException {
        String objectEntrySeparator = ", ";
        DefaultPrettyPrinter prettyPrinterWithObjectEntrySeparator = new DefaultPrettyPrinter();
        prettyPrinterWithObjectEntrySeparator.withSeparators(new Separators(objectEntrySeparator));
        prettyPrinterWithObjectEntrySeparator.writeObjectEntrySeparator(jsonGeneratorMock);
        assertEquals(objectEntrySeparator, jsonGeneratorMock.getOutputBuffer().toString());

        // Test writeObjectEntrySeparator without pretty-printing
        DefaultPrettyPrinter prettyPrinterWithoutPrettyPrinting = new DefaultPrettyPrinter();
        prettyPrinterWithoutPrettyPrinting.writeObjectEntrySeparator(jsonGeneratorMock);
        assertEquals(",", jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test writeArrayValueSeparator method
    @Test
    public void testWriteArrayValueSeparator() throws IOException, JsonProcessingException {
        String arrayValueSeparator = ", ";
        DefaultPrettyPrinter prettyPrinterWithArrayValueSeparator = new DefaultPrettyPrinter();
        prettyPrinterWithArrayValueSeparator.withSeparators(new Separators(arrayValueSeparator));
        prettyPrinterWithArrayValueSeparator.writeArrayValueSeparator(jsonGeneratorMock);
        assertEquals(arrayValueSeparator, jsonGeneratorMock.getOutputBuffer().toString());

        // Test writeArrayValueSeparator without pretty-printing
        DefaultPrettyPrinter prettyPrinterWithoutPrettyPrinting = new DefaultPrettyPrinter();
        prettyPrinterWithoutPrettyPrinting.writeArrayValueSeparator(jsonGeneratorMock);
        assertEquals(",", jsonGeneratorMock.getOutputBuffer().toString());
    }

    // Test indentObjectsWith method
    @Test
    public void testIndentObjectsWith() {
        Indenter objectIndenter = mock(Indenter.class);
        prettyPrinter.indentObjectsWith(objectIndenter);
        // TODO: Add assertions for this test case
    }

    // Test indentArraysWith method
    @Test
    public void testIndentArraysWith() {
        Indenter arrayIndenter = mock(Indenter.class);
        prettyPrinter.indentArraysWith(arrayIndenter);
        // TODO: Add assertions for this test case
    }

    // Test withSpacesInObjectEntries method
    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
        // TODO: Add assertions for this test case
    }

    // Test withoutSpacesInObjectEntries method
    @Test
    public void testWithoutSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinterWithoutSpacesInObjectEntries = prettyPrinter.withoutSpacesInObjectEntries();
        // TODO: Add assertions for this test case
    }

    // Test withSeparators method
    @Test
    public void testWithSeparators() {
        String objectEntrySeparator = ", ";
        String objectFieldValueSeparator = ": ";
        DefaultPrettyPrinter prettyPrinterWithSeparators = prettyPrinter.withSeparators(new Separators(objectEntrySeparator, objectFieldValueSeparator));
        // TODO: Add assertions for this test case
    }

    // Test null argument exceptions
    @Test(expected = NullPointerException.class)
    public void testNullJsonGenerator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartObject(null);
    }
}