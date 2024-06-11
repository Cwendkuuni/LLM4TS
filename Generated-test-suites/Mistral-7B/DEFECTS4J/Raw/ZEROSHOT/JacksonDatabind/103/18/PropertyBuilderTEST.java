package com.example.junit;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private SerializationConfig serializationConfig;
 private BeanDescription beanDescription;

 @Before
 public void setUp() {
 serializationConfig = new SerializationConfig();
 beanDescription = new BeanDescription(Object.class);
 propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 Class<?> clazz = String.class;
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = TypeFactory.defaultInstance().constructType(clazz);
 BeanPropertyWriter expectedWriter = mock(BeanPropertyWriter.class);
 when(expectedWriter.getValueSerializer()).thenReturn(serializer);

 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, annotatedMember, false);
 assertEquals(expectedWriter, actualWriter);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterException() throws JsonMappingException {
 Class<?> clazz = String[].class;
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = TypeFactory.defaultInstance().constructType(clazz);

 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, serializer, typeSerializer, null, annotatedMember, false);
 fail("Should throw exception");
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
 Annotated annotated = mock(Annotated.class);
 JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
 when(annotated.getType()).thenReturn(declaredType);
 when(propertyBuilder.findSerializationType(annotated, false, declaredType)).thenReturn(expectedType);
 JavaType actualType = propertyBuilder.findSerializationType(annotated, false, declaredType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeException() throws JsonMappingException {
 Class<?> clazz = String.class;
 Annotated annotated = mock(Annotated.class);
 JavaType declaredType = TypeFactory.defaultInstance().constructType(clazz);
 when(annotated.getType()).thenReturn(declaredType);
 when(propertyBuilder.findSerializationType(annotated, false, declaredType)).thenThrow(new IllegalArgumentException("Illegal concrete-type annotation for method 'testMethod'"));
 JavaType actualType = propertyBuilder.findSerializationType(annotated, false, declaredType);
 fail("Should throw exception");
 }

 @Test
 public void testGetDefaultBean() {
 Object expectedDefaultBean = new Object();
 Object actualDefaultBean = propertyBuilder.getDefaultBean();
 assertNull(actualDefaultBean);
 propertyBuilder.setDefaultBean(expectedDefaultBean);
 Object actualDefaultBean2 = propertyBuilder.getDefaultBean();
 assertEquals(expectedDefaultBean, actualDefaultBean2);
 }

 @Test
 public void testGetPropertyDefaultValue() {
 String name = "testProperty";
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object expectedDefaultValue = "default value";
 when(annotatedMember.getValue(propertyBuilder.getDefaultBean())).thenReturn(expectedDefaultValue);
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, annotatedMember, type);
 assertEquals(expectedDefaultValue, actualDefaultValue);
 }

 @Test(expected = DeprecatedException.class)
 public void testGetPropertyDefaultValueDeprecated() {
 String name = "testProperty";
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, annotatedMember, type);
 fail("Should throw deprecated exception");
 }

 @Test
 public void testGetDefaultValue() {
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object expectedDefaultValue = "default value";
 when(BeanUtil.getDefaultValue(type)).thenReturn(expectedDefaultValue);
 Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
 assertEquals(expectedDefaultValue, actualDefaultValue);
 }
}