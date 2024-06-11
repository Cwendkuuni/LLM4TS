package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

 private DefaultPrettyPrinter prettyPrinter;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 prettyPrinter = new DefaultPrettyPrinter();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDefaultConstructor() {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 try {
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
 prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");
 prettyPrinter.writeEndObject(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("{\n" +
               "  \"name\": \"John Doe\"\n" +
               "}", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testWithRootSeparatorConstructor() {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(": "));
 try {
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
 prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");
 prettyPrinter.writeEndObject(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("{\n" +
               "  : \n" +
               "  \"name\": \"John Doe\"\n" +
               "}", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testWithRootSeparatorConstructorString() {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(": "));
 prettyPrinter = new DefaultPrettyPrinter(prettyPrinter, null);
 try {
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
 prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");
 prettyPrinter.writeEndObject(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("{\n" +
               "  : \n" +
               "  \"name\": \"John Doe\"\n" +
               "}", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testWithRootSeparatorConstructorStringSerializable() {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(": "), null);
 try {
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
 prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");
 prettyPrinter.writeEndObject(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("{\n" +
               "  : \n" +
               "  \"name\": \"John Doe\"\n" +
               "}", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testWithArrayIndenter() {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter = new DefaultPrettyPrinter();
 try {
 prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.FixedSpaceIndenter());
 prettyPrinter.writeStartArray(jsonGenerator);
 prettyPrinter.writeArrayValue(jsonGenerator, 1);
 prettyPrinter.writeArrayValue(jsonGenerator, 2);
 prettyPrinter.writeEndArray(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("[ 1,\n" +
               "  2]", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testWithObjectIndenter() {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter = new DefaultPrettyPrinter();
 try {
 prettyPrinter.indentObjectsWith(new DefaultPrettyPrinter.DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
 prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");prettyPrinter.writeEndObject(jsonGenerator);String result = jsonGenerator.getOutputBuffer().toString();assertEquals("{\n" +
               "  \"name\"\n" +
               ":\n" +
               "  \"John Doe\"\n" +
               "}", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidWithObjectIndenter() {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 try {
 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter.indentObjectsWith(new DefaultPrettyPrinter.NopIndenter());
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
 prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");
 prettyPrinter.writeEndObject(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 fail();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testNullWriter() {
 JsonGenerator jsonGenerator = null;
 try {
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
 prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");
 prettyPrinter.writeEndObject(jsonGenerator);
 fail();
 } catch (JsonProcessingException e) {
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullRootSeparator() {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter = new DefaultPrettyPrinter(null);
 try {
 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeObjectFieldName(jsonGenerator, "name");
 prettyPrinter.writeObjectFieldValue(jsonGenerator, "John Doe");
 prettyPrinter.writeEndObject(jsonGenerator);
 fail();
 } catch (IOException e) {
 }
 }

}