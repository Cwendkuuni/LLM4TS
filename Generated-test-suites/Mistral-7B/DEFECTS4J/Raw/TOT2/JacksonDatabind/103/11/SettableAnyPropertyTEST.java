package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

 private SettableAnyProperty _property;
 private Object _pojo;

 @Before
 public void setUp() throws JsonProcessingException {
 _pojo = new HashMap<>();
 _property = new SettableAnyProperty(new BeanProperty(new AnnotatedClass(Map.class), "_dummy"),
 new AnnotatedMethod(Map.class, "put", new TypeReference<Map.Entry<Object, Object>>(){}.getType()),
 new JavaType(Map.class), null, null, null);
 }

 @Test
 public void testBasicSetter() throws IOException {
 Object key = "testKey";
 Object value = "testValue";
 _property.deserializeAndSet(new JsonParser(), new DeserializationContext(), _pojo, key);
 assertEquals(value, _pojo.get(key));
 }

 @Test(expected = IOException.class)
 public void testNullValueDeserializer() throws IOException {
 _property.deserializeAndSet(new JsonParser(), new DeserializationContext(), _pojo, "nullKey");
 fail("Expected IOException but got no exception");
 }

 @Test
 public void testSetterWithTypeDeserializer() throws IOException {
 Object key = "testKey";
 Object value = new AnnotatedClass(String.class).getRawClass();
 _property = new SettableAnyProperty(new BeanProperty(new AnnotatedClass(Map.class), "_dummy"),
 new AnnotatedMethod(Map.class, "put", new TypeReference<Map.Entry<Object, Object>>(){}.getType()),
 new JavaType(Map.class), null, null, new TypeDeserializer() {
 @Override public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsToken().getValueAsString();
 }
 });
 _property.deserializeAndSet(new JsonParser(), new DeserializationContext(), _pojo, key);
 assertEquals("testValue", _pojo.get(key));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidType() throws IOException {
 Object key = "testKey";
 Object value = new Object() {};
 _property = new SettableAnyProperty(new BeanProperty(new AnnotatedClass(Map.class), "_dummy"),
 new AnnotatedMethod(Map.class, "put", new TypeReference<Map.Entry<Object, Object>>(){}.getType()),
 new JavaType(Map.class), null, null, null);
 try {
 _property.deserializeAndSet(new JsonParser(), new DeserializationContext(), _pojo, key);
 fail("Expected JsonMappingException but got no exception");
 } catch (JsonMappingException e) {
 // expected exception
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testUnresolvedForwardReference() throws IOException {
 Object key = "testKey";
 Object value = "testValue";
 _property = new SettableAnyProperty(new BeanProperty(new AnnotatedClass(Map.class), "_dummy"),
 new AnnotatedMethod(Map.class, "put", new TypeReference<Map.Entry<Object, Object>>(){}.getType()),
 new JavaType(Map.class), null, new JsonDeserializer<Object>() {
 @Override public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 throw new UnresolvedForwardReference("TestReference");
 }
 }, null);
 try {
 _property.deserializeAndSet(new JsonParser(), new DeserializationContext(), _pojo, key);
 fail("Expected JsonMappingException but got no exception");
 } catch (JsonMappingException e) {
 // expected exception
 }
 }
}