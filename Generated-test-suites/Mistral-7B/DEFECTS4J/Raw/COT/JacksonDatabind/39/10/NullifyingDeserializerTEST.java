package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NullifyingDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserialize() throws IOException {
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.putPOJOInRoot("testObject", new TestObject());
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(MAPPER.getFactory().createParser(jsonNode.toString()), MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidJson() throws IOException {
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.putPOJOInRoot("testObject", new TestObject());
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 deserializer.deserialize(MAPPER.getFactory().createParser("{invalidJson}"), MAPPER.getDeserializationContext());
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.putPOJOInRoot("testObject", new TestObject());
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializers().findValueDeserializer(TestObject.class, null);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(MAPPER.getFactory().createParser(jsonNode.toString()), MAPPER.getDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithTypeInvalidJson() throws IOException {
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.putPOJOInRoot("testObject", new TestObject());
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializers().findValueDeserializer(TestObject.class, null);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 deserializer.deserializeWithType(MAPPER.getFactory().createParser("{invalidJson}"), MAPPER.getDeserializationContext(), typeDeserializer);
 }

 @Test
 public void testDeserializeWithArrayStartToken() throws IOException {
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[{}]");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithObjectStartToken() throws IOException {
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"testObject\": {}}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithFieldNameToken() throws IOException {
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"testField\": null}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
 assertNull(result);
 }
}