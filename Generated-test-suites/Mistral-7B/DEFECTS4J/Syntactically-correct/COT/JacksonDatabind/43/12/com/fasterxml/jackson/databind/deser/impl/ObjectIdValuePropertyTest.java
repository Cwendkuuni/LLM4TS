package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdReader objectIdReader;
 private PropertyName propertyName;
 private ObjectIdProperty idProperty;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> valueDeserializer;
 private DeserializationContext deserializationContext;
 private TypeDeserializer typeDeserializer;
 private JsonParser jsonParser;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectIdReader = mock(ObjectIdReader.class);
 propertyName = mock(PropertyName.class);
 idProperty = mock(BeanProperty.class);
 valueDeserializer = mock(JsonDeserializer.class);
 deserializationContext = mock(DeserializationContext.class);
 typeDeserializer = mock(TypeDeserializer.class);
 jsonParser = mock(JsonParser.class);

 when(objectIdReader.propertyName).thenReturn(propertyName);
 when(objectIdReader.getIdType()).thenReturn(Object.class);
 when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);
 when(objectIdReader.idProperty).thenReturn(idProperty);
 when(objectMapper.getTypeFactory().constructType(new TypeReference<Object>() {})).thenReturn(Object.class);
 }

 @Test
 public void testObjectIdValuePropertyConstruction() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, null);
 assertNotNull(obj);
 }

 @Test
 public void testObjectIdValuePropertyWithName() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, new PropertyName("test"));
 assertNotNull(obj);
 assertNotNull(obj.getName());
 assertSame(propertyName, obj.getName());
 }

 @Test
 public void testObjectIdValuePropertyWithValueDeserializer() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, valueDeserializer);
 assertNotNull(obj);
 assertNotNull(obj.getValueDeserializer());
 assertSame(valueDeserializer, obj.getValueDeserializer());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testObjectIdValuePropertyWithoutIdProperty() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, null);
 obj.set(ObjectTestClass.class, null);
 }

 @Test
 public void testGetAnnotation() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, null);
 assertNull(obj.getAnnotation(JsonId.class));
 }

 @Test
 public void testGetMember() {
 ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, null);
 assertNull(obj.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = new Object();
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
 when(idProperty.setAndReturn(ObjectTestClass.class, id)).thenReturn(ObjectTestClass.class);

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, ObjectTestClass.class);

 assertNotNull(ObjectTestClass.class.getDeclaredField("id"));
 assertNotNull(ObjectTestClass.class.getDeclaredField("id").get(ObjectTestClass.getInstance()));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(null);
 Object id = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, ObjectTestClass.class);
 assertNull(id);
 }

 @Test
 public void testDeserializeSetAndReturnNonNull() throws IOException {
 Object id = new Object();
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, ObjectTestClass.class);
 assertNotNull(result);
 }

 @Test
 public void testSet() throws IOException {
 Object id = new Object();
 objectIdValueProperty.set(ObjectTestClass.class, id);
 assertNotNull(ObjectTestClass.class.getDeclaredField("id").get(ObjectTestClass.getInstance()));
 }

 private static class ObjectIdReader {
 @JsonId
 public static PropertyName propertyName = new PropertyName("test");
 public static Class<? extends IdGenerator> idType = IdGenerator.class;
 public static JsonDeserializer<Object> getDeserializer() { return null; }
 public static BeanProperty idProperty = null;
 }

 private static class ObjectTestClass {
 public static PropertyName propertyName = new PropertyName("test");
 private Object id;

 @JsonProperty("id")
 public Object getId() { return id; }

 public static Object getInstance() {
 Object obj = new ObjectTestClass();
 obj.id = "testId";
 return obj;
 }
 }

}