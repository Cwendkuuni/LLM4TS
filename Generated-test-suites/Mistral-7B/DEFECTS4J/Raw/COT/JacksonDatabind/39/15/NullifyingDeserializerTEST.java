package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Test cases for NullifyingDeserializer class
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserialize() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{}"); // empty JSON
 Object result = new NullifyingDeserializer().deserialize(p, new DeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[]"); // empty JSON array
 Object result = new NullifyingDeserializer().deserializeWithType(p, new DeserializationContext(), null);
 assertNull(result);

 JsonParser pObject = mapper.getFactory().createParser("{\"name\": \"John\"}"); // JSON object
 Object resultType = new NullifyingDeserializer().deserializeWithType(pObject, new DeserializationContext(), null);
 assertNull(resultType);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidToken() throws IOException {
 JsonParser pInvalidToken = mapper.getFactory().createParser("123"); // invalid JSON integer
 new NullifyingDeserializer().deserializeWithType(pInvalidToken, new DeserializationContext(), null);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithTypeDeserializerNull() throws IOException {
 JsonParser pTypeDeserializerNull = mapper.getFactory().createParser("{'type': null}"); // JSON with null typeDeserializer
 new NullifyingDeserializer().deserializeWithType(pTypeDeserializerNull, new DeserializationContext(), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithUnknownToken() throws IOException {
 JsonParser pUnknownToken = mapper.getFactory().createParser("X:123"); // unknown token X
 new NullifyingDeserializer().deserializeWithType(pUnknownToken, new DeserializationContext(), null);
 }

 @Test
 public void testDeserializeWithStartArrayToken() throws IOException {
 JsonParser pStartArray = mapper.getFactory().createParser("[1, 2, 3]"); // JSON array
 Object resultType = new NullifyingDeserializer().deserializeWithType(pStartArray, new DeserializationContext(), null);
 assertNull(resultType);
 }

 @Test
 public void testDeserializeWithStartObjectToken() throws IOException {
 JsonParser pStartObject = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}"); // JSON object
 Object resultType = new NullifyingDeserializer().deserializeWithType(pStartObject, new DeserializationContext(), null);
 assertNull(resultType);
 }

 @Test
 public void testDeserializeWithFieldNameToken() throws IOException {
 JsonParser pFieldName = mapper.getFactory().createParser("{\"name\": null}"); // JSON with null field name
 Object resultType = new NullifyingDeserializer().deserializeWithType(pFieldName, new DeserializationContext(), null);
 assertNull(resultType);
 }
}