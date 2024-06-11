package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;
 private SerializationConfig serializationConfig;
 private BeanDescription beanDescription;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 serializationConfig = objectMapper.getSerializationConfig();
 try {
 beanDescription = objectMapper.introspect(DummyClass.class);
 } catch (IntrospectionException e) {
 throw new RuntimeException(e);
 }
 }

 @After
 public void tearDown() {
 objectMapper = null;
 }

 @Test
 public void testBuildWriter() throws JsonMappingException, JsonProcessingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, null, null, null, null, null, false);
 assertNotNull(beanPropertyWriter);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 JavaType declaredType = Mockito.mock(JavaType.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, false, declaredType);
 assertNull(serializationType);
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 public void testGetDefaultBean() throws JsonMappingException, InstantiationException {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 }

 @Test
 public void testGetPropertyDefaultValue() throws InstantiationException, IllegalAccessException, JsonMappingException {
 String propName = "testProperty";
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 JavaType javaType = Mockito.mock(JavaType.class);
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, annotatedMember, javaType);
 assertNull(propertyDefaultValue);
 }

 @Test
 public void testGetDefaultValue() {
 JavaType javaType = Mockito.mock(JavaType.class);
 Object defaultValue = propertyBuilder.getDefaultValue(javaType);
 assertEquals("", defaultValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDefaultValueWithNullType() {
 JavaType javaType = null;
 propertyBuilder.getDefaultValue(javaType);
 }
}

 class DummyClass {
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 String testProperty;
 }