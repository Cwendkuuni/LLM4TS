package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

public class InnerClassPropertyTest {

 private static class TestBean {
 private InnerClassProperty innerClassProperty;
 }

 @Test
 public void testConstructor() throws InstantiationException, IllegalAccessException, IOException {
 // Test default constructor
 InnerClassProperty icp = new InnerClassProperty(null, null);
 assertNull(icp._delegate);
 assertNull(icp._creator);
 assertNull(icp._annotated);

 // Test constructor with delegate and creator
 Class<?> clazz = TestBean.class;
 PropertyName name = PropertyName.construct("innerClassProperty");
 Constructor<?> constructor = clazz.getDeclaredConstructor();
 SettableBeanProperty delegate = new SettableBeanProperty(new AnnotatedClass(clazz), name);
 InnerClassProperty icp1 = new InnerClassProperty(delegate, constructor);
 assertSame(delegate, icp1._delegate);
 assertSame(constructor, icp1._creator);
 assertNull(icp1._annotated);

 // Test constructor with annotated constructor
 AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor(null, constructor, null, null);
 InnerClassProperty icp2 = new InnerClassProperty(null, icp1, annotatedConstructor);
 assertSame(icp1, icp2._delegate);
 assertSame(constructor, icp2._creator);
 assertSame(annotatedConstructor, icp2._annotated);
 }

 @Test
 public void testDeserializeAndSet() throws InstantiationException, IllegalAccessException, IOException {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClassProperty\": null}";
 JsonNode node = mapper.readTree(json);
 mapper.findAndRegisterValues();
 mapper.setDefaultPropertyInclusion(JsonInclude.Value.ALWAYS);
 InnerClassProperty icp = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor());
 AnnotatedMember member = new AnnotatedField(bean, "innerClassProperty");
 JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(member.getAnnotated());
 icp.setValueDeserializer(valueDeserializer);
 JsonParser jp = mapper.parserForTesting(new ByteArrayInputStream(json.getBytes()));
 InnerClassProperty icpDeserialized = new InnerClassProperty(icp, null);
 icpDeserialized.deserializeAndSet(jp, null, bean);
 assertNull(bean.innerClassProperty);
 }

 @Test
 public void testDeserializeSetAndReturn() throws InstantiationException, IllegalAccessException, IOException {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClassProperty\": null}";
 JsonNode node = mapper.readTree(json);
 mapper.findAndRegisterValues();
 mapper.setDefaultPropertyInclusion(JsonInclude.Value.ALWAYS);
 InnerClassProperty icp = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor());
 AnnotatedMember member = new AnnotatedField(bean, "innerClassProperty");
 JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(member.getAnnotated());
 icp.setValueDeserializer(valueDeserializer);
 Object deserialized = icp.deserializeSetAndReturn(mapper.parserForTesting(new ByteArrayInputStream(json.getBytes())), null, bean);
 assertNull(deserialized);
 assertNull(bean.innerClassProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws InstantiationException, IllegalAccessException {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClassProperty\": null}";
 JsonNode node = mapper.readTree(json);
 mapper.findAndRegisterValues();
 mapper.setDefaultPropertyInclusion(JsonInclude.Value.ALWAYS);
 InnerClassProperty icp = new InnerClassProperty(null, TestBean.class);
 AnnotatedMember member = new AnnotatedField(bean, "innerClassProperty");
 JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(member.getAnnotated());
 icp.setValueDeserializer(valueDeserializer);
 icp.deserializeAndSet(mapper.parserForTesting(new ByteArrayInputStream(json.getBytes())), null, bean);
 fail("Expected IllegalArgumentException for missing constructor.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() throws InstantiationException, IllegalAccessException, IOException {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClassProperty\": null}";
 JsonNode node = mapper.readTree(json);
 mapper.findAndRegisterValues();
 mapper.setDefaultPropertyInclusion(JsonInclude.Value.ALWAYS);
 InnerClassProperty icp = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor(String.class));
 AnnotatedMember member = new AnnotatedField(bean, "innerClassProperty");
 JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(member.getAnnotated());
 icp.setValueDeserializer(valueDeserializer);
 icp.deserializeAndSet(mapper.parserForTesting(new ByteArrayInputStream(json.getBytes())), null, bean);
 fail("Expected IllegalArgumentException for invalid constructor.");
 }

 @Test
 public void testSet() throws InstantiationException, IllegalAccessException, IOException {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClassProperty\": {}}";
 JsonNode node = mapper.readTree(json);
 mapper.findAndRegisterValues();
 mapper.setDefaultPropertyInclusion(JsonInclude.Value.ALWAYS);
 InnerClassProperty icp = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor());
 AnnotatedMember member = new AnnotatedField(bean, "innerClassProperty");
 JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(member.getAnnotated());
 icp.setValueDeserializer(valueDeserializer);
 JsonParser jp = mapper.parserForTesting(new ByteArrayInputStream(json.getBytes()));
 icp.deserializeAndSet(jp, null, bean);
 assertNotNull(bean.innerClassProperty);
 }

 @Test
 public void testSetAndReturn() throws InstantiationException, IllegalAccessException, IOException {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClassProperty\": {}}";
 JsonNode node = mapper.readTree(json);
 mapper.findAndRegisterValues();
 mapper.setDefaultPropertyInclusion(JsonInclude.Value.ALWAYS);
 InnerClassProperty icp = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor());
 AnnotatedMember member = new AnnotatedField(bean, "innerClassProperty");
 JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(member.getAnnotated());
 icp.setValueDeserializer(valueDeserializer);
 Object deserialized = icp.deserializeSetAndReturn(mapper.parserForTesting(new ByteArrayInputStream(json.getBytes())), null, bean);
 assertSame(bean.innerClassProperty, deserialized);
 }

 @Test
 public void testReadResolve() {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClassProperty\": null}";
 JsonNode node = mapper.readTree(json);
 mapper.findAndRegisterValues();
 mapper.setDefaultPropertyInclusion(JsonInclude.Value.ALWAYS);
 InnerClassProperty icp = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor());
 AnnotatedMember member = new AnnotatedField(bean, "innerClassProperty");
 JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(member.getAnnotated());
 icp.setValueDeserializer(valueDeserializer);
 JsonParser jp = mapper.parserForTesting(new ByteArrayInputStream(json.getBytes()));
 icp.deserializeAndSet(jp, null, bean);
 InnerClassProperty icpResolved = (InnerClassProperty) icp.readResolve();
 assertSame(icp, icpResolved);
 }

 @Test
 public void testWriteReplace() {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClassProperty\": null}";
 JsonNode node = mapper.readTree(json);
 mapper.findAndRegisterValues();
 mapper.setDefaultPropertyInclusion(JsonInclude.Value.ALWAYS);
 InnerClassProperty icp = new InnerClassProperty(null, TestBean.class.getDeclaredConstructor());
 AnnotatedMember member = new AnnotatedField(bean, "innerClassProperty");
 JsonDeserializer<?> valueDeserializer = mapper.findValueDeserializer(member.getAnnotated());
 icp.setValueDeserializer(valueDeserializer);
 JsonParser jp = mapper.parserForTesting(new ByteArrayInputStream(json.getBytes()));
 icp.deserializeAndSet(jp, null, bean);
 Object writeReplace = icp.writeReplace();
 assertSame(icp, writeReplace);
 }
}