package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

 // Test constructor
 @Test
 public void testDefaultPrettyPrinterConstructor() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 assertNotNull(prettyPrinter);
 }

 // Test constructor with rootSeparator
 @Test
 public void testDefaultPrettyPrinterConstructorWithRootSeparator() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
 assertNotNull(prettyPrinter);
 assertEquals("test", prettyPrinter.getRootSeparator().toString());
 }

 // Test method withRootSeparator
 @Test
 public void testWithRootSeparator() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertNull(prettyPrinter.getRootSeparator());

 DefaultPrettyPrinter prettyPrinterWithRootSeparator = prettyPrinter.withRootSeparator(new SerializedString("test"));
 jsonGenerator = prettyPrinterWithRootSeparator.getJsonGenerator();
 prettyPrinterWithRootSeparator.writeRootValueSeparator(jsonGenerator);
 assertEquals("test", prettyPrinterWithRootSeparator.getRootSeparator().toString());
 }

 // Test method withRootSeparator (String)
 @Test
 public void testWithRootSeparatorString() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertNull(prettyPrinter.getRootSeparator());

 DefaultPrettyPrinter prettyPrinterWithRootSeparator = prettyPrinter.withRootSeparator("test");
 jsonGenerator = prettyPrinterWithRootSeparator.getJsonGenerator();
 prettyPrinterWithRootSeparator.writeRootValueSeparator(jsonGenerator);
 assertEquals("test", prettyPrinterWithRootSeparator.getRootSeparator().toString());
 }

 // Test method indentArraysWith
 @Test
 public void testIndentArraysWith() throws IOException, JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();

 Indenter arrayIndenter = new Indenter() {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("Array level: " + level);
 }

 @Override
 public boolean isInline() {
 return false;
 }
 };

 prettyPrinter.indentArraysWith(arrayIndenter);
 prettyPrinter.writeStartArray(jsonGenerator);
 jsonGenerator.writeRaw("value1");
 jsonGenerator.writeEndArray(jsonGenerator, 1);

 String result = jsonGenerator.getOutputШтreamAsString();
 assertEquals("Array level: 0\n{" +
                "  \"@class\":\"StartArrayContext\"" +
                "\n}[" +
                "  \"value1\"" +
                "\n]" +
                "Array level: 1\n{" +
                "  \"@class\":\"EndArrayContext\"" +
                "}", result);
 }

 // Test method indentObjectsWith
 @Test
 public void testIndentObjectsWith() throws IOException, JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();

 Indenter objectIndenter = new Indenter() {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("Object level: " + level);
 }

 @Override
 public boolean isInline() {
 return false;
 }
 };

 prettyPrinter.indentObjectsWith(objectIndenter);
 prettyPrinter.writeStartObject(jsonGenerator);
 jsonGenerator.writeRaw("key1");
 jsonGenerator.writeRaw(":");
 jsonGenerator.writeString("value1");
 jsonGenerator.writeEndObject(jsonGenerator, 1);

 String result = jsonGenerator.getOutputШтreamAsString();
 assertEquals("Object level: 0\n{" +
                "  \"@class\":\"StartObjectContext\"" +
                "\n  \"key1\":\n" +
                "  \"value1\"" +
                "\n}" +
                "Object level: 1\n{" +
                "  \"@class\":\"EndObjectContext\"" +
                "}", result);
 }

 // Test method withArrayIndenter
 @Test
 public void testWithArrayIndenter() throws IOException, JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();

 Indenter arrayIndenter = new Indenter() {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("Array level: " + level);
 }

 @Override
 public boolean isInline() {
 return false;
 }
 };

 DefaultPrettyPrinter prettyPrinterWithArrayIndenter = prettyPrinter.withArrayIndenter(arrayIndenter);
 prettyPrinterWithArrayIndenter.writeStartArray(jsonGenerator);
 jsonGenerator.writeRaw("value1");
 jsonGenerator.writeEndArray(jsonGenerator, 1);

 String result = jsonGenerator.getOutputШтreamAsString();
 assertEquals("Array level: 0\n{" +
                "  \"@class\":\"StartArrayContext\"" +
                "\n}" +
                "Array level: 1\n[" +
                "  \"value1\"" +
                "\n]" +
                "Array level: 2\n{" +
                "  \"@class\":\"EndArrayContext\"" +
                "}", result);
 }

 // Test method withObjectIndenter
 @Test
 public void testWithObjectIndenter() throws IOException, JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();

 Indenter objectIndenter = new Indenter() {
 @Override
 public void writeIndentation(JsonGenerator g, int level) throws IOException {
 g.writeRaw("Object level: " + level);
 }

 @Override
 public boolean isInline() {
 return false;
 }
 };

 DefaultPrettyPrinter prettyPrinterWithObjectIndenter = prettyPrinter.withObjectIndenter(objectIndenter);
 prettyPrinterWithObjectIndenter.writeStartObject(jsonGenerator);
 jsonGenerator.writeRaw("key1");
 jsonGenerator.writeRaw(":");
 jsonGenerator.writeString("value1");
 jsonGenerator.writeEndObject(jsonGenerator, 1);

 String result = jsonGenerator.getOutputШтreamAsString();
 assertEquals("Object level: 0\n{" +
                "  \"@class\":\"StartObjectContext\"" +
                "\n  \"key1\":\n" +
                "  \"value1\"" +
                "\n}" +
                "Object level: 1\n{" +
                "  \"@class\":\"EndObjectContext\"" +
                "}", result);
 }

 // Test method withSpacesInObjectEntries
 @Test
 public void testWithSpacesInObjectEntries() throws IOException, JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();

 DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
 prettyPrinterWithSpacesInObjectEntries.writeStartObject(jsonGenerator);
 jsonGenerator.writeRaw("key1");
 jsonGenerator.writeRaw(":");
 jsonGenerator.writeString("value1");
 jsonGenerator.writeEndObject(jsonGenerator, 1);

 String result = jsonGenerator.getOutputШтreamAsString();
 assertEquals("Object level: 0\n{" +
                "  \"@class\":\"StartObjectContext\"" +
                "\n  \"key1\":\n" +
                "  \" value1\"" +
                "\n}" +
                "Object level: 1\n{" +
                "  \"@class\":\"EndObjectContext\"" +
                "}", result);
 }

 // Test method withoutSpacesInObjectEntries
 @Test
 public void testWithoutSpacesInObjectEntries() throws IOException, JsonProcessingException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();

 DefaultPrettyPrinter prettyPrinterWithoutSpacesInObjectEntries = prettyPrinter.withoutSpacesInObjectEntries();
 prettyPrinterWithoutSpacesInObjectEntries.writeStartObject(jsonGenerator);
 jsonGenerator.writeRaw("key1");
 jsonGenerator.writeRaw(":");
 jsonGenerator.writeString("value1");
 jsonGenerator.writeEndObject(jsonGenerator, 1);

 String result = jsonGenerator.getOutputШтreamAsString();
 assertEquals("Object level: 0\n{" +
                "  \"@class\":\"StartObjectContext\"" +
                "\n  \"key1\":\n" +
                "  \"value1\"" +
                "\n}" +
                "Object level: 1\n{" +
                "  \"@class\":\"EndObjectContext\"" +
                "}", result);
 }

 // Test method withSeparators
 @Test
 public void testWithSeparators() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = prettyPrinter.getJsonGenerator();

 Separators separators = new Separators() {
 @Override
 public String getObjectEntrySeparator() {
 return "|";
 }

 @Override
 public String getObjectFieldValueSeparator() {
 return "-";
 }
 };

 DefaultPrettyPrinter prettyPrinterWithSeparators = prettyPrinter.withSeparators(separators);
 prettyPrinterWithSeparators.writeStartObject(jsonGenerator);
 jsonGenerator.writeRaw("key1");
 jsonGenerator.writeRaw(":");
 jsonGenerator.writeString("value1");
 jsonGenerator.writeEndObject(jsonGenerator, 1);

 String result = jsonGenerator.getOutputШтreamAsString();
 assertEquals("Object level: 0\n{" +
                "  \"@class\":\"StartObjectContext\"" +
                "\n  \"key1\":\n" +
                "  \"value1\"" +
                "\n}" +
                "Object level: 1\n{" +
                "  \"@class\":\"EndObjectContext\"" +
                "}", result);
 }
}