package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;

public class TypeDeserializerBaseTest {

 private static final String TYPE_ID = "com.example.MyTypeId";
 private static final String TYPE_PROPERTY_NAME = "type";
 private static final String DEFAULT_IMPLEMENTATION_CLASS_NAME = "com.example.MyDefaultImpl";

 private TypeDeserializerBase testee;
 private BeanProperty property;
 private DeserializationContext context;
 private JsonParser parser;
 private JsonTypeInfo jsonTypeInfo;
 private TypeIdResolver typeIdResolver;
 private Class<?> baseTypeClass;

 @Before
 public void setup() throws Exception {
 JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
 property = new BeanProperty.Std("name", String.class);
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 jsonTypeInfo = mock(JsonTypeInfo.class);
 typeIdResolver = mock(TypeIdResolver.class);
 JavaType baseType = ObjectMapperUtil.constructType(getClass(), baseTypeClass);
 testee = new TypeDeserializerBase(baseType, typeIdResolver, TYPE_PROPERTY_NAME, true,
 new JavaType(Class.forName(DEFAULT_IMPLEMENTATION_CLASS_NAME)));
 when(parser.getTypeId()).thenReturn(TYPE_ID);
 when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
 }

 @Test
 public void testFindDeserializerWithTypeId() throws Exception {
 JsonDeserializer<Object> deserializer = testee._findDeserializer(context, TYPE_ID);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithNullTypeId() throws Exception {
 JsonDeserializer<Object> deserializer = testee._findDeserializer(context, null);
 assertNull(deserializer);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(context);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindDefaultImplDeserializerWhenDefaultImplIsNull() throws Exception {
 JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(context);
 assertNull(deserializer);
 }

 @Test
 public void test_deserializeWithNativeTypeId() throws IOException, JsonProcessingException {
 Object result = testee._deserializeWithNativeTypeId(parser, context);
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void test_deserializeWithNativeTypeIdWhenTypeIdIsNull() throws IOException, JsonProcessingException {
 testee._deserializeWithNativeTypeId(parser, context, null);
 }

 @Test(expected = JsonMappingException.class)
 public void test_deserializeWithNativeTypeIdWhenTypeIdIsEmptyString() throws IOException, JsonProcessingException {
 testee._deserializeWithNativeTypeId(parser, context, "");
 }

 @Test
 public void test_handleUnknownTypeId() throws Exception {
 JavaType result = testee._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, baseType);
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void test_handleUnknownTypeIdWhenTypeIdIsEmptyString() throws Exception {
 testee._handleUnknownTypeId(context, "", typeIdResolver, baseType);
 }

 @Test
 public void test_handleUnknownTypeIdWhenTypeIdIsNull() throws Exception {
 testee._handleUnknownTypeId(context, null, typeIdResolver, baseType);
 }

 @Test
 public void testForProperty() throws Exception {
 TypeDeserializer result = testee.forProperty(property);
 assertNotNull(result);
 }

 @Test
 public void testGetTypeInclusion() {
 JsonTypeInfo.As result = testee.getTypeInclusion();
 assertNotNull(result);
 }

 @Test
 public void testBaseTypeName() {
 String result = testee.baseTypeName();
 assertEquals(baseTypeClass.getName(), result);
 }

 @Test
 public void testToString() throws Exception {
 String result = testee.toString();
 // This test may fail because of line breaks, but the output should start and end
 // with '[' and ']' respectively
 assertTrue(result.startsWith("[") && result.endsWith("]"));
 }
}