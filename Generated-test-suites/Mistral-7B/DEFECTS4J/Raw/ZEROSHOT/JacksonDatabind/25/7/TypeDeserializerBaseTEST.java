package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private DeserializationContext context;
 private JsonFactory factory;

 @Before
 public void setup() throws Exception {
 factory = new JsonFactory();
 context = factory.createContextFinder().findContext(new AnonymousClassInnerClass());
 testee = new TypeDeserializerBase(JsonTypeInt.class, new StandardTypeIdResolver(),
 "typePropertyName", true, String.class) {
 @Override
 protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
 throws IOException {
 return null;
 }
 };
 }

 @Test
 public void testBaseTypeName() throws Exception {
 assertEquals("java.lang.Integer", testee.baseTypeName());
 }

 @Test
 public void testGetPropertyName() throws Exception {
 assertEquals("typePropertyName", testee.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() throws Exception {
 assertEquals(new StandardTypeIdResolver(), testee.getTypeIdResolver());
 }

 @Test
 public void testGetDefaultImpl() throws Exception {
 assertNull(testee.getDefaultImpl());
 }

 @Test
 public void testToString() throws Exception {
 String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class java.lang.Integer; id-resolver:com.fasterxml.jackson.databind.jsontype.impl.StandardTypeIdResolver@3426dc62; ]";
 assertEquals(expected, testee.toString());
 }

 @Test
 public void testFindDeserializerNull() throws Exception {
 assertNull(testee._findDeserializer(context, null));
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerUnknownTypeId() throws Exception {
 testee._findDeserializer(context, "unknownTypeId");
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testFindDeserializerKnownTypeId() throws Exception {
 JavaType javaType = factory.constructType(String.class);
 JsonDeserializer<Object> deserializer = context.findContextualValueDeserializer(javaType, null);
 String typeId = javaType.getName();
 assertEquals(deserializer, testee._findDeserializer(context, typeId));
 }

 @Test
 public void testFindDefaultImplDeserializerNull() throws Exception {
 assertNull(testee._findDefaultImplDeserializer(context));
 }

 @Test
 public void testFindDefaultImplDeserializerNotNull() throws Exception {
 JsonDeserializer<Object> deserializer = context.findValueDeserializer(String.class, null);
 testee._defaultImplDeserializer = deserializer;
 JsonDeserializer<Object> result = testee._findDefaultImplDeserializer(context);
 assertEquals(deserializer, result);
 }

 @Test
 public void testDeserializeWithNativeTypeIdNull() throws Exception {
 assertNull(testee._deserializeWithNativeTypeId(factory.createParser(factory.createCharArrayBuilder()), context));
 }

 @Test
 public void testDeserializeWithNativeTypeIdValid() throws Exception {
 String json = "{\"typeId\":\"java.lang.String\",\"value\":\"hello\"}";
 JsonParser parser = factory.createParser(json);
 assertEquals("hello", testee._deserializeWithNativeTypeId(parser, context));
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdInvalid() throws Exception {
 String json = "{\"typeId\":\"java.lang.Integer\",\"value\":\"hello\"}";
 testee._deserializeWithNativeTypeId(factory.createParser(json), context);
 fail("Expected IOException to be thrown");
 }
}

 class AnonymousClassInnerClass {
 // Empty class for context creation
 }