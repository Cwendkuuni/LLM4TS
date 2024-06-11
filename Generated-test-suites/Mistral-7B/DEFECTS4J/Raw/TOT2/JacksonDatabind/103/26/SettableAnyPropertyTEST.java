package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private static class TestClass {
 private Map<String, Object> map;

 @Settable
 public void setMap(Map<String, Object> map) {
 this.map = map;
 }
 }

 @Test
 public void testBasicUseCase() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{\"key\":\"value\"}");
 TestClass testClass = new TestClass();

 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "map", p, null);
 settableAnyProperty.deserializeAndSet(p, null, testClass, "map");

 assertEquals("value", testClass.map.get("key"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testBasicUseCase_NoSetter() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{\"key\":\"value\"}");
 TestClass testClass = new TestClass();

 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "notSettable", p, null);
 settableAnyProperty.deserializeAndSet(p, null, testClass, "notSettable");

 assertNull(testClass.map);
 }

 @Test
 public void testEdgeCase_NullInput() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("null");
 TestClass testClass = new TestClass();

 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "map", p, null);
 settableAnyProperty.deserializeAndSet(p, null, testClass, "map");

 assertNull(testClass.map);
 }

 @Test
 public void testEdgeCase_EmptyJsonObject() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{}");
 TestClass testClass = new TestClass();

 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "map", p, null);
 settableAnyProperty.deserializeAndSet(p, null, testClass, "map");

 assertTrue(testClass.map.isEmpty());
 }

 @Test
 public void testEdgeCase_NullValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{\"key\":null}");
 TestClass testClass = new TestClass();

 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "map", p, null);
 settableAnyProperty.deserializeAndSet(p, null, testClass, "map");

 assertNull(testClass.map.get("key"));
 }

 @Test
 public void testEdgeCase_ValueTypeMismatch() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{\"key\":1}");
 TestClass testClass = new TestClass();

 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "map", p, null);
 settableAnyProperty.deserializeAndSet(p, null, testClass, "map");

 assertTrue(testClass.map.isEmpty());
 }

 @Test
 public void testExceptionHandling_ClassCastException() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{" +
 "  \"key1\":{\"subKey\":1},\n" +
 "  \"key2\":\"value2\"" +
 "}");
 TestClass testClass = new TestClass();
 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "map", p, null);

 try {
 settableAnyProperty.deserializeAndSet(p, null, testClass, "key1");
 fail("Expected ClassCastException");
 } catch (ClassCastException e) {
 // expected
 }

 assertEquals(1, testClass.map.size());
 assertEquals(1, testClass.map.get("key1").toString().split("\n").length);
 assertEquals("1", testClass.map.get("key1").toString().split("\n")[0]);
 assertNull(testClass.map.get("key2"));
 }

 @Test
 public void testExceptionHandling_IOException() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{\"key\":\"value\"}");
 TestClass testClass = new TestClass();
 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "map", p, null);

 String originalErrorMessage = settableAnyProperty.deserialize(p, null).getMessage();

 p.nextToken(); // consume next token to cause IOException

 try {
 settableAnyProperty.deserializeAndSet(p, null, testClass, "map");
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 assertEquals(originalErrorMessage, e.getMessage());
 }

 assertEquals(0, testClass.map.size());
 }

 @Test
 public void testExceptionHandling_JsonMappingException() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{\"key\":\"value\"}");
 TestClass testClass = new TestClass();
 SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty(TestClass.class, "notSettable", p, null);

 try {
 settableAnyProperty.deserializeAndSet(p, null, testClass, "notSettable");
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }

 assertNull(testClass.map);
 }
}