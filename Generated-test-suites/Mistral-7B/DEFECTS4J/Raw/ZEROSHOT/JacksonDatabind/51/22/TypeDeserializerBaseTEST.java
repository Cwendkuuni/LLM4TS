package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DefaultSerializableString;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Introspect;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @org.junit.BeforeClass
 public static void setup() throws Exception {
 mapper.setAnnotationIntrospector(new AnnotationIntrospector());
 }

 @org.junit.Test
 public void testBaseTypeName() throws IOException {
 TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase("java.lang.String", null, null,
 false, null, null);
 assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
 }

 @org.junit.Test
 public void testGetPropertyName() throws IOException {
 TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase(null, null, null, false, null, null);
 assertNull(typeDeserializerBase.getPropertyName());
 }

 @org.junit.Test
 public void testGetTypeIdResolver() throws IOException {
 TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase(null, null, null, false, null, null);
 assertNull(typeDeserializerBase.getTypeIdResolver());
 }

 @org.junit.Test
 public void testGetDefaultImpl() throws IOException {
 TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase(null, null, null, false, null, null);
 assertNull(typeDeserializerBase.getDefaultImpl());
 }

 @org.junit.Test
 public void testToString() throws IOException {
 TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase("java.lang.String", null, null,
 false, null, null);
 assertEquals("[TypeDeserializerBase; base-type:java.lang.String; id-resolver:null]", typeDeserializerBase.toString());
 }

 @org.junit.Test
 public void testFindDeserializer() throws IOException {
 String typeId = "MyType";
 TypeIdResolver idResolver = mock(TypeIdResolver.class);
 when(idResolver.typeFromId(mock(DeserializationContext.class), typeId)).thenReturn(createJavaType(Map.class));
 TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase(null, null, null, false, idResolver, null);
 JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
 when(expectedDeserializer.getClass()).thenReturn(StdDeserializer.class);
 TypeDeserializer<Map<String, Object>> mapTypeDeserializer = mock(TypeDeserializer.class);
 when(mapTypeDeserializer.getClass()).thenReturn(TypeDeserializerBase.class);
 when(mapTypeDeserializer.forProperty(mock(BeanProperty.class))).thenReturn(expectedDeserializer);
 when(typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), typeId)).thenReturn(mapTypeDeserializer);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper objectMapperSpy = mock(ObjectMapper.class);
 when(objectMapperSpy.getFactory()).thenReturn(objectMapper.getFactory());
 JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId\":\"MyType\"}");
 jsonParser.setCodec(objectMapperSpy);
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(jsonParser.getCodec(), typeId);
 assertEquals(expectedDeserializer.getClass(), deserializer.getClass());
 }

 @org.junit.Test
 public void testFindDefaultImplDeserializer() throws IOException {
 TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase(null, null, null, false, null, null);
 JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
 when(expectedDeserializer.getClass()).thenReturn(NullifyingDeserializer.class);
 when(typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class))).thenReturn(expectedDeserializer);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper objectMapperSpy = mock(ObjectMapper.class);
 when(objectMapperSpy.getFactory()).thenReturn(objectMapper.getFactory());
 JsonParser jsonParser = mapper.getFactory().createParser("{}");
 jsonParser.setCodec(objectMapperSpy);
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(jsonParser.getCodec());
 assertEquals(expectedDeserializer.getClass(), deserializer.getClass());
 }

 @org.junit.Test
 public void testHandleUnknownTypeId() throws IOException {
 String typeId = "MyType";
 TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase(null, null, null, false, null, null);
 TypeDeserializerBase spyTypeDeserializerBase = mock(TypeDeserializerBase.class);
 when(spyTypeDeserializerBase.baseTypeName()).thenReturn("BaseType");
 when(spyTypeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), typeId, mock(TypeIdResolver.class), mock(JavaType.class))).thenCallRealMethod();
 TypeDeserializerBase typeDeserializerBaseSpy = spy(typeDeserializerBase);
 when(typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), typeId, mock(TypeIdResolver.class), mock(JavaType.class))).thenCallRealMethod();
 JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId\":\"" + typeId + "\"}");
 jsonParser.setCodec(mapper);
 JavaType javaType = mapper.getTypeFactory().constructType(getClass());
 when(mock(TypeIdResolver.class).typeFromId(mock(DeserializationContext.class), typeId)).thenReturn(javaType);
 when(mock(TypeIdResolver.class).forPropertyName(typeId)).thenReturn(mock(BeanProperty.class));
 Object result = typeDeserializerBase._handleUnknownTypeId(jsonParser.getCodec(), typeId, mock(TypeIdResolver.class), javaType);
 assertEquals(null, result);
 assertEquals("BaseType", typeDeserializerBaseSpy.baseTypeName());
 assertEquals("BaseType", spyTypeDeserializerBase.baseTypeName());
 }

 private TypeDeserializerBase createTypeDeserializerBase(String baseTypeName, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, TypeIdResolver resolver, JavaType defaultImpl) {
 return new TypeDeserializerBase(mock(JavaType.class), idResolver, typePropertyName, typeIdVisible, defaultImpl) {};
 }

 private JavaType createJavaType(Class<?> clazz) {
 return mapper.getTypeFactory().constructType(clazz);
 }
}