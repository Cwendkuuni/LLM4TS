package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class ObjectIdValuePropertyTest extends TestBase {

 private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public Class<Object> getIdType() {
 return Object.class;
 }

 @JsonProperty("id")
 public ObjectIdReader idProperty;

 @JsonProperty("generator")
 public ObjectIdGenerator idGenerator = new StdSerializer<ObjectIdGenerator>(ObjectIdGenerator.class) {
 @Override
 public void serialize(ObjectIdGenerator obj, JsonGenerator jg, SerializerProvider sp) throws IOException {
 jg.writeString("generatedId");
 }
 };

 @JsonProperty("resolver")
 public ObjectIdResolver idResolver = new StdSerializer<ObjectIdResolver>(ObjectIdResolver.class) {
 @Override
 public void serialize(ObjectIdResolver obj, JsonGenerator jg, SerializerProvider sp) throws IOException {
 jg.writeString("idResolver");
 }
 };
 };

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyConstructor() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertSame(objectIdReader.propertyName, property.getName());
 assertSame(Object.class, property.getType());
 assertSame(objectIdReader, property._objectIdReader);
 assertNull(property.getAnnotation(JsonProperty.class));
 assertNull(property.getMember());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyCopyConstructor() throws IOException {
 ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty dest = new ObjectIdValueProperty(src, getClass());
 assertSame(src.getName(), dest.getName());
 assertSame(src.getType(), dest.getType());
 assertSame(src._objectIdReader, dest._objectIdReader);
 assertSame(src.getIdProperty(), dest.getIdProperty());
 assertSame(src.getIdGenerator(), dest.getIdGenerator());
 assertSame(src.getIdResolver(), dest.getIdResolver());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyNameChange() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
 assertNotSame(property, newProperty);
 assertSame(objectIdReader, newProperty._objectIdReader);
 assertSame(new PropertyName("newName"), newProperty.getName());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyValueDeserializerChange() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newProperty = property.withValueDeserializer(new StdSerializer<JsonDeserializer<Object>>(JsonDeserializer.class) {
 @Override
 public JsonDeserializer<Object> createContextual(DeserializationContext ctxt, BeanProperty property) throws IOException {
 return new StdDeserializer<Object>(Object.class);
 }
 });
 assertNotSame(property, newProperty);
 assertSame(objectIdReader, newProperty._objectIdReader);
 assertSame(newProperty.getIdProperty(), newProperty.getIdProperty());
 assertSame(newProperty.getIdGenerator(), newProperty.getIdGenerator());
 assertSame(newProperty.getIdResolver(), newProperty.getIdResolver());
 assertSame(new StdDeserializer<Object>(Object.class), newProperty.getValueDeserializer());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = UnsupportedOperationException.class)
 public void testObjectIdValuePropertySetNullValue() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property.set(new Object(), null);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyDeserializeAndSet() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectNode node = objectMapper.createObjectNode();
 node.put("id", "123");
 JsonParser jp = objectMapper.treeToValues(node, Object.class);
 property.deserializeAndSet(jp, context, new Object());
 ObjectIdGenerator idGenerator = property.getIdGenerator();
 assertNotNull(idGenerator);
 assertSame("generatedId", idGenerator.idString);
 Object id = property.getIdProperty().getValue(new Object());
 assertSame("123", id.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyDeserializeSetAndReturn() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectNode node = objectMapper.createObjectNode();
 node.put("id", "123");
 JsonParser jp = objectMapper.treeToValues(node, Object.class);
 Object id = property.deserializeSetAndReturn(jp, context, new Object());
 assertSame("123", id.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertySetAndReturn() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object obj = new Object();
 property.setAndReturn(obj, "123");
 assertSame("123", property.getIdProperty().getValue(obj).toString());
 }
}