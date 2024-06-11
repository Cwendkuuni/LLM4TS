package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

 private DefaultPrettyPrinter prettyPrinter;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 prettyPrinter = new DefaultPrettyPrinter();
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultConstructor() {
 prettyPrinter = new DefaultPrettyPrinter();
 assertNotNull(prettyPrinter);
 }

 @Test
 public void testConstructorWithRootSeparator() {
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
 assertNotNull(prettyPrinter);
 }

 @Test
 public void testWriteRootValueSeparator() throws IOException {
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 prettyPrinter.writeRootValueSeparator(generator);
 String result = generator.getOutputContent();
 assertEquals("test", result);
 }

 @Test
 public void testWriteStartObject() throws IOException {
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 prettyPrinter.writeStartObject(generator);
 String result = generator.getOutputContent();
 assertEquals("{", result);
 }

 @Test
 public void testWriteStartArray() throws IOException {
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 prettyPrinter.writeStartArray(generator);
 String result = generator.getOutputContent();
 assertEquals("[", result);
 }

 @Test
 public void testWriteEndObject() throws IOException, JsonProcessingException {
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 generator.writeStartObject();
 generator.writeObjectFieldName("fieldName");
 generator.writeObjectFieldValue("fieldValue");
 generator.writeEndObject();
 String result = generator.getOutputContent();
 assertEquals("{\"fieldName\":\"fieldValue\"}", result);
 }

 @Test
 public void testWriteEndArray() throws IOException, JsonProcessingException {
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 generator.writeStartArray();
 generator.writeValue("value1");
 generator.writeValue("value2");
 generator.writeEndArray();
 String result = generator.getOutputContent();
 assertEquals("[\"value1\",\"value2\"]", result);
 }

 @Test
 public void testIndentArraysWith() throws IOException {
 Indenter indenter = new FixedSpaceIndenter();
 prettyPrinter.indentArraysWith(indenter);
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 generator.writeStartArray();
 generator.writeValue("value1");
 generator.writeValue("value2");
 generator.writeEndArray();
 String result = generator.getOutputContent();
 assertEquals("[   ]\n[  \"value1\"\n  \"value2\"\n]", result);
 }

 @Test
 public void testIndentObjectsWith() throws IOException, JsonProcessingException {
 Indenter indenter = new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE();
 prettyPrinter.indentObjectsWith(indenter);
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 generator.writeStartObject();
 generator.writeObjectFieldName("fieldName");
 generator.writeObjectFieldValue("fieldValue");
 generator.writeEndObject();
 String result = generator.getOutputContent();
 assertEquals("{\n  \"fieldName\":\n  \"fieldValue\"\n}", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullIndenter() {
 Indenter indenter = null;
 prettyPrinter.indentArraysWith(indenter);
 }

 @Test
 public void testWithRootSeparator() throws IOException {
 String rootSeparator = "test";
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
 prettyPrinter.writeRootValueSeparator(generator);
 String result = generator.getOutputContent();
 assertEquals("test", result);
 }

 @Test
 public void testWithSpacesInObjectEntries() {
 DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();
 assertNotSame(prettyPrinter, prettyPrinterWithSpaces);
 }

 @Test
 public void testWithObjectIndenter() throws IOException {
 Indenter objectIndenter = new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE();
 DefaultPrettyPrinter prettyPrinterWithObjectIndenter = prettyPrinter.withObjectIndenter(objectIndenter);assertNotSame(prettyPrinter, prettyPrinterWithObjectIndenter); }

 @Test
 public void testWithArrayIndenter() throws IOException {
 Indenter arrayIndenter = new FixedSpaceIndenter();
 DefaultPrettyPrinter prettyPrinterWithArrayIndenter = prettyPrinter.withArrayIndenter(arrayIndenter);
 assertNotSame(prettyPrinter, prettyPrinterWithArrayIndenter);
 }
}