package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectNode testNode;
 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 testNode = mapper.createObjectNode();
 SetterLookup setterLookup = mapper.getConfig().getAnnotationIntrospector().findProperties(Object.class, null);
 JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
 KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
 JsonDeserializer<Object> valueDeserializer = new StdSerializableJsonDeserializer<>(String.class);
 TypeDeserializer valueTypeDeserializer = null;
 settableAnyProperty = new SettableAnyProperty(
 setterLookup.findProperty("anyProperty"),
 setterLookup.findMethod("setAnyProperty", Object.class),
 javaType,
 keyDeserializer,
 valueDeserializer,
 valueTypeDeserializer);
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 Object target = new Object();
 String value = "test value";
 testNode.put("anyProperty", value);
 settableAnyProperty.deserializeAndSet(testNode.asParser(), new DeserializationContext(new ObjectMapper()), target, "anyProperty");
 assertEquals(value, target.getClass().getField("anyProperty").get(target));
 }

 @Test
 public void testHasValueDeserializer() {
 assertTrue(settableAnyProperty.hasValueDeserializer());
 }

 @Test
 public void testGetType() {
 assertEquals(Object.class, settableAnyProperty.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNullValue() throws JsonProcessingException {
 settableAnyProperty.deserializeAndSet(testNode.asParser(), new DeserializationContext(new ObjectMapper()), null, "anyProperty");
 fail("Should not be able to set null value");
 }

 @Test
 public void testDeserialize() throws JsonProcessingException {
 String value = "test value";
 JsonParser parser = testNode.asParser();
 parser.nextValue(); // consume null value
 Object deserializedValue = settableAnyProperty.deserialize(parser, new DeserializationContext(new ObjectMapper()));
 assertEquals(value, deserializedValue);
 }

 @Test
 public void testSet() throws JsonProcessingException {
 Object target = new Object();
 String value = "test value";
 settableAnyProperty.set(target, "anyProperty", value);
 assertEquals(value, target.getClass().getField("anyProperty").get(target));
 }
}