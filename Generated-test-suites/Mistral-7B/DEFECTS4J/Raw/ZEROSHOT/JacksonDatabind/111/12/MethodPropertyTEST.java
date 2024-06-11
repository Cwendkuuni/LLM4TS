package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.test.BaseMapTest;

public class MethodPropertyTest extends BaseMapTest {

 private static class TestBean {
 @JsonProperty("name")
 private String property;

 @JsonProperty("age")
 private int number;

 private MethodProperty propertyMethod;
 private MethodProperty numberMethod;

 public TestBean() {
 propertyMethod = new MethodProperty(ReflectionUtil.findMethod(TestBean.class, "setProperty", String.class), null, null, null, null);
 numberMethod = new MethodProperty(ReflectionUtil.findMethod(TestBean.class, "setNumber", int.class), null, null, null, null);
 }
 }

 @org.junit.Before
 public void setUp() throws Exception {
 super.setUp();
 }

 @org.junit.After
 public void tearDown() throws Exception {
 super.tearDown();
 }

 @org.junit.Test
 public void testConstructor() throws Exception {
 TestBean bean = new TestBean();
 assertNotNull(bean.propertyMethod);
 assertNotNull(bean.numberMethod);
 }

 @org.junit.Test
 public void testSetProperty() throws Exception {
 TestBean bean = new TestBean();
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "John Doe");
 JsonParser p = mapper.treeAsTokens(node);
 bean.propertyMethod.deserializeAndSet(p, context, bean);
 assertEquals("John Doe", bean.property);
 }

 @org.junit.Test
 public void testSetPropertyNull() throws Exception {
 TestBean bean = new TestBean();
 ObjectNode node = mapper.createObjectNode();
 node.putNull("name");
 JsonParser p = mapper.treeAsTokens(node);
 bean.propertyMethod.deserializeAndSet(p, context, bean);
 assertNull(bean.property);
 }

 @org.junit.Test
 public void testSetNumber() throws Exception {
 TestBean bean = new TestBean();
 ObjectNode node = mapper.createObjectNode();
 node.put("age", 30);
 JsonParser p = mapper.treeAsTokens(node);
 bean.numberMethod.deserializeAndSet(p, context, bean);
 assertEquals(30, bean.number);
 }

 @org.junit.Test
 public void testSetNumberNull() throws Exception {
 TestBean bean = new TestBean();
 ObjectNode node = mapper.createObjectNode();
 node.putNull("age");
 JsonParser p = mapper.treeAsTokens(node);
 bean.numberMethod.deserializeAndSet(p, context, bean);
 assertEquals(0, bean.number);
 }

 @org.junit.Test
 public void testSetAndReturn() throws Exception {
 TestBean bean = new TestBean();
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "John Doe");
 JsonParser p = mapper.treeAsTokens(node);
 Object result = bean.propertyMethod.deserializeSetAndReturn(p, context, bean);
 assertEquals("John Doe", result);
 }

 @org.junit.Test
 public void testSetAndReturnNull() throws Exception {
 TestBean bean = new TestBean();
 ObjectNode node = mapper.createObjectNode();
 node.putNull("name");
 JsonParser p = mapper.treeAsTokens(node);
 Object result = bean.propertyMethod.deserializeSetAndReturn(p, context, bean);
 assertNull(result);
 }

 @org.junit.Test
 public void testSet() throws Exception {
 TestBean bean = new TestBean();
 String value = "John Doe";
 bean.propertyMethod.set(bean, value);
 assertEquals(value, bean.property);
 }

 @org.junit.Test
 public void testSetNull() throws Exception {
 TestBean bean = new TestBean();
 bean.propertyMethod.set(bean, null);
 assertNull(bean.property);
 }

 @org.junit.Test
 public void testReadResolve() throws Exception {
 TestBean bean = new TestBean();
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "John Doe");
 JsonParser p = mapper.treeAsTokens(node);
 bean.propertyMethod.deserializeAndSet(p, context, bean);
 TestBean clone = (TestBean) bean.clone();
 assertEquals("John Doe", clone.property);
 TestBean deser = mapper.readValue(node.toString(), TestBean.class);
 assertEquals("John Doe", deser.property);
 assertEquals(bean.hashCode(), deser.hashCode());
 }
}