package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsPropertyTypeDeserializerTest extends BaseTest {

 private AsPropertyTypeDeserializer deserializer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "typePropertyName", true, null, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObject_NormalCase() throws IOException {
 Object result = deserializer.deserializeTypedFromObject(JsonParserFactory.getEmptyParser(), context);
 assertEquals("String", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedFromObject_WithTypeId() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"String\"}");
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals("String", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedFromObject_WrongPropertyName() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"wrongPropertyName\": \"String\"}");
 assertNull(deserializer.deserializeTypedFromObject(parser, context));
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObject_MissingProperty() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{}");
 deserializer.deserializeTypedFromObject(parser, context);
 }

 @Test
 public void testDeserializeTypedFromObject_NativeTypeId() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"int\"}");
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(Integer.class, result.getClass());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObject_WrongToken() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("[]");
 deserializer.deserializeTypedFromObject(parser, context);
 }

 @Test
 public void testDeserializeTypedFromObject_DefaultImpl() throws IOException {
 deserializer = new AsPropertyTypeDeserializer(null, null, null, false, mapper.getTypeFactory().constructType(String[].class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(JsonParserFactory.getEmptyParser(), context);
 assertEquals("[Ljava.lang.String;", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl_TokenBuffer() throws IOException {
 Object result = deserializer.deserializeTypedUsingDefaultImpl(JsonParserFactory.getEmptyParser(), context, null);
 assertEquals("[Ljava.lang.String;", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl_WithTokenBuffer() throws IOException {
 TokenBuffer tokenBuffer = new TokenBuffer(JsonParserFactory.getEmptyParser(), context);
 Object result = deserializer.deserializeTypedUsingDefaultImpl(JsonParserFactory.getEmptyParser(), context, tokenBuffer);
 assertEquals("[Ljava.lang.String;", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl_WithDeserializer() throws IOException {
 ObjectDeserializer stringDeserializer = mapper.findDeserializer(String.class);
 Object result = deserializer.deserializeTypedUsingDefaultImpl(JsonParserFactory.getEmptyParser(), context, stringDeserializer);
 assertEquals("java.lang.String", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedFromArray_NormalCase() throws IOException {
 Object result = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser("[\"String\"]"), context);
 assertEquals("[Ljava.lang.String;", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedFromArray_WrongToken() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("[1, 2]");
 assertNull(deserializer.deserializeTypedFromArray(parser, context));
 }

 @Test
 public void testDeserializeFromScalar_String() throws IOException {
 Object result = deserializer.deserializeFromScalar(mapper.getFactory().createParser("\"String\""), context);
 assertEquals("String", result.getClass().getName());
 }

 @Test
 public void testDeserializeFromScalar_Number() throws IOException {
 Object result = deserializer.deserializeFromScalar(mapper.getFactory().createParser("12"), context);
 assertEquals(Integer.class, result.getClass());
 }

 @Test
 public void testDeserializeFromScalar_Null() throws IOException {
 Object result = deserializer.deserializeFromScalar(mapper.getFactory().createParser(null), context);
 assertNull(result);
 }

 @Test
 public void testDeserializeFromScalar_Boolean() throws IOException {
 Object result = deserializer.deserializeFromScalar(mapper.getFactory().createParser("true"), context);
 assertEquals(Boolean.class, result.getClass());
 }

}