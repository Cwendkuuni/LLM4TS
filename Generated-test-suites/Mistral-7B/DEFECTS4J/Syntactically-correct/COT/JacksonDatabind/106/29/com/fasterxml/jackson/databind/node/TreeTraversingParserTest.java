package com.example.test;import static org.junit.Assert.*;import com.fasterxml.jackson.databind.JsonNode;import com.fasterxml.jackson.databind.ObjectMapper;import com.fasterxml.jackson.databind.node.TreeTraversingParser;import org.junit.Test;

public class TreeTraversingParserTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testTreeTraversingParserConstructorWithJsonNode() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{'name': 'John', 'age': 30}"));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("name", parser.getCurrentName());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("John", parser.getText());

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("age", parser.getCurrentName());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(30, parser.getIntValue());

 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);
 parser.close();
 }

 @Test
 public void testTreeTraversingParserConstructorWithJsonNodeAndObjectCodec() throws Exception {
 ObjectCodec objectCodec = mapper.getFactory().getCodec();
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{'name': 'John', 'age': 30}"), objectCodec);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("name", parser.getCurrentName());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("John", parser.getText());

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("age", parser.getCurrentName());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(30, parser.getIntValue());

 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);
 parser.close();
 }

 @Test(expected = IOException.class)
 public void testTreeTraversingParserNextTokenAfterClose() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{'name': 'John', 'age': 30}"));
 parser.close();
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testTreeTraversingParserNextTokenBeforeOpen() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(null);
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testTreeTraversingParserNextTokenAfterEndOfInput() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{'name': 'John'}"));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("name", parser.getCurrentName());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("John", parser.getText());

 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);

 // End of input
 token = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);
 parser.close();

 // Should throw exception
 parser.nextToken();
 }

}