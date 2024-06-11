package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdIdentifier;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ObjectIdValuePropertyTest {

 private static ObjectIdReader objectIdReader = new BaseObjectIdReader(new PropertyName("id"), ObjectIdIdentifier.class);

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyConstruction() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertTrue(property instanceof ObjectIdValueProperty);
 assertSame(objectIdReader.propertyName, property.getName());
 assertSame(ObjectIdIdentifier.class, property.getIdType());
 assertNull(property.getValueDeserializer());
 assertNull(property.getMember());
 assertNull(property.getDeserializer());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertySerialization() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 StdSerializer<ObjectIdValueProperty> serializer = new StdSerializer<ObjectIdValueProperty>(ObjectIdValueProperty.class) {};
 JsonSerializerProvider serializers = new StandardDeserializationProvisor().findValueSerializer(ObjectIdValueProperty.class, null);
 ObjectWriter writer = new JsonFactory().createGenerator(new ByteArrayOutputStream(), serializers);
 writer.writeValue(property, serializer);
 JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(writer.getOutputBytes()));
 ObjectIdValueProperty deserialized = parser.readValueAs(ObjectIdValueProperty.class);
 assertSame(property, deserialized);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyDeserialization() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonParser jp = new JsonFactory().createParser("{\"id\":123}");
 property.deserializeAndSet(jp, new TestContext(), new Object());
 ObjectIdIdentifier id = new ObjectIdIdentifier(123L);
 assertSame(id, property.getMember().get(new Object()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyDeserializationWithIdProperty() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(new PropertyName("idProperty"), objectIdReader.getIdType(), new PropertyMetadata(), new NoClassDeserializer(Long.class));
 property.setIdProperty(idProperty);
 JsonParser jp = new JsonFactory().createParser("{\"id\":123,\"idProperty\":456}");
 Object obj = new Object();
 property.deserializeAndSet(jp, new TestContext(), obj);
 ObjectIdIdentifier id = new ObjectIdIdentifier(123L);
 assertSame(id, property.getMember().get(obj));
 Long idPropertyValue = (Long) BeanUtil.findValueIgnoreType(obj, "idProperty");
 assertEquals(456L, idPropertyValue.longValue());
 }

 @Test
 public void testObjectIdValuePropertySetter() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object obj = new Object();
 property.set(obj, new ObjectIdIdentifier(123L));
 ObjectIdIdentifier id = new ObjectIdIdentifier(123L);
 assertSame(id, property.getMember().get(obj));
 }

 @Test
 public void testObjectIdValuePropertySetterWithIdProperty() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(new PropertyName("idProperty"), objectIdReader.getIdType(), new PropertyMetadata(), new NoClassDeserializer(Long.class));
 property.setIdProperty(idProperty);
 Object obj = new Object();
 property.set(obj, new ObjectIdIdentifier(123L));
 ObjectIdIdentifier id = new ObjectIdIdentifier(123L);
 assertSame(id, property.getMember().get(obj));
 Long idPropertyValue = (Long) BeanUtil.findValueIgnoreType(obj, "idProperty");
 assertEquals(null, idPropertyValue);
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testObjectIdValuePropertyWithName() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
 assertNotSame(property, newProperty);
 assertEquals("newName", newProperty.getName().getName());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testObjectIdValuePropertyWithValueDeserializer() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonDeserializer<String> deserializer = new StdDeserializer<String>(){};
 ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
 assertNotSame(property, newProperty);
 assertSame(deserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testObjectIdValuePropertyGetAnnotation() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertNull(property.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testObjectIdValuePropertyGetMember() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertNull(property.getMember());
 }
}

class TestContext extends DeserializationContext {
 @Override
 public ObjectMapper getContextualObjectMapper() {
 return null;
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectReader createContextualValueReader(JsonParser p) {
 return null;
 }

 @Override
 public ObjectReader _constructRootValueDeserializer(DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
 return null;
 }
}