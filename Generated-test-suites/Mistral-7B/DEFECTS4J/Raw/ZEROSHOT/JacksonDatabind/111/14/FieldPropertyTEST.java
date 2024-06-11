package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class FieldPropertyTest {

 private static class TestClass {
 private String field;
 }

 @org.junit.BeforeClass
 public static void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestClass instance = mapper.readValue(
 "{ \"field\": \"value\" }", TestClass.class);
 }

 @org.junit.Test
 public void testSetAndGet() throws Exception {
 FieldProperty property = new FieldProperty(null, new BeanPropertyDefinition(
 "field", null, TestClass.class, null), null, null, new AnnotatedField(
 TestClass.class.getDeclaredField("field"), null, null, null, null));

 Object originalValue = instance.field;

 property.set(instance, "newValue");
 assertEquals("newValue", instance.field);
 assertEquals("newValue", property.getValue(instance));

 property.set(instance, originalValue);
 assertEquals(originalValue, instance.field);
 }

 @org.junit.Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser parser = new ObjectMapper().getFactory().createParser(
 "{ \"field\": null }");
 DeserializationContext context = new TestDeserializationContext();
 TestClass instance = new TestClass();
 SettableBeanProperty property = new FieldProperty(null, new BeanPropertyDefinition(
 "field", null, TestClass.class, null), null, null, new AnnotatedField(
 TestClass.class.getDeclaredField("field"), null, null, null, null));

 property.deserializeAndSet(parser, context, instance);
 assertNull(instance.field);

 parser.nextToken(); // consume null value
 String value = "newValue";
 parser.setValue("newValue");
 property.deserializeAndSet(parser, context, instance);
 assertEquals(value, instance.field);
 }

 @org.junit.Test
 public void testDeserializeSetAndReturn() throws Exception {
 JsonParser parser = new ObjectMapper().getFactory().createParser(
 "{ \"field\": null }");
 DeserializationContext context = new TestDeserializationContext();
 TestClass instance = new TestClass();
 SettableBeanProperty property = new FieldProperty(null, new BeanPropertyDefinition(
 "field", null, TestClass.class, null), null, null, new AnnotatedField(
 TestClass.class.getDeclaredField("field"), null, null, null, null));

 Object result = property.deserializeSetAndReturn(parser, context, instance);
 assertSame(instance, result);
 assertNull(instance.field);

 parser.nextToken(); // consume null value
 String value = "newValue";
 parser.setValue("newValue");
 result = property.deserializeSetAndReturn(parser, context, instance);
 assertSame(instance, result);
 assertEquals(value, instance.field);
 }

 @org.junit.Test
 public void testSet() throws Exception {
 TestClass instance = new TestClass();
 SettableBeanProperty property = new FieldProperty(null, new BeanPropertyDefinition(
 "field", null, TestClass.class, null), null, null, new AnnotatedField(
 TestClass.class.getDeclaredField("field"), null, null, null, null));

 String value = "newValue";
 property.set(instance, value);
 assertEquals(value, instance.field);
 }

 @org.junit.Test
 public void testSetWithNullValue() throws Exception {
 TestClass instance = new TestClass();
 SettableBeanProperty property = new FieldProperty(null, new BeanPropertyDefinition(
 "field", null, TestClass.class, null), null, null, new AnnotatedField(
 TestClass.class.getDeclaredField("field"), null, null, null, null));

 property.set(instance, null);
 assertNull(instance.field);
 }

 @org.junit.Test
 public void testSetWithValueDeserializer() throws Exception {
 TestClass instance = new TestClass();
 ObjectMapper mapper = new ObjectMapper();
 JsonDeserializer<String> deserializer = mapper.deserializationConfig()
 .findValueDeserializer(String.class, null);
 SettableBeanProperty property = new FieldProperty(null, new BeanPropertyDefinition(
 "field", null, TestClass.class, null), null, null, new AnnotatedField(
 TestClass.class.getDeclaredField("field"), null, null, null, null), deserializer);

 String value = "newValue";
 property.setValue(instance, value);
 assertEquals(value, instance.field);
 }

 @org.junit.Test
 public void testDeserializeWithTypeDeserializer() throws Exception {
 TestClass instance = new TestClass();
 JsonParser parser = new ObjectMapper().getFactory().createParser(
 "{ \"field\": 42 }");
 DeserializationContext context = new TestDeserializationContext();
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory()
 .constructValueType(Number.class, int.class);
 SettableBeanProperty property = new FieldProperty(null, new BeanPropertyDefinition(
 "field", null, TestClass.class, null), null, null, new AnnotatedField(
 TestClass.class.getDeclaredField("field"), null, null, null, null), null, typeDeserializer);

 Object result = property.deserializeSetAndReturn(parser, context, instance);
 assertSame(instance, result);
 assertEquals(42, instance.field);
 }

 @org.junit.Test
 public void testNullValueProvider() throws Exception {
 TestClass instance = new TestClass();
 NullValueProvider nva = new StandardNullValueProvider();
 SettableBeanProperty property = new FieldProperty(null, new BeanPropertyDefinition(
 "field", null, TestClass.class, null), null, null, new AnnotatedField(
 TestClass.class.getDeclaredField("field"), null, null, null, null), null, null, nva);

 property.deserializeAndSet(parser, context, instance);
 assertNull(instance.field);
 }

 class TestDeserializationContext extends DeserializationContextBase {
 @Override
 public JsonFactory getFactory() {
 return new ObjectMapper().getFactory();
 }

 @Override
 public Object instantiateValue(BeanDescription beanDesc, JavaType type)
 throws InstantiationException, IllegalAccessException, IOException {
 return new Object();
 }

 @Override
 public Object instantiateValue(BeanDescription beanDesc, JavaType type,
 Object valueSource) throws InstantiationException, IllegalAccessException, IOException {
 return new Object();
 }
 }
}