package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 new SimpleType(String.class), null, null, false, null);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("[{\"type\":\"String\",\"value\":\"HelloWorld\"}]");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 assertEquals("HelloWorld", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 new SimpleType(String.class), null, null, false, null);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("[{\"type\":\"String\",\"value\":\"HelloWorld\"}]");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, new TestContext());
 assertEquals("HelloWorld", result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 new SimpleType(String.class), null, null, false, null);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("{\"type\":\"String\",\"value\":\"HelloWorld\"}");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, new TestContext());
 assertEquals("HelloWorld", result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 new SimpleType(String.class), null, null, false, null);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("[{\"type\":\"Integer\",\"value\":42}]");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, new TestContext());
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testThrowsIOExceptionWhenTypeIdNotFound() throws IOException {
 AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 new SimpleType(String.class), null, null, false, null);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("[{\"type\":\"UnknownType\",\"value\":\"HelloWorld\"}]");
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 }

 @Test(expected = IOException.class)
 public void testThrowsIOExceptionWhenInvalidJsonObjectStructure() throws IOException {
 AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 new SimpleType(String.class), null, null, false, null);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("[{\"type\":\"String\",\"value\":\"HelloWorld\"},42]");
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 }

 @Test(expected = IOException.class)
 public void testThrowsIOExceptionWhenMissingEndObject() throws IOException {
 AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 new SimpleType(String.class), null, null, false, null);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("[{\"type\":\"String\",\"value\":\"HelloWorld\"}");
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public BeanDescription getBeanDescription() { return null; }

 @Override
 public Object getContextualData() { return null; }
 }

}