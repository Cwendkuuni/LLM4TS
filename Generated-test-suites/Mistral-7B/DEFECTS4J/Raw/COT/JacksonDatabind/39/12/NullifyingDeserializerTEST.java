package com.example.nullifyingdeserializertest;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.type.TypeDeserializer;

import org.junit.Test;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = JsonMapperFactory.getJsonMapper();
 }

 // Testing deserialize method
 @Test
 public void testDeserialize() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(jsonParser, deserializationContext);
 assertNull(result);
 }

 // Testing deserializeWithType method for JsonTokenId.ID_START_ARRAY
 @Test
 public void testDeserializeWithType_ID_START_ARRAY() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{}]");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(Object[].class);
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 assertNull(result);
 }

 // Testing deserializeWithType method for JsonTokenId.ID_START_OBJECT
 @Test
 public void testDeserializeWithType_ID_START_OBJECT() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(Object.class);
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 assertNull(result);
 }

 // Testing deserializeWithType method for JsonTokenId.ID_FIELD_NAME
 @Test
 public void testDeserializeWithType_ID_FIELD_NAME() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": null}");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(Object.class);
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 assertNull(result);
 }

 // Testing deserializeWithType method for unexpected JsonTokenId
 @Test(expected = IOException.class)
 public void testDeserializeWithType_UnexpectedJsonTokenId() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"Hello\"");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(Object.class);
 new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 }
}