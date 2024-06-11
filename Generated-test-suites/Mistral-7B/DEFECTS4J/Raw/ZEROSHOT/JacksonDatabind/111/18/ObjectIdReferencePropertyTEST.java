package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.databind.JsonMapping.BuildableObjectId;
import static com.fasterxml.jackson.databind.JsonMapping.IdInfoForValue;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private ObjectIdReferenceProperty property;
 private JsonParser jsonParser;
 private DeserializationContext context;
 private Object instance;

 @Before
 public void setup() {
 objectIdInfo = mock(ObjectIdInfo.class);
 when(objectIdInfo.getObjectIdGenerator()).thenReturn(ObjectIdGenerators.ObjectIdGenerators.IntIdentifierGenerator.instance());
 instance = new Object();
 jsonParser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 property = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), objectIdInfo);
 }

 @Test
 public void testEmptyConstructor() {
 assertNull(property.getForwardProperty());
 assertNull(property.getObjectIdInfo());
 }

 @Test
 public void testConstructionWithForwardProperty() {
 SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
 property = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 assertEquals(forwardProperty, property.getForwardProperty());
 assertEquals(objectIdInfo, property.getObjectIdInfo());
 }

 @Test
 public void testConstructionWithDeserializer() {
 JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
 property = new ObjectIdReferenceProperty(property, deserializer, new NullValueProvider());
 assertEquals(property, property.getForwardProperty());
 assertEquals(objectIdInfo, property.getObjectIdInfo());
 assertEquals(deserializer, property.getValueDeserializer());
 }

 @Test
 public void testConstructionWithNewName() {
 PropertyName newName = PropertyName.construct("testProperty");
 property = new ObjectIdReferenceProperty(property, newName);
 assertEquals(property, property.getForwardProperty());
 assertEquals(objectIdInfo, property.getObjectIdInfo());
 assertEquals(newName, property.getName());
 }

 @Test
 public void testFixAccess() {
 property.fixAccess(context);
 verify(property.getForwardProperty()).fixAccess(context);
 }

 @Test
 public void testGetAnnotation() {
 JsonIdentityInfo jsonIdentityInfo = new JsonIdentityInfo(ObjectIdGenerators.ObjectIdGenerators.IntIdentifierGenerator.instance());
 when(objectIdInfo.getIdProperty()).thenReturn(property);
 JsonIdentityAnnotation jsonIdentityAnnotation = new JsonIdentityAnnotation();
 when(jsonIdentityAnnotation.annotationType()).thenReturn(JsonIdentityInfo.class);
 when(property.getAnnotation(JsonIdentityInfo.class)).thenReturn(jsonIdentityInfo);
 when(jsonIdentityInfo.getIdentifierType()).thenReturn(JsonIdentityAnnotation.class);
 ObjectIdInfo expectedObjectIdInfo = IdInfoForValue(jsonIdentityAnnotation);
 assertEquals(expectedObjectIdInfo, property.getObjectIdInfo());
 }

 @Test
 public void testGetMember() {
 AnnotatedMember expectedMember = mock(AnnotatedMember.class);
 when(property.getForwardProperty().getMember()).thenReturn(expectedMember);
 assertEquals(expectedMember, property.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 when(property.getForwardProperty().getCreatorIndex()).thenReturn(1);
 assertEquals(1, property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = ObjectNode.class.getDeclaredField("id").get(new ObjectNode());
 Object idValue = new Object();
 when(objectIdInfo.getObjectIdGenerator()).thenReturn(BuildableObjectId.instance());
 when(jsonParser.getValueAsToken()).thenReturn(ContextualSerializationTest.createTestObjectIdNode(id.getName(), idValue));
 when(jsonParser.readValueAs(Object.class)).thenReturn(idValue);
 property.deserializeAndSet(jsonParser, context, instance);
 assertEquals(idValue, property.get(instance));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 Object id = ObjectNode.class.getDeclaredField("id").get(new ObjectNode());
 Object idValue = new Object();
 when(objectIdInfo.getObjectIdGenerator()).thenReturn(BuildableObjectId.instance());
 when(jsonParser.getValueAsToken()).thenReturn(ContextualSerializationTest.createTestObjectIdNode(id.getName(), idValue));
 when(jsonParser.readValueAs(Object.class)).thenReturn(null);
 assertNull(property.deserializeSetAndReturn(jsonParser, context, instance));
 }

 @Test
 public void testDeserializeSetAndReturnNonnull() throws IOException {
 Object id = ObjectNode.class.getDeclaredField("id").get(new ObjectNode());
 Object idValue = new Object();
 Object newValue = new Object();
 when(objectIdInfo.getObjectIdGenerator()).thenReturn(BuildableObjectId.instance());
 when(jsonParser.getValueAsToken()).thenReturn(ContextualSerializationTest.createTestObjectIdNode(id.getName(), idValue));
 when(jsonParser.readValueAs(Object.class)).thenReturn(newValue);
 assertEquals(newValue, property.deserializeSetAndReturn(jsonParser, context, instance));
 }

 @Test
 public void testSet() throws IOException {
 Object id = ObjectNode.class.getDeclaredField("id").get(new ObjectNode());
 Object idValue = new Object();
 when(objectIdInfo.getObjectIdGenerator()).thenReturn(BuildableObjectId.instance());
 when(jsonParser.getValueAsToken()).thenReturn(ContextualSerializationTest.createTestObjectIdNode(id.getName(), idValue));
 when(jsonParser.readValueAs(Object.class)).thenReturn(idValue);
 property.set(instance, idValue);
 assertEquals(idValue, property.get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = ObjectNode.class.getDeclaredField("id").get(new ObjectNode());
 Object idValue = new Object();
 Object newValue = new Object();
 when(objectIdInfo.getObjectIdGenerator()).thenReturn(BuildableObjectId.instance());
 when(jsonParser.getValueAsToken()).thenReturn(ContextualSerializationTest.createTestObjectIdNode(id.getName(), idValue));
 when(jsonParser.readValueAs(Object.class)).thenReturn(newValue);
 assertEquals(newValue, property.setAndReturn(instance, newValue));
 }

 @Test
 public void testResolveForwardReference() throws IOException {
 UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
 PropertyReferring referencing = new ObjectIdReferenceProperty.PropertyReferring(property, reference, Object.class, instance);
 Object id = ObjectNode.class.getDeclaredField("id").get(new ObjectNode());
 Object idValue = new Object();
 Object newValue = new Object();
 when(objectIdInfo.getObjectIdGenerator()).thenReturn(BuildableObjectId.instance());
 when(jsonParser.getValueAsToken()).thenReturn(ContextualSerializationTest.createTestObjectIdNode(id.getName(), idValue));
 when(jsonParser.readValueAs(Object.class)).thenReturn(newValue);
 property.deserializeAndSet(jsonParser, context, instance);
 referencing.handleResolvedForwardReference(idValue, newValue);
 assertEquals(newValue, property.get(instance));
 }
}