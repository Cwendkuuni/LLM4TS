package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;

import org.junit.Test;

/**
 * Test class for {@link NullifyingDeserializer}
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper(new JsonFactoryBuilder()
         .withoutFeatures(JsonParser.Feature.AUTO_CLOSE_SOURCE)
         .build());

 @Test
 public void testDeserializeWithNullifyingDeserializer() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

 Object result = nullifyingDeserializer.deserialize(jsonParser, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerNullifyingDeserializer() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
 TypeDeserializer typeDeserializer = null; // not used in this test
 DeserializationContext context = objectMapper.getDeserializationContext();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

 Object result = nullifyingDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerNullifyingDeserializerFieldName() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": null}");
 TypeDeserializer typeDeserializer = null; // not used in this test
 DeserializationContext context = objectMapper.getDeserializationContext();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

 Object result = nullifyingDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerNullifyingDeserializerStartArray() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[null]");
 TypeDeserializer typeDeserializer = null; // not used in this test
 DeserializationContext context = objectMapper.getDeserializationContext();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

 Object result = nullifyingDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerNullifyingDeserializerStartObject() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": null}");
 TypeDeserializer typeDeserializer = null; // not used in this test
 DeserializationContext context = objectMapper.getDeserializationContext();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

 Object result = nullifyingDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 assertNull(result);
 }
}