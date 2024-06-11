package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PropertyBuilderTest {

 @BeforeEach
 void setup() {
 ObjectMapper mapper = new ObjectMapper();
 _objectMapper = mapper;
 }

 private ObjectMapper _objectMapper;

 @Test
 void testBuildWriter_SimpleProperty() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(String.class);
 ObjectNode simpleNode = _objectMapper.createObjectNode();
 simpleNode.putPOJO("name", "John Doe");
 BeanPropertyWriter writer = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, Object.class.getType(String.class), null, null, null, null, false
 );
 ObjectWriter objectWriter = _objectMapper.writerWith(new DefaultSerializerProvider())
 .withRootValueSerializer(new StringSerializer())
 .withPropertyWriter(writer);
 ObjectNode actualNode = objectWriter.writeValueAsTree(simpleNode);
 assertEquals("John Doe", actualNode.get("name"));
 }

 @Test
 void testBuildWriter_ArrayProperty() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(int[].class);
 int[] simpleArray = {1, 2, 3};
 BeanPropertyWriter writer = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, int[].class, null, null, null, null, false
 );
 ObjectWriter objectWriter = _objectMapper.writerWith(new DefaultSerializerProvider())
 .withRootValueSerializer(new ArrayNodeSerializer(int[].class))
 .withPropertyWriter(writer);
 ObjectNode actualNode = objectWriter.writeValueAsTree(ArrayBuilders.arrayNode(simpleArray));
 assertArrayEquals(simpleArray, _objectMapper.readValue(actualNode.toString(), new TypeReference<int[]>() {}));
 }

 @Test
 void testBuildWriter_ListProperty() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(java.util.List.class);
 List<Integer> simpleList = Arrays.asList(1, 2, 3);
 BeanPropertyWriter writer = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, java.util.List.class, null, null, null, null, false
 );
 ObjectWriter objectWriter = _objectMapper.writerWith(new DefaultSerializerProvider())
 .withRootValueSerializer(new ArrayNodeSerializer(java.util.List.class))
 .withPropertyWriter(writer);
 ObjectNode actualNode = objectWriter.writeValueAsTree(ValueFactory.arrayNode(new ObjectNode[simpleList.size()]));
 for (int i = 0; i < simpleList.size(); i++) {
 ObjectNode actualNodeElement = (ObjectNode) actualNode.elements().get(i);
 assertEquals(simpleList.get(i), actualNodeElement.get("_value").asInt());
 }
 }

 @Test
 void testBuildWriter_MapProperty() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(java.util.Map.class);
 Map<String, Integer> simpleMap = new HashMap<>();
 simpleMap.put("one", 1);
 simpleMap.put("two", 2);
 BeanPropertyWriter writer = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, java.util.Map.class, null, null, null, null, false
 );
 ObjectWriter objectWriter = _objectMapper.writerWith(new DefaultSerializerProvider())
 .withRootValueSerializer(new MapSerializer(java.util.Map.class))
 .withPropertyWriter(writer);
 ObjectNode actualNode = objectWriter.writeValueAsTree(ValueFactory.objectNode());
 ObjectNode actualNodeKeyOne = (ObjectNode) actualNode.get("one");
 assertEquals("one", actualNodeKeyOne.get("_class").asText());
 assertEquals(1, actualNodeKeyOne.get("_value").asInt());
 ObjectNode actualNodeKeyTwo = (ObjectNode) actualNode.get("two");
 assertEquals("two", actualNodeKeyTwo.get("_class").asText());
 assertEquals(2, actualNodeKeyTwo.get("_value").asInt());
 }

 @Test
 void testBuildWriter_CustomSerializer() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(MyCustomClass.class);
 MyCustomClass customObj = new MyCustomClass();
 BeanPropertyWriter writer = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, MyCustomClass.class, new MyCustomSerializer(), null, null, null, false
 );
 ObjectWriter objectWriter = _objectMapper.writerWith(new DefaultSerializerProvider())
 .withRootValueSerializer(new MyRootSerializer())
 .withPropertyWriter(writer);
 ObjectNode actualNode = objectWriter.writeValueAsTree(customObj);
 assertNotNull(actualNode);
 // Add custom validation logic here
 }

 @Test
 void testBuildWriter_NullProperty() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(String.class);
 BeanPropertyWriter writer = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, Object.class.getType(String.class), null, null, null, null, false
 );
 ObjectWriter objectWriter = _objectMapper.writerWith(new DefaultSerializerProvider())
 .withPropertyWriter(writer);
 ObjectNode actualNode = objectWriter.writeValueAsTree(null);
 assertTrue(actualNode.isNull());
 }

 @Test
 void testBuildWriter_EmptyCollection() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(ArrayList.class);
 BeanPropertyWriter writer = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, ArrayList.class, null, null, null, null, false
 );
 ObjectWriter objectWriter = _objectMapper.writerWith(new DefaultSerializerProvider())
 .withRootValueSerializer(new ArrayNodeSerializer(ArrayList.class))
 .withPropertyWriter(writer);
 ObjectNode actualNode = objectWriter.writeValueAsTree(ValueFactory.arrayNode(new ObjectNode[0]));
 assertTrue(actualNode.isEmpty());
 }

 @Test
 void testBuildWriter_MissingDefaultBean() {
 assertThrows(JsonMappingException.class, () -> {
 BeanDescription beanDesc = new BeanDescription(MyMissingDefaultBean.class);
 new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, MyMissingDefaultBean.class, null, null, null, null, false
 );
 });
 }

 @Test
 void testBuildWriter_NoAccessToDefaultBean() {
 // Assuming MyPrivateBean is a private class
 assertThrows(JsonMappingException.class, () -> {
 BeanDescription beanDesc = new BeanDescription(MyPrivateBean.class);
 new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc).buildWriter(
 _objectMapper.getSerializerProvider(), null, MyPrivateBean.class, null, null, null, null, false
 );
 });
 }

 @AfterEach
 void teardown() {
 _objectMapper = null;
 }
}