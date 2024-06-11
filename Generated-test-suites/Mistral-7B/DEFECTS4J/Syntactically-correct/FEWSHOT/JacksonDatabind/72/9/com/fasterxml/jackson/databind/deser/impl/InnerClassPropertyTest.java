package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonValue;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.StdFromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullValueDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class InnerClassPropertyTest {

 private class TestInnerClass {
 private int value;

 @JsonValue
 public int getValue() {
 return value;
 }

 @JsonPOJOBuilder(withPrefix = "")
 static class Builder {
 int value;

 public Builder() {}

 public TestInnerClass build() {
 TestInnerClass obj = new TestInnerClass();
 ReflectionUtil.copy(this, obj);
 return obj;
 }
 }

 public static class InnerClass {
 private TestInnerClass inner;

 public InnerClass() {
 this.inner = new TestInnerClass.Builder().build();
 }
 }
 }

 @Test
 public void testInnerClassProperty() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestInnerClass.InnerClass.class.getDeclaredConstructor());

 assertNull(property.getValue());

 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("value", "123");
 Object value = new ObjectMapper().readValue(node, TestInnerClass.InnerClass.class);
 property.setValue(new ObjectMapper().findAndRegisterModules(), null, new ObjectNode(node), null, null, value);

 assertEquals(123, property.getValue().inner.getValue());
 }

 @Test
 public void testSetValueDeserializer() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestInnerClass.InnerClass.class.getDeclaredConstructor());
 property.setValueDeserializer(new StdFromStringDeserializer<>(""));

 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("value", "TestInnerClass.InnerClass@1234");
 Object value = new ObjectMapper().readValue(node, TestInnerClass.InnerClass.class);
 property.setValue(new ObjectMapper().findAndRegisterModules(), null, new ObjectNode(node), null, new StdFromStringDeserializer<>(""), value);

 assertEquals("TestInnerClass.InnerClass@1234", property.getValue().toString());
 }

 @Test
 public void testValueDeserializer() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestInnerClass.InnerClass.class.getDeclaredConstructor());
 property.setValueDeserializer(new NullValueDeserializer());

 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("value", null);
 Object value = new ObjectMapper().readValue(node, TestInnerClass.InnerClass.class);
 property.setValue(new ObjectMapper().findAndRegisterModules(), null, new ObjectNode(node), null, new NullValueDeserializer(), value);

 assertNull(property.getValue());
 }

 @Test
 public void testWithValueDeserializer() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestInnerClass.InnerClass.class.getDeclaredConstructor());
 JsonDeserializer<TestInnerClass.InnerClass> deserializer = mock(JsonDeserializer.class);
 when(deserializer.deserialize(anyObject(), eq(new ObjectMapper().findAndRegisterModules()), anyObject(), anyObject(), anyObject()))
 .thenReturn(new TestInnerClass.InnerClass());
 property.setValueDeserializer(deserializer);

 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("value", "{}");
 Object value = new ObjectMapper().readValue(node, TestInnerClass.InnerClass.class);
 property.setValue(new ObjectMapper().findAndRegisterModules(), null, new ObjectNode(node), null, null, value);

 assertEquals(new TestInnerClass.InnerClass(), property.getValue());
 }

 @Test
 public void testWithName() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestInnerClass.InnerClass.class.getDeclaredConstructor());
 property = property.withName(new PropertyName("test"));
 assertEquals("test", property.getName().getShortName());
 }

 @Test
 public void testWithDeserializer() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestInnerClass.InnerClass.class.getDeclaredConstructor());
 property = property.withValueDeserializer(new StdFromStringDeserializer<>(""));
 assertEquals(StdFromStringDeserializer.class, property.getValueDeserializer().getClass());
 }

 @Test
 public void testReadResolve() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestInnerClass.InnerClass.class.getDeclaredConstructor());
 Object writtenValue = property;
 property = property.readResolve();
 assertNotNull(property);
 assertNotSame(writtenValue, property);
 }

 @Test
 public void testWriteReplace() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, TestInnerClass.InnerClass.class.getDeclaredConstructor());
 Object writtenValue = property;
 property = property.writeReplace();
 assertNotNull(property);
 assertNotSame(writtenValue, property);
 }
}