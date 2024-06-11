package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * InnerClassPropertyTest class is used to test the functionality of InnerClassProperty class.
 */
public class InnerClassPropertyTest {

 private ObjectMapper objectMapper;
 private JsonDeserializer<Object> jsonDeserializer;
 private InnerClassProperty innerClassProperty;
 private JSONFactory jsonFactory;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 jsonDeserializer = new StdDeserializer<>();
 jsonFactory = objectMapper.getFactory();
 try {
 Class<?> innerClass = InnerClassPropertyTest.class.getDeclaredInnerClass("InnerTest");
 AnnotatedConstructor ac = jsonFactory.constructAnnotationInstance(InnerClassProperty.InnerClassConstructor.class, innerClass);
 innerClassProperty = new InnerClassProperty(null, ac);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 /**
 * Test deserializeAndSet method.
 * @throws IOException
 */
 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode jsonNode = jsonFactory.createObjectNode();
 jsonNode.put("innerClass", "SomeInnerClassInstance");
 Object bean = new SomeOuterClass();
 innerClassProperty.deserializeAndSet(objectMapper.readTree(jsonNode.toString()), null, bean);
 assertEquals("SomeInnerClassInstance", bean.getInnerClassProperty());
 }

 /**
 * Test set method with valid value.
 */
 @Test
 public void testSet() {
 SomeOuterClass bean = new SomeOuterClass();
 innerClassProperty.set(bean, new InnerTest("TestValue"));
 assertEquals("TestValue", bean.getInnerClassProperty());
 }

 /**
 * Test set method with null value.
 */
 @Test(expected = NullPointerException.class)
 public void testSetNull() {
 SomeOuterClass bean = new SomeOuterClass();
 innerClassProperty.set(bean, null);
 }

 /**
 * Test deserializeSetAndReturn method.
 * @throws IOException
 */
 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode jsonNode = jsonFactory.createObjectNode();
 jsonNode.put("innerClass", "SomeInnerClassInstance");
 SomeOuterClass bean = new SomeOuterClass();
 Object result = innerClassProperty.deserializeSetAndReturn(objectMapper.readTree(jsonNode.toString()), null, bean);
 assertEquals("SomeInnerClassInstance", result);
 }

 /**
 * Test readResolve method.
 */
 @Test
 public void testReadResolve() {
 InnerClassProperty innerClassProperty2 = (InnerClassProperty) innerClassProperty.readResolve();
 assertEquals(innerClassProperty, innerClassProperty2);
 }

 /**
 * Test writeReplace method.
 */
 @Test
 public void testWriteReplace() {
 InnerClassProperty innerClassProperty2 = new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, null, null, null));
 assertEquals(innerClassProperty, innerClassProperty2.writeReplace());
 }

 /**
 * Test inner class.
 * @hide
 * @SuppressWarnings("unchecked")
 */
 private static class InnerTest {
 private final String value;

 public InnerTest(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
 }

 private static class SomeOuterClass {
 private InnerTest innerClassProperty;

 public SomeOuterClass() {
 }

 public InnerTest getInnerClassProperty() {
 return innerClassProperty;
 }

 public void setInnerClassProperty(InnerTest innerClassProperty) {
 this.innerClassProperty = innerClassProperty;
 }
 }

 private static class InnerClassConstructor {
 public AnnotatedConstructor(AnnotatedMember member,
 Constructor<?> constructor,
 Class<?> rawType,
 Class<?> componentType) {
 // Empty constructor
 }
 }
}