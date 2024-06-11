package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {

 private InnerClassProperty property;
 private ObjectNode node;
 private JsonParser parser;
 private DeserializationContext context;
 private Object bean;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException {
 Class<?> clazz = MyClassWithInnerClass.class;
 AnnotatedClass ac = Introspector.getAnnotated(clazz);
 BeanDescription bd = Introspector.findBeanDescription(ac, null);
 BeanPropertyDefinition bpd = bd.findPropertyDefinition("innerClassProperty");
 AnnotatedConstructor constructor = ac.getConstructor(clazz.getDeclaredInnerClass("InnerClass"));
 PropertyName name = PropertyName.construct("innerClassProperty");
 property = new InnerClassProperty(null, constructor);
 property = new InnerClassProperty(property, name);
 property = new InnerClassProperty(property, new JsonDeserializer<Object>() {});
 node = ObjectMapper.getDefaultInstance().createObjectNode();
 parser = ObjectMapper.getDefaultInstance().getFactory().createParser(node);
 context = ObjectMapper.getDefaultInstance().getFactory().createContext();
 bean = ObjectMapper.getDefaultInstance().readValue(node.traverse(), clazz);
 }

 @Test
 public void testDelegateMethod() throws IOException {
 Object value = new Object();
 property.set(bean, value);
 assertEquals(value, property.get(bean));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object value = new Object();
 node.put("innerClassProperty", ObjectMapper.getDefaultInstance().valueToTree(value));
 property.deserializeAndSet(parser, context, bean);
 assertEquals(value, property.get(bean));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object value = new Object();
 Object returnedValue = property.deserializeSetAndReturn(parser, context, bean);
 assertEquals(value, returnedValue);
 }

 @Test
 public void testSet() throws IOException {
 Object value = new Object();
 property.set(bean, value);
 assertEquals(value, property.get(bean));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object value = new Object();
 Object returnedValue = property.setAndReturn(bean, value);
 assertEquals(value, returnedValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws IOException {
 property = new InnerClassProperty(property, null);
 property.deserializeAndSet(parser, context, bean);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testReadResolve() throws IOException {
 InnerClassProperty resolvedProperty = (InnerClassProperty) property.readResolve();
 assertEquals(property, resolvedProperty);
 }

 @Test
 public void testWriteReplace() {
 InnerClassProperty replacedProperty = new InnerClassProperty(property, new AnnotatedConstructor(null, property.getCreator(), null, null));
 assertEquals(property, replacedProperty);
 }
}

class MyClassWithInnerClass {
 @JsonDeserialize(using = InnerClassDeserializer.class)
 public static class InnerClass {

 }
}