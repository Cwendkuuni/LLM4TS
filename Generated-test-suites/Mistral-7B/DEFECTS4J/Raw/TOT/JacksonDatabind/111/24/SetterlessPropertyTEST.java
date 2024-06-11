package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class SetterlessPropertyTest extends TestBase {

 private SettableBeanProperty property;
 private Object testInstance;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
 testInstance = new TestClass();
 property = new SetterlessProperty(
 new BeanPropertyDefinition(TestClass.class.getDeclaredField("testList"), Object.class),
 ObjectTypeFactory.defaultInstance.constructType(java.util.List.class),
 null, null,
 new AnnotatedMethod(TestClass.class.getMethod("getTestList"))) {

 @Override
 public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
 super.deserializeAndSet(p, ctxt, instance);
 }

 @Override
 public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
 return super.deserializeSetAndReturn(p, ctxt, instance);
 }

 @Override
 public final void set(Object instance, Object value) throws IOException {
 // Do nothing, test only deserialization
 }

 @Override
 public Object setAndReturn(Object instance, Object value) throws IOException {
 return instance;
 }
 };
 }

 @Test
 public void testDeserializationEmptyJson() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("[]");
 mapper.readValue(p, TestClass.class);
 assertNull(property.getValue(testInstance));
 }

 @Test
 public void testDeserializationSimpleJson() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser(
 "[1,2,3,4,5]"); // Note: this is a valid JSON for a collection
 ObjectNode node = mapper.readValue(p, ObjectNode.class);
 mapper.readValue(p, TestClass.class);
 assertNotNull(property.getValue(testInstance));
 }

 @Test
 public void testDeserializationNullJson() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("null");
 mapper.readValue(p, TestClass.class);
 assertNull(property.getValue(testInstance));
 }

 @Test
 public void testDeserializationInvalidJson() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{foo:5}");
 Object node = mapper.readValue(p, JsonNode.class);
 mapper.readValue(p, TestClass.class);
 assertNull(property.getValue(testInstance));
 }

 @Test
 public void testDeserializationMissingGetMethod() throws IOException {
 TestClass testClass = new TestClass() {
 private java.util.List testList = new ArrayList();

 @JsonDeserialize(using = JsonMissingGetMethodDeserializer.class)
 public List getTestList() {
 return testList;
 }
 };
 property = new SetterlessProperty(
 new BeanPropertyDefinition(TestClass.class.getDeclaredField("testList"), Object.class),
 ObjectTypeFactory.defaultInstance.constructType(java.util.List.class),
 null, null, null) {

 @Override
 public AnnotatedMethod _annotated() {
 return null;
 }

 @Override
 public Method _getter() {
 throw new UnsupportedOperationException();
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("[1,2,3,4,5]");
 mapper.readValue(p, TestClass.class);
 assertNull(property.getValue(testInstance));
 }

 @Test
 public void testDeserializationMissingSetter() throws IOException, InvocationTargetException, IllegalAccessException {
 TestClass testClass = new TestClass() {
 private java.util.List testList = new ArrayList();

 @JsonDeserialize(using = JsonMissingSetterDeserializer.class)
 public List getTestList() {
 return testList;
 }
 };
 property = new SetterlessProperty(
 new BeanPropertyDefinition(TestClass.class.getDeclaredField("testList"), Object.class),
 ObjectTypeFactory.defaultInstance.constructType(java.util.List.class),
 null, null, new AnnotatedMethod(TestClass.class.getMethod("getTestList"))) {

 @Override
 public void setterlessDeserialize(Object instance, Object value) throws IOException {
 // Do nothing, test only deserialization
 }

 @Override
 public Object setAndReturn(Object instance, Object value) throws IOException {
 return instance;
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("[1,2,3,4,5]");
 mapper.readValue(p, TestClass.class);
 assertNotNull(property.getValue(testInstance));
 }

 @Test
 public void testDeserializationInvalidType() throws IOException {
 property = new SetterlessProperty(
 new BeanPropertyDefinition(TestClass.class.getDeclaredField("testList"), Integer.class),
 ObjectTypeFactory.defaultInstance.constructType(java.util.List.class),
 null, null, new AnnotatedMethod(TestClass.class.getMethod("getTestList"))) {

 @Override
 public void setterlessDeserialize(Object instance, Object value) throws IOException {
 // Do nothing, test only deserialization
 }

 @Override
 public Object setAndReturn(Object instance, Object value) throws IOException {
 return instance;
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("[1,2,3,4,5]");
 ObjectNode node = mapper.readValue(p, ObjectNode.class);
 mapper.readValue(p, TestClass.class);
 assertNull(property.getValue(testInstance));
 }

 private static class TestClass {
 private java.util.List testList;

 @JsonDeserialize(using = JsonTestListDeserializer.class)
 public List getTestList() {
 return testList;
 }

 public void setTestList(List list) {
 testList = list;
 }
 }

 private static class JsonMissingGetMethodDeserializer extends JsonDeserializer<List> {
 @Override
 public List deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 }

 private static class JsonMissingSetterDeserializer extends JsonDeserializer<List> {
 @Override
 public List deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 ObjectNode node = p.getCodec().readTree(p);
 return BeanUtil.filterNulls(node);
 }
 }

 private static class JsonTestListDeserializer extends JsonDeserializer<List> {
 @Override
 public List deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 ObjectNode node = p.getCodec().readTree(p);
 return BeanUtil.filterNulls(node);
 }
 }
}