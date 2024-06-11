package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestClass.class);
 collector = new CreatorCollector(beanDesc, true);
 }

 @Test
 public void testDefaultCreator() {
 AnnotatedWithParams creator = new AnnotatedMethod(TestClass.class.getDeclaredConstructor());
 collector.setDefaultCreator(creator);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 assertTrue(inst instanceof StdValueInstantiator);
 }

 @Test
 public void testStringCreator() throws Exception {
 StringCreator creator = new StringCreator();
 collector.addStringCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 assertTrue(inst instanceof StdValueInstantiator);
 assertEquals( creator.getClass(), inst.getClass());
 }

 @Test
 public void testIntCreator() throws Exception {
 IntCreator creator = new IntCreator();
 collector.addIntCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 assertTrue(inst instanceof StdValueInstantiator);
 assertEquals( creator.getClass(), inst.getClass());
 }

 @Test
 public void testLongCreator() throws Exception {
 LongCreator creator = new LongCreator();
 collector.addLongCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 assertTrue(inst instanceof StdValueInstantiator);
 assertEquals( creator.getClass(), inst.getClass());
 }

 @Test
 public void testDoubleCreator() throws Exception {
 DoubleCreator creator = new DoubleCreator();
 collector.addDoubleCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 assertTrue(inst instanceof StdValueInstantiator);
 assertEquals( creator.getClass(), inst.getClass());
 }

 @Test
 public void testBooleanCreator() throws Exception {
 BooleanCreator creator = new BooleanCreator();
 collector.addBooleanCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 assertTrue(inst instanceof StdValueInstantiator);
 assertEquals( creator.getClass(), inst.getClass());
 }

 @Test
 public void testDelegateCreator() throws Exception {
 DelegateCreator creator = new DelegateCreator();
 collector.addDelegatingCreator(creator.getMethod(), new CreatorProperty[] { new CreatorProperty(new AnnotatedMember(creator.getField())) }, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 assertTrue(inst instanceof StdValueInstantiator);
 assertEquals( creator.getClass(), inst.getClass());
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 PropertyBasedCreator creator = new PropertyBasedCreator();
 collector.addPropertyCreator(creator, new CreatorProperty[] { new CreatorProperty(new AnnotatedMember(creator.getField1())) }, true);
 collector.addPropertyCreator(creator, new CreatorProperty[] { new CreatorProperty(new AnnotatedMember(creator.getField2())) }, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 assertTrue(inst instanceof StdValueInstantiator);
 assertEquals( creator.getClass(), inst.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 StringCreator creator = new StringCreator();
 collector.addStringCreator(creator, true);
 collector.addStringCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreatorProperty() throws Exception {
 PropertyBasedCreator creator = new PropertyBasedCreator();
 collector.addPropertyCreator(creator, new CreatorProperty[] { new CreatorProperty(new AnnotatedMember(creator.getField1())) }, true);
 collector.addPropertyCreator(creator, new CreatorProperty[] { new CreatorProperty(new AnnotatedMember(creator.getField1())) }, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testCanFixAccess() throws Exception {
 TestClassWithPrivateConstructor classToTest = new TestClassWithPrivateConstructor();
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestClassWithPrivateConstructor.class);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 AnnotatedWithParams creator = new AnnotatedMethod(classToTest.getClass().getDeclaredConstructor());
 collector.setDefaultCreator(creator);
 assertTrue(ClassUtil.isAccessible(creator.getAnnotated()));
 }

 @Test
 public void testCannotFixAccess() throws Exception {
 TestClassWithPrivateConstructor classToTest = new TestClassWithPrivateConstructor();
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestClassWithPrivateConstructor.class);
 CreatorCollector collector = new CreatorCollector(beanDesc, false);
 AnnotatedWithParams creator = new AnnotatedMethod(classToTest.getClass().getDeclaredConstructor());
 collector.setDefaultCreator(creator);
 assertFalse(ClassUtil.isAccessible(creator.getAnnotated()));
 }

 private static class TestClass {

 }

 private static class StringCreator {
 @JsonCreator
 public TestClass(@JsonProperty("str") String str) {
 }
 }

 private static class IntCreator {
 @JsonCreator
 public TestClass(@JsonProperty("int") int i) {
 }
 }

 private static class LongCreator {
 @JsonCreator
 public TestClass(@JsonProperty("long") long l) {
 }
 }

 private static class DoubleCreator {
 @JsonCreator
 public TestClass(@JsonProperty("double") double d) {
 }
 }

 private static class BooleanCreator {
 @JsonCreator
 public TestClass(@JsonProperty("bool") boolean b) {
 }
 }

 private static class DelegateCreator {
 @JsonCreator
 public TestClass(@JsonProperty("str") String str, @JsonProperty("delegate") MyDelegate delegate) {
 }

 private MyDelegate delegate;
 }

 private static class MyDelegate {
 private String value;

 public String getValue() {
 return value;
 }

 public void setValue(String value) {
 this.value = value;
 }

 @JsonProperty("value")
 public String getField() {
 return value;
 }
 }

 private static class PropertyBasedCreator {
 @JsonCreator
 public TestClass(@JsonProperty("field1") String str1, @JsonProperty("field2") String str2) {
 }

 private String field1;
 private String field2;
 }

 private static class TestClassWithPrivateConstructor {
 @JsonCreator
 private TestClassWithPrivateConstructor(@JsonProperty("str") String str) {
 }

 private String value;
 }
}