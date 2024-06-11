package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for DefaultPrettyPrinter class.
 */
public class DefaultPrettyPrinterTest {

 private JsonFactory jsonFactory;
 private JsonGenerator jsonGenerator;

 @Before
 public void setup() throws Exception {
 jsonFactory = new JsonFactory();
 jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), this::writeStreamClose);
 }

 /**
 * Test constructor with no arguments.
 */
 @Test
 public void testDefaultConstructor() throws JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 jsonGenerator.writeObjectStart();
 jsonGenerator.writeString("rootValue");
 jsonGenerator.writeObjectEnd();
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().array(), 0, jsonGenerator.getOutputBuffer().size());
 assertEquals("rootValue", result);
 }

 /**
 * Test constructor with rootSeparator argument.
 */
 @Test
 public void testRootSeparatorConstructor() throws JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(":rootValue:"));
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 jsonGenerator.writeObjectStart();
 jsonGenerator.writeString("rootValue");
 jsonGenerator.writeObjectEnd();
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().array(), 0, jsonGenerator.getOutputBuffer().size());
 assertEquals(":rootValue:rootValue", result);
 }

 /**
 * Test writeObjectFieldValueSeparator method with spacesInObjectEntries set to true.
 */
 @Test
 public void testWriteObjectFieldValueSeparatorWithSpaces() throws JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.withSpacesInObjectEntries();
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "value1");
 jsonGenerator.writeStringField("field2", "value2");
 jsonGenerator.writeEndObject();
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().array(), 0, jsonGenerator.getOutputBuffer().size());
 assertEquals("{\n  \"field1\": \"value1\",\n  \"field2\": \"value2\"\n}", result);
 }

 /**
 * Test writeObjectFieldValueSeparator method with spacesInObjectEntries set to false.
 */
 @Test
 public void testWriteObjectFieldValueSeparatorWithoutSpaces() throws JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "value1");
 jsonGenerator.writeStringField("field2", "value2");
 jsonGenerator.writeEndObject();
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().array(), 0, jsonGenerator.getOutputBuffer().size());
 assertEquals("{\"field1\":\"value1\",\"field2\":\"value2\"}", result);
 }

 /**
 * Test writeObjectEntrySeparator method.
 */
 @Test
 public void testWriteObjectEntrySeparator() throws JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "value1");
 jsonGenerator.writeStringField("field2", "value2");
 jsonGenerator.writeEndObject();
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().array(), 0, jsonGenerator.getOutputBuffer().size());
 assertEquals("{\n  \"field1\":\"value1\",\n  \"field2\":\"value2\"\n}", result);
 }

 /**
 * Test writeArrayValueSeparator method.
 */
 @Test
 public void testWriteArrayValueSeparator() throws JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 jsonGenerator.writeStartArray();
 jsonGenerator.writeNumber(1);
 jsonGenerator.writeNumber(2);
 jsonGenerator.writeEndArray();
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().array(), 0, jsonGenerator.getOutputBuffer().size());
 assertEquals("[1,\n  2]", result);
 }

 /**
 * Test writeRootValueSeparator method with null rootSeparator.
 */
 @Test
 public void testWriteRootValueSeparatorWithNullRootSeparator() throws JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 jsonGenerator.writeObjectStart();
 jsonGenerator.writeString("rootValue");
 jsonGenerator.writeObjectEnd();
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().array(), 0, jsonGenerator.getOutputBuffer().size());
 assertNull(result);
 }

 /**
 * Test writeObjectStart method.
 */
 @Test(expected = IOException.class)
 public void testWriteObjectStartWithException() throws Exception {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 jsonGenerator.writeObjectStart();
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 }

 /**
 * Test writeObjectEnd method.
 */
 @Test(expected = IOException.class)
 public void testWriteObjectEndWithException() throws Exception {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 jsonGenerator.writeStartObject();
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 prettyPrinter.writeObjectEnd(jsonGenerator, 0);
 }

 /**
 * Test writeArrayValueSeparator method with null separators.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testWriteArrayValueSeparatorWithNullSeparators() throws Exception {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 jsonGenerator.writeStartArray();
 prettyPrinter.writeArrayValueSeparator(jsonGenerator);
 }

 /**
 * Test writeObjectFieldValueSeparator method with null separators.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testWriteObjectFieldValueSeparatorWithNullSeparators() throws Exception {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 jsonGenerator.writeStartObject();
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 }

 private void writeStreamClose(OutputStream outputStream, byte[] buffer, int offset, int length) throws IOException {
 outputStream.close();
 }

}