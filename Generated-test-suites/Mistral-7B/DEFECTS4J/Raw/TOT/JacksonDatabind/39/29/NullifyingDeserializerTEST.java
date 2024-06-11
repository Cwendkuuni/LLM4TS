package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithNullInput() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("null");
 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext(null));
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithArrayInput() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext(null));
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithObjectInput() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"name\": \"John\", \"age\": 30}");
 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext(null));
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithStartArrayInput() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[]");
 TypeDeserializer typeDeserializer = objectMapper.getFactory().getPolymorphicTypeDeserializer();
 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithStartObjectInput() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{}");
 TypeDeserializer typeDeserializer = objectMapper.getFactory().getPolymorphicTypeDeserializer();
 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithFieldNameInput() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser(new SerializedString("[\"name\", null]"));
 TypeDeserializer typeDeserializer = objectMapper.getFactory().getPolymorphicTypeDeserializer();
 NullifyingDeserializer deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(typeDeserializer), typeDeserializer);
 assertNull(result);
 }
}