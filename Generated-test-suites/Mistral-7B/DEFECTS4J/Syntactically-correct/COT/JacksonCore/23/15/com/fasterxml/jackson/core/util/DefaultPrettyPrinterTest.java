package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

 // Test for constructor without arguments
 @Test
 public void testDefaultConstructor() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 assertNotNull(prettyPrinter);
 }

 // Test for constructor with root separator
 @Test
 public void testConstructorWithRootSeparator() {
 String rootSeparator = "test";
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
 assertNotNull(prettyPrinter);
 assertEquals(rootSeparator, prettyPrinter.getRootSeparator());
 }

 // Test for method withRootSeparator
 @Test
 public void testWithRootSeparator() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 String rootSeparator = "test";
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withRootSeparator(new SerializedString(rootSeparator));
 assertNotSame(prettyPrinter, newPrettyPrinter);
 assertEquals(rootSeparator, newPrettyPrinter.getRootSeparator());
 }

 // Test for method indentArraysWith
 @Test
 public void testIndentArraysWith() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 Indenter indenter = new Indenter() {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("test");
 }

 @Override
 public boolean isInline() {
 return false;
 }
 };
 prettyPrinter.indentArraysWith(indenter);
 Indenter arrayIndenter = prettyPrinter.getArrayIndenter();
 assertEquals(indenter, arrayIndenter);
 }

 // Test for method indentObjectsWith
 @Test
 public void testIndentObjectsWith() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 Indenter indenter = new Indenter() {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("test");
 }

 @Override
 public boolean isInline() {
 return false;
 }
 };
 prettyPrinter.indentObjectsWith(indenter);
 Indenter objectIndenter = prettyPrinter.getObjectIndenter();
 assertEquals(indenter, objectIndenter);
 }

 // Test for method withArrayIndenter
 @Test
 public void testWithArrayIndenter() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 Indenter indenter = new Indenter() {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("test");
 }

 @Override
 public boolean isInline() {
 return false;
 }
 };
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withArrayIndenter(indenter);
 assertNotSame(prettyPrinter, newPrettyPrinter);
 Indenter arrayIndenter = newPrettyPrinter.getArrayIndenter();
 assertEquals(indenter, arrayIndenter);
 }

 // Test for method withObjectIndenter
 @Test
 public void testWithObjectIndenter() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 Indenter indenter = new Indenter() {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("test");
 }

 @Override
 public boolean isInline() {
 return false;
 }
 };
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withObjectIndenter(indenter);
 assertNotSame(prettyPrinter, newPrettyPrinter);
 Indenter objectIndenter = newPrettyPrinter.getObjectIndenter();
 assertEquals(indenter, objectIndenter);
 }

 // Test for method withSpacesInObjectEntries
 @Test
 public void testWithSpacesInObjectEntries() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withSpacesInObjectEntries();
 assertNotSame(prettyPrinter, newPrettyPrinter);
 assertTrue(newPrettyPrinter.isSpacesInObjectEntries());
 }

 // Test for method withoutSpacesInObjectEntries
 @Test
 public void testWithoutSpacesInObjectEntries() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withoutSpacesInObjectEntries();
 assertNotSame(prettyPrinter, newPrettyPrinter);
 assertFalse(newPrettyPrinter.isSpacesInObjectEntries());
 }

 // Test for method withSeparators
 @Test
 public void testWithSeparators() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 Separators separators = new Separators() {
 @Override
 public String getObjectEntrySeparator() {
 return "testObjectEntrySeparator";
 }

 @Override
 public String getObjectFieldValueSeparator() {
 return "testObjectFieldValueSeparator";
 }

 @Override
 public String getArrayValueSeparator() {
 return "testArrayValueSeparator";
 }
 };
 DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withSeparators(separators);
 assertNotSame(prettyPrinter, newPrettyPrinter);
 assertEquals(separators.getObjectEntrySeparator(), newPrettyPrinter.getObjectEntrySeparator());
 assertEquals(separators.getObjectFieldValueSeparator(), newPrettyPrinter.getObjectFieldValueSeparator());
 assertEquals(separators.getArrayValueSeparator(), newPrettyPrinter.getArrayValueSeparator());
 }

 // Test for writeRootValueSeparator method
 @Test
 public void testWriteRootValueSeparator() throws JsonProcessingException, IOException {
 String rootSeparator = "test";
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 if (prettyPrinter.getRootSeparator() != null) {
 jsonGenerator.writeRaw(prettyPrinter.getRootSeparator().getValueAsString());
 }
 assertEquals(rootSeparator, jsonGenerator.getOutputBuffer().toString());
 }

 // Test for writeStartObject method
 @Test
 public void testWriteStartObject() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter.writeStartObject(jsonGenerator);
 assertEquals("{\n", jsonGenerator.getOutputBuffer().toString());
 }

 // Test for beforeObjectEntries method
 @Test
 public void testBeforeObjectEntries() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter.beforeObjectEntries(jsonGenerator);
 assertEquals("  \n", jsonGenerator.getOutputBuffer().toString());
 }

 // Test for writeObjectFieldValueSeparator method
 @Test
 public void testWriteObjectFieldValueSeparator() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 String objectFieldValueSeparator = "test";
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 if (prettyPrinter.isSpacesInObjectEntries()) {
 jsonGenerator.writeRaw(prettyPrinter.getObjectFieldValueSeparatorWithSpaces());
 } else {
 jsonGenerator.writeRaw(prettyPrinter.getSeparators().getObjectFieldValueSeparator());
 }
 assertEquals(objectFieldValueSeparator, jsonGenerator.getOutputBuffer().toString());
 }

 // Test for writeObjectEntrySeparator method
 @Test
 public void testWriteObjectEntrySeparator() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 assertEquals("\n  ", jsonGenerator.getOutputBuffer().toString());
 }

 // Test for writeEndObject method
 @Test
 public void testWriteEndObject() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter.writeEndObject(jsonGenerator, 0);
 assertEquals("}\n", jsonGenerator.getOutputBuffer().toString());
 }

 // Test for writeStartArray method
 @Test
 public void testWriteStartArray() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter.writeStartArray(jsonGenerator);
 assertEquals("[", jsonGenerator.getOutputBuffer().toString());
 }

 // Test for beforeArrayValues method
 @Test
 public void testBeforeArrayValues() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter.beforeArrayValues(jsonGenerator);
 assertEquals("  \n", jsonGenerator.getOutputBuffer().toString());
 }

 // Test for writeArrayValueSeparator method
 @Test
 public void testWriteArrayValueSeparator() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 String arrayValueSeparator = "test";
 prettyPrinter.writeArrayValueSeparator(jsonGenerator);
 assertEquals(arrayValueSeparator, jsonGenerator.getOutputBuffer().toString());
 }

 // Test for writeEndArray method
 @Test
 public void testWriteEndArray() throws JsonProcessingException, IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter.writeEndArray(jsonGenerator, 0);
 assertEquals("]\n", jsonGenerator.getOutputBuffer().toString());
 }
}