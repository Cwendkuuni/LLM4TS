package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeneratorBaseTest {

 private JsonGenerator generator;

 @Before
 public void setup() throws IOException {
 generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), new ObjectMapper().getFactory());
 }

 // Testing basic functionality of writeString method
 @Test
 public void testWriteString() throws IOException {
 generator.writeString("Hello World");
 assertEquals("Hello World", generator.getCurrentValue().asText());
 }

 // Testing writeString method with char array
 @Test
 public void testWriteStringWithCharArray() throws IOException {
 char[] text = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
 generator.writeString(text, 0, text.length);
 assertEquals(new String(text), generator.getCurrentValue().asText());
 }

 // Testing writeRawValue method with String
 @Test
 public void testWriteRawValueWithString() throws IOException {
 generator.writeRawValue("{\"name\":\"John\",\"age\":30}");
 JsonNode node = JsonParser.parseRaw(generator.getOutputBuffer().toString());
 assertEquals("{\"name\":\"John\",\"age\":30}", node.toString());
 }

 // Testing writeRawValue method with char array
 @Test
 public void testWriteRawValueWithCharArray() throws IOException {
 char[] json = {'{', '\"', 'n', 'a', 'm', 'e', '\":', '\"', 'J', 'o', 'h', 'n', '\"', ':', '3', '0', '}'};
 generator.writeRawValue(json, 0, json.length);
 JsonNode node = JsonParser.parseRaw(generator.getOutputBuffer().toString());
 assertEquals("{\"name\":\"John\",\"age\":30}", node.toString());
 }

 // Testing writeObject method with String
 @Test
 public void testWriteObjectWithString() throws IOException {
 String jsonString = "{\"name\":\"John\",\"age\":30}";
 generator.writeObject(jsonString);
 JsonNode node = JsonParser.parseRaw(generator.getOutputBuffer().toString());
 assertEquals("{\"name\":\"John\",\"age\":30}", node.toString());
 }

 // Testing writeObject method with TreeNode
 @Test
 public void testWriteObjectWithTreeNode() throws IOException {
 ObjectNode node = JsonParser.parseController(new ByteArrayInputStream("{\"name\":\"John\",\"age\":30}".getBytes())).getObject();
 generator.writeObject(node);
 JsonNode resultNode = JsonParser.parseRaw(generator.getOutputBuffer().toString());
 assertEquals(node, resultNode);
 }

 // Testing writeNull method
 @Test
 public void testWriteNull() throws IOException {
 generator.writeNull();
 assertEquals("null", generator.getCurrentValue().asText());
 }

 // Testing writeBoolean method
 @Test
 public void testWriteBoolean() throws IOException {
 generator.writeBoolean(true);
 assertTrue(generator.getCurrentValue().asBoolean());
 generator.writeBoolean(false);
 assertFalse(generator.getCurrentValue().asBoolean());
 }

 // Testing writeNumber method with int
 @Test
 public void testWriteNumberWithInt() throws IOException {
 generator.writeNumber(123);
 assertEquals("123", generator.getCurrentValue().asText());
 }

 // Testing writeNumber method with long
 @Test
 public void testWriteNumberWithLong() throws IOException {
 generator.writeNumber(1234567890L);
 assertEquals("1234567890", generator.getCurrentValue().asText());
 }

 // Testing writeNumber method with double
 @Test
 public void testWriteNumberWithDouble() throws IOException {
 generator.writeNumber(123.45);
 assertEquals("123.45", generator.getCurrentValue().asText());
 }

 // Testing writeNumber method with BigDecimal
 @Test
 public void testWriteNumberWithBigDecimal() throws IOException {
 BigDecimal number = new BigDecimal("123.45");
 generator.writeNumber(number);
 assertEquals("123.45", generator.getCurrentValue().asText());
 }

 // Testing writeStartArray method
 @Test
 public void testWriteStartArray() throws IOException {
 generator.writeStartArray();
 assertTrue(generator.getCurrentValue().isArray());
 }

 // Testing writeEndArray method
 @Test
 public void testWriteEndArray() throws IOException {
 generator.writeStartArray();
 generator.writeEndArray();
 assertFalse(generator.getCurrentValue().isArray());
 }

 // Testing writeStartObject method
 @Test
 public void testWriteStartObject() throws IOException {
 generator.writeStartObject();
 assertTrue(generator.getCurrentValue().isObject());
 }

 // Testing writeEndObject method
 @Test
 public void testWriteEndObject() throws IOException {
 generator.writeStartObject();
 generator.writeEndObject();
 assertFalse(generator.getCurrentValue().isObject());
 }

 // Testing writeBinary method
 @Test(expected = UnsupportedOperationException.class)
 public void testWriteBinary() throws IOException {
 generator.writeBinary(JsonEncoding.Base64Variant.STANDARD, new ByteArrayInputStream("data".getBytes()), 4);
 }

 // Testing flush method
 @Test
 public void testFlush() throws IOException {
 generator.writeString("Hello");
 generator.flush();
 assertEquals("Hello", generator.getCurrentValue().asText());
 generator.close();
 }

 // Testing close method
 @Test
 public void testClose() throws IOException {
 generator.writeString("Hello");
 generator.close();
 assertFalse(generator.isClosed());
 }

 // Testing isClosed method
 @Test
 public void testIsClosed() throws IOException {
 generator.writeString("Hello");
 generator.close();
 assertTrue(generator.isClosed());
 }
}