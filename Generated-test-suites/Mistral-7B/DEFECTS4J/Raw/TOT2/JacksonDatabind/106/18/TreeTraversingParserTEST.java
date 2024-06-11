package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private JsonParser parser;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 parser = mapper.treeAsTokensParser("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\"]}");
 }

 @Test
 public void testParseNormal() throws Exception {
 JsonToken token = parser.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("name", token.asString());

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.VALUE_STRING, token);
 Assert.assertEquals("John", token.asString());

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("age", token.asString());

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(30, token.asInt());

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("cities", token.asString());

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.START_ARRAY, token);

 JsonToken cityToken = parser.nextToken();
 Assert.assertEquals(JsonToken.VALUE_STRING, cityToken);
 Assert.assertEquals("New York", cityToken.asString());

 parser.skipChildren(); // move past the rest of the array

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.END_OBJECT, token);
 }

 @Test(expected = IOException.class)
 public void testParseMissingRoot() throws Exception {
 JsonParser parser = mapper.treeAsTokensParser("{}");
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME

 // this should throw an IOException
 parser.nextToken();
 }

 @Test(expected = IOException.class)
 public void testParseInvalidToken() throws Exception {
 JsonParser parser = mapper.treeAsTokensParser("{\"name\":30}");
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME

 // this should throw an IOException
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testParseNullValue() throws Exception {
 JsonParser parser = mapper.treeAsTokensParser("{\"name\":null}");
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME
 parser.nextToken(); // consume VALUE_NULL
 parser.nextToken(); // consume END_OBJECT
 }

 @Test(expected = JsonParseException.class)
 public void testParseBooleanValue() throws Exception {
 JsonParser parser = mapper.treeAsTokensParser("{\"name\":true}");
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME
 parser.nextToken(); // consume VALUE_BOOLEAN
 parser.nextToken(); // consume END_OBJECT
 }

 @Test
 public void testParseArray() throws Exception {
 JsonToken token = parser.nextToken();
 Assert.assertEquals(JsonToken.START_ARRAY, token);

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(1, token.asInt());

 parser.skipChildren(); // move past the rest of the array

 token = parser.nextToken();
 Assert.assertEquals(JsonToken.END_ARRAY, token);
 }

 @Test(expected = IOException.class)
 public void testParseEmptyArray() throws Exception {
 JsonParser parser = mapper.treeAsTokensParser("[]");
 parser.nextToken(); // consume START_ARRAY
 parser.nextToken(); // consume END_ARRAY

 // this should throw an IOException
 parser.nextToken();
 }

 @Test
 public void testParseNullArray() throws Exception {
 JsonParser parser = mapper.treeAsTokensParser("null");
 parser.nextToken(); // consume START_NULL
 parser.nextToken(); // consume END_ARRAY
 }
}