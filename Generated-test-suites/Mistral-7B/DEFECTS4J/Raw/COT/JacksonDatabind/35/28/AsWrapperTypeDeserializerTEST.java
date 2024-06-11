package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final JavaClassBase TEST_CLASS = new ClassType(AsWrapperTypeDeserializer.class);

 @Test
 public void testConstructor() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 Assert.assertNotNull(deserializer);
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 As typeInclusion = deserializer.getTypeInclusion();
 Assert.assertEquals(As.WRAPPER_OBJECT, typeInclusion);
 }

 @Test
 public void testDeserializeTypedFromObject_NullContext() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 Object result = deserializer.deserializeTypedFromObject(parser, null);
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObject_ValidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonObjectStart
 parser.nextToken(); // fieldName
 parser.setText("myTypeId");
 JsonDeserializer<String> deserializerForMyTypeId = createMockDeserializer(String.class);
 when(mockContext.findDeserializerByName("myTypeId")).thenReturn(deserializerForMyTypeId);
 Object result = deserializer.deserializeTypedFromObject(parser, createMockContext());
 verify(deserializerForMyTypeId).deserialize(parser, eq(createMockContext()));
 Assert.assertNull(parser.getCurrentToken()); // checks that the mock parser was advanced correctly
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromObject_InvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonObjectStart
 parser.nextToken(); // fieldName
 parser.setText("invalidTypeId");
 Object result = deserializer.deserializeTypedFromObject(parser, createMockContext());
 // exception should be thrown by the _findDeserializer method
 }

 @Test
 public void testDeserializeTypedFromObject_NullTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonObjectStart
 parser.nextToken(); // fieldName
 parser.setText(null);
 Object result = deserializer.deserializeTypedFromObject(parser, createMockContext());
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromArray_NullContext() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 Object result = deserializer.deserializeTypedFromArray(parser, null);
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromArray_ValidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonArrayStart
 Object[] result = deserializer.deserializeTypedFromArray(parser, createMockContext());
 // no verification is needed since no mock deserializer is used
 Assert.assertNull(parser.getCurrentToken()); // checks that the mock parser was advanced correctly
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromArray_InvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonArrayStart
 Object[] result = deserializer.deserializeTypedFromArray(parser, createMockContext());
 // exception should be thrown by the _findDeserializer method
 }

 @Test
 public void testDeserializeTypedFromScalar_NullContext() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 Object result = deserializer.deserializeTypedFromScalar(parser, null);
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromScalar_ValidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonNumber
 parser.setText("123");
 JsonDeserializer<Integer> deserializerForInteger = createMockDeserializer(Integer.class);
 when(mockContext.findDeserializerByName("Integer")).thenReturn(deserializerForInteger);
 Object result = deserializer.deserializeTypedFromScalar(parser, createMockContext());
 verify(deserializerForInteger).deserialize(parser, eq(createMockContext()));
 Assert.assertEquals(123, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromScalar_InvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonNumber
 parser.setText("123");
 Object result = deserializer.deserializeTypedFromScalar(parser, createMockContext());
 // exception should be thrown by the _findDeserializer method
 }

 @Test
 public void testDeserializeTypedFromAny_NullContext() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 Object result = deserializer.deserializeTypedFromAny(parser, null);
 Assert.assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromAny_ValidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonString
 parser.setText("myTypeId:{\"myField\":123}");
 JsonDeserializer<MyType> deserializerForMyType = createMockDeserializer(MyType.class);
 when(mockContext.findDeserializerByName("MyType")).thenReturn(deserializerForMyType);
 Object result = deserializer.deserializeTypedFromAny(parser, createMockContext());
 verify(deserializerForMyType).deserialize(parser, eq(createMockContext()));
 Assert.assertSame(parser.getCurrentToken(), JsonToken.END_OBJECT);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromAny_InvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonString
 parser.setText("invalidTypeId:{\"myField\":123}");
 Object result = deserializer.deserializeTypedFromAny(parser, createMockContext());
 // exception should be thrown by the _findDeserializer method
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser parser = createMockParser();
 parser.nextToken(); // jsonString
 parser.setText("{typeId:\"MyType\", value:{\"myField\":123}}");
 JsonDeserializer<MyType> deserializerForMyType = createMockDeserializer(MyType.class);
 when(mockContext.findDeserializerByName("MyType")).thenReturn(deserializerForMyType);
 Object result = deserializer.deserializeWithNativeTypeId(parser, createMockContext());
 verify(deserializerForMyType).deserialize(parser, eq(createMockContext()));
 Assert.assertSame(parser.getCurrentToken(), JsonToken.END_OBJECT);
 }

 private JsonParser createMockParser() {
 return createMock(JsonParser.class);
 }

 private DeserializationContext createMockContext() {
 return createMock(DeserializationContext.class);
 }

 private JsonDeserializer<?> createMockDeserializer(Class<?> targetType) {
 return createMock(JsonDeserializer.class, targetType.getName());
 }

}