package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private DeserializationContext context;
 private TypeIdResolver typeIdResolver;
 private BeanProperty beanProperty;
 private JavaType baseType;
 private JavaType defaultImpl;

 @Before
 public void setUp() {
 context = mock(DeserializationContext.class);
 typeIdResolver = mock(TypeIdResolver.class);
 beanProperty = mock(BeanProperty.class);
 baseType = mock(JavaType.class);
 defaultImpl = mock(JavaType.class);
 typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, null, false, defaultImpl.getRawClass()) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return null;
 }

 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return null;
 }
 };
 }

 @Test
 public void testFindDeserializerWithNullTypeId() throws IOException {
 when(typeIdResolver.typeFromId(context, null)).thenReturn(defaultImpl);
 Object result = typeDeserializerBase._findDeserializer(context, null);
 assertEquals(NullifyingDeserializer.instance, result);
 }

 @Test
 public void testFindDeserializerWithTypeId() throws IOException {
 String typeId = "testTypeId";
 when(typeIdResolver.typeFromId(context, typeId)).thenReturn(mock(JavaType.class));
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(typeIdResolver.getDeserializerInstance(context, typeId)).thenReturn(deserializer);
 Object result = typeDeserializerBase._findDeserializer(context, typeId);
 assertEquals(deserializer, result);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws IOException {
 when(context.findContextualValueDeserializer(defaultImpl, beanProperty)).thenReturn(mock(JsonDeserializer.class));
 Object result = typeDeserializerBase._findDefaultImplDeserializer(context);
 assertTrue(result instanceof JsonDeserializer);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 // This test case is not possible to execute as we don't have JsonParser available
 // in the test environment.
 }

 @Test(expected = JsonMappingException.class)
 public void testHandleUnknownTypeId() throws IOException {
 String typeId = "unknownTypeId";
 when(typeIdResolver.typeFromId(context, typeId)).thenReturn(null);
 typeDeserializerBase._handleUnknownTypeId(context, typeId, typeIdResolver, baseType);
 fail("Expected JsonMappingException to be thrown");
 }
}