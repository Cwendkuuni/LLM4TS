package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Test cases for theNullifyingDeserializer class
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testNullifyingDeserializer_deserialize() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig(), null, null));
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_deserializeWithType_InvalidTokenId() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"invalidTokenId\": []}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(mapper.getConfig(), null, null), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_deserializeWithType_StartArray() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(mapper.getConfig(), null, null), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_deserializeWithType_StartObject() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"key\": \"value\"}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(mapper.getConfig(), null, null), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_deserializeWithType_FieldName() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"key\": null}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(mapper.getConfig(), null, null), null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testNullifyingDeserializer_deserializeWithType_InvalidTypeDeserializer() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[]");
 TypeDeserializer typeDeserializer = null;
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(mapper.getConfig(), null, null), typeDeserializer);
 fail("Expected IOException but got " + result);
 }

 @Test
 public void testNullifyingDeserializer_deserializeWithType_ValidTypeDeserializer() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(int[].class);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(mapper.getConfig(), null, null), typeDeserializer);
 assertNull(result);
 }
}