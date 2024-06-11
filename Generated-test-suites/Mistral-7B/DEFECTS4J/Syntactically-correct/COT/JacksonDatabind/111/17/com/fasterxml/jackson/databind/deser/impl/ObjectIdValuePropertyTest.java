package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdIdentifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Type;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private JsonDeserializer<?> jsonDeserializer;
 private Object id;
 private NullValueProvider nullValueProvider;
 private SettableBeanProperty idProperty;
 private Object instance;
 private ObjectIdIdentifier objectIdIdentifier;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 objectIdReader = mock(ObjectIdReader.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 jsonDeserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 id = Mockito.mock(Object.class);
 idProperty = mock(SettableBeanProperty.class);
 instance = Mockito.mock(Object.class);
 objectIdIdentifier = Mockito.mock(ObjectIdIdentifier.class);

 when(objectIdReader.propertyName).thenReturn(new PropertyName("testProperty"));
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.getDeserializer()).thenReturn(jsonDeserializer);
 when(objectIdReader.generator).thenReturn(Mockito.mock(ObjectIdGenerator.class));
 when(objectIdReader.resolver).thenReturn(Mockito.mock(ObjectIdResolver.class));
 when(objectIdReader.idProperty).thenReturn(idProperty);
 }

 @Test
 public void testConstructor() {
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 assertSame(objectIdReader.propertyName, objectIdValueProperty.getName());
 assertSame(objectIdReader.getIdType(), objectIdValueProperty.getType());
 assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
 assertNull(objectIdValueProperty._valueDeserializer);
 assertNull(objectIdValueProperty._nullProvider);
 }

 @Test
 public void testConstructorWithValueDeserializer() {
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, jsonDeserializer, nullValueProvider);
 assertSame(objectIdReader.propertyName, objectIdValueProperty.getName());
 assertSame(objectIdReader.getIdType(), objectIdValueProperty.getType());
 assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
 assertSame(jsonDeserializer, objectIdValueProperty._valueDeserializer);
 assertSame(nullValueProvider, objectIdValueProperty._nullProvider);
 }

 @Test
 public void testConstructorWithNewName() {
 objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"));
 assertSame(new PropertyName("newName"), objectIdValueProperty.getName());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Mockito.any(Annotation.class).getClass()));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 when(jsonParser.getCurrentName()).thenReturn("testId");
 when(jsonParser.getValueAsString()).thenReturn("testValue");

 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals("testId", objectIdValueProperty.getName().getShortName());
 assertSame(id, objectIdValueProperty.getId());
 assertSame(instance, objectIdValueProperty.getValueId());

 // test with null id
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertNull(objectIdValueProperty.getId());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 when(jsonParser.getCurrentName()).thenReturn("testId");
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertEquals(instance, result);
 assertSame(id, objectIdValueProperty.getId());
 assertSame(instance, objectIdValueProperty.getValueId());

 // test with null id
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 Object resultNull = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertEquals(instance, resultNull);
 assertNull(objectIdValueProperty.getId());
 }

 @Test
 public void testSet() throws IOException {
 when(jsonParser.getCurrentName()).thenReturn("testId");
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

 objectIdValueProperty.set(instance, id);
 assertSame(instance, objectIdValueProperty.getValueId());
 assertSame(id, objectIdValueProperty.getId());
 }

 @Test
 public void testSetNull() throws IOException {
 when(jsonParser.getCurrentName()).thenReturn("testId");
 when(idProperty.setAndReturn(instance, null)).thenReturn(instance);

 objectIdValueProperty.set(instance, null);
 assertSame(instance, objectIdValueProperty.getValueId());
 assertNull(objectIdValueProperty.getId());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNoIdProperty() throws IOException {
 when(objectIdReader.idProperty).thenReturn(null);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, jsonDeserializer, nullValueProvider);
 assertThrows(UnsupportedOperationException.class, () -> objectIdValueProperty.set(instance, id));
 }

 @Test
 public void testWithName() {
 SettableBeanProperty result = objectIdValueProperty.withName(new PropertyName("newName"));
 assertSame(new PropertyName("newName"), result.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 SettableBeanProperty result = objectIdValueProperty.withValueDeserializer(jsonDeserializer);
 assertSame(jsonDeserializer, result._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 SettableBeanProperty result = objectIdValueProperty.withNullProvider(nullValueProvider);
 assertSame(nullValueProvider, result._nullProvider);
 }
}