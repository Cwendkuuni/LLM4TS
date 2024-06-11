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
 JsonParser p = mapper.getFactory().createParser("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(p, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializer();
 Object result = new NullifyingDeserializer().deserializeWithType(p, ctxt, typeDeserializer);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidTokenId() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{ \"invalidTokenId\": 42 }");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializer();
 new NullifyingDeserializer().deserializeWithType(p, ctxt, typeDeserializer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithStartArrayToken() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[\"value\"]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializer();
 new NullifyingDeserializer().deserializeWithType(p, ctxt, typeDeserializer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithStartObjectToken() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"key\":\"value\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializer();
 new NullifyingDeserializer().deserializeWithType(p, ctxt, typeDeserializer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithFieldNameToken() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"key\":null}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializer();
 new NullifyingDeserializer().deserializeWithType(p, ctxt, typeDeserializer);
 }

 @Test
 public void testDeserializeWithValidTokenIds() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializer();
 Object result = new NullifyingDeserializer().deserializeWithType(p, ctxt, typeDeserializer);
 assertNull(result);

 p = mapper.getFactory().createParser("{\"name\":\"value\"}");
 result = new NullifyingDeserializer().deserializeWithType(p, ctxt, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithEmptyJsonInput() throws IOException {
 JsonParser p = mapper.getFactory().createParser("");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializer();
 Object result = new NullifyingDeserializer().deserializeWithType(p, ctxt, typeDeserializer);
 assertNull(result);
 }

}