package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private ObjectMapper mapper;
 private AnnotationIntrospector annotationIntrospector;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() throws JsonMappingException {
 config = new SerializationConfig(new AnnotationIntrospector());
 mapper = new ObjectMapper(config);
 annotationIntrospector = config.getDefaultAnnotationIntrospector();
 beanDesc = mapper.findBeanDescription(PropertyBuilder.class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testBuildWriterNullDeclaredType() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("_useRealPropertyDefaults");
 SerializerProvider prov = mapper.getSerializerProvider();
 TypeSerializer typeSer = mapper.getTypeSerializer(Integer.class);
 TypeSerializer contentTypeSer = mapper.getTypeSerializer(String.class);
 AnnotatedMember am = null;
 JavaType declaredType = null;
 assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, null, typeSer, contentTypeSer, am, true));
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("buildWriter");
 String propertyName = "buildWriter";
 Class<?> propertyType = BeanPropertyWriter.class;
 JavaType javaType = mapper.getTypeFactory().constructType(propertyType, propDef.getType().getRawClass(), propDef.getType(), JsonSerializer.class, JavaType.class, JavaType.class, TypeSerializer.class, TypeSerializer.class, AnnotatedMember.class, boolean.class);

 JsonSerializer<String> jsonSerializer = mapper.findValueSerializer(String.class);
 JsonSerializer<Integer> jsonSerializerInt = mapper.findValueSerializer(Integer.class);

 TypeSerializer typeSerializer = mapper.getTypeSerializer(String.class);
 TypeSerializer contentTypeSerializer = mapper.getTypeSerializer(Integer.class);

 AnnotatedMember am = mapper.findSerializableAnnotatedMember("_config", Integer.class);
 boolean defaultUseStaticTyping = false;

 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, javaType, jsonSerializerInt, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);
 assertNotNull(beanPropertyWriter);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember am = mapper.findSerializableAnnotatedMember("_config", SerializationConfig.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(SerializationConfig.class);
 assertSame(declaredType, propertyBuilder.findSerializationType(am, true, declaredType));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindSerializationTypeInvalidType() throws JsonMappingException {
 AnnotatedMember am = mapper.findSerializableAnnotatedMember("_config", Object.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(SerializationConfig.class);
 propertyBuilder.findSerializationType(am, true, declaredType);
 }

 @Test
 public void testGetClassAnnotations() {
 assertEquals("Expected class annotations to have size 0", 0, propertyBuilder.getClassAnnotations().size());
 }

 @Test
 public void testGetAnnotationIntrospector() {
 assertEquals("Expected annotation introspector to be instance of com.fasterxml.jackson.databind.util.AnnotationIntrospector", annotationIntrospector, propertyBuilder.getAnnotationIntrospector());
 }

 @Test
 public void testGetDefaultBeanNull() throws JsonMappingException {
 assertEquals("Expected defaultBean to be null", null, propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetDefaultBeanNotNull() throws JsonMappingException {
 Object defaultBean = mapper.findValueSerializer(PropertyBuilder.class).getDefaultValue();
 assertEquals("Expected defaultBean to be instance of PropertyBuilder", defaultBean, propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetPropertyDefaultValueNull() throws JsonMappingException {
 String propertyName = "_useRealPropertyDefaults";
 assertEquals("Expected getPropertyDefaultValue to return null for non-existent property", null, propertyBuilder.getPropertyDefaultValue(propertyName, null, null));
 }

 @Test
 public void testGetPropertyDefaultValueBoolean() throws JsonMappingException {
 String propertyName = "_useRealPropertyDefaults";
 Object defaultBean = mapper.findValueSerializer(PropertyBuilder.class).getDefaultValue();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, mapper.getTypeFactory().constructType(Boolean.TYPE));
 assertTrue("Expected propertyDefaultValue to be true for _useRealPropertyDefaults", propertyDefaultValue != null);
 assertTrue("Expected propertyDefaultValue to be equal to defaultBoolean", propertyDefaultValue.equals(defaultBean));
 }

 @Test
 public void testGetPropertyDefaultValueInt() throws JsonMappingException {
 String propertyName = "_defaultInclusion";
 Object defaultBean = mapper.findValueSerializer(PropertyBuilder.class).getDefaultValue();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, mapper.getTypeFactory().constructType(Integer.TYPE));
 assertEquals("Expected propertyDefaultValue to be equal to defaultInt", 0, propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueString() throws JsonMappingException {
 String propertyName = "_defaultInclusion";
 Object defaultBean = mapper.findValueSerializer(PropertyBuilder.class).getDefaultValue();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, mapper.getTypeFactory().constructType(String.class));
 assertEquals("Expected propertyDefaultValue to be equal to defaultString", "", propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueArray() throws JsonMappingException {
 String propertyName = "_useRealPropertyDefaults";
 Object defaultBean = mapper.findValueSerializer(PropertyBuilder.class).getDefaultValue();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, mapper.getTypeFactory().constructArrayType(Integer.TYPE, 1));
 assertNotNull("Expected propertyDefaultValue to be non-null", propertyDefaultValue);
 assertTrue("Expected propertyDefaultValue to be instance of int[]", propertyDefaultValue instanceof int[]);
 }

 @Test
 public void testGetPropertyDefaultValueList() throws JsonMappingException {
 String propertyName = "_useRealPropertyDefaults";
 Object defaultBean = mapper.findValueSerializer(PropertyBuilder.class).getDefaultValue();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, mapper.getTypeFactory().constructType(JavaType.class, "java.util.ArrayList", "java.lang.Object"));
 assertNotNull("Expected propertyDefaultValue to be non-null", propertyDefaultValue);
 assertTrue("Expected propertyDefaultValue to be instance of java.util.ArrayList", propertyDefaultValue instanceof java.util.ArrayList);
 }

 @Test
 public void testGetPropertyDefaultValueMap() throws JsonMappingException {
 String propertyName = "_useRealPropertyDefaults";
 Object defaultBean = mapper.findValueSerializer(PropertyBuilder.class).getDefaultValue();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, mapper.getTypeFactory().constructType(JavaType.class, "java.util.HashMap", "java.lang.String", "java.lang.Object"));
 assertNotNull("Expected propertyDefaultValue to be non-null", propertyDefaultValue);
 assertTrue("Expected propertyDefaultValue to be instance of java.util.HashMap", propertyDefaultValue instanceof java.util.HashMap);
 }
}