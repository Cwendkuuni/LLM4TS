package com.example.jackson.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private DeserializationContext context;
 private JsonParser parser;
 private TypeIdResolver typeIdResolver;

 @Before
 public void setUp() {
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 typeIdResolver = mock(TypeIdResolver.class);

 typeDeserializerBase = new TypeDeserializerBase(
 new JavaType(Object.class), typeIdResolver,
 "_typePropertyName", true, new JavaType(String.class)) {
 @Override
 protected TypeDeserializer forProperty(BeanProperty prop) {
 throw new UnsupportedOperationException();
 }
 };
 }

 @Test
 public void testFindDeserializer() throws JsonProcessingException {
 String typeId = "testTypeId";
 when(typeIdResolver.typeFromId(context, typeId)).thenReturn(null);
 when(context.findContextualValueDeserializer(any(), any())).thenReturn(mock(JsonDeserializer.class));

 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
 verify(context).findContextualValueDeserializer(eq(typeId), any());
 assertEquals(deserializer, TypeDeserializerBase.this.deserializers.get(typeId));
 }

 @Test
 public void testFindDefaultImplDeserializer() throws JsonProcessingException {
 when(context.findContextualValueDeserializer(eq(new JavaType(String.class)), any()))
 .thenReturn(mock(JsonDeserializer.class));
 assertEquals(TypeDeserializerBase.this.defaultImplDeserializer, typeDeserializerBase._findDefaultImplDeserializer(context));
 }

 @Test(expected = NullPointerException.class)
 public void testFindDeserializerNullContext() throws JsonProcessingException {
 typeDeserializerBase._findDeserializer(null, "typeId");
 }

 @Test(expected = NullPointerException.class)
 public void testFindDeserializerNullTypeId() throws JsonProcessingException {
 typeDeserializerBase._findDeserializer(context, null);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws JsonProcessingException {
 String typeId = "testTypeId";
 when(context.findContextualValueDeserializer(parser, eq(typeId)))
 .thenReturn(mock(JsonDeserializer.class));
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, context, typeId);
 verify(parser).nextToken();
 verify(context).findContextualValueDeserializer(parser, eq(typeId));
 assertEquals(result, TypeDeserializerBase.this.deserializers.get(typeId).deserialize(parser, context));
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeWithNativeTypeIdNullParser() throws JsonProcessingException {
 typeDeserializerBase._deserializeWithNativeTypeId(null, context, "typeId");
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeWithNativeTypeIdNullContext() throws JsonProcessingException {
 typeDeserializerBase._deserializeWithNativeTypeId(parser, null, "typeId");
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeWithNativeTypeIdNullTypeId() throws JsonProcessingException {
 typeDeserializerBase._deserializeWithNativeTypeId(parser, context, null);
 }

}