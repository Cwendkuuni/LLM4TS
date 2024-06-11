package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class MethodPropertyTest {

 private static final MethodProperty property;
 private static final Object instance;
 static {
 try {
 Object obj = new Object() {
 private String field;

 @JsonSetter("field")
 public void setField(String value) {
 this.field = value;
 }

 @JsonGetter("field")
 public String getField() {
 return field;
 }

 @JsonProperty("field")
 public Method getter() {
 return getClass().getMethod("getField");
 }

 @JsonProperty("field")
 public void setter(Method setter) {
 this.setter = setter;
 }
 };

 property = new MethodProperty(
 new BeanPropertyDefinition(new AnnotatedClass(obj.getClass()), "field", null, null),
 ObjectType.StandardClass(obj.getClass()), null, null, obj.getter());
 instance = obj;
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }

 @org.junit.Test
 public void testConstructor() {
 MethodProperty methodProperty = new MethodProperty(property, property.getAnnotated(), null);
 assertEquals(property, methodProperty);
 }

 @org.junit.Test
 public void testSetterDeserializeAndSet() throws IOException {
 JsonParser parser = new ObjectMapper().getFactory().createParser("{\"field\":\"value\"}");
 property.deserializeAndSet(parser, new DeserializationContext(new ObjectMapper()), instance);
 assertEquals("value", instance.getField());
 }

 @org.junit.Test
 public void testSetterDeserializeSetAndReturn() throws IOException {
 JsonParser parser = new ObjectMapper().getFactory().createParser("{\"field\":null}");
 Object result = property.deserializeSetAndReturn(parser, new DeserializationContext(new ObjectMapper()), instance);
 assertNull(result);
 assertEquals(null, instance.getField());

 parser = new ObjectMapper().getFactory().createParser("{\"field\":\"value2\"}");
 result = property.deserializeSetAndReturn(parser, new DeserializationContext(new ObjectMapper()), instance);
 assertEquals(instance, result);
 assertEquals("value2", instance.getField());
 }

 @org.junit.Test
 public void testSetterSet() throws IOException, InvocationTargetException, IllegalAccessException {
 JsonParser parser = new ObjectMapper().getFactory().createParser("{\"field\":null}");
 property.deserializeAndSet(parser, new DeserializationContext(new ObjectMapper()), instance);
 assertNull(instance.getField());

 parser = new ObjectMapper().getFactory().createParser("{\"field\":\"value3\"}");
 property.deserializeAndSet(parser, new DeserializationContext(new ObjectMapper()), instance);
 assertEquals("value3", instance.getField());

 parser = new ObjectMapper().getFactory().createParser("{\"field\":null}");
 parser.nextToken(); // consume [null]
 property.set(instance, "value4");
 assertEquals("value4", instance.getField());
 }

 @org.junit.Test(expected = IOException.class)
 public void testNullValueDeserializer() throws IOException {
 JsonParser parser = new ObjectMapper().getFactory().createParser("null");
 property.deserializeAndSet(parser, new DeserializationContext(new ObjectMapper()), instance);
 fail("MethodProperty should skip null values if _skipNulls is set to true");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testSetterWithInvalidType() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = mapper.getTypeFactory()
 .defaultTypingResolver();
 TypeDeserializer typeDeserializer = defaultTypingResolver.findValueDeserializer(int.class, property.getType());
 property = new MethodProperty(property, property.getAnnotated(), null, null, property.getter()) {
 @Override
 TypeDeserializer getValueTypeDeserializer() {
 return typeDeserializer;
 }
 };
 JsonParser parser = new ObjectMapper().getFactory()
 .createParser("{\"field\":42}");
 property.deserializeAndSet(parser, new DeserializationContext(new ObjectMapper()), instance);
 fail("MethodProperty should throw an IllegalArgumentException when setting a value of an incompatible type");
 }

 @org.junit.Test(expected = IllegalAccessException.class)
 public void testSetterWithPrivateAccess() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = mapper.getTypeFactory()
 .defaultTypingResolver();
 TypeDeserializer typeDeserializer = defaultTypingResolver.findValueDeserializer(String.class, property.getType());
 property = new MethodProperty(property, property.getAnnotated(), null, null, property.getter()) {
 @Override
 Method getSetter() {
 return instance.getClass().getDeclaredMethod("setField", String.class);
 }

 @Override
 void fixAccess(DeserializationConfig config) {
 getSetter().setAccessible(true);
 }

 @Override
 void set(Object instance, Object value) throws IOException {
 super.set(instance, value);
 }
 };

 JsonParser parser = new ObjectMapper().getFactory().createParser("{\"field\":\"value\"}");
 try {
 property.deserializeAndSet(parser, new DeserializationContext(new ObjectMapper()), instance);
 fail("MethodProperty should throw an IllegalAccessException when setting a private property");
 } catch (IllegalAccessException e) {
 // Expected
 }
 }
}