package com.example.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() throws JsonMappingException {
 Class<?> cls = MyPojo.class;
 ObjectMapper mapper = new ObjectMapper();
 config = mapper.getSerializationConfig();
 beanDesc = mapper.findBeanDescription(cls);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testGetClassAnnotations() throws JsonMappingException {
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 assertEquals(1, annotations.size());
 JsonInclude annotation = annotations.findAnnotation(JsonInclude.class);
 assertNotNull(annotation);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getBeanProperties().get(0);
 JavaType declaredType = propDef.getType();
 JsonSerializer<?> ser = mapper.findValueSerializer(declaredType, null);
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(bpw);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithNullContentTypeSerializer() throws JsonMappingException {
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getBeanProperties().get(0);
 JavaType declaredType = propDef.getType();
 JsonSerializer<?> ser = mapper.findValueSerializer(declaredType, null);
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 Annotated a = spy(new AnnotatedMember() {
 @Override
 public Class<?> getRawType() {
 return Integer.class;
 }

 @JsonSerialize
 public int getIntValue() {
 return 42;
 }
 });
 JavaType declaredType = TypeFactory.defaultInstance().constructType(MyPojo.class);
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 JavaType serializationType = propertyBuilderSpy.findSerializationType(a, false, declaredType);
 assertEquals(Integer.TYPE, serializationType.getRawType());
 }

 @Test
 public void testGetDefaultBean() {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 }

 @Test
 public void testgetPropertyDefaultValue() throws JsonMappingException {
 String propName = "name";
 AnnotatedMember member = spy(new AnnotatedMember() {
 @Override
 public String getName() {
 return propName;
 }
 });
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object defaultBean = propertyBuilder.getDefaultBean();
 Object defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(defaultValue);
 }

 @Test
 public void testGetDefaultValue() throws JsonMappingException {
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object defaultValue = propertyBuilder.getDefaultValue(type);
 assertNull(defaultValue);
 }

 @Test
 public void testThrowWrapped() throws JsonMappingException {
 Exception e = new IllegalStateException("Test exception");
 String propName = "name";
 Object defaultBean = new Object();
 Object thrown = propertyBuilder.throwWrapped(e, propName, defaultBean);
 fail("Expected IllegalArgumentException");
 }
}

class MyPojo {
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 String name;
}