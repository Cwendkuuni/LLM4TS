package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsPropertyTypeDeserializerTest extends BaseTest {

 private AsPropertyTypeDeserializer deserializer;
 private JsonParser parser;
 private DeserializationContext context;
 private ObjectMapper mapper;
 private TypeIdResolver idResolver;
 private JsonDeserializer<Object> defaultImplDeserializer;
 private TypeDeserializer typeDeserializer;
 private JavaType javaType;
 private TypeReference<String> stringType;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 idResolver = mock(TypeIdResolver.class);
 javaType = mapper.getTypeFactory().constructType(String.class);
 stringType = new TypeReference<String>() {};
 defaultImplDeserializer = mock(JsonDeserializer.class);
 typeDeserializer = mock(TypeDeserializer.class);
 deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, defaultImplDeserializer, As.PROPERTY);
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 }

 // Test constructor
 @Test
 public void testConstructor() {
 // Test no-args constructor
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
 assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
 // Test constructor with parameters
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, defaultImplDeserializer, As.PROPERTY);
 assertEquals(javaType, deserializer.getBaseType());
 assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
 }

 // Test method forProperty
 @Test
 public void testForProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, null, As.PROPERTY);
 AsPropertyTypeDeserializer deserializer2 = deserializer1.forProperty(null);
 assertEquals(deserializer1, deserializer2);
 }

 // Test method deserializeTypedFromObject
 @Test
 public void testDeserializeTypedFromObject_JsonObject() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("type");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("StringType");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(idResolver.resolveDeserializer(javaType, context)).thenReturn(stringDeserializer);

 deserializer.deserializeTypedFromObject(parser, context);
 assertEquals("StringType", deserializer.getDeserializedTypeName());
 }

 // Test method deserializeTypedFromObject with no type property
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObject_NoTypeProperty() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("name");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("value");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 deserializer.deserializeTypedFromObject(parser, context);
 }

 // Test method deserializeTypedFromObject with non-matching type property
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObject_NonMatchingTypeProperty() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("anotherType");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("StringType");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 deserializer.deserializeTypedFromObject(parser, context);
 }

 // Test method deserializeTypedFromObject with invalid JSON
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObject_InvalidJson() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("type");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("{");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 deserializer.deserializeTypedFromObject(parser, context);
 }

 // Test method deserializeTypedFromObject with native type id
 @Test
 public void testDeserializeTypedFromObject_NativeTypeId() throws Exception {
 when(parser.canReadTypeId()).thenReturn(true);
 when(parser.getTypeId()).thenReturn("StringType");

 deserializer.deserializeTypedFromObject(parser, context);
 assertEquals("StringType", deserializer.getDeserializedTypeName());
 }

 // Test method deserializeTypedFromObject with null typeId
 @Test
 public void testDeserializeTypedFromObject_NullTypeId() throws Exception {
 when(parser.canReadTypeId()).thenReturn(false);

 deserializer.deserializeTypedFromObject(parser, context);
 assertNull(deserializer.getDeserializedTypeName());
 }

 // Test method deserializeTypedFromObject with default impl
 @Test
 public void testDeserializeTypedFromObject_DefaultImpl() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
 when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(defaultImplDeserializer, deserializer.getDeserializedType());
 }

 // Test method deserializeTypedFromObject with natural value
 @Test
 public void testDeserializeTypedFromObject_NaturalValue() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 when(parser.getIntValue()).thenReturn(1);

 deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(Integer.TYPE, deserializer.getDeserializedType().getClass());
 }

 // Test method deserializeTypedFromObject with wrapper array
 @Test
 public void testDeserializeTypedFromObject_WrapperArray() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(List.class, deserializer.getDeserializedType().getClass());
 }

 // Test method deserializeTypedUsingDefaultImpl
 @Test
 public void testDeserializeTypedUsingDefaultImpl() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("type");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("StringType");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 deserializer.deserializeTypedUsingDefaultImpl(parser, context);
 assertEquals(stringType, deserializer.getDeserializedType());
 }

 // Test method deserializeTypedFromAny
 @Test
 public void testDeserializeTypedFromAny_JsonObject() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("type");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("StringType");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 when(parser.getIntValue()).thenReturn(1);
 when(idResolver.resolveDeserializer(javaType, context)).thenReturn(stringDeserializer);

 deserializer.deserializeTypedFromAny(parser, context);
 assertEquals("StringType", deserializer.getDeserializedTypeName());
 }

 // Test method deserializeTypedFromAny with no type property
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromAny_NoTypeProperty() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
 when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 deserializer.deserializeTypedFromAny(parser, context);
 }

 // Test method deserializeTypedFromAny with invalid JSON
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromAny_InvalidJson() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("type");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getText()).thenReturn("{");
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 deserializer.deserializeTypedFromAny(parser, context);
 }

 // Test method deserializeTypedFromAny with wrapper array
 @Test
 public void testDeserializeTypedFromAny_WrapperArray() throws Exception {
 when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 deserializer.deserializeTypedFromAny(parser, context);
 assertEquals(List.class, deserializer.getDeserializedType().getClass());
 }
}