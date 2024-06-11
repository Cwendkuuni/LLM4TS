package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testAsWrapperTypeDeserializer() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 javaTypeOf(String.class), null, "type", false, String.class);

 // test serialization
 ObjectNode wrapperObject = mapper.createObjectNode();
 wrapperObject.put("type", "java.lang.String");
 wrapperObject.put("value", "test string");
 String json = mapper.writerWithTypeValue(As.WRAPPER_OBJECT).writeValueAsString(wrapperObject);

 Object result = mapper.readValue(json, deserializer.getType());
 assertEquals("test string", result);

 // test deserialization with native type id
 ObjectNode wrapperObjectWithTypeId = mapper.createObjectNode();
 wrapperObjectWithTypeId.put("type", "java.lang.String");
 wrapperObjectWithTypeId.put("value", "test string");
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 idResolver.init(mapper.getConfiguration());
 TypeId id = idResolver.resolveTypeId(wrapperObjectWithTypeId, DeserializationContext.emptyContext());
 TypeDeserializer deserializerWithId = deserializer.forProperty(null);
 result = deserializerWithId.deserializeTypedFromAny(mapper.parser(), new DeserializationContext.Contextual(idResolver, null));
 assertEquals("test string", result);
 }

 @Test(expected = IOException.class)
 public void testAsWrapperTypeDeserializerWithInvalidJson() throws Exception {
 String json = "{\"type\":\"java.lang.String\", \"value\":\"test string\"}";
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 javaTypeOf(String.class), null, "type", false, String.class);
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 JsonParserSequence sequence = JsonParserSequence.createFlattened(jsonParser, new TokenBuffer(null, false).nextToken());
 deserializer.deserializeTypedFromAny(sequence, new DeserializationContext.RootContext());
 fail("Expected IOException but did not receive one.");
 }

 @Test(expected = IOException.class)
 public void testAsWrapperTypeDeserializerWithMissingTypeId() throws Exception {
 String json = "{\"value\":\"test string\"}";
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 javaTypeOf(String.class), null, "type", false, String.class);
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 deserializer.deserializeTypedFromAny(jsonParser, new DeserializationContext.RootContext());
 fail("Expected IOException but did not receive one.");
 }

 @Test(expected = IOException.class)
 public void testAsWrapperTypeDeserializerWithMissingValue() throws Exception {
 String json = "{\"type\":\"java.lang.String\"}";
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 javaTypeOf(String.class), null, "type", false, String.class);
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 deserializer.deserializeTypedFromAny(jsonParser, new DeserializationContext.RootContext());
 fail("Expected IOException but did not receive one.");
 }
}