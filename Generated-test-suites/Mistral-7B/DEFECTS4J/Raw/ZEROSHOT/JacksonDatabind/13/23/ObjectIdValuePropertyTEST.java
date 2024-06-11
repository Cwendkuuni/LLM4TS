package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonId;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector.FindValueType;
import com.fasterxml.jackson.databind.json.JsonParserBasedDeserializer.StdDeserializer;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonParser jsonParserMock;
 private DeserializationContext deserializationContextMock;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return p.getValueAsString();
 }
 };
 objectIdReader = Mockito.spy(new ObjectIdReader("id", IdType.LONG, stringDeserializer, null, null, null));
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 jsonParserMock = mock(JsonParser.class);
 deserializationContextMock = mock(DeserializationContext.class);
 }

 @Test
 public void testConstructor() throws Exception {
 assertNotNull(objectIdValueProperty);
 assertEquals("id", objectIdValueProperty.getName());
 assertSame(IdType.LONG, objectIdValueProperty.getIdType());
 assertNull(objectIdValueProperty.getMetadata());
 assertNull(objectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 AnnotatedClass ac = Mockito.mock(AnnotatedClass.class);
 JsonId jsonIdAnnotation = Mockito.mock(JsonId.class);
 when(ac.getAnnotation(JsonId.class)).thenReturn(jsonIdAnnotation);
 when(objectIdValueProperty.getAnnotatedMember()).thenReturn(ac);
 assertEquals(jsonIdAnnotation, objectIdValueProperty.getAnnotation(JsonId.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullInstance() throws Exception {
 objectIdValueProperty.set(null, "id");
 }

 @Test
 public void testSetAndReturnWithInstance() throws Exception {
 Object instance = new Object();
 when(deserializationContextMock.findObjectId(Mockito.any(Long.class), Mockito.any(ObjectIdGenerator.class),
 Mockito.any(ObjectIdResolver.class))).thenReturn(Mockito.mock(ReadableObjectId.class));
 Object returnValue = new Object();
 when(objectIdValueProperty.getObjectIdReader().getIdProperty()).thenReturn(new ObjectIdValuePropertyTest.TestSettableBeanProperty());
 when(objectIdValueProperty.getObjectIdReader().getIdProperty().setAndReturn(instance, "id")).thenReturn(returnValue);
 objectIdValueProperty.set(instance, "id");
 assertEquals(returnValue, objectIdValueProperty.getObjectIdReader().getIdProperty().get(instance));
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String id = "123";
 when(jsonParserMock.getValueAsString()).thenReturn(id);
 when(deserializationContextMock.findObjectId(Mockito.eq(id), Mockito.any(ObjectIdGenerator.class),
 Mockito.any(ObjectIdResolver.class))).thenReturn(Mockito.mock(ReadableObjectId.class));
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instance);
 verify(objectIdValueProperty, times(1)).setAndReturn(instance, id);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Long id = 123L;
 Object instance = new Object();
 when(deserializationContextMock.findObjectId(Mockito.eq(id), Mockito.any(ObjectIdGenerator.class),
 Mockito.any(ObjectIdResolver.class))).thenReturn(Mockito.mock(ReadableObjectId.class));
 Object returnValue = new Object();
 when(objectIdValueProperty.getObjectIdReader().getIdProperty()).thenReturn(new ObjectIdValuePropertyTest.TestSettableBeanProperty());
 when(objectIdValueProperty.getObjectIdReader().getIdProperty().setAndReturn(instance, "id")).thenReturn(returnValue);
 Object deserializedValue = objectIdValueProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instance);
 assertEquals(returnValue, deserializedValue);
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newName"));
 assertEquals("newName", newObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(new None());
 assertNotNull(newObjectIdValueProperty.getValueDeserializer());
 assertSame(new None(), newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testFindValueType() throws Exception {
 AnnotatedClass ac = Mockito.mock(AnnotatedClass.class);
 when(ac.getRawValueType()).thenReturn(new TypeReference<Number>() {}.getType());
 when(objectIdValueProperty.getAnnotatedMember()).thenReturn(ac);
 FindValueType findValueType = objectIdValueProperty.findValueType();
 assertEquals(Number.class, findValueType.getValueClass());
 }

}

class TestSettableBeanProperty extends SettableBeanProperty {
 @Override
 public Object get(Object instance) {
 return null;
 }

 @Override
 public void set(Object instance, Object value) throws IOException {
 }
}