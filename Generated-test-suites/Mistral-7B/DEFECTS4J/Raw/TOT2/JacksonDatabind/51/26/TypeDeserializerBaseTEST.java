package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 typeDeserializerBase = new TypeDeserializerBase(
 ObjectType.getDefaultInstance(), typeIdResolver, null, false, null);
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty beanProperty = new BeanProperty("testProperty", String.class);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
 assertEquals("TypeDeserializer for property 'testProperty'", typeDeserializerBase.getClass(), typeDeserializer.getClass());
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 assertEquals(JsonTypeInfo.As.WRAP_OBJECT, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 String baseTypeName = typeDeserializerBase.baseTypeName();
 assertEquals("java.lang.Object", baseTypeName);
 }

 @Test
 public void testToString() throws Exception {
 String toString = typeDeserializerBase.toString();
 assertTrue(toString.startsWith("[TypeDeserializerBase"));
 }

 @Test(expected = NullPointerException.class)
 public void testForPropertyNullProperty() throws Exception {
 BeanProperty beanProperty = null;
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
 fail("TypeDeserializer should not be created with null property");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNonBeanProperty() throws Exception {
 int testProperty = 5;
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(new BeanProperty("testProperty", int.class, "", null, null, null, null));
 fail("TypeDeserializer should not be created with non-BeanProperty");
 }

 @Test
 public void testFindDeserializerNullContext() throws Exception {
 String typeId = "EmptyTypeId";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(null, typeId);
 assertNull("Deserializer should be null for null context", deserializer);
 }

 @Test
 public void testFindDeserializerNullTypeId() throws Exception {
 JsonDeserializationContext context = objectMapper.getDeserializationContext();
 String typeId = null;
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
 assertNull("Deserializer should be null for null typeId", deserializer);
 }

 @Test
 public void testFindDeserializerKnownTypeId() throws Exception {
 String typeId = "java.lang.String";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
 assertNotNull("Deserializer should not be null for known typeId", deserializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDeserializerInvalidTypeId() throws Exception {
 String typeId = "InvalidTypeId";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
 fail("Deserializer should throw IllegalArgumentException for invalid typeId");
 }

 @Test
 public void testFindDefaultImplDeserializerNullContext() throws Exception {
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(null);
 assertNull("Deserializer should be null for null context", deserializer);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
 assertNotNull("Deserializer should not be null", deserializer);
 }

 @Test
 public void testHandleUnknownTypeIdNullContext() throws Exception {
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), null, typeIdResolver, null);
 assertNull("JavaType should be null for null context", javaType);
 }

 @Test
 public void testHandleUnknownTypeIdNullTypeId() throws Exception {
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "", typeIdResolver, null);
 assertNull("JavaType should be null for empty typeId", javaType);
 }

 @Test
 public void testHandleUnknownTypeIdCustomTypeIdResolver() throws Exception {
 TypeIdResolver typeIdResolver = new CustomTypeIdResolver();
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "", typeIdResolver, null);
 assertNotNull("JavaType should not be null for custom typeIdResolver", javaType);
 }

 @Test
 public void testHandleUnknownTypeIdUnknownBaseType() throws Exception {
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "UnknownTypeId", typeIdResolver, null);
 assertNull("JavaType should be null for unknown baseType", javaType);
 }

 @Test
 public void testHandleUnknownTypeIdKnownBaseType() throws Exception {
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "UnknownTypeId", typeIdResolver, ObjectType.getDefaultInstance());
 assertNotNull("JavaType should not be null for known baseType", javaType);
 }

 @Test
 public void testHandleUnknownTypeIdWithContextualDeserializer() throws Exception {
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "ContextualTypeId", typeIdResolver, ObjectType.getDefaultInstance());
 assertNotNull("JavaType should not be null for contextual deserializer", javaType);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeIdNoDeserializerFound() throws Exception {
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "NoDeserializerFoundTypeId", typeIdResolver, ObjectType.getDefaultInstance());
 fail("Should throw IOException for no deserializer found");
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeIdWithDefaultImpl() throws Exception {
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "WithDefaultImplTypeId", typeIdResolver, ObjectType.getDefaultInstance());
 assertNotNull("JavaType should not be null for default impl", javaType);
 }

}