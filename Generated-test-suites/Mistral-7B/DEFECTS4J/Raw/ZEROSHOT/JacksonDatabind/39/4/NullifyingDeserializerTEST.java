package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

/**
 * Test class for NullifyingDeserializer
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNullifyingDeserializer() throws JsonProcessingException {
 JsonParser jsonParser = mapper.getFactory().createParser("{}");
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserialize(jsonParser, new DeserializationContext(mapper.getFactory(), null, null));
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer() throws JsonProcessingException {
 JsonParser jsonParser = mapper.getFactory().createParser("[]");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(mapper.getFactory(), null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithArrayStartTokenNullifyingDeserializer() throws JsonProcessingException {
 JsonParser jsonParser = mapper.getFactory().createParser("[{}]");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(mapper.getFactory(), null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithObjectStartTokenNullifyingDeserializer() throws JsonProcessingException {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\":\"John\"}");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(mapper.getFactory(), null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithFieldNameTokenNullifyingDeserializer() throws JsonProcessingException {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\":null}");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(mapper.getFactory(), null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidTokenNullifyingDeserializer() throws JsonProcessingException {
 JsonParser jsonParser = mapper.getFactory().createParser("true");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 NullifyingDeserializer serializer = NullifyingDeserializer.instance;
 Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(mapper.getFactory(), null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }
}