package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

public class AsPropertyTypeDeserializerTest extends TestBase {

 private static final String CLASS_NAME = AsPropertyTypeDeserializer.class.getName();

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject_WithPropertyName() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 JsonTypeFactory.instance.constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 true,
 JsonTypeFactory.instance.constructType(Integer.class),
 As.PROPERTY);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerProvider(getStandardSerializerProvider());
 ObjectReader reader = mapper.readerForType(Object.class);
 String json = "{\"typePropertyName\":\"StringType\",\"name\":\"John Doe\"}";
 Object result = reader.readValue(new StringReader(json), deserializer.getType());
 assertEquals("John Doe", result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject_WithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 JsonTypeFactory.instance.constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 true,
 JsonTypeFactory.instance.constructType(Integer.class),
 As.PROPERTY);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerProvider(getStandardSerializerProvider());
 ObjectReader reader = mapper.readerForType(Object.class);
 String json = "{\"@type\":\"StringType\",\"name\":\"John Doe\"}";
 Object result = reader.readValue(new StringReader(json), deserializer.getType());
 assertEquals("John Doe", result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject_WithMissingTypeProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 JsonTypeFactory.instance.constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 true,
 JsonTypeFactory.instance.constructType(Integer.class),
 As.PROPERTY);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerProvider(getStandardSerializerProvider());
 ObjectReader reader = mapper.readerForType(Object.class);
 String json = "{\"name\":\"John Doe\"}";
 try {
 reader.readValue(new StringReader(json), deserializer.getType());
 fail("Expected JacksonDataBindException to be thrown");
 } catch (JsonProcessingException e) {
 assertEquals("missing property 'typePropertyName' that is to contain type id",
 e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject_WithDefaultImpl() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 null,
 new TypeIdResolverBase(),
 "typePropertyName",
 true,
 JsonTypeFactory.instance.constructType(Integer.class),
 As.PROPERTY);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerProvider(getStandardSerializerProvider());
 ObjectReader reader = mapper.readerForType(Object.class);
 String json = "{\"name\":\"10\"}";
 Object result = reader.readValue(new StringReader(json), deserializer.getType());
 assertEquals(10, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject_WithArrayWrapper() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 JsonTypeFactory.instance.constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 true,
 JsonTypeFactory.instance.constructType(Integer.class),
 As.PROPERTY);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerProvider(getStandardSerializerProvider());
 ObjectReader reader = mapper.readerForType(Object.class);String json = "[\"StringType\",\"Hello World\"]"; Object result = reader.readValue(new StringReader(json), deserializer.getType()); assertEquals("Hello World", result); } }