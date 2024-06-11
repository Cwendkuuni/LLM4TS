package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.TestUtils;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.SerializationTest;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private static final IOContext ioc = new IOContext();

 @Test
 public void testDefaultSettings() throws IOException {
 JsonGeneratorImpl gen = new UTF8JsonGenerator(ioc, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectMapper(ioc));

 // write a simple JSON object
 gen.writeStartObject();
 gen.writeStringField("name", "Alice");
 gen.writeNumberField("age", 30);
 gen.writeEndObject();
 gen.close();

 // check serialized output
 String result = TestUtils.writeValueAsString(gen);
 JsonNode jsonNode = new ObjectMapper(ioc).readTree(result);
 JsonNode rootNode = jsonNode.get(0);
 assertEquals("{\"name\":\"Alice\",\"age\":30}", result);
 assertEquals("{\"name\":\"Alice\",\"age\":30}", TestUtils.writeValueAsString(rootNode));
 }

 @Test
 public void testQuoteFieldNames() throws IOException {
 JsonGeneratorImpl gen = new UTF8JsonGenerator(ioc, Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.QUOTE_FIELD_NAMES.getMask(), new ObjectMapper(ioc));

 gen.writeStartObject();
 gen.writeStringField("field", "value");
 gen.writeEndObject();
 gen.close();

 String result = TestUtils.writeValueAsString(gen);
 JsonNode jsonNode = new ObjectMapper(ioc).readTree(result);
 JsonNode rootNode = jsonNode.get(0);
 assertEquals("{\"field\":\"value\"}", result);
 assertEquals("{\"field\":\"value\"}", TestUtils.writeValueAsString(rootNode));
 }

 @Test
 public void testCustomEscapes() throws IOException {
 JsonGeneratorImpl gen = new UTF8JsonGenerator(ioc, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectMapper(ioc) {
 @Override
 public CharacterEscapes getCharacterEscapes() {
 return new CharacterEscapes() {
 @Override
 public int[] getEscapeCodesForAscii() {
 int[] escapes = JsonGeneratorImpl.sOutputEscapes.clone();
 escapes[CharTypes.QUOTE.asInt()] = '\u001d'; // CHAR_ESC_BACKSLASH;
 return escapes;
 }
 };
 }
 });

 gen.writeStartArray();
 gen.writeString("Alice\\\\Bob");
 gen.writeEndArray();
 gen.close();

 String result = TestUtils.writeValueAsString(gen);
assertEquals("[\"Alice\\\\u001dBob\"]", result);
 }

 @Test(expected = IOException.class)
 public void testNegativeMaximumNonEscapedChar() throws IOException {
 JsonGeneratorImpl gen = new UTF8JsonGenerator(ioc, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectMapper(ioc));
 gen.setHighestNonEscapedChar(-1);
 gen.writeStartObject();
 gen.writeStringField("name", "\u0001Alice");
 gen.writeEndObject();
 gen.close();
 }

 @Test(expected = IOException.class)
 public void testMaximumNonEscapedChar() throws IOException {
 JsonGeneratorImpl gen = new UTF8JsonGenerator(ioc, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectMapper(ioc));
 gen.setHighestNonEscapedChar(127);
 gen.writeStartObject();
 gen.writeStringField("name", "\u007FAlice"); // 127 is the FS character
 gen.writeEndObject();
 gen.close();
 }

 @Test
 public void testRootValueSeparator() throws IOException {
 DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
 printer.setRootValueSeparator(": ");
 JsonGeneratorImpl gen = new UTF8JsonGenerator(ioc, Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.QUOTE_FIELD_NAMES.getMask(), new ObjectMapper(ioc));
 gen.setPrettyPrinter(printer);
 gen.writeStartObject();
 gen.writeStringField("name", "Alice");
 gen.writeNumberField("age", 30);
 gen.writeEndObject();
 gen.close();

 String result = TestUtils.writeValueAsString(gen);
 assertEquals("{\n: name : \"Alice\",\n: age : 30\n}", result);
 }
}