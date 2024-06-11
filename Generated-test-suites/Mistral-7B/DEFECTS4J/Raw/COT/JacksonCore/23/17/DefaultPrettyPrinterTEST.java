package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

 DefaultPrettyPrinter prettyPrinter;
 JsonFactory jsonFactory;
 ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 prettyPrinter = new DefaultPrettyPrinter();
 jsonFactory = new JsonFactory(prettyPrinter);
 objectMapper = new ObjectMapper(jsonFactory);
 }

 // Test constructor with no arguments
 @Test
 public void testDefaultConstructor() throws JsonProcessingException {
 String json = prettyPrinter.writeValueAsString(new Object());
 assertEquals("{\n", json.substring(0, 4));
 }

 // Test constructor with rootSeparator argument
 @Test
 public void testConstructorWithRootSeparator() throws JsonProcessingException {
 String rootSeparator = "|";
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
 String json = prettyPrinter.writeValueAsString(new Object());
 assertEquals(rootSeparator.getValue(), json.substring(0, rootSeparator.getValue().length()));
 }

 // Test writeRootValueSeparator method
 @Test
 public void testWriteRootValueSeparator() throws JsonGenerationException, IOException {
 String rootSeparator = "|";
 JsonGenerator generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
 prettyPrinter.writeRootValueSeparator(generator);
 generator.writeRaw(rootSeparator.getValue());
 generator.close();
 }

 // Test writeStartObject method
 @Test
 public void testWriteStartObject() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new Object());
 assertEquals("{\n", json.substring(0, 4));
 }

 // Test writeEndObject method with no entries
 @Test
 public void testWriteEndObjectWithNoEntries() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new Object());
 assertEquals("{\n}", json);
 }

 // Test writeEndObject method with one entry
 @Test
 public void testWriteEndObjectWithOneEntry() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new Object() {
 Field field = new Field("field");
 String value = "value";

 @Override
 public String toString() {
 return "{" + field.getName() + ":" + value + "}";
 }

 class Field {
 String name;

 Field(String name) {
 this.name = name;
 }

 String getName() {
 return name;
 }
 }
 });
 assertEquals("{\n  \"field\":\"value\"\n}", json);
 }

 // Test writeStartArray method
 @Test
 public void testWriteStartArray() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new int[] { 1, 2, 3 });
 assertEquals("[", json.substring(0, 1));
 }

 // Test writeEndArray method with no values
 @Test
 public void testWriteEndArrayWithNoValues() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new int[] {});
 assertEquals("[", json.substring(0, 1));
 }

 // Test writeEndArray method with one value
 @Test
 public void testWriteEndArrayWithOneValue() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new int[] { 1 });
 assertEquals("[1]", json);
 }

 // Test writeEndArray method with multiple values
 @Test
 public void testWriteEndArrayWithMultipleValues() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new int[] { 1, 2, 3 });
 assertEquals("[1,\n 2,\n 3]", json);
 }

 // Test writeObjectEntrySeparator method
 @Test
 public void testWriteObjectEntrySeparator() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new Object() {
 Field field1 = new Field("field1");
 Field field2 = new Field("field2");
 String value = "value";

 @Override
 public String toString() {
 return "{" + field1.getName() + ":" + value + ",\n " + field2.getName() + ":" + value + "}";
 }

 class Field {
 String name;

 Field(String name) {
 this.name = name;
 }

 String getName() {
 return name;
 }
 }
 });
 assertEquals("{\n  \"field1\":\"value\"\n  ", json.substring(0, json.length()-1));
 assertEquals(":", json.substring(json.indexOf(":"), json.indexOf(":")+1).toString());
 }

 // Test writeArrayValueSeparator method
 @Test
 public void testWriteArrayValueSeparator() throws JsonGenerationException, IOException {
 String json = prettyPrinter.writeValueAsString(new int[] { 1, 2, 3 });
 assertEquals(",\n  ", json.substring(json.indexOf(",")+1, json.length()));
 }

 // Test indentObjectsWith and indentArraysWith methods
 @Test
 public void testIndentObjectsWithAndIndentArraysWith() throws JsonProcessingException {
 Indenter objectIndenter = new ObjectIndenter();
 Indenter arrayIndenter = new ArrayIndenter();
 prettyPrinter.indentObjectsWith(objectIndenter);
 prettyPrinter.indentArraysWith(arrayIndenter);
 String json = prettyPrinter.writeValueAsString(new Object() {
 Field field1 = new Field("field1");
 Field field2 = new Field("field2");
 String value = "value";

 @Override
 public String toString() {
 return "{" + field1.getName() + ":" + value + ",\n " + field2.getName() + ":" + value + "}";
 }

 class Field {
 String name;

 Field(String name) {
 this.name = name;
 }

 String getName() {
 return name;
 }
 }
 });
 assertEquals("{\n  \"field1\":\"value\"\n  ", json.substring(0, json.length()-1));
 assertEquals("{\n", json.substring(0, json.indexOf("{\n")+1));
 String arrayJson = prettyPrinter.writeValueAsString(new int[] { 1, 2, 3 });
 assertEquals("[1,\n 2,\n 3]", arrayJson);
 assertEquals("[", arrayJson.substring(0, arrayJson.indexOf("[")+1));
 }

 // Test withSpacesInObjectEntries method
 @Test
 public void testWithSpacesInObjectEntries() throws JsonProcessingException {
 boolean withSpaces = true;
 DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();
 String json = prettyPrinterWithSpaces.writeValueAsString(new Object() {
 Field field1 = new Field("field1");
 Field field2 = new Field("field2");
 String value = "value";

 @Override
 public String toString() {
 return "{" + field1.getName() + ":" + value + ",\n " + field2.getName() + ":" + value + "}";
 }

 class Field {
 String name;

 Field(String name) {
 this.name = name;
 }

 String getName() {
 return name;
 }
 }
 });
 assertEquals("{\n  \"field1\":\"value\"\n  ", json.substring(0, json.length()-1));
 assertEquals(" ", json.substring(json.indexOf(" ")+1, json.indexOf(":",json.lastIndexOf(":"))).toString());
 assertEquals(":", json.substring(json.lastIndexOf(":")-1, json.lastIndexOf(":")).toString());
 }

 // Test withoutSpacesInObjectEntries method
 @Test
 public void testWithoutSpacesInObjectEntries() throws JsonProcessingException {
 boolean withSpaces = false;
 DefaultPrettyPrinter prettyPrinterWithoutSpaces = prettyPrinter.withSpacesInObjectEntries();
 String json = prettyPrinterWithoutSpaces.writeValueAsString(new Object() {
 Field field1 = new Field("field1");
 Field field2 = new Field("field2");
 String value = "value";

 @Override
 public String toString() {
 return "{" + field1.getName() + ":" + value + ",\n " + field2.getName() + ":" + value + "}";
 }

 class Field {
 String name;

 Field(String name) {
 this.name = name;
 }

 String getName() {
 return name;
 }
 }
 });
 assertEquals("{\n  \"field1\":value\n  ", json.substring(0, json.length()-1));
 assertEquals(":", json.substring(json.lastIndexOf(":")-1, json.lastIndexOf(":")).toString());
 }

 // Test withSeparators method
 @Test
 public void testWithSeparators() throws JsonProcessingException {
 String objectFieldValueSeparator = ",";
 String objectEntrySeparator = "\n";
 String arrayValueSeparator = "|";
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 Separators separators = new Separators(objectFieldValueSeparator, objectEntrySeparator, arrayValueSeparator);
 prettyPrinter.withSeparators(separators);
 String json = prettyPrinter.writeValueAsString(new Object() {
 Field field1 = new Field("field1");
 Field field2 = new Field("field2");
 String value = "value";

 @Override
 public String toString() {
 return "{" + field1.getName() + separators.getObjectFieldValueSeparator() + value + ",\n " + field2.getName() + separators.getObjectFieldValueSeparator() + value + "}";
 }

 class Field {
 String name;

 Field(String name) {
 this.name = name;
 }

 String getName() {
 return name;
 }
 }
 });
 assertEquals("{\n  \"field1\"", json.substring(0, json.indexOf(separators.getObjectFieldValueSeparator())));
 assertEquals(separators.getObjectFieldValueSeparator(), json.substring(json.indexOf(separators.getObjectFieldValueSeparator()), json.indexOf(separators.getObjectFieldValueSeparator())+separators.getObjectFieldValueSeparator().length()));
 assertEquals(":\n  \"value\"", json.substring(json.lastIndexOf(":")+1, json.length()));
 String jsonArray = prettyPrinter.writeValueAsString(new int[] { 1, 2, 3 });
 assertEquals("[1", jsonArray.substring(0, jsonArray.indexOf(separators.getArrayValueSeparator())));
 assertEquals(separators.getArrayValueSeparator(), jsonArray.substring(jsonArray.indexOf(separators.getArrayValueSeparator()), jsonArray.indexOf(separators.getArrayValueSeparator())+separators.getArrayValueSeparator().length()));
 assertEquals(",\n  2", jsonArray.substring(jsonArray.indexOf(",")+1, jsonArray.indexOf(",")+separators.getArrayValueSeparator().length()+1));
 assertEquals(",\n  3", jsonArray.substring(jsonArray.lastIndexOf(",")+1, jsonArray.length()));
 }

}

class ObjectIndenter implements Indenter {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("\t".repeat(level));
 }

 @Override
 public boolean isInline() {
 return false;
 }
}

class ArrayIndenter implements Indenter {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("  ".repeat(level));
 }

 @Override
 public boolean isInline() {
 return false;
 }
}

class Separators {
 String objectFieldValueSeparator;
 String objectEntrySeparator;
 String arrayValueSeparator;

 Separators(String objectFieldValueSeparator, String objectEntrySeparator, String arrayValueSeparator) {
 this.objectFieldValueSeparator = objectFieldValueSeparator;
 this.objectEntrySeparator = objectEntrySeparator;
 this.arrayValueSeparator = arrayValueSeparator;
 }

 String getObjectFieldValueSeparator() {
 return objectFieldValueSeparator;
 }

 String getObjectEntrySeparator() {
 return objectEntrySeparator;
 }

 String getArrayValueSeparator() {
 return arrayValueSeparator;
 }
}