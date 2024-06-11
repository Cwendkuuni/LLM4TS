package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

 private static class TestBean {
 private Object id;
 private String name;

 @JsonId
 public Object getId() {
 return id;
 }

 public void setId(Object id) {
 this.id = id;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testObjectIdValueProperty() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectIdReader objectIdReader = mapper.getFactory()
 .constructObjectIdReader(StringSerializer.instance, new NameTransformer() {
 @Override
 public String transform(String name, JsonParser p) {
 return name + "_id";
 }
 });
 BeanPropertyDefinition idPropDef = new AnnotationIntrospector()
 .findBeanPropertyDefinition(TestBean.class, "id");
 JsonDeserializer<Object> idDeserializer = new ObjectIdReaderDeserializer(objectIdReader);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, idPropDef.getName(), idPropDef.getMetadata(), idDeserializer, null);

 TestBean bean = new TestBean();
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"testName\"}");
 property.deserializeAndSet(p, mapper.getDeserializationContext(), bean);
 assertEquals("testName_id", bean.getId());

 p = mapper.getFactory().createParser("{\"name\":\"testName\",\"id\":null}");
 property.deserializeAndSet(p, mapper.getDeserializationContext(), bean);
 assertNull(bean.getId());

 p = mapper.getFactory().createParser("{\"name\":\"testName\",\"id\":\"123\"}");
 property.deserializeAndSet(p, mapper.getDeserializationContext(), bean);
 assertEquals("123", bean.getId());

 property.set(bean, "456");
 assertEquals("456", bean.getId());

 ObjectIdValueProperty clone = (ObjectIdValueProperty) property.clone();
 property.setName("newName");
 assertEquals("newName", property.getName().getName());
 assertEquals("id", clone.getName().getName());
 }
}

class ObjectIdReaderDeserializer extends StdScalarDeserializer {
 private ObjectIdReader _objectIdReader;

 public ObjectIdReaderDeserializer(ObjectIdReader objectIdReader) {
 super(Object.class);
 _objectIdReader = objectIdReader;
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return _objectIdReader.readObjectId(p);
 }
}