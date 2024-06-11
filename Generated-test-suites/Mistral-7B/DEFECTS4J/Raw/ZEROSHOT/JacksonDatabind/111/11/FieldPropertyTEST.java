package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FieldPropertyTest {

 private static class TestClass {
 private int field;
 }

 @Test
 public void testFieldProperty() throws IOException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 TestClass testClass = new TestClass();
 FieldProperty fieldProperty = new FieldProperty(
 new BeanPropertyDefinition(this.getClass().getDeclaredField("testClass")),
 mapper.getTypeFactory().constructType(TestClass.class), null, null,
 new AnnotatedField(this.getClass().getDeclaredField("testClass"),
 this.getClass().getDeclaredField("testClass")));

 // When deserializing from JSON
 String json = "{\"field\": 1}";
 JsonParser parser = mapper.getFactory().createParser(json);
 fieldProperty.deserializeAndSet(parser, new DeserializationContext(mapper, null), testClass);

 // Then
 assertEquals(1, testClass.field);

 // When deserializing from JSON with null value
 json = "{\"field\": null}";
 parser = mapper.getFactory().createParser(json);
 if (fieldProperty._skipNulls) {
 assertTrue(testClass.field == 0);
 } else {
 assertEquals(0, testClass.field);
 }

 // When deserializing from JSON with valueDeserializer
 JsonDeserializer<Integer> intDeserializer = new StandardDeserializer<Integer>(Integer.class) {
 @Override
 public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new Integer(10);
 }
 };
 fieldProperty = new FieldProperty(
 new BeanPropertyDefinition(this.getClass().getDeclaredField("testClass")),
 mapper.getTypeFactory().constructType(TestClass.class), null, intDeserializer,
 new AnnotatedField(this.getClass().getDeclaredField("testClass"),
 this.getClass().getDeclaredField("testClass")));
 json = "{\"field\": 10}";
 parser = mapper.getFactory().createParser(json);
 fieldProperty.deserializeAndSet(parser, new DeserializationContext(mapper, null), testClass);

 // Then
 assertEquals(10, testClass.field);

 // When deserializing from JSON with typeDeserializer
 TypeDeserializer intTypeDeserializer = new StandardTypeDeserializer() {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
 throws IOException {
 return new Integer(15);
 }
 };
 fieldProperty = new FieldProperty(
 new BeanPropertyDefinition(this.getClass().getDeclaredField("testClass")),
 mapper.getTypeFactory().constructType(TestClass.class), intTypeDeserializer, null,
 new AnnotatedField(this.getClass().getDeclaredField("testClass"),
 this.getClass().getDeclaredField("testClass")));
 json = "{\"field\": 15}";
 parser = mapper.getFactory().createParser(json);
 fieldProperty.deserializeAndSet(parser, new DeserializationContext(mapper, null), testClass);

 // Then
 assertEquals(15, testClass.field);

 // When setting value
 ObjectNode node = mapper.createObjectNode();
 node.put("field", 20);
 JsonParser parser2 = mapper.getFactory().createParser(node);
 fieldProperty.set(testClass, parser2);

 // Then
 assertEquals(20, testClass.field);

 // When setting and returning value
 Object returnedInstance = fieldProperty.setAndReturn(testClass, parser2);
 assertEquals(testClass, returnedInstance);
 assertEquals(20, testClass.field);

 // When deserializing from JDK serialization
 TestClass testClassSerialized = new TestClass();
 Field field = TestClass.class.getDeclaredField("field");
 field.setAccessible(true);
 field.set(testClassSerialized, 30);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(objectMapper.writeValueAsBytes(testClassSerialized)));
 fieldProperty = new FieldProperty(fieldProperty);
 fieldProperty.deserializeAndSet(jsonParser, new DeserializationContext(mapper, null), testClass);

 // Then
 assertEquals(30, testClass.field);
 }

 @Test(expected = JsonParseException.class)
 public void testNullValueProvider() throws IOException {
 // Given
 JsonParser parser = mapper.getFactory().createParser("{}");
 FieldProperty fieldProperty = new FieldProperty(
 new BeanPropertyDefinition(this.getClass().getDeclaredField("testClass")),
 mapper.getTypeFactory().constructType(TestClass.class), null, null,
 new AnnotatedField(this.getClass().getDeclaredField("testClass"),
 this.getClass().getDeclaredField("testClass")));

 // When deserializing with null value
 fieldProperty.deserializeAndSet(parser, new DeserializationContext(mapper, null), new TestClass());

 // Then
 fail("Expected JsonParseException");
 }
}