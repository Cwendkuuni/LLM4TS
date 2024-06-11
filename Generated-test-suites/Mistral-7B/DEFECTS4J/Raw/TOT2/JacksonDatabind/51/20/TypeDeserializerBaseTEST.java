package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

 // Basic test case for the constructor
 @Test
 public void testTypeDeserializerBaseConstructor() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
 new TypeReference<Number>() {}, new TypeIdResolver() {}, "", false, null);
 assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase",
 typeDeserializerBase.getClass().getName());
 }

 // Basic test case for the abstract method forProperty
 @SuppressWarnings("unchecked")
 @Test
 public void testForPropertyMethod() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
 .constructType(TypeDeserializerBase.class);
 BeanProperty property = new BeanProperty(null, "testProperty");
 TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
 assertEquals(TypeDeserializer.class, forProperty.getClass());
 }

 // Test for getTypeInclusion method
 @Test
 public void testgetTypeInclusionMethod() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
 new TypeReference<Number>() {}, new TypeIdResolver() {}, "", false, null);
 assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES, typeDeserializerBase.getTypeInclusion());
 }

 // Test for baseTypeName method
 @Test
 public void testBaseTypeNameMethod() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
 new TypeReference<Number>() {}, new TypeIdResolver() {}, "", false, null);
 assertEquals("java.lang.Number", typeDeserializerBase.baseTypeName());
 }

 // Test for getPropertyName method
 @Test
 public void testgetPropertyNameMethod() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
 new TypeReference<Number>() {}, new TypeIdResolver() {}, "", false, null);
 assertEquals("", typeDeserializerBase.getPropertyName());
 }

 // Test for getTypeIdResolver method
 @Test
 public void testgetTypeIdResolverMethod() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
 new TypeReference<Number>() {}, new TypeIdResolver() {}, "", false, null);
 assertNotNull(typeDeserializerBase.getTypeIdResolver());
 }

 // Test for getDefaultImpl method
 @Test
 public void testgetDefaultImplMethod() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
 new TypeReference<Number>() {}, new TypeIdResolver() {}, "", false, null);
 assertNull(typeDeserializerBase.getDefaultImpl());
 }

 // Test for toString method
 @Test
 public void testToStringMethod() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
 new TypeReference<Number>() {}, new TypeIdResolver() {}, "", false, null);
 assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Number; id-resolver:class com.fasterxml.jackson.databind.TypeIdResolver$Null;]",
 typeDeserializerBase.toString());
 }

 // Test for findDeserializer method
 @Test
 public void testFindDeserializerMethod() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
 .constructType(TypeDeserializerBase.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String typeId = "StringDeserializer";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
 assertNotNull(deserializer);
 assertEquals(StringDeserializer.class, deserializer.getClass());
 }

 // Test for findDefaultImplDeserializer method
 @Test
 public void testFindDefaultImplDeserializerMethod() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
 .constructType(TypeDeserializerBase.class);
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
 assertNotNull(deserializer);
 assertEquals(NullifyingDeserializer.instance.getClass(), deserializer.getClass());
 }

 // Test for deserializeWithNativeTypeId method with null typeId
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
 .constructType(TypeDeserializerBase.class);
 JsonParser jsonParser = mapper.getFactory().createParser("{}}");
 deserializeWithNativeTypeId(typeDeserializerBase, jsonParser, null);
 }

 // Test for deserializeWithNativeTypeId method with valid typeId
 @Test
 public void testDeserializeWithNativeTypeIdValidTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
 .constructType(TypeDeserializerBase.class);
 String typeId = "StringDeserializer";
 JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId\":\"" + typeId + "\"}");
 Object result = deserializeWithNativeTypeId(typeDeserializerBase, jsonParser, jsonParser.getText());
 assertEquals("StringDeserializer", result.getClass().getName());
 }

 // Test for handleUnknownTypeId method
 @Test(expected = JsonMappingException.class)
 public void testHandleUnknownTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
 .constructType(TypeDeserializerBase.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String typeId = "UnknownTypeId";
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(context, typeId, new TypeIdResolver() {}, null);
 assertNull(javaType);
 }

 // Helper method for deserializeWithNativeTypeId
 private void deserializeWithNativeTypeId(TypeDeserializerBase typeDeserializerBase, JsonParser jsonParser,
 String typeId) throws IOException {
 SerializerProvider serializerProvider = jsonParser.getCodec().getSerializerProvider();
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, serializerProvider.getDeserializationContext(),
 typeId);
 }
}