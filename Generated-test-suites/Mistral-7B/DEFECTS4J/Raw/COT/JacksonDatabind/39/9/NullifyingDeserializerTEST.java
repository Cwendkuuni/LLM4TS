package com.example.nullifyingdeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

 private static final Object NULL = null;

 // Testing deserialize method
 @Test
 public void testDeserialize() throws JsonProcessingException {
 JsonParser parser = createParser();
 Object result = new NullifyingDeserializer().deserialize(parser, new TestContext());
 assertNull(result);
 }

 // Testing deserializeWithType method when JsonParser is not pointing to an array or object
 @Test
 public void testDeserializeWithType_NotArrayOrObject() throws JsonProcessingException {
 JsonParser parser = createParser(JsonToken.VALUE_NUMBER_INT.getNumberId());
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestContext(), new NoOpTypeDeserializer());
 assertNull(result);
 }

 // Testing deserializeWithType method when JsonParser is pointing to an array
 @Test
 public void testDeserializeWithType_Array() throws JsonProcessingException {
 JsonParser parser = createParser(JsonToken.START_ARRAY);
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestContext(), new NoOpTypeDeserializer());
 assertNull(result);
 }

 // Testing deserializeWithType method when JsonParser is pointing to an object
 @Test
 public void testDeserializeWithType_Object() throws JsonProcessingException {
 JsonParser parser = createParser(JsonToken.START_OBJECT);
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestContext(), new NoOpTypeDeserializer());
 assertNull(result);
 }

 // Testing deserializeWithType method when JsonParser is pointing to a field name
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithType_FieldName() throws JsonProcessingException {
 JsonParser parser = createParser(JsonToken.FIELD_NAME);
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestContext(), new NoOpTypeDeserializer());
 fail("Field name should not be processed by NullifyingDeserializer");
 }

 private JsonParser createParser() throws JsonProcessingException {
 return createParser(JsonToken.START_OBJECT);
 }

 private JsonParser createParser(int tokenId) throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser parser = jsonFactory.createParser(new StringReader("{}"));
 parser.nextToken(); // Consume START_OBJECT token
 parser.setCurrentToken(JsonToken.getCurrentToken()); // Set current token to specified one
 parser.nextToken(); // Consume the next token
 return parser;
 }

 class TestContext extends DeserializationContext {}

 class NoOpTypeDeserializer extends TypeDeserializer {
 @Override
 public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
 return NULL;
 }
 }

}