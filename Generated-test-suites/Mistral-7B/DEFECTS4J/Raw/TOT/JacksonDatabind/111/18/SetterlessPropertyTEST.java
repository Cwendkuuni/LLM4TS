package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();
 private static final TypeDeserializer NULL_VALUE_PROVIDER = MAPper.getFactory().findValueDeserializer(null, null);

 @Test
 public void testSetterlessProperty_deserializeAndSet() throws IOException {
 SettableBeanProperty property = new SetterlessProperty(
 new BeanPropertyDefinition(new AnnotatedField("testProperty", Integer[].class), null),
 TypeFactory.defaultInstance.constructCollectionType(List.class, Integer.class),
 null, null, new AnnotatedMethod(new Method("getTestProperty", new Class[]{})));

 Object testInstance = new Object() {{
 List<Integer> testProperty = new ArrayList<>();
 }};

 JsonParser parser = MAPPER.getFactory().createParser("[]");
 property.deserializeAndSet(parser, MAPPER.getDeserializationContext(), testInstance);

 assertEquals(0, testInstance.getClass().getDeclaredField("testProperty").get(testInstance).size());
 parser.nextValue(); // Consume "[1, 2, 3]"
 property.deserializeAndSet(parser, MAPPER.getDeserializationContext(), testInstance);
 assertEquals(3, testInstance.getClass().getDeclaredField("testProperty").get(testInstance).size());
 }

 @Test
 public void testSetterlessProperty_deserializeSetAndReturn() throws IOException {
 SettableBeanProperty property = new SetterlessProperty(
 new BeanPropertyDefinition(new AnnotatedField("testProperty", Integer[].class), null),
 TypeFactory.defaultInstance.constructCollectionType(List.class, Integer.class),
 null, null, new AnnotatedMethod(new Method("getTestProperty", new Class[]{})));

 Object testInstance = new Object() {{
 List<Integer> testProperty = new ArrayList<>();
 }};

 JsonParser parser = MAPPER.getFactory().createParser("[]");
 Object result = property.deserializeSetAndReturn(parser, MAPPER.getDeserializationContext(), testInstance);
 assertEquals(testInstance, result);

 parser.nextValue(); // Consume "[1, 2, 3]"
 result = property.deserializeSetAndReturn(parser, MAPPER.getDeserializationContext(), testInstance);
 assertEquals(testInstance, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetterlessProperty_set() throws IOException {
 SettableBeanProperty property = new SetterlessProperty(
 new BeanPropertyDefinition(new AnnotatedField("testProperty", Integer[].class), null),
 TypeFactory.defaultInstance.constructCollectionType(List.class, Integer.class),
 null, null, new AnnotatedMethod(new Method("getTestProperty", new Class[]{})));

 Object testInstance = new Object() {{
 List<Integer> testProperty = new ArrayList<>();
 }};

 property.set(testInstance, new Integer(42));
 }

 @Test
 public void testSetterlessProperty_deserialize_nullValue() throws IOException {
 SettableBeanProperty property = new SetterlessProperty(
 new BeanPropertyDefinition(new AnnotatedField("testProperty", Integer[].class), null),
 TypeFactory.defaultInstance.constructCollectionType(List.class, Integer.class),
 null, null, new AnnotatedMethod(new Method("getTestProperty", new Class[]{})));

 Object testInstance = new Object() {{
 List<Integer> testProperty = new ArrayList<>();
 }};

 JsonParser parser = MAPPER.getFactory().createParser(null);
 property.deserializeAndSet(parser, MAPPER.getDeserializationContext(), testInstance);

 assertNull(testInstance.getClass().getDeclaredField("testProperty").get(testInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testSetterlessProperty_deserialize_invalidJson() throws IOException {
 SettableBeanProperty property = new SetterlessProperty(
 new BeanPropertyDefinition(new AnnotatedField("testProperty", Integer[].class), null),
 TypeFactory.defaultInstance.constructCollectionType(List.class, Integer.class),
 null, null, new AnnotatedMethod(new Method("getTestProperty", new Class[]{})));

 Object testInstance = new Object() {{
 List<Integer> testProperty = new ArrayList<>();
 }};

 JsonParser parser = MAPPER.getFactory().createParser("{ \"invalid\":" + 1 + " }");
 property.deserializeAndSet(parser, MAPPER.getDeserializationContext(), testInstance);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSetterlessProperty_deserialize_jsonNull() throws IOException {
 SettableBeanProperty property = new SetterlessProperty(
 new BeanPropertyDefinition(new AnnotatedField("testProperty", Integer[].class), null),
 TypeFactory.defaultInstance.constructCollectionType(List.class, Integer.class),
 null, null, new AnnotatedMethod(new Method("getTestProperty", new Class[]{})));

 Object testInstance = new Object() {{
 List<Integer> testProperty = new ArrayList<>();
 }};

 JsonParser parser = MAPPER.getFactory().createParser(null);
 property.deserializeAndSet(parser, MAPPER.getDeserializationContext(), testInstance);

 parser.nextValue(); // Consume "null"
 property.deserializeAndSet(parser, MAPPER.getDeserializationContext(), testInstance);

 assertNotNull(testInstance.getClass().getDeclaredField("testProperty").get(testInstance));
 }
}