package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private DeserializationContext ctxt;
 private JsonParser jp;
 private TypeIdResolver idResolver;
 private JavaType baseType;
 private BeanProperty property;
 private String typeId;

 @Before
 public void setUp() throws Exception {
 idResolver = mock(TypeIdResolver.class);
 baseType = mock(JavaType.class);
 property = mock(BeanProperty.class);
 ctxt = mock(DeserializationContext.class);
 testee = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }
 };
 jp = mock(JsonParser.class);
 typeId = "typeId";
 }

 @Test
 public void testFindDeserializer() throws Exception {
 when(idResolver.typeFromId(ctxt, typeId)).thenReturn(baseType);
 when(ctxt.findContextualValueDeserializer(baseType, property)).thenReturn(mock(JsonDeserializer.class));
 assertEquals(mock(JsonDeserializer.class), testee._findDeserializer(ctxt, typeId));
 }

 @Test
 public void testFindDeserializer_null() throws Exception {
 when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);
 assertNull(testee._findDeserializer(ctxt, typeId));
 }

 @Test
 public void testFindDeserializer_defaultImpl() throws Exception {
 assertNull(testee._findDeserializer(ctxt, null));
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 when(ctxt.findContextualValueDeserializer(baseType, property)).thenReturn(mock(JsonDeserializer.class));
 JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(ctxt);
 assertEquals(mock(JsonDeserializer.class), deserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDefaultImplDeserializer_null() throws Exception {
 testee._findDefaultImplDeserializer(ctxt);
 fail("Should throw IOException");
 }

 @Test(expected = JsonMappingException.class)
 public void testHandleUnknownTypeId() throws Exception {
 when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);
 String expectedMessage = "No (native) type id found when one was expected for polymorphic type handling";
 assertEquals(expectedMessage, testee._handleUnknownTypeId(ctxt, typeId, idResolver, baseType).getMessage());
 }
}