package com.example.propertybuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
 public void setup() {
 Class<?> testClass = MyPojo.class;
 ObjectMapper mapper = new ObjectMapper();
 config = mapper.getSerializerProvider().getConfig();
 beanDesc = mapper.constructType(testClass).getBeanDescription();
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations anns = propertyBuilder.getClassAnnotations();
 assertNotNull(anns);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("propertyName");
 JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
 JsonSerializer<String> ser = mapper.findValueSerializer(String.class);
 TypeSerializer contentTypeSer = null;
 TypeSerializer typeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = true;
 BeanPropertyWriter writer = propertyBuilder.buildWriter(config.getDefaultSerializerProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(writer);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithInvalidSerializationType() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("propertyName");
 JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
 JsonSerializer<String[]> ser = mapper.findValueSerializer(String[].class);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(config.getDefaultSerializerProvider(), propDef, declaredType, ser, null, null, null, true);
 fail("Should have thrown exception");
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember am = null;
 JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
 assertNull(propertyBuilder.findSerializationType(am, true, declaredType));
 }

 @Test
 public void testFindSerializationTypeWithJsonSerializeAnnotation() throws JsonMappingException {
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getRawType()).thenReturn(MyList.class);
 when(am.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize());
 JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
 JsonSerializer<String[]> jsonSerializer = mock(JsonSerializer.class);
 when(mapper.findValueSerializer(String[].class)).thenReturn(jsonSerializer);
 JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
 assertEquals(String[].class, serializationType.getRawClass());
 }

 @Test
 public void testGetDefaultBean() {
 assertNull(propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetPropertyDefaultValue() {
 String propName = "propertyName";
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition(propName);
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propName);
 when(member.getValue(anyObject())).thenReturn("defaultValue");
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object defaultBean = propertyBuilder.getDefaultBean();
 Object value = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertEquals("defaultValue", value);
 }

 @Test(expected = AssertionError.class)
 public void testGetPropertyDefaultValueWithNullDefaultBean() {
 String propName = "propertyName";
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition(propName);
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propName);
 when(member.getValue(anyObject())).thenThrow(new RuntimeException());
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object defaultBean = null;
 Object value = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 fail("Should have thrown AssertionError");
 }

 @Test
 public void testGetDefaultValue() {
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object value = propertyBuilder.getDefaultValue(type);
 assertEquals("", value);
 }

 @Test
 public void testThrowWrapped() {
 Exception e = new RuntimeException();
 String propName = "propertyName";
 Object defaultBean = new Object();
 Object throwable = propertyBuilder.throwWrapped(e, propName, defaultBean);
 fail("Should have thrown IllegalArgumentException");
 }

}

class MyPojo {
 // Add any required annotations and fields here
}

class MyList extends ArrayList<String> {
 // Add any required annotations and fields here
}