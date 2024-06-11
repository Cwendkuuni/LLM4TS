package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectNode testObject;

 @Before
 public void setUp() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> nullSerializer = mapper.serializerForType(Object.class);
 TypeSerializer stringSerializer = mapper.getTypeSerializer(String.class);

 BeanDescription beanDesc = mapper.findBeanDescription(TestClass.class);
 SerializationConfig config = mapper.getSerializationConfig();
 PropertyBuilder testingPropertyBuilder = new PropertyBuilder(config, beanDesc);
 propertyBuilder = testingPropertyBuilder;

 // Create test object
 TestClass testClass = new TestClass("Test");
 testObject = mapper.valueToTree(testClass);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 Annotations classAnnotations = propertyBuilder.getClassAnnotations();

 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");

 // Test without any serializer or type information
 SerializerProvider prov = mapper.getSerializerProvider();
 JavaType declaredType = mapper.constructType(TestClass.class);
 JsonSerializer<String> stringSerializer1 = mapper.findValueSerializer(String.class);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, stringSerializer1, null, null, null, false);
 assertEquals("name", writer.getName());
 assertEquals(String.class, writer.getType());
 assertEquals(stringSerializer1, writer.getSerializer());
 assertFalse(writer.usesContentTypeSerializer());

 // Test with contentTypeSerializer
 TypeSerializer stringSerializer2 = mapper.getTypeSerializer(String.class);
 BeanPropertyWriter writerWithContentTypeSerializer = propertyBuilder.buildWriter(prov, propDef, declaredType, stringSerializer1, null, stringSerializer2, null, false);
 assertTrue(writerWithContentTypeSerializer.usesContentTypeSerializer());
 assertEquals(stringSerializer2, writerWithContentTypeSerializer.getContentTypeSerializer());

 // Test with JsonInclude.Include.NON_DEFAULT
 JsonInclude.Value defaultInclusion = propertyBuilder._defaultInclusion;
 JsonInclude.Include inclusion = JsonInclude.Include.NON_DEFAULT;
 propertyBuilder._defaultInclusion = new JsonInclude.Value(inclusion);
 Object defaultValue = propertyBuilder.getDefaultValue(declaredType);
 assertEquals(inclusion, propertyBuilder._defaultInclusion.getValueInclusion());
 assertNull(defaultValue);

 // Test with JsonInclude.Include.NON_EMPTY
 inclusion = JsonInclude.Include.NON_EMPTY;
 propertyBuilder._defaultInclusion = new JsonInclude.Value(inclusion);
 defaultValue = propertyBuilder.getDefaultValue(declaredType);
 assertEquals(inclusion, propertyBuilder._defaultInclusion.getValueInclusion());
 assertNotNull(defaultValue);

 // Test getClassAnnotations()
 JsonInclude annotation1 = testClass.getClass().getAnnotation(JsonInclude.class);
 JsonSerialize annotation2 = testClass.getClass().getAnnotation(JsonSerialize.class);
 assertEquals(annotation1, classAnnotations.getAnnotation(JsonInclude.class));
 assertEquals(annotation2, classAnnotations.getAnnotation(JsonSerialize.class));
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWriterNullContentTypeSerializer() throws JsonMappingException {
 TypeSerializer nullContentTypeSerializer = null;
 propertyBuilder.buildWriter(null, null, null, null, null, nullContentTypeSerializer, null, false);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember am = null;
 JavaType declaredType = mapper.constructType(TestClass.class);
 propertyBuilder.findSerializationType(am, false, declaredType);
 }

 @Test
 public void testGetPropertyDefaultValue() throws JsonMappingException {
 String propName = "name";
 String defaultValueString = "defaultName";
 TestClass defaultBean = new TestClass(defaultValueString);
 Object defaultValue = propertyBuilder.getPropertyDefaultValue(propName, null, declaredType);
 assertNull(defaultValue);

 defaultBean.setName(defaultValueString);
 defaultValue = propertyBuilder.getPropertyDefaultValue(propName, null, declaredType);
 assertEquals(defaultValueString, defaultValue);
 }

 @Test
 public void testGetDefaultValue() throws JsonMappingException {
 String defaultValueString = "defaultValue";
 JavaType stringType = mapper.constructType(String.class);
 Object defaultValue = propertyBuilder.getDefaultValue(stringType);
 assertNull(defaultValue);

 JavaType arrayType = mapper.constructType(int[].class);
 defaultValue = propertyBuilder.getDefaultValue(arrayType);
 assertArrayEquals(new int[0], (int[]) defaultValue);
 }
}

class TestClass {
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private String name;

 public TestClass() {
 }

 public TestClass(String name) {
 this.name = name;
 }

 @JsonSerialize
 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
}