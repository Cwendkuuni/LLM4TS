package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdResolver;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private BeanDeserializerFactory beanDeserializerFactory;
 private JsonMapper mapper;

 @Before
 public void setup() throws Exception {
 ObjectIdReader.IdGenerator idGenerator = new ObjectIdGenerator();
 ObjectIdReader.IdResolver idResolver = new ObjectIdResolver();

 objectIdReader = new ObjectIdReader(new PropertyName("id"), IdType.STRING, null, idGenerator, idResolver, null);
 beanDeserializerFactory = new BeanDeserializerFactory(mapper.getConfiguration());
 mapper = new JsonMapper();
 mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 }

 @Test
 public void testObjectIdValueProperty() throws IOException, JsonProcessingException {
 BeanDescription beanDescription = mapper.getConfiguration()
 .findBeanDescription(ObjectIdValueProperty.class);
 ObjectIdValueProperty property = objectIdValueProperty = (ObjectIdValueProperty) beanDescription
 .findProperty("id");

 assertNotNull(property);
 assertNotNull(property._objectIdReader);
 assertEquals(ObjectIdReader.IdType.STRING, property._objectIdReader.getIdType());
 assertEquals(IdGenerator.class, property._objectIdReader.generator.getClass());
 assertEquals(IdResolver.class, property._objectIdReader.resolver.getClass());

 // Test deserialization
 Object id = mapper.readValue("\"123\"", String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object instance = new Object();
 property.deserializeAndSet(mapper.getFactory().createParser("{ \"id\": \"" + id + "\" }"), ctxt, instance);
 assertEquals(id, ctxt.findObjectId(instance, objectIdReader.generator, objectIdReader.resolver).getId());

 // Test setting value
 Object idValue = mapper.readValue("123", Long.class);
 property.setAndReturn(instance, idValue);
 assertEquals(idValue, ctxt.findObjectId(instance, objectIdReader.generator, objectIdReader.resolver).getId());

 // Test with null id
 Object nullIdInstance = new Object();
 property.setAndReturn(nullIdInstance, null);
 assertNull(ctxt.findObjectId(nullIdInstance, objectIdReader.generator, objectIdReader.resolver));

 // Test with custom deserializer
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(null, mapper.getConfiguration().findValueTypeDeserializer(Long.class));
 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(new BeanPropertyDefinition(new PropertyName("id"), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null), typeDeserializer));
 IdReaderCustom idReaderCustom = new IdReaderCustom();
 property._objectIdReader = new ObjectIdReader(new PropertyName("id"), IdType.LONG, null, idReaderCustom, null, null);
 property.deserializeAndSet(mapper.getFactory().createParser("{ \"id\": 123 }"), ctxt, instance);
 assertEquals(123L, ctxt.findObjectId(instance, objectIdReader.generator, objectIdReader.resolver).getId());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullProperty() throws IOException {
 ObjectIdValueProperty nullProperty = new ObjectIdValueProperty(objectIdReader, null);
 Object instance = new Object();
 nullProperty.setAndReturn(instance, null);
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }
}

 class ObjectIdGenerator implements ObjectIdGenerator {
 @Override
 public Object generateId(DeserializationContext ctxt) {
 return System.currentTimeMillis();
 }
 }

 class ObjectIdResolver implements IdResolver {
 @Override
 public String getIdDescription(DeserializationContext ctxt, Object id) {
 return id.toString();
 }
 }

 class ObjectIdReaderCustom implements ObjectIdReader.IdReader {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return IdType.LONG;
 }

 @Override
 public Object idFromString(DeserializationContext ctxt, String id) throws IOException {
 return Long.parseLong(id);
 }

 @Override
 public String idToString(DeserializationContext ctxt, Object id) throws IOException {
 return id.toString();
 }
 }

 class StdTypeDeserializer extends TypeDeserializer {
 @Override
 public JsonDeserializer<?> createContextualDeserializer(DeserializationContext ctxt, BeanPropertyDefinition propertyDefinition, JsonDeserializer<?> valueDeserializer) throws JsonMappingException {
 return new None();
 }
}