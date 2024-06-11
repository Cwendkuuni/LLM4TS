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
 public void testDeserialize() throws JsonProcessingException {
 Object result = new NullifyingDeserializer().deserialize(mapper.getFactory().createParser("{}"), new DeserializationContext(mapper));
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType() throws JsonProcessingException {
 Object result = new NullifyingDeserializer().deserializeWithType(mapper.getFactory().createParser("[]"), new DeserializationContext(mapper), null);
 assertNull(result);

 Object result1 = new NullifyingDeserializer().deserializeWithType(mapper.getFactory().createParser("{\"name\": \"John\"}"), new DeserializationContext(mapper), null);
 assertNull(result1);

 Object result2 = new NullifyingDeserializer().deserializeWithType(mapper.getFactory().createParser("null"), new DeserializationContext(mapper), null);
 assertNull(result2);

 Object result3 = new NullifyingDeserializer().deserializeWithType(mapper.getFactory().createParser("[]"), new DeserializationContext(mapper), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
 assertNull(result3);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidToken() throws JsonProcessingException {
 Object result = new NullifyingDeserializer().deserializeWithType(mapper.getFactory().createParser("123"), new DeserializationContext(mapper), null);
 fail("Expected IOException but got null");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithTypeMismatch() throws JsonProcessingException {
 Object result = new NullifyingDeserializer().deserializeWithType(mapper.getFactory().createParser("{\"name\": 123}"), new DeserializationContext(mapper), null);
 fail("Expected JsonMappingException but got null");
 }

}