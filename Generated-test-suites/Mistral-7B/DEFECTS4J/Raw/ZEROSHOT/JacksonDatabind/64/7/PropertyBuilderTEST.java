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
 private Object defaultBean;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() throws JsonMappingException {
 Class<?> testClass = TestClass.class;
 ObjectMapper objectMapper = new ObjectMapper();
 config = objectMapper.getSerializationConfig();
 AnnotationIntrospector annotationIntrospector = config.getDefaultAnnotationIntrospector();
 BeanFactory beanFactory = objectMapper.getBeanFactory();
 ReflectorFactory reflectorFactory = objectMapper.getReflectionFactory();
 BeanDescription.StandardDescBuilder stdBuilder = BeanDescription.standardBuilder(testClass);
 beanDesc = stdBuilder.buildDescription();
 propertyBuilder = new PropertyBuilder(config, beanDesc, annotationIntrospector, beanFactory, reflectorFactory);
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations actual = propertyBuilder.getClassAnnotations();
 Annotations expected = beanDesc.getClassAnnotations();
 assertArrayEquals(expected.getClassAnnotations(), actual.getClassAnnotations());
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 SerializedName serializedName = new SerializedName("name");
 JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
 JsonSerializer<?> jsonSerializer = context -> context.valueAsString();
 TypeSerializer typeSerializer = null;
 TypeSerializer contentTypeSerializer = null;
 AnnotatedMember am = new StubAnnotatedMember("name", null, null, null);
 boolean defaultUseStaticTyping = true;
 BeanPropertyWriter actual = propertyBuilder.buildWriter(null, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);
 BeanPropertyWriter expected = new BeanPropertyWriter(propDef, am, beanDesc.getClassAnnotations(), declaredType, jsonSerializer, null, null, false, null);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithNullDeclaredType() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 JsonSerializer<?> jsonSerializer = context -> context.valueAsString();
 TypeSerializer typeSerializer = null;
 TypeSerializer contentTypeSerializer = null;
 AnnotatedMember am = new StubAnnotatedMember("name", null, null, null);
 boolean defaultUseStaticTyping = true;
 propertyBuilder.buildWriter(null, propDef, null, jsonSerializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithNullPropertyDefinition() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 JsonSerializer<?> jsonSerializer = context -> context.valueAsString();
 TypeSerializer typeSerializer = null;
 TypeSerializer contentTypeSerializer = null;
 AnnotatedMember am = new StubAnnotatedMember("name", null, null, null);
 boolean defaultUseStaticTyping = true;
 BeanPropertyWriter actual = propertyBuilder.buildWriter(prov, null, null, jsonSerializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember am = new StubAnnotatedMember("name", null, new JsonSerializeAnnotation(String.class), null);
 JavaType actual = propertyBuilder.findSerializationType(am, true, null);
 JavaType expected = TypeFactory.defaultInstance().constructType(String.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindSerializationTypeWithNullAnnotatedMember() throws JsonMappingException {
 JavaType actual = propertyBuilder.findSerializationType(null, true, null);
 assertNull(actual);
 }

 @Test
 public void testFindSerializationTypeWithDefaultUseStaticTypingFalse() throws JsonMappingException {
 AnnotatedMember am = new StubAnnotatedMember("name", null, null, null);
 JavaType actual = propertyBuilder.findSerializationType(am, false, null);
 assertNull(actual);
 }

 @Test
 public void testFindSerializationTypeWithDeclaredTypeSuperType() throws JsonMappingException {
 AnnotatedMember am = new StubAnnotatedMember("name", null, new JsonSerializeAnnotation(String[].class), null);
 JavaType actual = propertyBuilder.findSerializationType(am, true, null);
 JavaType expected = TypeFactory.defaultInstance().constructType(String[].class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindSerializationTypeWithAnnotatedMemberSubType() throws JsonMappingException {
 AnnotatedMember am = new StubAnnotatedMember("name", null, new JsonSerializeAnnotation(TestSubClass.class), null);
 JavaType actual = propertyBuilder.findSerializationType(am, true, null);
 JavaType expected = TypeFactory.defaultInstance().constructType(TestSubClass.class);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindSerializationTypeWithAnnotatedMemberTypeMismatch() throws JsonMappingException {
 AnnotatedMember am = new StubAnnotatedMember("name", null, new JsonSerializeAnnotation(Number.class), null);
 propertyBuilder.findSerializationType(am, true, null);
 }

 @Test
 public void testGetDefaultBean() {
 Object actual = propertyBuilder.getDefaultBean();
 assertNull(actual);
 }

 @Test
 public void testGetPropertyDefaultValue() throws Exception {
 String propName = "name";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, null);
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertEquals("", actual);
 }

 @Test
 public void testGetPropertyDefaultValueWithNullMember() throws Exception {
 String propName = "name";
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(propName, null, type);
 assertNull(actual);
 }

 @Test
 public void testGetPropertyDefaultValueWithNullPropertyName() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(null, null, type);
 assertNull(actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValueWithNullDefaultBean() throws Exception {
 String propName = "name";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, null);
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 propertyBuilder.getPropertyDefaultValue(propName, member, type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValueWithAccessException() throws Exception {
 String propName = "name";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, new AccessException("Access exception"));
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 propertyBuilder.getPropertyDefaultValue(propName, member, type);
 }

 @Test
 public void testGetPropertyDefaultValueWithDeclaredTypePrimitive() throws Exception {
 String propName = "intValue";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, null);
 JavaType type = TypeFactory.defaultInstance().constructType(int.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertEquals(0, actual);
 }

 @Test
 public void testGetPropertyDefaultValueWithDeclaredTypeWrapper() throws Exception {
 String propName = "intValue";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, null);
 JavaType type = TypeFactory.defaultInstance().constructType(Integer.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertEquals(0, actual);
 }

 @Test
 public void testGetPropertyDefaultValueWithDeclaredTypeArray() throws Exception {
 String propName = "strings";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, null);
 JavaType type = TypeFactory.defaultInstance().constructType(String[].class);
 Object actual = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertEquals(new String[0], actual);
 }

 @Test
 public void testGetPropertyDefaultValueWithDeclaredTypeMap() throws Exception {
 String propName = "map";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, null);
 JavaType type = TypeFactory.defaultInstance().constructType(Map.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertEquals(Collections.emptyMap(), actual);
 }

 @Test
 public void testGetPropertyDefaultValueWithDeclaredTypeReferenceType() throws Exception {
 String propName = "testClass";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, null);
 JavaType type = TypeFactory.defaultInstance().constructType(TestClass.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(actual);
 }

 @Test
 public void testGetPropertyDefaultValueWithDeclaredTypeNull() throws Exception {
 String propName = "name";
 AnnotatedMember member = new StubAnnotatedMember(propName, null, null, null);
 JavaType type = null;
 Object actual = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(actual);
 }
}

 class StubAnnotatedMember implements AnnotatedMember {

 private final String name;
 private final SerializableWithType serializable;
 private final JsonSerializerAnnotation jsonSerializerAnnotation;
 private final JsonSerializeAnnotation serializationAnnotation;
 private final AccessType accessType;

 public StubAnnotatedMember(String name, SerializableWithType serializable, JsonSerializerAnnotation jsonSerializerAnnotation, JsonSerializeAnnotation serializationAnnotation) {
 this.name = name;
 this.serializable = serializable;
 this.jsonSerializerAnnotation = jsonSerializerAnnotation;
 this.serializationAnnotation = serializationAnnotation;
 this.accessType = AccessType.FIELD;
 }

 @Override
 public String getName() {
 return name;
 }

 @Override
 public Annotated getAnnotated() {
 return null;
 }

 @Override
 public SerializableWithType getSerializableWithType() {
 return serializable;
 }

 @Override
 public JsonSerializerAnnotation getJsonSerializerAnnotation() {
 return jsonSerializerAnnotation;
 }

 @Override
 public JsonSerializeAnnotation getSerializationAnnotation() {
 return serializationAnnotation;
 }

 @Override
 public AccessType getAccessType() {
 return accessType;
 }

 @Override
 public Method getMember() {
 return null;
 }

 @Override
 public Field getField() {
 return null;
 }

 @Override
 public Annotated getParent() {
 return null;
 }

 @Override
 public Annotated getAnnotatingElement() {
 return null;
 }

 @Override
 public boolean isContainer() {
 return false;
 }

 @Override
 public boolean isStatic() {
 return false;
 }

 @Override
 public boolean isLeaf() {
 return true;
 }

 @Override
 public boolean isSynthetic() {
 return false;
 }

 @Override
 public boolean isTransient() {
 return false;
 }
 }

 class TestClass {

 private String name;
 private int intValue;
 private String[] strings;
 private Map<String, Integer> map;
 private TestSubClass testSubClass;

 @JsonSerialize(using = ToStringSerializer.class)
 public String getName() {
 return name;
 }

 public String[] getStrings() {
 return strings;
 }

 public Map<String, Integer> getMap() {
 return map;
 }

 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 public TestSubClass getTestSubClass() {
 return testSubClass;
 }
 }

 class TestSubClass {
 }