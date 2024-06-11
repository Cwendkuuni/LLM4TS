package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

 private JsonDeserializer<Object> valueDeserializer;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonParser mockJsonParser;
 private DeserializationContext mockDeserializationContext;
 private Object instance;
 private StdSerializer<String> idPropertySetter;

 @Before
 public void setup() throws Exception {
 valueDeserializer = mock(JsonDeserializer.class);
 objectIdReader = mock(ObjectIdReader.class);
 idPropertySetter = new StdSerializer<String>(String.class);
 mockJsonParser = mock(JsonParser.class);
 mockDeserializationContext = mock(DeserializationContext.class);

 when(objectIdReader.getIdType()).thenReturn(IdType.OBJECT_ID);
 when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
 when(objectIdReader.idProperty).thenReturn(new ObjectIdValueProperty.Std(idPropertySetter, null, null));
 when(mockDeserializationContext.findObjectId(_anyLong(), _anyObjectIdGenerator(), _anyObjectIdResolver())).thenReturn(new ReadableObjectId());
 when(mockDeserializationContext.findValueDeserializer(_any(Class.class))).thenReturn(valueDeserializer);

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 instance = new Object();
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 when(mockJsonParser.getValueAsString()).thenReturn("testId");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

 objectIdValueProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);

 Object id = "testId";
 ReadableObjectId roid = mock(ReadableObjectId.class);
 when(roid.getId()).thenReturn(id);
 when(mockDeserializationContext.findObjectId(_anyLong(), _anyObjectIdGenerator(), _anyObjectIdResolver())).thenReturn(roid);

 SettableBeanProperty idProp = idPropertySetter;
 when(idProp.setAndReturn(instance, id)).thenReturn(instance);

 verify(mockJsonParser).nextToken();
 verify(mockJsonParser).getValueAsString();
 verify(mockDeserializationContext).findObjectId(eq(id), _anyObjectIdGenerator(), _anyObjectIdResolver());
 verify(idProp).setAndReturn(instance, id);
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws Exception {
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, instance);

 assertNull(result);
 }

 @Test
 public void testSet() throws Exception {
 Object id = new Object();
 objectIdValueProperty.set(instance, id);

 SettableBeanProperty idProp = idPropertySetter;
 when(idProp.setAndReturn(instance, id)).thenReturn(instance);

 verify(idProp).setAndReturn(instance, id);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object id = new Object();
 Object result = objectIdValueProperty.setAndReturn(instance, id);

 assertNotSame(instance, result);
 assertSame(instance, result);
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = new PropertyName("newName");
 SettableBeanProperty result = objectIdValueProperty.withName(newName);

 assertEquals(newName.getShortName(), result.getName().getShortName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<String> newValueDeserializer = mock(JsonDeserializer.class);
 SettableBeanProperty result = objectIdValueProperty.withValueDeserializer(newValueDeserializer);

 assertEquals(newValueDeserializer, result.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() throws Exception {
 NullValueProvider newNullProvider = mock(NullValueProvider.class);
 SettableBeanProperty result = objectIdValueProperty.withNullProvider(newNullProvider);

 assertEquals(newNullProvider, result.getNullProvider());
 }
}