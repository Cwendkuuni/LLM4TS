package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setUp() {
 BeanProperty property = new BeanProperty(new AnnotatedField(Foo.class, "anyProperty"), null, null);
 AnnotatedMember setter = new AnnotatedField(Foo.class, "anyProperty");
 JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class);
 JsonDeserializer<Object> valueDeserializer = new ObjectDeserializer();
 TypeDeserializer valueTypeDeserializer = null;
 KeyDeserializer keyDeserializer = null;
 settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 }

 @Test
 public void testConstructor() {
 // Basic functionality test
 BeanProperty property = new BeanProperty(new AnnotatedField(Foo.class, "anyProperty"), null, null);
 AnnotatedMember setter = new AnnotatedField(Foo.class, "anyProperty");
 JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class);
 KeyDeserializer keyDeserializer = null;
 JsonDeserializer<Object> valueDeserializer = new ObjectDeserializer();
 TypeDeserializer valueTypeDeserializer = null;
 SettableAnyProperty actualSettableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 SettableAnyProperty expectedSettableAnyProperty = settableAnyProperty;
 assertEquals(expectedSettableAnyProperty, actualSettableAnyProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetterIsNull() {
 BeanProperty property = new BeanProperty(new AnnotatedField(Foo.class, "anyProperty"), null, null);
 JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class);
 SettableAnyProperty actualSettableAnyProperty = new SettableAnyProperty(property, null, type, null, null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testHasValueDeserializer() {
 assertTrue(settableAnyProperty.hasValueDeserializer());
 }

 @Test
 public void testGetType() {
 assertEquals(settableAnyProperty.getType(), ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class));
 }

 @Test
 public void testDeserializeAndSetNullValue() throws IOException {
 Object instance = new Foo();
 String propName = "anyProperty";
 settableAnyProperty.deserializeAndSet(new JsonParser() {
 @Override
 public JsonToken currentToken() {
 return JsonToken.VALUE_NULL;
 }
 }, null, instance, propName);
 assertEquals(null, settableAnyProperty.getProperty().get(instance, propName));
 }

 @Test
 public void testDeserializeAndSetValue() throws IOException {
 Object instance = new Foo();
 String propName = "anyProperty";
 JsonParser parser = new JsonParser();
 parser.setValue("value");
 settableAnyProperty.deserializeAndSet(parser, null, instance, propName);
 assertEquals("value", settableAnyProperty.getProperty().get(instance, propName));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetValueOfDifferentType() throws IOException {
 Object instance = new Foo();
 String propName = "anyProperty";
 JsonParser parser = new JsonParser();
 parser.setValue("integerValue:42");
 try {
 settableAnyProperty.deserializeAndSet(parser, null, instance, propName);
 fail("Should throw JsonMappingException");
 } catch (JsonMappingException e) {
 String expectedMessage = "Problem deserializing \"any\" property 'anyProperty' of class [com.example.Foo] (expected type: class java.lang.Object; actual type: class java.lang.Integer)";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Foo();
 String propName = "anyProperty";
 Object value = "value";
 settableAnyProperty.set(instance, propName, value);
 assertEquals(value, settableAnyProperty.getProperty().get(instance, propName));
 }

 @Test(expected = IOException.class)
 public void testSetWithException() throws IOException {
 Object instance = new Foo();
 String propName = "anyProperty";
 Object value = "value";
 SettableAnyProperty spySettableAnyProperty = spy(settableAnyProperty);
 doThrow(new IOException("Test exception")).when(spySettableAnyProperty).set(instance, propName, value);
 try {
 spySettableAnyProperty.deserializeAndSet(new JsonParser(), null, instance, propName);
 fail("Should throw IOException");
 } catch (IOException e) {
 assertEquals("Test exception", e.getMessage());
 }
 }

 @Test
 public void testFixAccess() {
 settableAnyProperty.fixAccess(null);
 // No specific assertion as the method does not have a return value
 // and the access is assumed to be fixed in the test setup
 }

}

 class Foo {
 Map<Object, Object> anyProperty;
 }