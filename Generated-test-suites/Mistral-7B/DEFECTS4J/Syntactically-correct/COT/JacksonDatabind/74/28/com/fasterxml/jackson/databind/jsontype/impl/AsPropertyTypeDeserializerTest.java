package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.JsonParserSequence;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 private static final AsPropertyTypeDeserializer testInstance = new AsPropertyTypeDeserializer(
         mock(JavaType.class), mock(TypeIdResolver.class), "typePropertyName", true, mock(JavaType.class), As.PROPERTY);

 @SuppressWarnings("unchecked")
 private static final AsPropertyTypeDeserializer testInstanceWithProperty = new AsPropertyTypeDeserializer(
         testInstance, mock(BeanProperty.class));

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedFromObject() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 String typePropertyName = "typePropertyName";
 JavaType baseType = mock(JavaType.class);
 Object expected = new Object();
 when(ctxt.findValueDeserializer(baseType)).thenReturn(mock(JsonDeserializer.class));
 when(ctxt.findBeanProperty(typePropertyName, baseType)).thenReturn(null);

 // When
 Object result = testInstance.deserializeTypedFromObject(jp, ctxt);

 // Then
 assertNull(result);
 verify(ctxt, times(1)).reportWrongTokenException(jp, JsonToken.FIELD_NAME,
 "missing property '"+typePropertyName+"' that is to contain type id  (for class "+baseType.getClass().getName()+")");
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedFromObjectWithTypeId() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 String typePropertyName = "typePropertyName";
 JavaType baseType = mock(JavaType.class);
 ObjectTypeIdResolver idResolver = mock(ObjectTypeIdResolver.class);
 when(ctxt.getTypeIdResolver()).thenReturn(idResolver);
 when(idResolver.findTypeId(jp)).thenReturn("typeId");
 JacksonAnnotationIntrospector introspector = mock(JacksonAnnotationIntrospector.class);
 when(ctxt.findAnnotationIntrospector()).thenReturn(introspector);
 when(introspector.findMixInAnnotations(baseType)).thenReturn(new Class[0]);
 when(ctxt.findValueDeserializer(baseType)).thenReturn(mock(JsonDeserializer.class));
 when(ctxt.findBeanProperty(typePropertyName, baseType)).thenReturn(mock(BeanProperty.class));
 when(ctxt.findValueDeserializer(baseType)).thenReturn(mock(JsonDeserializer.class));

 // When
 Object result = testInstance.deserializeTypedFromObject(jp, ctxt);

 // Then
 assertEquals(expected, result);
 verify(ctxt, times(1)).findValueDeserializer(baseType);
 verify(ctxt, times(1)).findBeanProperty(typePropertyName, baseType);
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 String typePropertyName = "typePropertyName";
 JavaType baseType = mock(JavaType.class);
 JavaType defaultImpl = mock(JavaType.class);
 when(ctxt.findValueDeserializer(baseType)).thenReturn(null);
 when(ctxt.findBeanProperty(typePropertyName, baseType)).thenReturn(null);
 when(ctxt.findValueDeserializer(defaultImpl)).thenReturn(mock(JsonDeserializer.class));

 // When
 Object result = testInstance.deserializeTypedFromObject(jp, ctxt);

 // Then
 assertNull(result);
 verify(ctxt, times(0)).reportWrongTokenException(jp, JsonToken.FIELD_NAME,
 "missing property '"+typePropertyName+"' that is to contain type id  (for class "+baseType.getClass().getName()+")");
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedFromObjectWithProperty() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 String typePropertyName = "typePropertyName";
 JavaType baseType = mock(JavaType.class);
 BeanProperty property = mock(BeanProperty.class);
 when(ctxt.findBeanProperty(typePropertyName, baseType)).thenReturn(property);
 when(property.getType()).thenReturn(baseType);
 when(property.getName()).thenReturn(typePropertyName);
 when(property.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty());

 // When
 Object result = testInstanceWithProperty.deserializeTypedFromObject(jp, ctxt);

 // Then
 assertNull(result);
 verify(ctxt, times(0)).reportWrongTokenException(jp, JsonToken.FIELD_NAME,
 "missing property '"+typePropertyName+"' that is to contain type id  (for class "+baseType.getClass().getName()+")");
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedForId() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 String typePropertyName = "typePropertyName";
 JavaType baseType = mock(JavaType.class);
 ObjectTypeIdResolver idResolver = mock(ObjectTypeIdResolver.class);
 when(ctxt.getTypeIdResolver()).thenReturn(idResolver);
 when(idResolver.findTypeId(jp)).thenReturn("typeId");
 Object expected = new Object();
 when(ctxt.findBeanProperty(typePropertyName, baseType)).thenReturn(mock(BeanProperty.class));
 when(ctxt.findValueDeserializer(baseType)).thenReturn(mock(JsonDeserializer.class));

 // When
 Object result = testInstance.deserializeTypedForId(jp, ctxt, null);

 // Then
 assertEquals(expected, result);
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedForIdWithTokenBuffer() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 String typePropertyName = "typePropertyName";
 JavaType baseType = mock(JavaType.class);
 ObjectTypeIdResolver idResolver = mock(ObjectTypeIdResolver.class);
 when(ctxt.getTypeIdResolver()).thenReturn(idResolver);
 when(idResolver.findTypeId(jp)).thenReturn("typeId");
 TokenBuffer tb = new TokenBuffer(jp, ctxt);
 when(ctxt.findBeanProperty(typePropertyName, baseType)).thenReturn(mock(BeanProperty.class));
 when(ctxt.findValueDeserializer(baseType)).thenReturn(mock(JsonDeserializer.class));

 // When
 Object result = testInstance.deserializeTypedForId(jp, ctxt, tb);

 // Then
 assertEquals(expected, result);
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedUsingDefaultImpl() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 String typePropertyName = "typePropertyName";
 JavaType baseType = mock(JavaType.class);
 JavaType defaultImpl = mock(JavaType.class);
 when(ctxt.findValueDeserializer(baseType)).thenReturn(null);
 when(ctxt.findBeanProperty(typePropertyName, baseType)).thenReturn(null);
 when(ctxt.findValueDeserializer(defaultImpl)).thenReturn(mock(JsonDeserializer.class));

 // When
 Object result = testInstance.deserializeTypedFromObject(jp, ctxt);

 // Then
 assertNull(result);
 verify(ctxt, times(0)).reportWrongTokenException(jp, JsonToken.FIELD_NAME,
 "missing property '"+typePropertyName+"' that is to contain type id  (for class "+baseType.getClass().getName()+")");

 // When
 Object result2 = testInstance.deserializeTypedUsingDefaultImpl(jp, ctxt, null);

 // Then
 assertEquals(defaultImpl, result2);
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedUsingDefaultImplWithTokenBuffer() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 String typePropertyName = "typePropertyName";
 JavaType baseType = mock(JavaType.class);
 JavaType defaultImpl = mock(JavaType.class);
 when(ctxt.findValueDeserializer(baseType)).thenReturn(null);
 when(ctxt.findBeanProperty(typePropertyName, baseType)).thenReturn(null);
 when(ctxt.findValueDeserializer(defaultImpl)).thenReturn(mock(JsonDeserializer.class));
 TokenBuffer tb = new TokenBuffer(jp, ctxt);

 // When
 Object result = testInstance.deserializeTypedFromObject(jp, ctxt);

 // Then
 assertNull(result);
 verify(ctxt, times(0)).reportWrongTokenException(jp, JsonToken.FIELD_NAME,
 "missing property '"+typePropertyName+"' that is to contain type id  (for class "+baseType.getClass().getName()+")");

 // When
 Object result2 = testInstance.deserializeTypedUsingDefaultImpl(jp, ctxt, tb);

 // Then
 assertEquals(defaultImpl, result2);
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedFromArray() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType baseType = mock(JavaType.class);
 Object[] expectedArray = new Object[1];
 when(ctxt.findValueDeserializer(baseType)).thenReturn(mock(JsonDeserializer.class));

 // When
 Object[] result = testInstance.deserializeTypedFromArray(jp, ctxt);

 // Then
 assertArrayEquals(expectedArray, result);
 }

 @SuppressWarnings("resource")
 @org.junit.Test
 public void testDeserializeTypedFromAny() throws IOException {
 // Given
 JsonParser jp = createMockJsonParser();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType baseType = mock(JavaType.class);
 when(ctxt.findValueDeserializer(baseType)).thenReturn(mock(JsonDeserializer.class));

 // When
 Object result = testInstance.deserializeTypedFromAny(jp, ctxt);

 // Then
 assertNull(result);
 }

 private JsonParser createMockJsonParser() {
 JsonParser jp = mock(JsonParser.class);
 when(jp.Tokens()).thenReturn(new JsonToken[0]);
 return jp;
 }
}