package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;

public class NullifyingDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserialize() throws Exception {
 JsonParser jsonParser = MAPPER.getFactory().createParser("{}");
 DeserializationContext context = new DeserializationContext(MAPPER.getConfig(), null, null);
 assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, context));
 }

 @Test
 public void testDeserializeWithType() throws Exception {
 JsonParser jsonParser = MAPPER.getFactory().createParser("[]");
 DeserializationContext context = new DeserializationContext(MAPper.getConfig(), null, null);
 TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructType(int[].class);
 assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, typeDeserializer));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidToken() throws Exception {
 JsonParser jsonParser = MAPPER.getFactory().createParser("123");
 DeserializationContext context = new DeserializationContext(MAPper.getConfig(), null, null);
 TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructType(int[].class);
 NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, typeDeserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithObjectToken() throws Exception {
 JsonParser jsonParser = MAPPER.getFactory().createParser("{\"name\": \"John\"}");
 DeserializationContext context = new DeserializationContext(MAPper.getConfig(), null, null);
 TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructType(Person.class);
 NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, typeDeserializer);
 }

 @Test
 public void testDeserializeWithArrayToken() throws Exception {
 JsonParser jsonParser = MAPPER.getFactory().createParser("[1,2,3]");
 DeserializationContext context = new DeserializationContext(MAPper.getConfig(), null, null);
 TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructType(int[].class);
 assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, typeDeserializer));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithFieldNameToken() throws Exception {
 JsonParser jsonParser = MAPPER.getFactory().createParser("{\"name\": null}");
 DeserializationContext context = new DeserializationContext(MAPper.getConfig(), null, null);
 TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructType(Person.class);
 NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, typeDeserializer);
 }

}