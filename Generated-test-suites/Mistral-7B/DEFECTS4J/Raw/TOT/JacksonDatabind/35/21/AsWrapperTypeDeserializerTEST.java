package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

 private static class TestBean {
 @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.WRAPPER_OBJECT)
 public String testProperty;
 }

 @Test
 public void testDeserializeObject() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(TestBean.class, As.WRAPPER_OBJECT, null, null, null);
 String json = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestBean\", \"testProperty\":\"testValue\"}";
 TestBean bean = mapper.readValue(json, TestBean.class);
 assertEquals("testValue", bean.testProperty);

 // Test error cases
 JsonParser parser = mapper.getFactory().createParser(json);
 deserializer = mapper.getTypeDeserializerInstance(TestBean.class, As.WRAPPER_OBJECT, null, null, null);
 Object wrongType = deserializer.deserializeTypedFromAny(parser, context);
 assertEquals(null, wrongType);
 parser.nextToken(); // Consume "@type" token
 Object missingTypeId = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(null, missingTypeId);
 }

 @Test
 public void testDeserializeArray() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(TestBean[].class, As.WRAPPER_OBJECT, null, null, null);
 String json = "[{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestBean\", \"testProperty\":\"testValue1\"}, {\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestBean\", \"testProperty\":\"testValue2\"}]";
 TestBean[] beans = mapper.readValue(json, TestBean[].class);
 assertEquals(2, beans.length);
 TestBean firstBean = beans[0];
 assertEquals("testValue1", firstBean.testProperty);
 TestBean secondBean = beans[1];
 assertEquals("testValue2", secondBean.testProperty);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissingTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[{\"testProperty\":\"testValue\"}]";
 TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(TestBean[].class, As.WRAPPER_OBJECT, null, null, null);
 deserializer.deserializeTypedFromArray(parser, context);
 }

 @Test
 public void testDeserializeScalar() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "\"testValue\"";
 TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(String.class, As.WRAPPER_OBJECT, null, null, null);
 String result = (String) deserializer.deserializeTypedFromScalar(parser, context);
 assertEquals("testValue", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissingField() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestBean\"}"; // Missing "testProperty" field
 TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(TestBean.class, As.WRAPPER_OBJECT, null, null, null);
 deserializer.deserializeTypedFromObject(parser, context);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissingEndObject() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestBean\", \"testProperty\":\"testValue\"}";
 TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(TestBean.class, As.WRAPPER_OBJECT, null, null, null);
 deserializer.deserializeTypedFromObject(parser, context); // Consume "@type" and "testProperty"
 deserializer.deserializeTypedFromObject(parser, context); // Consume deserialized value
 // Missing "}"
 }
}