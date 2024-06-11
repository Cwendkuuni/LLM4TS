package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Test cases for NullifyingDeserializer class.
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeBasic() throws IOException {
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserialize(mapper.createParser("{}"), new DeserializationContext(null, null, null));
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeBasic() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[]");
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructType(int[].class);
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(null, null, null), typeDeserializer);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidToken() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("5");
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructType(int[].class);
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 serializer.deserializeWithType(jsonParser, new DeserializationContext(null, null, null), typeDeserializer);
 fail("Should have thrown IOException");
 }

 @Test
 public void testDeserializeWithStartArrayToken() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[1,2,3]");
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructType(int[].class);
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(null, null, null), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithStartObjectToken() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"name\":\"John\",\"age\":30}");
 ObjectMapper objectMapper = new ObjectMapper();
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(null, null, null), null);
 assertNull(result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithFieldNameToken() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"name\":\"John\"}");
 ObjectMapper objectMapper = new ObjectMapper();
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(null, null, null), null);
 fail("Should have thrown JsonProcessingException");
 }
}