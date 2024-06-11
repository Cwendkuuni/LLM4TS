package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerProvider.BeanDeserializerSupplier;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private BeanDeserializerSupplier deserializers;
 private ObjectIdValueProperty property;

 @Before
 public void setUp() throws Exception {
 objectIdReader = mock(ObjectIdReader.class);
 deserializers = mock(BeanDeserializerSupplier.class);
 when(deserializers.findBeanDeserializer(Object.class, null)).thenReturn(null);
 when(deserializers.findBeanDeserializer(String.class, null)).thenReturn(null);
 when(deserializers.findBeanDeserializer(Integer.class, null)).thenReturn(null);

 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testCreate() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, deserializers);
 assertSame(objectIdReader, property._objectIdReader);
 assertNull(property._valueDeserializer);
 assertNull(_nullProvider);
 }

 @Test
 public void testCreateWithValueDeserializer() {
 JsonDeserializer<Integer> intDeserializer = mock(JsonDeserializer.class);
 property = new ObjectIdValueProperty(objectIdReader, intDeserializer, deserializers);
 assertSame(objectIdReader, property._objectIdReader);
 assertSame(intDeserializer, property._valueDeserializer);
 assertNull(_nullProvider);
 }

 @Test
 public void testCreateWithNullProvider() {
 NullValueProvider nullProvider = mock(NullValueProvider.class);
 property = new ObjectIdValueProperty(objectIdReader, deserializers, nullProvider);
 assertSame(objectIdReader, property._objectIdReader);
 assertNull(property._valueDeserializer);
 assertSame(nullProvider, property._nullProvider);
 }

 @Test
 public void testCreateWithName() {
 PropertyName name = mock(PropertyName.class);
 property = new ObjectIdValueProperty(property, name);
 assertSame(property, property._original);
 assertSame(name, property.getName());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(property.getAnnotation(Object.class));
 }

 @Test
 public void testGetMember() {
 assertNull(property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentName()).thenReturn("id");
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("123");

 property.deserializeAndSet(parser, new TestContext(), null);
 Object id = _objectIdReader.getIdType().valueInstance();
 ReadableObjectId roid = mock(ReadableObjectId.class);
 when(roid.bindItem(null)).thenReturn(id);
 when(objectIdReader.generator.idFromString("123")).thenReturn(roid);

 assertSame(id, property.getValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getCurrentName()).thenReturn("id");
 when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.getValueAsString()).thenReturn("123");
 when(parser.hasToken(JsonToken.FIELD_NAME)).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 Object id = _objectIdReader.getIdType().valueInstance();
 ReadableObjectId roid = mock(ReadableObjectId.class);
 when(roid.bindItem(null)).thenReturn(id);
 when(objectIdReader.generator.idFromString("123")).thenReturn(roid);
 when(objectIdReader.idProperty.getName()).thenReturn(new PropertyName("name"));
 when(objectIdReader.idProperty.setValueDeserializer(null)).thenReturn(null);

 Object result = property.deserializeSetAndReturn(parser, new TestContext(), null);
 assertSame(id, result);
 }

 @Test
 public void testSet() throws IOException {
 Object id = _objectIdReader.getIdType().valueInstance();
 property.set(null, id);
 ObjectIdValueProperty property = (ObjectIdValueProperty) property._original;
 assertSame(id, property.getValue());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = _objectIdReader.getIdType().valueInstance();
 Object result = property.setAndReturn(null, id);
 assertSame(id, result);
 }
}

class TestContext extends DeserializationContext {
 @Override
 public ObjectIdReader.ObjectIdGenerator idGenerator() {
 return mock(ObjectIdGenerator.class);
 }

 @Override
 public ObjectIdReader.ObjectIdResolver objectIdResolver() {
 return mock(ObjectIdResolver.class);
 }

 @Override
 public Object readValue(Class<?> c, JsonParser p) throws IOException {
 return null;
 }

 @Override
 public void findValueDeserializer(Class<?> valueType, JsonDeserializer<?> vd,
 JsonDeserializer<?> contextValueDeserializer, BeanDeserializerSupplier deserializers)
 throws IOException {
 // empty
 }

 @Override
 public Object findValueInstantiator(Class<?> valueType, BeanDeserializerSupplier deserializers)
 throws IOException {
 return null;
 }

 @Override
 public JsonDeserializer<?> findBeanDeserializer(Class<?> beanType,
 JsonDeserializer.None nullValueHandler) throws IOException {
 return null;
 }

 @Override
 public void findBeanDeserializer(Class<?> beanType, JsonDeserializer<?> vd,
 PropertyName propertyName, BeanDeserializerSupplier beanDeserializers)
 throws IOException {
 // empty
 }

 @Override
 public BeanDescription findBeanDescription(Class<?> beanType) throws IOException {
 return null;
 }

 @Override
 public void findTypeDeserializer(TypeDeserializer td, BeanDescription beanDesc,
 DeserializationConfig config) {
 // empty
 }

 @Override
 public Object findInitialValue(AnnotatedMember am, Object valueInstantiator)
 throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(Class<?> valueType, JsonParser p,
 DeserializationContext ctxt) throws IOException {
 return null;
 }

 @Override
 public void findValueWithType(Class<?> valueType, JsonParser p, DeserializationContext ctxt,
 BeanDeserializerSupplier beanDeserializers) throws IOException {
 // empty
 }

 @Override
 public Object findValueWithType(Class<?> valueType, JsonParser p,
 DeserializationContext ctxt, BeanDescription beanDesc) throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(Class<?> valueType, JsonParser p,
 DeserializationContext ctxt, JsonDeserializer<?> deserializer) throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(Class<?> valueType, JsonParser p,
 DeserializationContext ctxt, JsonDeserializer<?> deserializer,
 JsonDeserializer.None nullProvider) throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(Class<?> valueType, JsonParser p,
 DeserializationContext ctxt, JsonDeserializer<?> deserializer,
 BeanDescription beanDesc) throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(JsonParser p, DeserializationContext ctxt,
 JsonDeserializer<?> deserializer) throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(JsonParser p, DeserializationContext ctxt,
 JsonDeserializer<?> deserializer, JsonDeserializer<?> contextValueDeserializer)
 throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(Class<?> valueType, JsonParser p,
 DeserializationContext ctxt, NameTransformer nameTransformer) throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(JsonParser p, DeserializationContext ctxt,
 JsonDeserializer<?> deserializer, NameTransformer nameTransformer) throws IOException {
 return null;
 }

 @Override
 public Object findValueWithType(Class<?> valueType, JsonParser p,
 DeserializationContext ctxt, BeanDescription beanDesc, Object initialValue)
 throws IOException {
 return null;
 }
}