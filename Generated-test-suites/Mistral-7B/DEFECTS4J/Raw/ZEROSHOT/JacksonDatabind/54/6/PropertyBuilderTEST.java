package com.example.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testPropertyBuilder() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();

 // when
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

 // then
 assertNotNull(propertyBuilder);
 assertEquals(config, propertyBuilder._config);
 assertEquals(beanDescription, propertyBuilder._beanDesc);
 // default inclusion value should be ALWAYS for properties that do not have any inclusion annotation
 assertEquals(JsonInclude.Include.ALWAYS, propertyBuilder._defaultInclusion.getValueInclusion());
 assertNotNull(propertyBuilder._annotationIntrospector);
 }

 @Test
 public void testGetClassAnnotations() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

 // when
 Annotations annotations = propertyBuilder.getClassAnnotations();

 // then
 assertEquals(beanDescription.getClassAnnotations(), annotations);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 ObjectWriter writer = objectMapper.writerFor(beanSnapshot.getClass());
 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "age");

 // when
 SerializerProvider serializerProvider = writer.getSerializerProvider();
 JsonSerializer<BeanSnapshot> jsonSerializer = writer.findValueSerializer(BeanSnapshot.class, null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
 TypeSerializer contentTypeSerializer = objectMapper.getTypeSerializer();
 AnnotatedMember am = ReflectionTestUtils.findAnnotatedMembers(BeanSnapshot.class, JsonSerialize.class, "name")[0];
 BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition("name");
 JavaType declaredType = objectMapper.getTypeFactory().constructType(String.class);
 BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, am, true);

 // then
 assertNotNull(propertyWriter);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterException() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 ObjectWriter writer = objectMapper.writerFor(beanSnapshot.getClass());
 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "age");

 // when
 SerializerProvider serializerProvider = writer.getSerializerProvider();
 JsonSerializer<BeanSnapshot> jsonSerializer = writer.findValueSerializer(BeanSnapshot.class, null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
 TypeSerializer contentTypeSerializer = objectMapper.getTypeSerializer();
 AnnotatedMember am = ReflectionTestUtils.findAnnotatedMembers(BeanSnapshot.class, JsonInclude.class, "name")[0];
 BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition("age");
 JavaType declaredType = objectMapper.getTypeFactory().constructType(Integer.class);
 BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, am, true);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 AnnotatedMember am = ReflectionTestUtils.findAnnotatedMembers(BeanSnapshot.class, JsonSerialize.class, "name")[0];
 JavaType declaredType = objectMapper.getTypeFactory().constructType(String.class);

 // when
 JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);

 // then
 assertEquals(declaredType, serializationType);
 }

 @Test
 public void testFindSerializationTypeWithJsonSerializerAnnotation() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 AnnotatedMember am = ReflectionTestUtils.findAnnotatedMembers(BeanSnapshot.class, JsonSerialize.class, "age")[0];
 JavaType declaredType = objectMapper.getTypeFactory().constructType(Integer.class);

 // when
 JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);

 // then
 assertEquals(Integer.TYPE, serializationType.getRawClass());
 }

 @Test
 public void testGetDefaultBean() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

 // when
 Object defaultBean = propertyBuilder.getDefaultBean();

 // then
 assertNull(defaultBean);
 }

 @Test
 public void testGetPropertyDefaultValue() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

 // when
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("name", ReflectionTestUtils.findAnnotatedMembers(BeanSnapshot.class, JsonSerialize.class, "name")[0], objectMapper.getTypeFactory().constructType(String.class));

 // then
 assertEquals("", defaultValue);
 }

 @Test
 public void testGetDefaultValue() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

 // when
 Object defaultValue = propertyBuilder.getDefaultValue(objectMapper.getTypeFactory().constructType(List.class));

 // then
 assertNull(defaultValue);
 }

 @Test
 public void testGetDefaultValueForList() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

 // when
 Object defaultValue = propertyBuilder.getDefaultValue(objectMapper.getTypeFactory().constructType(List.class).mappedType(String.class));

 // then
 assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
 }

 @Test
 public void testGetDefaultValueForMap() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

 // when
 Object defaultValue = propertyBuilder.getDefaultValue(objectMapper.getTypeFactory().constructType(Map.class.getName(), String.class, String.class));

 // then
 assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
 }

 @Test
 public void testGetDefaultValueForMapWithValueTypeAnnotation() throws JsonMappingException, JsonProcessingException {
 // given
 BeanSnapshot beanSnapshot = new BeanSnapshot();
 BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(BeanSnapshot.class).getBeanDescription();
 SerializationConfig config = objectMapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

 // when
 Object defaultValue = propertyBuilder.getDefaultValue(objectMapper.getTypeFactory().constructType("Map<String, @JsonInclude(Include.NON_NULL)>]"));

 // then
 assertEquals(JsonInclude.Include.NON_NULL, defaultValue);
 }

}

class BeanSnapshot {
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private String name;
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private int age;
}