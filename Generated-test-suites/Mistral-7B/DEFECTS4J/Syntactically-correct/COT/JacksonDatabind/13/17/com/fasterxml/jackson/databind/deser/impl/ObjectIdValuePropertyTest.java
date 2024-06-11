package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.databind.JsonTestUtil.readValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<?> jsonDeserializer;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object id;

 @Before
 public void setUp() {
 objectIdReader = mock(ObjectIdReader.class);
 jsonDeserializer = mock(JsonDeserializer.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 id = new ObjectIdNode(1L).asText();
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
 assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
 assertNull(objectIdValueProperty.getMember());
 assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
 assertNull(objectIdValueProperty._objectIdReader);
 }

 @Test
 public void testDeprecatedConstructor() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, jsonDeserializer);
 assertEquals(objectIdValueProperty.getName(), objectIdValueProperty._objectIdReader.propertyName);
 assertEquals(objectIdValueProperty.getType(), objectIdReader.getIdType());
 assertNull(objectIdValueProperty.getMember());
 assertEquals(jsonDeserializer, objectIdValueProperty.getValueDeserializer());
 assertEquals(objectIdValueProperty, objectIdValueProperty._objectIdReader);
 }

 @Test
 public void testGetAnnotation() {
 assertEquals(null, objectIdValueProperty.getAnnotation(Object.class));
 }

 @Test
 public void testGetMember() {
 assertEquals(null, objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
 when(deserializationContext.findObjectId(eq(id), any(), any())).thenReturn(mock(ReadableObjectId.class));

 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, new Object());
 verify(objectIdValueProperty._objectIdReader.idProperty, times(1)).bindItem(any());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
 when(deserializationContext.findObjectId(eq(id), any(), any())).thenReturn(mock(ReadableObjectId.class));

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, new Object());
 assertEquals(result, new Object());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 Object instance = new Object();
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 objectIdValueProperty.set(instance, null);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 SettableBeanProperty idProp = mock(SettableBeanProperty.class);
 when(objectIdReader.idProperty).thenReturn(idProp);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 when(idProp.setAndReturn(instance, id)).thenReturn(instance);
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(result, instance);
 }
}