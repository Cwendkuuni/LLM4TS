package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

 private ObjectMapper mapper;
 private SetterlessProperty property;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Class<?> targetClass = new TestTargetClass();
 BeanPropertyDefinition propertyDefinition = new BeanPropertyDefinition(
 "_listProperty", TestTargetClass.class, List.class, null);
 property = new SetterlessProperty(propertyDefinition, mapper.getTypeFactory().constructCollectionType(List.class, Integer.class), null, null, null);
 }

 @Test
 public void testDeserialization_withNullValue() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("_listProperty", null);
 Object targetObject = mapper.readValue(jsonNode, targetClass);
 assertNull(targetObject._listProperty);
 }

 @Test
 public void testDeserialization_withEmptyList() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("_listProperty", mapper.createArrayNode());
 Object targetObject = mapper.readValue(jsonNode, targetClass);
 assertTrue(targetObject._listProperty.isEmpty());
 }

 @Test
 public void testDeserialization_withSingleElementList() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("_listProperty", mapper.createArrayNode().add(1));
 Object targetObject = mapper.readValue(jsonNode, targetClass);
 assertEquals(1, targetObject._listProperty.size());
 assertEquals(1, targetObject._listProperty.get(0).intValue());
 }

 @Test
 public void testDeserialization_withListContainingMultipleElements() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 ObjectNode listNode = jsonNode.putArray("_listProperty");
 listNode.add(1);
 listNode.add(2);
 Object targetObject = mapper.readValue(jsonNode, targetClass);
 assertEquals(2, targetObject._listProperty.size());
 assertEquals(1, targetObject._listProperty.get(0).intValue());
 assertEquals(2, targetObject._listProperty.get(1).intValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetterMethod_call() throws IOException {
 Object targetObject = mapper.readValue(mapper.createObjectText("{\"_listProperty\": [1, 2]}"), targetClass);
 targetObject.set_listProperty(new ArrayList<>()); // This should not be allowed
 }

 @Test
 public void testDeserializerWithTypeDeserializer() throws JsonProcessingException, JsonMappingException {
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(ListType.class);
 JsonNode node = mapper.readTree("{\"_listProperty\": [1, 2]}");
 Object targetObject = mapper.readerForUpdating(new TestTargetClass()).readValue(node);
 assertEquals(List.class, targetObject._listProperty.getClass());
 List<Integer> list = (List<Integer>) targetObject._listProperty;
 assertEquals(2, list.size());
 assertEquals(1, list.get(0).intValue());
 assertEquals(2, list.get(1).intValue());
 }
}

class TestTargetClass {
 List<Integer> _listProperty;
}

class ListType {
}