package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static com.fasterxml.jackson.core.JsonParser.EventType.*;

public class AsPropertyTypeDeserializerTest {

 private AsPropertyTypeDeserializer deserializer;
 private ObjectMapper mapper;

 @Before
 public void setup() throws IOException {
 mapper = new ObjectMapper();
 deserializer = new AsPropertyTypeDeserializer(
 MapTypeFactory.construct(Map.class, String.class, String.class),
 mapper.getTypeIdResolver(), "type", true, String.class);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "MyType");
 node.put("myProperty", "value");
 JsonParser parser = mapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 Assert.assertEquals("value", result);
 }

 @Test
 public void testDeserializeWithFieldNameButNoMatchingProperty() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"anotherField\": 123}");
 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME "anotherField"
 parser.nextToken(); // VALUE_NUMBER_INT
 deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 // Test that deserialization did not fail and did not return any value
 // (since there is no matching property)
 parser.nextToken(); // END_OBJECT
 parser.close();
 }

 @Test
 public void testDeserializeWithFieldNameAndMatchingProperty() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "MyType");
 node.put("myProperty", "value");
 JsonParser parser = mapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 Assert.assertEquals("value", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithUnexpectedToken() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "MyType");
 node.put("myProperty", 123); // INT instead of STRING
 JsonParser parser = mapper.treeAsTokens(node);
 deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 }
}