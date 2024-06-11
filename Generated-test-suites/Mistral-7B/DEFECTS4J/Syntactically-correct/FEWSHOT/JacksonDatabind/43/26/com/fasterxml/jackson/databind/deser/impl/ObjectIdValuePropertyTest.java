package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockedObjectIdReader;
 private ObjectMapper mockedObjectMapper;
 private SettableBeanProperty mockedIdProperty;

 @Before
 public void setup() {
 mockedObjectIdReader = mock(ObjectIdReader.class);
 mockedObjectMapper = mock(ObjectMapper.class);
 mockedIdProperty = mock(SettableBeanProperty.class);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser mockedParser = mock(JsonParser.class);
 DeserializationContext mockedContext = mock(DeserializationContext.class);
 Object instance = new Object();
 Object id = "123";

 when(mockedParser.getValueAsToken()).thenReturn(JsonParser.ValueTokens.STRING);
 when(mockedObjectMapper.readTree(mockedParser)).thenReturn(mock(ObjectMapper.TreeNode.class));

 ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 return mockedIdProperty;
 }
 };

 property.deserializeAndSet(mockedParser, mockedContext, instance);

 verify(mockedIdProperty).setAndReturn(instance, id);
 }

 @Test
 public void testDeserializeSetAndReturnNullId() throws Exception {
 JsonParser mockedParser = mock(JsonParser.class);
 DeserializationContext mockedContext = mock(DeserializationContext.class);
 Object instance = new Object();

 when(mockedParser.getValueAsToken()).thenReturn(JsonParser.ValueTokens.NULL);

 ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };

 Object result = property.deserializeSetAndReturn(mockedParser, mockedContext, instance);

 assertNull(result);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 JsonParser mockedParser = mock(JsonParser.class);
 DeserializationContext mockedContext = mock(DeserializationContext.class);
 Object instance = new Object();
 Object id = "123";

 when(mockedParser.getValueAsToken()).thenReturn(JsonParser.ValueTokens.STRING);
 when(mockedObjectMapper.readTree(mockedParser)).thenReturn(mock(ObjectMapper.TreeNode.class));

 ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 return mockedIdProperty;
 }
 };

 Object result = property.deserializeSetAndReturn(mockedParser, mockedContext, instance);

 assertEquals(instance, result);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 Object id = "123";

 when(mockedIdProperty.setAndReturn(instance, id)).thenReturn(instance);

 ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 return mockedIdProperty;
 }
 };

 Object result = property.setAndReturn(instance, id);

 assertEquals(instance, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 Object instance = new Object();
 Object id = "123";

 ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
 @Override
 public SettableBeanProperty idProperty() {
 throw new UnsupportedOperationException();
 }
 };

 property.set(instance, id);
 }

 @Test
 public void testGetAnnotation() {
 Class<Annotation> annotationType = mock(Annotation.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
 @Override
 public <A extends Annotation> A getAnnotation(Class<A> acls) {
 return null;
 }
 };

 assertNull(property.getAnnotation(annotationType));
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = mock(AnnotatedMember.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
 @Override
 public AnnotatedMember getMember() {
 return member;
 }
 };

 assertSame(member, property.getMember());
 }

 @Test
 public void testCreateCopy() throws Exception {
 ObjectIdReader mockedSourceObjectIdReader = mock(ObjectIdReader.class);
 ObjectIdValueProperty sourceProperty = new ObjectIdValueProperty(mockedSourceObjectIdReader, new PropertyMetadata());

 ObjectIdValueProperty copiedProperty = new ObjectIdValueProperty(sourceProperty, new JsonDeserializer.None());

 assertEquals(sourceProperty.getName(), copiedProperty.getName());
 assertEquals(sourceProperty.getIdType(), copiedProperty.getIdType());
 assertEquals(sourceProperty.getMetadata(), copiedProperty.getMetadata());
 assertEquals(sourceProperty._objectIdReader, copiedProperty._objectIdReader);
 }

 @Test
 public void testCreateCopyWithName() throws Exception {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty sourceProperty = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata());

 ObjectIdValueProperty copiedProperty = new ObjectIdValueProperty(sourceProperty, new JsonDeserializer.None(), newName);

 assertEquals(newName, copiedProperty.getName());
 assertEquals(sourceProperty.getIdType(), copiedProperty.getIdType());
 assertEquals(sourceProperty.getMetadata(), copiedProperty.getMetadata());
 assertEquals(sourceProperty._objectIdReader, copiedProperty._objectIdReader);
 }

 @Test
 public void testCreateCopyWithDeserializer() throws Exception {
 JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
 ObjectIdValueProperty sourceProperty = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata(), deserializer);

 ObjectIdValueProperty copiedProperty = new ObjectIdValueProperty(sourceProperty, new JsonDeserializer.None());

 assertEquals(deserializer, copiedProperty.getDeserializer());
 assertEquals(sourceProperty.getIdType(), copiedProperty.getIdType());
 assertEquals(sourceProperty.getMetadata(), copiedProperty.getMetadata());
 assertEquals(sourceProperty._objectIdReader, copiedProperty._objectIdReader);
 }

 @Test
 public void testCreateCopyWithIdProperty() throws Exception {
 SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
 ObjectIdValueProperty sourceProperty = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata(), idProperty);

 ObjectIdValueProperty copiedProperty = new ObjectIdValueProperty(sourceProperty, new JsonDeserializer.None());

 assertEquals(idProperty, copiedProperty.getIdProperty());
 assertEquals(sourceProperty.getIdType(), copiedProperty.getIdType());
 assertEquals(sourceProperty.getMetadata(), copiedProperty.getMetadata());
 assertEquals(sourceProperty._objectIdReader, copiedProperty._objectIdReader);
 }

}