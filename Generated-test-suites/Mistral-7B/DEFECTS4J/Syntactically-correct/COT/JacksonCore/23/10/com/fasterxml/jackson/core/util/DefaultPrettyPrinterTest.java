package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.io.SerializedString;

/**
 * Test class for DefaultPrettyPrinter
 */
public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setUp() {
        // Initialize the DefaultPrettyPrinter instance for testing
        prettyPrinter = new DefaultPrettyPrinter();
    }

    

    // Test method for DefaultPrettyPrinter constructor and getters
    @Test
    public void testDefaultPrettyPrinter() {
        assertNotNull(prettyPrinter);
        assertEquals(DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        assertNull(prettyPrinter._arrayIndenter);
        assertNull(prettyPrinter._objectIndenter);
        assertNull(prettyPrinter._rootSeparator);
        assertFalse(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);
    }

    // Test method for withRootSeparator constructor and setter
    @Test
    public void testWithRootSeparatorConstructorAndSetter() {
        String rootSeparator = "rootSeparator";
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        assertEquals(rootSeparator, prettyPrinterWithRootSeparator._rootSeparator.asString());

        DefaultPrettyPrinter prettyPrinterWithRootSeparator2 = prettyPrinterWithRootSeparator.withRootSeparator(new SerializedString("rootSeparator2"));
        assertEquals("rootSeparator2", prettyPrinterWithRootSeparator2._rootSeparator.asString());
    }

    // Test method for indentArraysWith method
    @Test
    public void testIndentArraysWith() {
        Indenter arrayIndenter = new FixedSpaceIndenter();
        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = prettyPrinter.withArrayIndenter(arrayIndenter);
        assertEquals(arrayIndenter, prettyPrinterWithArrayIndenter._arrayIndenter);
    }

    // Test method for indentObjectsWith method
    @Test
    public void testIndentObjectsWith() {
        Indenter objectIndenter = new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE();
        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = prettyPrinter.withObjectIndenter(objectIndenter);
        assertEquals(objectIndenter, prettyPrinterWithObjectIndenter._objectIndenter);
    }

    // Test method for withSpacesInObjectEntries method
    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
        assertTrue(prettyPrinterWithSpacesInObjectEntries._spacesInObjectEntries);

        DefaultPrettyPrinter prettyPrinterWithoutSpacesInObjectEntries = prettyPrinter.withoutSpacesInObjectEntries();
        assertFalse(prettyPrinterWithoutSpacesInObjectEntries._spacesInObjectEntries);
    }

    // Test method for writeRootValueSeparator method
    @Test
    public void testWriteRootValueSeparator() throws IOException, JsonProcessingException {
        String rootSeparator = "rootSeparator";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = jsonGenerator.getPrettyPrinter().setRootValueSeparator(new SerializedString(rootSeparator));
        prettyJsonGenerator.writeStartObject();
        prettyJsonGenerator.writeRootValueSeparator(prettyJsonGenerator);
        prettyJsonGenerator.writeEndObject();
        prettyJsonGenerator.close();

        assertEquals(rootSeparator, jsonGenerator.getOutputContent().toString().trim());
    }

    // Test method for writeStartObject method
    @Test
    public void testWriteStartObject() throws IOException, JsonProcessingException {
        String expectedOutput = "{\n";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartObject();
        prettyJsonGenerator.writeEndObject();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        assertEquals(expectedOutput, jsonGenerator.getOutputContent().toString());
    }

    // Test method for beforeObjectEntries method
    @Test
    public void testBeforeObjectEntries() throws IOException, JsonProcessingException {
        String expectedIndentation = "  ";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartObject();
        prettyJsonGenerator.beforeObjectEntries(prettyJsonGenerator);
        prettyJsonGenerator.writeObjectFieldName("field1");
        prettyJsonGenerator.writeObjectFieldValue("value1");
        prettyJsonGenerator.writeEndObject();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        String actualIndentation = prettyJsonGenerator.getOutputContent().toString().substring(0, expectedIndentation.length());
        assertEquals(expectedIndentation, actualIndentation);
    }

    // Test method for writeObjectFieldValueSeparator method
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException, JsonProcessingException {
        String separator = ": ";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartObject();
        prettyJsonGenerator.writeObjectFieldName("field1");
        prettyJsonGenerator.writeObjectFieldValueSeparator(prettyJsonGenerator);
        prettyJsonGenerator.writeObjectFieldValue("value1");
        prettyJsonGenerator.writeEndObject();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        String expectedOutput = "{\n  \"field1\":\n";
        String actualOutput = jsonGenerator.getOutputContent().toString();

        assertTrue(actualOutput.contains(expectedOutput));
        assertTrue(actualOutput.contains(separator));
    }

    // Test method for writeObjectEntrySeparator method
    @Test
    public void testWriteObjectEntrySeparator() throws IOException, JsonProcessingException {
        String separator = ",";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartObject();
        prettyJsonGenerator.writeObjectFieldName("field1");
        prettyJsonGenerator.writeObjectFieldValue("value1");
        prettyJsonGenerator.writeObjectEntrySeparator(prettyJsonGenerator);
        prettyJsonGenerator.writeObjectFieldName("field2");
        prettyJsonGenerator.writeObjectFieldValue("value2");
        prettyJsonGenerator.writeEndObject();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        String expectedOutput = "{\n  \"field1\":\n";
        String actualOutput = jsonGenerator.getOutputContent().toString();

        assertTrue(actualOutput.contains(expectedOutput + separator));
    }

    // Test method for writeEndObject method
    @Test
    public void testWriteEndObject() throws IOException, JsonProcessingException {
        String expectedOutput = "}\n";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartObject();
        prettyJsonGenerator.writeEndObject();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        assertEquals(expectedOutput, jsonGenerator.getOutputContent().toString());
    }

    // Test method for writeStartArray method
    @Test
    public void testWriteStartArray() throws IOException, JsonProcessingException {
        String expectedOutput = "[";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartArray();
        prettyJsonGenerator.writeEndArray();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        assertEquals(expectedOutput, jsonGenerator.getOutputContent().toString());
    }

    // Test method for beforeArrayValues method
    @Test
    public void testBeforeArrayValues() throws IOException, JsonProcessingException {
        String expectedIndentation = "  ";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartArray();
        prettyJsonGenerator.beforeArrayValues(prettyJsonGenerator);
        prettyJsonGenerator.writeValue("value1");
        prettyJsonGenerator.writeValue("value2");
        prettyJsonGenerator.writeEndArray();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        String actualIndentation = prettyJsonGenerator.getOutputContent().toString().substring(0, expectedIndentation.length());
        assertEquals(expectedIndentation, actualIndentation);
    }

    // Test method for writeArrayValueSeparator method
    @Test
    public void testWriteArrayValueSeparator() throws IOException, JsonProcessingException {
        String separator = ",";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartArray();
        prettyJsonGenerator.writeValue("value1");
        prettyJsonGenerator.writeArrayValueSeparator(prettyJsonGenerator);
        prettyJsonGenerator.writeValue("value2");
        prettyJsonGenerator.writeEndArray();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        String expectedOutput = "[";
        String actualOutput = jsonGenerator.getOutputContent().toString();

        assertTrue(actualOutput.contains(expectedOutput + separator));
    }

    // Test method for writeEndArray method
    @Test
    public void testWriteEndArray() throws IOException, JsonProcessingException {
        String expectedOutput = "]";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonGenerator prettyJsonGenerator = prettyPrinter.getFactory().createGenerator(System.out);
        prettyJsonGenerator.setPrettyPrinter(prettyPrinter);
        prettyJsonGenerator.writeStartArray();
        prettyJsonGenerator.writeEndArray();
        prettyJsonGenerator.close();
        jsonGenerator.close();

        assertEquals(expectedOutput, jsonGenerator.getOutputContent().toString());
    }

    
}