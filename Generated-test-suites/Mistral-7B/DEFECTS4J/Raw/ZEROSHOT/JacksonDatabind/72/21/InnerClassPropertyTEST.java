package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class InnerClassPropertyTest {

 private static class TestBean {
 private InnerClass inner;

 public TestBean() {
 }

 public TestBean(String str) {
 this();
 inner = new InnerClass(str);
 }

 public static class InnerClass {
 private final String value;

 public InnerClass(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
 }

 public InnerClass getInner() {
 return inner;
 }

 public void setInner(InnerClass inner) {
 this.inner = inner;
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDefaultConstructor() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jp = mapper.getFactory().createParser("{}");
 TestBean bean = mapper.readValue(jp, TestBean.class);
 assertNull(bean.getInner());
 }

 @Test
 public void testValueDeserializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jp = mapper.getFactory().createParser("{\"inner\":{\"value\":\"test\"}}");
 TestBean bean = mapper.readValue(jp, TestBean.class);
 assertEquals("test", bean.getInner().getValue());
 }

 @Test
 public void testAnnotatedConstructor() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule().addDeserializer(TestBean.InnerClass.class, new TestBeanInnerClassDeserializer()));
 JsonParser jp = mapper.getFactory().createParser("{\"inner\":{\"value\":\"test\"}}");
 TestBean bean = mapper.readValue(jp, TestBean.class);
 assertEquals("test", bean.getInner().getValue());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean("test");
 Converter converter = mapper.getTypeFactory().constructValueDeserializer(new TypeReference<TestBean>() {});
 JsonParser jp = mapper.treeToValues(mapper.valueToTree(bean), converter);
 TestBean deserializedBean = mapper.readValue(jp, TestBean.class);
 assertEquals("test", deserializedBean.getInner().getValue());
 }

 @Test
 public void testSerializationWithDefaultConstructor() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 Converter converter = mapper.getTypeFactory().constructValueDeserializer(new TypeReference<TestBean>() {});
 JsonParser jp = mapper.treeToValues(mapper.valueToTree(bean), converter);
 TestBean deserializedBean = mapper.readValue(jp, TestBean.class);
 assertNull(deserializedBean.getInner());
 }

 @Test
 public void testReadResolve() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean("test");
 Object serialized = mapper.writeValueAsBytes(bean);
 TestBean deserialized = mapper.readValue(serialized, TestBean.class);
 assertEquals("test", deserialized.getInner().getValue());

 Object inner = deserialized.getInner();
 TestBean innerBean = mapper.readValue(inner.getClass().getDeclaredConstructor().newInstance(inner), TestBean.class);
 assertEquals("test", innerBean.getInner().getValue());
 }

 @Test
 public void testWriteReplace() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean("test");
 Object serialized = mapper.writeValueAsBytes(bean);
 TestBean deserialized = mapper.readValue(serialized, TestBean.class);
 Object inner = deserialized.getInner();
 TestBean innerDeserialized = mapper.readValue(inner.getClass().getDeclaredConstructor().newInstance(inner), TestBean.class);
 assertEquals("test", innerDeserialized.getInner().getValue());

 Object innerSerialized = innerDeserialized.getInner();
 TestBean innerDeserializedFromSerialized = mapper.readValue(innerSerialized, TestBean.class);
 assertEquals("test", innerDeserializedFromSerialized.getInner().getValue());
 }

 @Test
 public void testSettableBeanProperty() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean("test");
 JsonParser jp = mapper.getFactory().createParser("{\"inner\":{\"value\":\"newValue\"}}");
 mapper.findAndRegisterDeserializer(TestBean.InnerClass.class, new TestBeanInnerClassDeserializer());
 mapper.setPropertyValue(bean, "inner", jp);
 assertEquals("newValue", bean.getInner().getValue());
 }

 @SuppressWarnings("unused")
 private static class TestBeanInnerClassDeserializer
 extends JsonDeserializer<TestBean.InnerClass> {
 @Override
 public TestBean.InnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 TestBean.InnerClass inner = new TestBean.InnerClass("test");
 jp.nextToken();
 return inner;
 }
 }
}