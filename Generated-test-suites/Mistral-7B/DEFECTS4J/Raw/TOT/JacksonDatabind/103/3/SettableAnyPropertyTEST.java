package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectMapper mapper;
 private SettableAnyProperty property;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 property = new SettableAnyProperty(
 new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty")),
 new AnnotatedField(MyPojo.class, "anyProperty"),
 mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class),
 null,
 null,
 null);
 }

 @Test
 public void testSettableAnyProperty_StringValue() throws Exception {
 MyPojo pojo = new MyPojo();
 property.deserializeAndSet(mapper.getFactory().createParser("{\"anyProperty\":\"value\"}"), mapper.getDeserializationContext(), pojo, "anyProperty");
 assertEquals("value", pojo.getAnyProperty().get("stringKey"));
 }

 @Test
 public void testSettableAnyProperty_NumberValue() throws Exception {
 MyPojo pojo = new MyPojo();
 property.deserializeAndSet(mapper.getFactory().createParser("{\"anyProperty\":123}"), mapper.getDeserializationContext(), pojo, "anyProperty");
 assertEquals(123, pojo.getAnyProperty().get("numberKey"));
 }

 @Test
 public void testSettableAnyProperty_NullValue() throws Exception {
 MyPojo pojo = new MyPojo();
 property.deserializeAndSet(mapper.getFactory().createParser("{\"anyProperty\":null}"), mapper.getDeserializationContext(), pojo, "anyProperty");
 assertNull(pojo.getAnyProperty().get("nullKey"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyProperty_InvalidValueType() throws Exception {
 MyPojo pojo = new MyPojo();
 property.deserializeAndSet(mapper.getFactory().createParser("{\"anyProperty\":[1,2,3]}"), mapper.getDeserializationContext(), pojo, "anyProperty");
 fail("Expected JsonMappingException but did not receive one");
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyProperty_MissingPropertyName() throws Exception {
 MyPojo pojo = new MyPojo();
 property.deserializeAndSet(mapper.getFactory().createParser("{\"foo\":123}"), mapper.getDeserializationContext(), pojo, null);
 fail("Expected JsonMappingException but did not receive one");
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyProperty_MissingValueDeserializer() throws Exception {
 property = new SettableAnyProperty(
 new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty")),
 new AnnotatedField(MyPojo.class, "anyProperty"),
 mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class),
 null,
 null,
 null);
 MyPojo pojo = new MyPojo();
 property.deserializeAndSet(mapper.getFactory().createParser("{\"anyProperty\":\"value\"}"), mapper.getDeserializationContext(), pojo, "anyProperty");
 fail("Expected JsonMappingException but did not receive one");
 }
}

class MyPojo {
 private Map<String, Object> anyProperty;

 public Map<String, Object> getAnyProperty() { return anyProperty; }
}