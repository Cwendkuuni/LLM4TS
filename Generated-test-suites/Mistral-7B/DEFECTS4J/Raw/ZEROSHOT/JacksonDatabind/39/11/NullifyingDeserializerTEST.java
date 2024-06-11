package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerTestBase;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest extends TypeDeserializerTestBase {

 private static final NullifyingDeserializer NULLIFYING_DESERIALIZER = NullifyingDeserializer.instance;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule()
 .addDeserializer(Object.class, NULLIFYING_DESERIALIZER));
 jsonParser = mapper.getFactory().createParser(getResourceAsStream("/your-json-file.json"));
 }

 @Test
 public void testDeserializeNullifyingDeserializer() throws IOException, JsonProcessingException {
 JsonToken token = jsonParser.getCurrentToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());
 assertNull(NULLIFYING_DESERIALIZER.deserialize(jsonParser, context));
 token = jsonParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertNull(NULLIFYING_DESERIALIZER.deserialize(jsonParser, context));
 token = jsonParser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());
 assertNull(NULLIFYING_DESERIALIZER.deserialize(jsonParser, context));
 token = jsonParser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token.getId());
 assertNull(NULLIFYING_DESERIALIZER.deserialize(jsonParser, context));
 token = jsonParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId());
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer() throws IOException, JsonProcessingException {
 JsonToken token = jsonParser.getCurrentToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());
 assertNull(NULLIFYING_DESERIALIZER.deserializeWithType(jsonParser, context, nullTypeDeserializer));
 token = jsonParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertNull(NULLIFYING_DESERIALIZER.deserializeWithType(jsonParser, context, nullTypeDeserializer));
 token = jsonParser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());
 assertNull(NULLIFYING_DESERIALIZER.deserializeWithType(jsonParser, context, nullTypeDeserializer));
 token = jsonParser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token.getId());
 assertNull(NULLIFYING_DESERIALIZER.deserializeWithType(jsonParser, context, nullTypeDeserializer));
 token = jsonParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId());
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializerWithTypeDeserializer() throws IOException, JsonProcessingException {
 JsonToken token = jsonParser.getCurrentToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());
 Object deserialized = NULLIFYING_DESERIALIZER.deserializeWithType(jsonParser, context, integerTypeDeserializer);
 assertNull(deserialized);
 token = jsonParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 deserialized = NULLIFYING_DESERIALIZER.deserializeWithType(jsonParser, context, integerTypeDeserializer);
 assertNull(deserialized);
 token = jsonParser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());
 deserialized = NULLIFYING_DESERIALIZER.deserializeWithType(jsonParser, context, jsonTypeDeserializer);
 assertNull(deserialized);
 token = jsonParser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token.getId());
 deserialized = NULLIFYING_DESERIALIZER.deserializeWithType(jsonParser, context, jsonTypeDeserializer);
 assertNull(deserialized);
 token = jsonParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId());
 }
}