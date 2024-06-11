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

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() throws Exception {
 config = new SerializationConfig(new AnnotationIntrospector());
 beanDesc = new BeanDescription(ObjectWithProperties.class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testGetClassAnnotations() throws Exception {
 Annotations actual = propertyBuilder.getClassAnnotations();
 Annotations expected = new AnnotatedClass(ObjectWithProperties.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testBuildWriter() throws Exception {
 ObjectWithProperties obj = new ObjectWithProperties();
 ObjectWriter writer = new ObjectMapper().writerWithView(MyCustomView.class);
 SerializerProvider prov = writer.getSerializerProvider();
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(String.class);
 JsonSerializer<String> ser = prov.findValueSerializer(String.class);
 TypeSerializer typeSer = null; // no need for type serializer here
 TypeSerializer contentTypeSer = null; // same
 AnnotatedMember am = new AnnotatedMember(ReflectionUtil.findMethod(ObjectWithProperties.class, "getName"), ObjectWithProperties.class);
 boolean defaultUseStaticTyping = true;
 BeanPropertyWriter actual = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

 assertEquals(propDef, actual.getBeanPropertyDefinition());
 assertEquals(am, actual.getAnnotatedMember());
 assertEquals(ObjectWithProperties.class.getAnnotations(), actual.getClassAnnotations());
 assertEquals(declaredType, actual.getType());
 assertEquals(ser, actual.getSerializer());
 assertEquals(typeSer, actual.getTypeSerializer());
 assertTrue(actual.isSuppressNulls());
 assertNull(actual.getValueToSuppress());
 }

 @Test
 public void testBuildWriterWithContentTypeSerializer() throws Exception {
 ObjectWithProperties obj = new ObjectWithProperties();
 ObjectWriter writer = new ObjectMapper().writerWithView(MyCustomView.class);
 SerializerProvider prov = writer.getSerializerProvider();
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("list");
 JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(List.class);
 JavaType contentType = ObjectTypeFactory.defaultInstance().constructType(Integer.class);
 JsonSerializer<List<Integer>> ser = prov.findValueSerializer(List.class);
 TypeSerializer typeSer = prov.getTypeSerializer();
 TypeSerializer contentTypeSer = prov.getSerializerProvider().findContentSerializer(Integer.class, null);
 AnnotatedMember am = new AnnotatedMember(ReflectionUtil.findMethod(ObjectWithProperties.class, "getList"), ObjectWithProperties.class);
 boolean defaultUseStaticTyping = true;
 BeanPropertyWriter actual = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

 assertEquals(propDef, actual.getBeanPropertyDefinition());
 assertEquals(am, actual.getAnnotatedMember());
 assertEquals(ObjectWithProperties.class.getAnnotations(), actual.getClassAnnotations());
 assertEquals(declaredType, actual.getType());
 assertEquals(ser, actual.getSerializer());
 assertEquals(typeSer, actual.getTypeSerializer());
 assertTrue(actual.isSuppressNulls());
 assertNull(actual.getValueToSuppress());
 }

 @Test
 public void testFindSerializationType() throws Exception {
 AnnotatedMember am = new AnnotatedMember(ReflectionUtil.findMethod(ObjectWithProperties.class, "getName"), ObjectWithProperties.class);
 JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(String.class);
 boolean useStaticTyping = true;
 JavaType actual = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
 assertEquals(declaredType, actual);

 am = new AnnotatedMember(ReflectionUtil.findMethod(ObjectWithProperties.class, "list"), ObjectWithProperties.class);
 declaredType = ObjectTypeFactory.defaultInstance().constructType(List.class);
 useStaticTyping = false;
 actual = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
 assertNull(actual);

 am = new AnnotatedMember(ReflectionUtil.findMethod(ObjectWithProperties.class, "list"), ObjectWithProperties.class);
 declaredType = ObjectTypeFactory.defaultInstance().constructType(List.class);
 useStaticTyping = true;
 actual = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
 assertEquals(declaredType, actual);
 }

 @Test
 public void testGetPropertyDefaultValue() throws Exception {
 ObjectWithProperties obj = new ObjectWithProperties();
 ObjectWriter writer = new ObjectMapper().writerWithView(MyCustomView.class);
 SerializerProvider prov = writer.getSerializerProvider();
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 AnnotatedMember am = new AnnotatedMember(ReflectionUtil.findMethod(ObjectWithProperties.class, "getName"), ObjectWithProperties.class);
 JavaType type = ObjectTypeFactory.defaultInstance().constructType(String.class);
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("name", am, type);
 assertEquals("", defaultValue);

 propDef = beanDesc.findPropertyDefinition("list");
 am = new AnnotatedMember(ReflectionUtil.findMethod(ObjectWithProperties.class, "getList"), ObjectWithProperties.class);
 type = ObjectTypeFactory.defaultInstance().constructType(List.class);
 defaultValue = propertyBuilder.getPropertyDefaultValue("list", am, type);
 assertNotNull(defaultValue);
 assertTrue(defaultValue instanceof ArrayList);
 }

 @Test
 public void testGetDefaultValue() throws Exception {
 JavaType type = ObjectTypeFactory.defaultInstance().constructType(String.class);
 Object defaultValue = propertyBuilder.getDefaultValue(type);
 assertNull(defaultValue);

 type = ObjectTypeFactory.defaultInstance().constructType(List.class);
 defaultValue = propertyBuilder.getDefaultValue(type);
 assertNotNull(defaultValue);
 assertTrue(defaultValue instanceof ArrayList);
 }
}

class MyCustomView extends StandardSerializer<Object> {
 protected MyCustomView() { this(null); }
 protected MyCustomView(Class<Object> t) { super(t); }

 @Override
 public void serializeWithView(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 // custom serialization logic here
 }
}

class ObjectWithProperties {
 private String name;
 private List<Integer> list;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public List<Integer> getList() {
 return list;
 }

 public void setList(List<Integer> list) {
 this.list = list;
 }
}