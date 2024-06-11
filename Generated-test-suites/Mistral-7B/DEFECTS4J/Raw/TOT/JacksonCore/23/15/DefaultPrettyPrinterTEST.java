package com.fasterxml.jackson.core.util; // import statements go here

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Test cases for DefaultPrettyPrinter class.
 */
public class DefaultPrettyPrinterTest {

 private static final DefaultPrettyPrinter DEFAULT_PP = new DefaultPrettyPrinter();

 @Test
 public void testDefaultPrettyPrint() throws JsonProcessingException {
 JsonFactory factory = new JsonFactory();
 JsonGenerator generator = factory.createGenerator(System.out, DEFAULT_PP);

 generator.writeStartObject();
 generator.writeNumberField("integerField", 123);
 generator.writeStringField("stringField", "test");
 generator.writeEndObject();
 generator.close();

 JsonNode jsonNode = factory.readValue(new StringReader(generator.getOutputContent()), JsonNode.class);
 String prettyPrintedJson = DEFAULT_PP.writeValueAsString(jsonNode);
 System.out.println(prettyPrintedJson);
 }

 @Test
 public void testCustomRootSeparator() throws JsonProcessingException {
 JsonFactory factory = new JsonFactory();
 DefaultPrettyPrinter customPP = new DefaultPrettyPrinter("[");
 JsonGenerator generator = factory.createGenerator(System.out, customPP);

 generator.writeStartObject();
 generator.writeNumberField("integerField", 123);
 generator.writeStringField("stringField", "test");
 generator.writeEndObject();
 generator.close();

 JsonNode jsonNode = factory.readValue(new StringReader(generator.getOutputContent()), JsonNode.class);
 String prettyPrintedJson = customPP.writeValueAsString(jsonNode);
 System.out.println(prettyPrintedJson);
 }

 @Test
 public void testCustomIndenters() throws JsonProcessingException {
 JsonFactory factory = new JsonFactory();
 DefaultPrettyPrinter customPP = new DefaultPrettyPrinter();

 // Use custom indenters for array and object
 customPP.indentArraysWith(new FixedSpaceIndenter());
 customPP.indentObjectsWith(new LineFeedIndenter());

 JsonGenerator generator = factory.createGenerator(System.out, customPP);

 generator.writeStartArray();
 generator.writeNumber(1);
 generator.writeNumber(2);
 generator.writeNumber(3);
 generator.writeEndArray();

 generator.writeStartObject();
 generator.writeNumberField("integerField1", 123);
 generator.writeStringField("stringField1", "test1");
 generator.writeNumberField("integerField2", 456);
 generator.writeStringField("stringField2", "test2");
 generator.writeEndObject();
 generator.close();
 }

 @Test
 public void testSpacesInObjectEntries() throws JsonProcessingException {
 JsonFactory factory = new JsonFactory();
 DefaultPrettyPrinter customPP = new DefaultPrettyPrinter();

 // Use custom indenters for object entries
 customPP.withSpacesInObjectEntries();

 JsonGenerator generator = factory.createGenerator(System.out, customPP);

 generator.writeStartObject();
 generator.writeNumberField("integerField1", 123);
 generator.writeStringField("stringField1", "test1");
 generator.writeNumberField("integerField2", 456);
 generator.writeStringField("stringField2", "test2");
 generator.writeEndObject();
 generator.close();
 }

 // Add more test cases as needed

}