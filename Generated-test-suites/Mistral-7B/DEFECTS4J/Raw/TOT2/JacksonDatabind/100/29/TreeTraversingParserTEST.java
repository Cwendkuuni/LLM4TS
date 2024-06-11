package com.fasterxml.jackson.databind.node;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class TreeTraversingParserTest {

 private JsonParser parser;
 private ObjectReader reader;

 @Before
 public void setUp() {
 JsonFactory factory = new JsonFactory();
 parser = new TreeTraversingParser(factory.createParser("[]"));
 reader = factory.getObjectReader();
 }

 @After
 public void tearDown() {
 if (parser != null) {
 parser.close();
 parser = null;
 }
 }

 @Test
 public void testBasicParsing() throws IOException {
 JsonNode root = parser.readValueAsTree();
 Assert.assertTrue("Root should be an empty array", root.isArray());
 }

 @Test
 public void testParsingEmptyObject() throws IOException {
 parser.nextToken(); // consume [
 parser.nextToken(); // consume {}
 parser.nextToken(); // consume ]
 JsonNode root = parser.readValueAsTree();
 Assert.assertEquals("Root should be an empty object", JsonNode.VALUE_NULL, root);
 }

 @Test
 public void testParsingSimpleObject() throws IOException {
 JsonNode root = reader.readValueAsTree("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
 Assert.assertTrue("Root should be an object", root.isObject());

 String name = root.get("name").asText();
 Assert.assertEquals("Name should be John", "John", name);

 int age = root.get("age").asInt();
 Assert.assertEquals("Age should be 30", 30, age);

 String city = root.get("city").asText();
 Assert.assertEquals("City should be New York", "New York", city);
 }

 @Test(expected = IOException.class)
 public void testParsingMalformedJson() throws IOException {
 parser.nextToken(); // consume [
 parser.nextToken(); // consume {
 parser.nextToken(); // consume "name":
 parser.nextToken(); // consume "John"
 parser.nextToken(); // consume ,
 parser.nextToken(); // consume {
 parser.nextToken(); // consume "age":
 parser.nextToken(); // consume 30
 parser.nextToken(); // consume ,
 parser.nextToken(); // consume "city":
 parser.nextToken(); // consume "New

 // This should throw an IOException
 parser.nextToken();
 }

 @Test
 public void testParsingArray() throws IOException {
 JsonNode root = parser.readValueAsTree();
 Assert.assertTrue("Root should be an array", root.isArray());

 JsonNode first = root.get(0);
 Assert.assertTrue("First element should be a number", first.isNumber());
 int firstValue = first.asInt();
 Assert.assertEquals("First value should be 1", 1, firstValue);

 JsonNode second = root.get(1);
 Assert.assertTrue("Second element should be a string", second.isTextual());
 String secondValue = second.asText();
 Assert.assertEquals("Second value should be two", "two", secondValue);
 }

 @Test(expected = IOException.class)
 public void testParsingMalformedArray() throws IOException {
 parser.nextToken(); // consume [
 parser.nextToken(); // consume 1
 parser.nextToken(); // consume ,
 parser.nextToken(); // consume "two"

 // This should throw an IOException
 parser.nextToken();
 }

 @Test
 public void testParsingNull() throws IOException {
 JsonNode root = parser.readValueAsTree();
 Assert.assertTrue("Root should be an empty array", root.isArray());

 JsonNode first = root.get(0);
 Assert.assertEquals("First value should be null", JsonNode.VALUE_NULL, first);
 }

 @Test
 public void testParsingBinaryData() throws IOException {
 parser.nextToken(); // consume [
 parser.nextToken(); // consume "data":
 parser.nextToken(); // consume "base64"
 parser.nextToken(); // consume "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsCiAgICAtLSBQYXVsCm91bmQh"
 parser.nextToken(); // consume ]
 JsonNode root = parser.readValueAsTree();
 byte[] data = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
 Assert.assertNotNull("Data should not be null", data);
 }
}