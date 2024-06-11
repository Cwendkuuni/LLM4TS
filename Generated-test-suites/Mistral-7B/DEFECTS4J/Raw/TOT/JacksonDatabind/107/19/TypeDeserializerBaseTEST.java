package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testBaseTypeDeserialization() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 MapperFactory.getDefaultInstance().constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 false,
 null) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }
 };

 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("\"Hello World\"");
 deserializer.deserialize(parser, new DeserializationContext.Builder()
 .withBaseContext(new BaseContextClassLoader(TypeDeserializerBase.class))
 .build(), null);

 assertEquals("Hello World", deserializer.getDefaultImpl());
 }

 @Test
 public void testTypeIdDeserialization() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 MapperFactory.getDefaultInstance().constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 false,
 null) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }

 @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="typePropertyName")
 public Class<?> getDefaultImpl() {
 return Number.class;
 }
 };

 ObjectMapper mapper = new ObjectMapper();
 mapper.registerTypeDeserializer(Number.class, new NumberDeserializer());

 String json = "{\"value\":42,\"typePropertyName\":\"Java.lang.Integer\"}";
 Object readValue = mapper.readValue(json, deserializer.getType());

 assertEquals(42, readValue);
 }

 @Test(expected = IOException.class)
 public void testUnknownTypeId() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 MapperFactory.getDefaultInstance().constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 false,
 null) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }
 };

 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("{\"typePropertyName\":\"Java.lang.Foo\"}");
 deserializer.deserialize(parser, new DeserializationContext.Builder()
 .withBaseContext(new BaseContextClassLoader(TypeDeserializerBase.class))
 .build(), null);
 }

 @Test
 public void testMissingTypeId() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 MapperFactory.getDefaultInstance().constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 false,
 null) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }
 };

 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("{\"value\":42}");
 try {
 deserializer.deserialize(parser, new DeserializationContext.Builder()
 .withBaseContext(new BaseContextClassLoader(TypeDeserializerBase.class))
 .build(), null);
 fail("Should have thrown an exception");
 } catch (IOException e) {
 // Expected
 }
 }
}