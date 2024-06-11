package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

 private static ObjectMapper objectMapper;
 private static AsPropertyTypeDeserializer asPropertyTypeDeserializer;
 private static JavaType javaType;
 private static TypeIdResolver typeIdResolver;
 private static BeanProperty beanProperty;

 static {
 try {
 objectMapper = new ObjectMapper();
 javaType = objectMapper.getTypeFactory().constructType(String.class);
 typeIdResolver = objectMapper.getTypeFactory();
 beanProperty = new BeanProperty.Std("testProperty", javaType);
 asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "testProperty", false, null, As.PROPERTY);
 } catch (Exception e) {
 throw new RuntimeException("Failed to initialize test setup", e);
 }
 }

 @Test
 public void testDeserializeTypedFromObjectWithProperty() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"testProperty\":\"StringValue\",\"typeId\":\"StringTypeId\"}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(beanProperty);
 Object result = deserializer.deserializeTypedFromObject(jsonParser, context);
 assertEquals("StringValue", result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithoutProperty() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"typeId\":\"StringTypeId\"}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 TypeDeserializer deserializer = asPropertyTypeDeserializer;
 Object result = deserializer.deserializeTypedFromObject(jsonParser, context);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObjectWithWrongPropertyName() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"wrongPropertyName\":\"StringValue\",\"typeId\":\"StringTypeId\"}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(beanProperty);
 deserializer.deserializeTypedFromObject(jsonParser, context);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"testProperty\":\"StringValue\",\"@type\":\"StringTypeId\"}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(beanProperty);
 Object result = deserializer.deserializeTypedFromObject(jsonParser, context);
 assertEquals("StringValue", result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"typeId\":null}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
 assertEquals("", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\"StringValue\"]");
 DeserializationContext context = objectMapper.getDeserializationContext();
 TypeDeserializer deserializer = new AsArrayTypeDeserializer(javaType, typeIdResolver, As.WRAPPER_ARRAY);
 Object result = deserializer.deserializeTypedFromArray(jsonParser, context);
 assertEquals("[StringValue]", result.toString());
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\"StringValue\"]");
 DeserializationContext context = objectMapper.getDeserializationContext();
 TypeDeserializer deserializer = asPropertyTypeDeserializer;
 Object result = deserializer.deserializeTypedFromAny(jsonParser, context);
 assertEquals("[StringValue]", result.toString());
 }
}