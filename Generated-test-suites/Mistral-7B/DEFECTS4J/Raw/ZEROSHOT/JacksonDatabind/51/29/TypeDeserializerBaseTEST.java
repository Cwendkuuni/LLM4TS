package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 this.objectMapper = mapper;
 typeDeserializerBase = new BaseTypeDeserializer(
 JsonTypeFactory.defaultInstance,
 new StdTypeIdResolver(mapper),
 "",
 false,
 mapper.getTypeFactory().constructType(Map.class));
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty beanProperty = new BeanProperty("testProperty", String.class);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
 assertNotNull(typeDeserializer);
 assertEquals(String.class, typeDeserializer.getType());
 }

 @Test
 public void testGetStringTypeInclusion() throws Exception {
 TypeDeserializer typeDeserializer = typeDeserializerBase;
 assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeDeserializer.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 String expected = "class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase";
 String result = typeDeserializerBase.baseTypeName();
 assertEquals(expected, result);
 }

 @Test
 public void testGetPropertyName() throws Exception {
 String expected = "";
 String result = typeDeserializerBase.getPropertyName();
 assertEquals(expected, result);
 }

 @Test
 public void testGetTypeIdResolver() throws Exception {
 TypeIdResolver expected = new StdTypeIdResolver(objectMapper);
 TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
 assertEquals(expected, result);
 }

 @Test
 public void testGetDefaultImpl() throws Exception {
 Class<?> expected = Map.class;
 Class<?> result = typeDeserializerBase.getDefaultImpl();
 assertEquals(expected, result);
 }

 @Test
 public void testToString() throws Exception {
 String expected = "[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver:com.fasterxml.jackson.databind.impl.StdTypeIdResolver@78f670";
 String result = typeDeserializerBase.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testFindDeserializer() throws Exception {
 String typeId = "TestStringDeserializer";
 JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
 assertNull(result);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testHandleUnknownTypeId() throws Exception {
 typeDeserializerBase = new BaseTypeDeserializer(
 JsonTypeFactory.defaultInstance,
 new StdTypeIdResolver(objectMapper),
 "",
 false,
 mapper.getTypeFactory().constructType(Number.class));
 String typeId = "UnknownTypeId";
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), typeId, new StdTypeIdResolver(objectMapper), mapper.getTypeFactory().constructType(Number.class));
 fail("HandleUnknownTypeId should throw JsonMappingException");
 }
}

class BaseTypeDeserializer extends TypeDeserializerBase {

 public BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver,
 String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
 super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }

 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return null;
 }

 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.WRAP_ONLY;
 }
}