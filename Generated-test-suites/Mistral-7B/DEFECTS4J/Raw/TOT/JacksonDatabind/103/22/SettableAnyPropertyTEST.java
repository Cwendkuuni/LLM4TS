package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private SettableAnyProperty property;

 @Before
 public void setup() {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(jsonFactory);
 BeanProperty propertyBean = mapper.findProperty("anyProperty", String.class, String.class);
 AnnotatedMember setter = mapper.findSetter(String.class, "anyProperty");
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 KeyDeserializer keyDeserializer = mapper.getFactory().findKeyDeserializer(propertyBean.getName(), null);
 JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class, null);
 TypeDeserializer valueTypeDeserializer = mapper.getTypeFactory().findValueTypeDeserializer(javaType, null);
 property = new SettableAnyProperty(propertyBean, setter, javaType, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 }

 @Test
 public void testSettableAnyPropertyWithValidInput() throws Exception {
 Object pojo = new Object();
 property.deserializeAndSet(jsonFactory.createParser("{\"anyProperty\":\"value\"}"), jsonFactory.getDeserializationContext(), pojo, "anyProperty");
 assertEquals("value", property.getProperty().get(pojo));
 }

 @Test
 public void testSettableAnyPropertyWithNullInput() throws Exception {
 Object pojo = new Object();
 property.deserializeAndSet(jsonFactory.createParser("{\"anyProperty\":null}"), jsonFactory.getDeserializationContext(), pojo, "anyProperty");
 assertNull(property.getProperty().get(pojo));
 }

 @Test
 public void testSettableAnyPropertyWithComplexObjectInput() throws Exception {
 Object pojo = new Object();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 property.deserializeAndSet(jsonFactory.createParser("{\"anyProperty\":{" +
 "\"key1\":\"value1\"," +
 "\"key2\":\"value2\"" +
 "}}"), jsonFactory.getDeserializationContext(), pojo, "anyProperty");
 assertEquals(map, property.getProperty().get(pojo));
 }

 @Test(expected = IOException.class)
 public void testSettableAnyPropertyWithInvalidInput() throws Exception {
 Object pojo = new Object();
 property.deserializeAndSet(jsonFactory.createParser("{\"anyProperty\":123}"), jsonFactory.getDeserializationContext(), pojo, "anyProperty");
 fail("Expected IOException to be thrown, but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettableAnyPropertyWithInvalidPropertyName() throws Exception {
 Object pojo = new Object();
 property.deserializeAndSet(jsonFactory.createParser("{\"invalidProperty\":\"value\"}"), jsonFactory.getDeserializationContext(), pojo, "invalidProperty");
 fail("Expected IllegalArgumentException to be thrown, but no exception was thrown.");
 }
}