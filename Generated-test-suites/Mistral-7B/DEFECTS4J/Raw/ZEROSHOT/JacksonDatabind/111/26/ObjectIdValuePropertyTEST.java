package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.PojoIntrospector;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName getPropertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public Class<?> getIdType() {
 return Long.class;
 }

 @Override
 public JsonDeserializer<Object> getIdDeserializer() {
 return new StdDeserializer<>(Long.class) {};
 }

 @Override
 public ObjectIdGenerator getGenerator() {
 return new ObjectIdGenerator() {
 @Override
 public SerializableObjectId generateId(SerializerProvider serializers, BeanProperty property, Object value) {
 return null;
 }
 };
 }

 @Override
 public ObjectIdResolver getResolver() {
 return new ObjectIdResolver() {
 @Override
 public ObjectId idFromString(String id, DeserializationContext ctxt) {
 return new SerializableObjectId(1L);
 }
 };
 }

 @Override
 public String idToString(SerializableObjectId id) {
 return Long.toString(id.getId());
 }
 };

 objectMapper = new ObjectMapper();
 }

 @Test
 public void testObjectIdValuePropertyCreation() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 assertNotNull(objectIdValueProperty);
 assertEquals("id", objectIdValueProperty.getName());
 assertEquals(Long.class, objectIdValueProperty.getType());
 assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));
 assertNull(objectIdValueProperty.getMember());
 assertNull(objectIdValueProperty.getValueDeserializer());
 assertNull(objectIdValueProperty.getNullProvider());
 }

 @Test
 public void testObjectIdValuePropertyWithValueDeserializer() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 JsonDeserializer<String> stringDeserializer = new StdDeserializer<>(String.class) {};
 ObjectIdValueProperty objectIdValuePropertyWithDeserializer = new ObjectIdValueProperty(objectIdReader, stringDeserializer, null);
 assertNotNull(objectIdValuePropertyWithDeserializer);
 assertSame(objectIdValueProperty, objectIdValuePropertyWithDeserializer.getPropertyName());
 assertSame(objectIdValueProperty, objectIdValuePropertyWithDeserializer.getIdType());
 assertSame(stringDeserializer, objectIdValuePropertyWithDeserializer.getValueDeserializer());
 assertNull(objectIdValuePropertyWithDeserializer.getNullProvider());
 }

 @Test
 public void testObjectIdValuePropertyWithNullProvider() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 NullValueProvider nullValueProvider = new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) {
 return null;
 }
 };
 ObjectIdValueProperty objectIdValuePropertyWithNullProvider = new ObjectIdValueProperty(objectIdReader, nullValueProvider);
 assertNotNull(objectIdValuePropertyWithNullProvider);
 assertSame(objectIdValueProperty, objectIdValuePropertyWithNullProvider.getPropertyName());
 assertSame(objectIdValueProperty, objectIdValuePropertyWithNullProvider.getIdType());
 assertNull(objectIdValuePropertyWithNullProvider.getValueDeserializer());
 assertSame(nullValueProvider, objectIdValuePropertyWithNullProvider.getNullProvider());
 }

 @Test
 public void testObjectIdValuePropertyWithNameChange() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 ObjectIdValueProperty objectIdValuePropertyWithName = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"));
 assertNotNull(objectIdValuePropertyWithName);
 assertSame(objectIdReader, objectIdValuePropertyWithName._objectIdReader);
 assertNotSame(objectIdValueProperty, objectIdValuePropertyWithName);
 assertSame("newName", objectIdValuePropertyWithName.getName());
 }

 @Test
 public void testObjectIdValuePropertyDeserializeAndSet() throws Exception {
 Object idInstance = new Object();
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 Object deserializedValue = objectIdValueProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), idInstance);
 assertNotNull(deserializedValue);
 assertEquals(123L, deserializedValue);
 }

 @Test
 public void testObjectIdValuePropertySetAndReturn() throws Exception {
 Object idInstance = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 Object newValue = new Object();
 Object deserializedValue = objectIdValueProperty.setAndReturn(idInstance, newValue);
 assertSame(idInstance, deserializedValue);
 }

 @Test
 public void testObjectIdValuePropertyAnnotation() throws Exception {
 PojoIntrospector pojoIntrospector = new PojoIntrospector(objectMapper.getFactory());
 AnnotationIntrospector annotationIntrospector = pojoIntrospector.getAnnotationIntrospector();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 Object idInstance = new Object();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"id\":123}");
 Object deserializedValue = objectMapper.readValue(jsonParser, idInstance.getClass());
 Annotation annotation = annotationIntrospector.findAnnotation(objectIdValueProperty, JsonProperty.class);
 assertNotNull(annotation);
 assertEquals("id", annotation.value());
 }
}