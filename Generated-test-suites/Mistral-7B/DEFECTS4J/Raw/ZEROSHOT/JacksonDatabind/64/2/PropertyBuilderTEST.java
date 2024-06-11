package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private SerializationConfig config;
 private BeanDescription beanDescription;
 private AnnotationIntrospector annotationIntrospector;

 @Before
 public void setUp() {
 config = Mockito.mock(SerializationConfig.class);
 beanDescription = Mockito.mock(BeanDescription.class);
 annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
 propertyBuilder = new PropertyBuilder(config, beanDescription);
 }

 @Test
 public void testGetClassAnnotations() {
 assertNotNull(propertyBuilder.getClassAnnotations());
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 SerializerProvider prov = Mockito.mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
 JavaType declaredType = Mockito.mock(JavaType.class);
 JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
 TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());

 propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

 verify(prov, times(1)).serializerInstance(eq(am), any(JsonSerializer.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithException() throws JsonMappingException {
 SerializerProvider prov = Mockito.mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
 JavaType declaredType = Mockito.mock(JavaType.class);
 JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
 TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());
 when(am.getName()).thenReturn("testName");
 doThrow(new JsonMappingException("Test exception")).when(am).getValue(any());

 propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 Annotated a = Mockito.mock(Annotated.class);
 JavaType declaredType = Mockito.mock(JavaType.class);
 when(declaredType.getRawClass()).thenReturn(String[].class);
 JavaType secondary = Mockito.mock(JavaType.class);
 when(secondary.getRawClass()).thenReturn(int[].class);
 when(annotationIntrospector.refineSerializationType(eq(config), eq(a), eq(declaredType))).thenReturn(secondary);

 JavaType result = propertyBuilder.findSerializationType(a, true, declaredType);
 assertEquals(int[].class, result);
 }

 @Test
 public void testFindSerializationTypeWithSameClass() throws JsonMappingException {
 Annotated a = Mockito.mock(Annotated.class);
 JavaType declaredType = Mockito.mock(JavaType.class);
 JavaType secondary = Mockito.mock(JavaType.class);
 when(declaredType.getRawClass()).thenReturn(String.class);
 when(secondary.getRawClass()).thenReturn(String.class);
 when(annotationIntrospector.refineSerializationType(eq(config), eq(a), eq(declaredType))).thenReturn(secondary);

 JavaType result = propertyBuilder.findSerializationType(a, true, declaredType);
 assertEquals(String.class, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeWithDifferentClasses() throws JsonMappingException {
 Annotated a = Mockito.mock(Annotated.class);
 JavaType declaredType = Mockito.mock(JavaType.class);
 JavaType secondary = Mockito.mock(JavaType.class);
 when(declaredType.getRawClass()).thenReturn(String.class);
 when(secondary.getRawClass()).thenReturn(Integer.class);
 when(annotationIntrospector.refineSerializationType(eq(config), eq(a), eq(declaredType))).thenReturn(secondary);

 JavaType result = propertyBuilder.findSerializationType(a, true, declaredType);
 fail("Should throw exception");
 }

 @Test
 public void testGetDefaultBean() {
 Object result = propertyBuilder.getDefaultBean();
 assertNull(result);
 }

 @Test
 public void testGetPropertyDefaultValue() throws Exception {
 String name = "testName";
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 JavaType type = Mockito.mock(JavaType.class);
 Object defaultBean = new Object();
 when(member.getValue(eq(defaultBean))).thenReturn(42);
 when(beanDescription.instantiateBean(anyBoolean())).thenReturn(defaultBean);
 when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());

 Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertEquals(42, result);
 }

 @Test
 public void testGetPropertyDefaultValueWithNullBean() throws Exception {
 String name = "testName";
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 JavaType type = Mockito.mock(JavaType.class);
 when(beanDescription.instantiateBean(anyBoolean())).thenReturn(null);
 when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());

 Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertNull(result);
 }

 @Test
 public void testGetPropertyDefaultValueWithNoDefaultConstructor() throws Exception {
 String name = "testName";
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 JavaType type = Mockito.mock(JavaType.class);
 when(beanDescription.instantiateBean(anyBoolean())).thenThrow(new IllegalStateException());
 when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());

 Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertNull(result);
 }

 @Test
 public void testGetPropertyDefaultValueWithNonDefaultType() throws Exception {
 String name = "testName";
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 JavaType type = Mockito.mock(JavaType.class);
 when(beanDescription.instantiateBean(anyBoolean())).thenReturn(new Object());
 when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());
 when(member.getValue(eq(anyObject()))).thenReturn(new Object());
 when(member.getName()).thenReturn(name);

 Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertNull(result);
 }

 @Test
 public void testGetDefaultValue() {
 Class<?> cls = String[].class;
 Object result = propertyBuilder.getDefaultValue(cls);
 assertNull(result);
 }

 @Test
 public void testGetDefaultValueWithPrimitiveType() {
 Class<?> cls = int.class;
 Object result = propertyBuilder.getDefaultValue(cls);
 assertEquals(0, result);
 }

 @Test
 public void testGetDefaultValueWithStringType() {
 Class<?> cls = String.class;
 Object result = propertyBuilder.getDefaultValue(cls);
 assertEquals("", result);
 }

 @Test
 public void testGetDefaultValueWithContainerType() {
 Class<?> cls = HashMap.class;
 Object result = propertyBuilder.getDefaultValue(cls);
 assertEquals(JsonInclude.Include.NON_EMPTY, result);
 }

 @Test
 public void testGetDefaultValueWithReferenceType() {
 Class<?> cls = String.class;
 Object result = propertyBuilder.getDefaultValue(cls);
 assertEquals(JsonInclude.Include.NON_EMPTY, result);
 }

}