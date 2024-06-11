package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class TypeDeserializerBaseTest {

 private ObjectMapper objectMapper;
 private DeserializationContext ctx;
 private TypeDeserializerBase typeDeserializerBase;
 private JsonDeserializer<Object> jsonDeserializerMock;
 private TypeIdResolver typeIdResolverMock;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 ctx = mock(DeserializationContext.class);
 typeIdResolverMock = mock(TypeIdResolver.class);
 jsonDeserializerMock = mock(JsonDeserializer.class);
 when(typeIdResolverMock.typeFromId(ctx, "testId"))
         .thenReturn(new ClassType(String.class));
 typeDeserializerBase = new TypeDeserializerBase(
         new ClassType(Object.class), typeIdResolverMock,
         "testProperty", true, new ClassType(String.class)) {
     @Override
     protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt,
             String typeId) throws IOException {
         return jsonDeserializerMock;
     }
 };
 }

 @Test
 public void testForProperty() throws Exception {
     BeanProperty beanPropertyMock = mock(BeanProperty.class);
     TypeDeserializer<String> expectedTypeDeserializer = new TypeDeserializerBase(typeDeserializerBase, beanPropertyMock);
     assertEquals(expectedTypeDeserializer.getClass(), typeDeserializerBase.forProperty(beanPropertyMock).getClass());
 }

 @Test
 public void testGetTypeInclusion() {
     assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
     assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testGetPropertyName() {
     assertEquals("testProperty", typeDeserializerBase.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() {
     assertEquals(typeIdResolverMock, typeDeserializerBase.getTypeIdResolver());
 }

 @Test
 public void testDefaultImpl() {
     assertEquals(String.class, typeDeserializerBase.getDefaultImpl().asRawType());
 }

 @Test
 public void testBaseType() {
     assertEquals(Object.class, typeDeserializerBase.baseType());
 }

 @Test
 public void testFindDeserializer() throws Exception {
     when(jsonDeserializerMock.getClass()).thenReturn(StringDeserializer.class);
     ArgumentCaptor<String> typeIdCaptor = ArgumentCaptor.forClass(String.class);
     typeDeserializerBase.findDeserializer(ctx, "testId");
     typeDeserializerBase.getDeserializers().forEach(deserializer -> assertEquals(jsonDeserializerMock, deserializer));
     verify(jsonDeserializerMock).deserialize(any(JsonParser.class), eq(ctx));
     assertEquals(StringDeserializer.class, typeDeserializerBase.getDeserializers().get("testId").getClass());
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
     ArgumentCaptor<BeanProperty> beanPropertyCaptor = ArgumentCaptor.forClass(BeanProperty.class);
     typeDeserializerBase.findDefaultImplDeserializer(ctx);
     verify(ctx).findContextualValueDeserializer(eq(String.class), beanPropertyCaptor.capture());
     assertEquals(beanPropertyMock, beanPropertyCaptor.getValue());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
     String jsonString = "{\"testProperty\":\"testValue\",\"typeId\":\"testId\"}";
     JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
     when(jsonDeserializerMock.deserialize(jsonParser, ctx)).thenReturn("testResult");
     Object result = typeDeserializerBase.deserializeWithNativeTypeId(jsonParser, ctx, "testId");
     assertEquals("testResult", result);
 }

 @Test
 public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
     String jsonString = "{\"testProperty\":\"testValue\"}";
     JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
     when(jsonDeserializerMock.deserialize(jsonParser, ctx)).thenReturn(null);
     assertNull(typeDeserializerBase.deserializeWithNativeTypeId(jsonParser, ctx, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNativeTypeIdUnknownTypeId() throws Exception {
     String jsonString = "{\"testProperty\":\"testValue\",\"typeId\":\"unknownId\"}";
     JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
     typeDeserializerBase.deserializeWithNativeTypeId(jsonParser, ctx, "unknownId");
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
     when(typeIdResolverMock.getDescForKnownTypeIds()).thenReturn("knownTypeIds");
     when(typeIdResolverMock.typeFromId(ctx, "unknownId"))
             .thenThrow(new IOException("Unknown type id: unknownId"));
     when(jsonDeserializerMock.getClass()).thenReturn(StringDeserializer.class);
     when(jsonDeserializerMock.deserialize(any(JsonParser.class), eq(ctx)))
             .thenReturn("testResult");
     ObjectMapper objectMapper = new ObjectMapper();
     TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
             new ClassType(Object.class), typeIdResolverMock,
             "testProperty", true, new ClassType(String.class)) {
         @Override
         protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt,
                 String typeId) throws IOException {
             return jsonDeserializerMock;
         }

         @Override
         protected void _handleUnknownTypeId(DeserializationContext ctxt, String extraDesc)
                 throws IOException {
             super._handleMissingTypeId(ctxt, extraDesc);
         }
     };
     String jsonString = "{\"testProperty\":\"testValue\",\"typeId\":\"unknownId\"}";
     JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
     assertEquals("testResult", typeDeserializerBase.handleUnknownTypeId(jsonParser, ctx, "unknownId"));
 }

 @Test(expected = JsonMappingException.class)
 public void testHandleMissingTypeId() throws Exception {
     when(typeIdResolverMock.getDescForKnownTypeIds()).thenReturn("knownTypeIds");
     when(jsonDeserializerMock.getClass()).thenReturn(StringDeserializer.class);
     when(jsonDeserializerMock.deserialize(any(JsonParser.class), eq(ctx)))
             .thenReturn("testResult");
     objectMapper.findAndRegisterModules();
     JavaType javaType = new TypeReference<Map<String, Object>>() {}.getType();
     TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
             new ClassType(Object.class), typeIdResolverMock,
             "testProperty", true, javaType) {
         @Override
         protected void _handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                 throws IOException {
             throw new JsonMappingException("test");
         }
     };
     String jsonString = "{\"testProperty\":\"testValue\"}";
     JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
     jsonParser.nextToken();
 }
}

class StringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
     return jsonParser.getValueAsString();
 }
}