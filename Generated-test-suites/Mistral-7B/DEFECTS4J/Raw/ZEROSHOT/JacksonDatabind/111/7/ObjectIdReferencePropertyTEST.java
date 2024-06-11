package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectIdIdentifier;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private JsonDeserializer<Object> jsonDeserializer;
 private SettableBeanProperty settableBeanProperty;
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object instance;
 private NullValueProvider nullValueProvider;

 @Before
 public void setUp() throws Exception {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = new Object();
 nullValueProvider = Mockito.mock(NullValueProvider.class);
 }

 @Test
 public void testObjectIdReferencePropertyCreation() {
 // Test default constructor
 ObjectIdReferenceProperty defaultObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, null);
 assertEquals(SettableBeanProperty.class, defaultObjectIdReferenceProperty.getClass());
 assertNull(defaultObjectIdReferenceProperty._forward);
 assertNull(defaultObjectIdReferenceProperty._objectIdInfo);

 // Test constructor with SettableBeanProperty and ObjectIdInfo
 ObjectIdReferenceProperty objectIdReferencePropertyWithSettableBeanPropertyAndObjectIdInfo = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 assertEquals(ObjectIdReferenceProperty.class, objectIdReferencePropertyWithSettableBeanPropertyAndObjectIdInfo.getClass());
 assertEquals(settableBeanProperty, objectIdReferencePropertyWithSettableBeanPropertyAndObjectIdInfo._forward);
 assertEquals(objectIdInfo, objectIdReferencePropertyWithSettableBeanPropertyAndObjectIdInfo._objectIdInfo);

 // Test constructor with same ObjectIdReferenceProperty and JsonDeserializer
 ObjectIdReferenceProperty objectIdReferencePropertyWithSameObjectIdReferencePropertyAndJsonDeserializer = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
 assertEquals(ObjectIdReferenceProperty.class, objectIdReferencePropertyWithSameObjectIdReferencePropertyAndJsonDeserializer.getClass());
 assertEquals(objectIdReferenceProperty, objectIdReferencePropertyWithSameObjectIdReferencePropertyAndJsonDeserializer._forward);
 assertEquals(objectIdInfo, objectIdReferencePropertyWithSameObjectIdReferencePropertyAndJsonDeserializer._objectIdInfo);
 assertEquals(jsonDeserializer, objectIdReferencePropertyWithSameObjectIdReferencePropertyAndJsonDeserializer._valueDeserializer);

 // Test constructor with same ObjectIdReferenceProperty and PropertyName
 ObjectIdReferenceProperty objectIdReferencePropertyWithSameObjectIdReferencePropertyAndPropertyName = new ObjectIdReferenceProperty(objectIdReferenceProperty, new PropertyName("newName"));
 assertEquals(ObjectIdReferenceProperty.class, objectIdReferencePropertyWithSameObjectIdReferencePropertyAndPropertyName.getClass());
 assertEquals(objectIdReferenceProperty, objectIdReferencePropertyWithSameObjectIdReferencePropertyAndPropertyName._forward);
 assertEquals(objectIdInfo, objectIdReferencePropertyWithSameObjectIdReferencePropertyAndPropertyName._objectIdInfo);
 assertEquals("newName", objectIdReferencePropertyWithSameObjectIdReferencePropertyAndPropertyName.getName().asString());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 when(settableBeanProperty.getAnnotation(Mockito.any(Annotation.class))).thenReturn(mock(Annotation.class));
 when(objectIdInfo.getAnnotation(Mockito.any(Annotation.class))).thenReturn(mock(Annotation.class));

 assertEquals(mock(Annotation.class), objectIdReferenceProperty.getAnnotation(mock(Annotation.class).getClass()));
 verify(settableBeanProperty, times(1)).getAnnotation(Mockito.any(Annotation.class).getClass());
 verify(objectIdInfo, times(1)).getAnnotation(Mockito.any(Annotation.class).getClass());
 }

 @Test
 public void testGetMember() throws Exception {
 when(settableBeanProperty.getMember()).thenReturn(mock(AnnotatedMember.class));

 assertEquals(mock(AnnotatedMember.class), objectIdReferenceProperty.getMember());
 verify(settableBeanProperty, times(1)).getMember();
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 verify(settableBeanProperty, times(1)).getCreatorIndex();
 }

 @Test
 public void testDeserializeAndSet() throws IOException, JsonProcessingException {
 ObjectIdIdentifier objectIdIdentifier = new ObjectIdIdentifier(1L, Object.class);
 UnresolvedForwardReference unresolvedForwardReference = mock(UnresolvedForwardReference.class);
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, unresolvedForwardReference, Object.class, instance);

 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
 when(jsonParser.nextFieldName()).thenReturn("id");
 when(jsonParser.nextValue()).thenReturn(new ObjectIdIdentifier(2L, Object.class).getText());

 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(new Object());

 objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 verify(settableBeanProperty, times(1)).set(instance, argThat(arg -> ClassUtil.classMatches(arg, Object.class)));
 verify(jsonDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
 verify(unresolvedForwardReference, times(1)).getRoid().appendReferring(propertyReferring);
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
 when(jsonParser.nextFieldName()).thenReturn("id");
 when(jsonParser.nextValue()).thenReturn(null);

 ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 verify(settableBeanProperty, times(1)).set(instance, null);
 assertNull(result);
 }

 @Test
 public void testDeserializeSetAndReturnObject() throws IOException, JsonProcessingException {
 ObjectIdIdentifier objectIdIdentifier = new ObjectIdIdentifier(1L, Object.class);
 UnresolvedForwardReference unresolvedForwardReference = mock(UnresolvedForwardReference.class);
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, unresolvedForwardReference, Object.class, instance);

 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
 when(jsonParser.nextFieldName()).thenReturn("id");
 when(jsonParser.nextValue()).thenReturn(new ObjectIdIdentifier(2L, Object.class).getText());
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(new Object());

 Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 verify(settableBeanProperty, times(1)).set(instance, argThat(arg -> ClassUtil.classMatches(arg, Object.class)));
 verify(jsonDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
 assertEquals(new Object(), result);
 }

 @Test
 public void testSerializeAndSet() throws IOException {
 ObjectIdIdentifier objectIdIdentifier = new ObjectIdIdentifier(1L, Object.class);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 ObjectWriter objectWriter = mock(ObjectWriter.class);
 ObjectWriter.DefaultPrettyPrinter defaultPrettyPrinter = mock(ObjectWriter.DefaultPrettyPrinter.class);
 ObjectWriter objectWriterWithDefaultPrettyPrinter = Mockito.spy(objectWriter);
 objectWriterWithDefaultPrettyPrinter.setPrettyPrinter(defaultPrettyPrinter);

 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
 when(jsonParser.nextFieldName()).thenReturn("id");
 when(jsonParser.nextValue()).thenReturn(objectIdIdentifier.getText());
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(new Object());

 when(objectWriterWithDefaultPrettyPrinter.writeValueAsString(instance)).thenReturn("{\"id\":1}");

 objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 verify(jsonGenerator, times(1)).writeStartObject();
 verify(jsonGenerator, times(1)).writeFieldName("id");
 verify(jsonGenerator, times(1)).writeValue(objectIdIdentifier.getText());
 verify(jsonGenerator, times(1)).writeEndObject();
 verify(jsonDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
 }
}