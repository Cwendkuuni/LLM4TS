package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ObjectID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

 private ObjectIdValueProperty property;
 private ObjectMapper objectMapper;
 private DeserializationContext context;
 private JsonParser parser;
private Object reader;
 private ObjectReader objectReader;

 @Before
 public void setUp() throws Exception {
 ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
 PropertyName propertyName = new PropertyName("id");
 JsonDeserializer<ObjectID> idDeserializer = mock(JsonDeserializer.class);
 TypeDeserializer idTypeDeserializer = mock(TypeDeserializer.class);
 when(objectIdReader.propertyName).thenReturn(propertyName);
 when(objectIdReader.getIdType()).thenReturn(ObjectID.class);
 when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);
 when(objectIdReader.getIdProperty()).thenReturn(null);

 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 objectMapper = new ObjectMapper();
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 reader = new Object() {};
 objectReader = objectMapper.readerForTemplate(new TypeReference<Object>() {});
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectID id = new ObjectID(1L);
 when(parser.getValueAsToken()).thenReturn(objectMapper.valueToTree(id));
 when(idDeserializer.deserialize(parser, context)).thenReturn(id);
 when(context.findObjectId(id, mock(ObjectIdGenerator.class), mock(ObjectIdResolver.class))).thenReturn(id);

 property.deserializeAndSet(parser, context, reader);
 ObjectIdValueProperty testProperty = (ObjectIdValueProperty) property;
 assertSame(id, testProperty.getValueId());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectID id = new ObjectID(1L);
 when(parser.getValueAsToken()).thenReturn(objectMapper.valueToTree(id));
 when(idDeserializer.deserialize(parser, context)).thenReturn(id);
 when(context.findObjectId(id, mock(ObjectIdGenerator.class), mock(ObjectIdResolver.class))).thenReturn(id);
 Object target = new Object();
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(property, idDeserializer);
 when(testProperty.getIdProperty()).thenReturn(new ObjectIdValueProperty(new PropertyName("targetId"), mock(ObjectIdReader.class), new PropertyMetadata()));
 ObjectIdValueProperty idProperty = (ObjectIdValueProperty) testProperty.getIdProperty();
 when(idProperty.setAndReturn(target, id)).thenReturn(target);

 Object result = testProperty.deserializeSetAndReturn(parser, context, target);
 assertSame(target, result);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 ObjectID id = new ObjectID(1L);
 Object target = new Object();
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(property, idDeserializer);
 when(testProperty.getIdProperty()).thenReturn(idProperty);
 ObjectIdValueProperty idProperty = (ObjectIdValueProperty) testProperty.getIdProperty();
 when(idProperty.setAndReturn(target, id)).thenReturn(target);

 Object result = testProperty.setAndReturn(target, id);
 assertSame(target, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 Object target = new Object();
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(property, idDeserializer);
 Object result = testProperty.setAndReturn(target, null);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 BeanDescription beanDescription = new BeanDescription(Object.class);
 AnnotationIntrospector introspector = new AnnotationIntrospector();
 Annotation annotation = introspector.findAnnotation(beanDescription, Object.class, Id.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(new PropertyName("id"), ObjectID.class, null), new PropertyMetadata());
 assertNull(property.getAnnotation(Id.class));
 }

 @Test
 public void testGetMember() throws Exception {
 BeanDescription beanDescription = new BeanDescription(Object.class);
 AnnotationIntrospector introspector = new AnnotationIntrospector();
 AnnotatedMember member = introspector.findMember(beanDescription, Object.class, "id");
 ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(new PropertyName("id"), ObjectID.class, null), new PropertyMetadata());
 assertNull(property.getMember());
 }
}