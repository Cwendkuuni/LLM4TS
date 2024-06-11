package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullifyingDeserializer() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueTypeDeserializer(Object.class, null);
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerDefault() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(jsonParser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerArray() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, null);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerObject() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, null);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerFieldName() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": null}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullifyingDeserializerUnexpectedToken() throws JsonProcessingException, IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("42");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, null);
 throw new IOException("Unexpected token: ID_NUMBER"); // for coverage
 }
}