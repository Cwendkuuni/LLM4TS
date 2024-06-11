package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty property;
 private JsonDeserializer<String> stringDeserializer;
 private ObjectMapper objectMapper;
 private DeserializationConfig config;

 @Before
 public void setup() {
 stringDeserializer = mock(JsonDeserializer.class);
 objectMapper = new ObjectMapper();
 config = objectMapper.getDeserializationConfig();
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 property = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(null, stringDeserializer), objectIdInfo, null);
 }

 @Test
 public void testSetAndReturn() throws JsonProcessingException {
 Object pojo = new Object();
 String value = "testValue";
 when(stringDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(value);
 Object result = property.deserializeSetAndReturn(mock(JsonParser.class), config, pojo);
 assertEquals(value, property.get(pojo));
 assertEquals(null, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnresolvedForwardReferenceWithNoIdentityInfo() throws JsonProcessingException {
 Object pojo = new Object();
 when(stringDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(new Object());
 property.deserializeSetAndReturn(mock(JsonParser.class), config, pojo);
 }

 @Test
 public void testPropertyReferring() throws JsonProcessingException {
 Object pojo = new Object();
 ObjectIdReferenceProperty property1 = new ObjectIdReferenceProperty(null, stringDeserializer, null);
 ObjectIdReferenceProperty property2 = new ObjectIdReferenceProperty(property1, null);
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 Object id = new Object();
 when(stringDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(id);
 when(property1.getObjectIdInfo()).thenReturn(objectIdInfo);
 property1.deserializeSetAndReturn(mock(JsonParser.class), config, pojo);
 property2.deserializeSetAndReturn(mock(JsonParser.class), config, pojo);
 property2.handleResolvedForwardReference(id, new Object());
 assertEquals(id, property1.get(pojo));
 }

 @Test
 public void testWithName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdReferenceProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Integer> integerDeserializer = mock(JsonDeserializer.class);
 ObjectIdReferenceProperty newProperty = property.withValueDeserializer(integerDeserializer);
 assertEquals(integerDeserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 ObjectIdReferenceProperty newProperty = property.withNullProvider(nullValueProvider);
 assertEquals(nullValueProvider, newProperty.getNullProvider());
 }

 @Test
 public void testFixAccess() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 property.fixAccess(config);
 verify(config).findBeanProperty(property.getName());
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = mock(Annotation.class);
 when(property.getMember().getAnnotation(Annotation.class)).thenReturn(annotation);
 assertEquals(annotation, property.getAnnotation(annotation.getClass()));
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(property.getMember()).thenReturn(member);
 assertEquals(member, property.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(-1, property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 Object pojo = new Object();
 String value = "testValue";
when(stringDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(value);
 property.deserializeAndSet(mock(JsonParser.class), config, pojo);
 assertEquals(value, property.get(pojo));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws JsonProcessingException {
 Object pojo = new Object();
 ObjectNode node = ObjectNode.wrap(new Object());
 property.deserializeSetAndReturn(mock(JsonParser.class), config, pojo);
 assertNull(property.get(pojo));
 }
}