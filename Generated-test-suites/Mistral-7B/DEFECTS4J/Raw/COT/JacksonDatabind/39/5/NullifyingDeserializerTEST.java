package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerTestBase;

import org.junit.Test;

public class NullifyingDeserializerTest extends TypeDeserializerTestBase {

 private static final NullifyingDeserializer NULLIFYING_DESERIALIZER = NullifyingDeserializer.instance;

 // Test for deserialize method
 @Test
 public void testDeserialize() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{ \"name\": \"John Doe\" }");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertNull(NULLIFYING_DESERIALIZER.deserialize(p, ctxt));
 }

 // Test for deserializeWithType method when JsonTokenId.ID_START_ARRAY is encountered
 @Test
 public void testDeserializeWithType_ID_START_ARRAY() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("[]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertNull(NULLIFYING_DESERIALIZER.deserializeWithType(p, ctxt, nullTypeDeserializer));
 }

 // Test for deserializeWithType method when JsonTokenId.ID_START_OBJECT is encountered
 @Test
 public void testDeserializeWithType_ID_START_OBJECT() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{\"name\": \"John Doe\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertNull(NULLIFYING_DESERIALIZER.deserializeWithType(p, ctxt, nullTypeDeserializer));
 }

 // Test for deserializeWithType method when JsonTokenId.ID_FIELD_NAME is encountered
 @Test
 public void testDeserializeWithType_ID_FIELD_NAME() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{\"name\": null}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertNull(NULLIFYING_DESERIALIZER.deserializeWithType(p, ctxt, nullTypeDeserializer));
 }

 // Test for deserializeWithType method when other JsonTokenId is encountered
 @Test (expected = IOException.class)
 public void testDeserializeWithType_Other_JsonTokenId() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("\"Hello World\"");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 NULLIFYING_DESERIALIZER.deserializeWithType(p, ctxt, nullTypeDeserializer);
 }
}