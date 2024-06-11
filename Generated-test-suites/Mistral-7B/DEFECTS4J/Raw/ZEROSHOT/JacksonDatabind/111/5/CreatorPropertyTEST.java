package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector.Finder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

public class CreatorPropertyTest {

 private static class TestBean {
 @JsonProperty("name1")
 private String name1;
 @JsonProperty("name2")
 private CreatorProperty creatorProperty;

 public TestBean(String name1, CreatorProperty creatorProperty) {
 this.name1 = name1;
 this.creatorProperty = creatorProperty;
 }

 public String getName1() {
 return name1;
 }

 public CreatorProperty getCreatorProperty() {
 return creatorProperty;
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestCreatorProperty extends CreatorProperty<String, CreatorProperty> {
 public TestCreatorProperty(PropertyName name, JavaType type, PropertyName wrapperName,
 TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param,
 int index, Object injectableValueId, PropertyMetadata metadata) {
 super(name, type, wrapperName, typeDeser, contextAnnotations, param, index,
 injectableValueId, metadata);
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestJsonDeserializer extends JsonDeserializer<CreatorProperty> {
 private final ObjectMapper objectMapper;

 public TestJsonDeserializer(ObjectMapper objectMapper) {
 this.objectMapper = objectMapper;
 }

 @Override
 public CreatorProperty deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return new TestCreatorProperty(p, ctxt);
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestTypeDeserializer extends TypeDeserializer {
 private final Converter<Object, CreatorProperty> converter;

 public TestTypeDeserializer(Converter<Object, CreatorProperty> converter) {
 this.converter = converter;
 }

 @Override
 public CreatorProperty deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return converter.convert(p.getValueAsToken());
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestPropertyMetadata implements PropertyMetadata {
 @Override
 public JsonDeserializer<?> getEmptyDeserializer() {
 return null;
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestAnnotatedParameter extends AnnotatedParameter {
 public TestAnnotatedParameter(int index) {
 super(null, null, null, null, null, null, null, null);
 this.index = index;
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestContextAnnotations extends Annotations {
 @Override
 public JsonDeserializer<?> findValueDeserializer(DeserializationConfig config,
 PropertyName name, JavaType type) {
 return null;
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestAnnotatedMember extends AnnotatedMember {
 @Override
 public AnnotatedMember getBaseMember() {
 return null;
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestFinder implements Finder<AnnotatedMember> {
 @Override
 public AnnotatedMember findAnnotationWithValue(Class<?> annType,
 AnnotatedMember member, DeserializationContext context) {
 return null;
 }

 @Override
 public AnnotatedMember findAnnotationWithValue(Class<?> annType,
 Class<?> baseType, DeserializationContext context) {
 return null;
 }

 @Override
 public AnnotatedMember findAnnotationWithValue(Class<?> annType,
 PropertyName name, DeserializationContext context) {
 return null;
 }
 }

 @org.junit.Before
 public void setUp() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setAnnotationIntrospector(new AnnotationIntrospector() {
 @Override
 public void findProperties(BeanDescription beanDesc, List<BeanPropertyDefinition> propDefs) {
 for (BeanPropertyDefinition def : propDefs) {
 if (def.getName().getShortName().equals("creatorProperty")) {
 def.setValueDeserializer(new TestJsonDeserializer(objectMapper));
 def.setValueTypeDeserializer(new TestTypeDeserializer(
 new StdConverter<Object, CreatorProperty>(new TestCreatorPropertyFactory())));
 break;
 }
 }
 }
 });
anObjectMapper = objectMapper;
}

@org.junit.Test
public void testCreatorPropertyNullValue() throws Exception {
 // test null value injection
 String json = "{\"name1\":\"test\",\"creatorProperty\":null}";
 TestBean testBean = objectMapper.readValue(json, TestBean.class);
 assertNull(testBean.getCreatorProperty());
}

@org.junit.Test
public void testCreatorPropertyValue() throws Exception {
 // test value injection
 String json = "{\"name1\":\"test\",\"creatorProperty\":{\"name\":\"creatorProperty\",\"value\":\"testValue\"}}";
 TestBean testBean = objectMapper.readValue(json, TestBean.class);
 assertEquals("testValue", testBean.getCreatorProperty().getValue());
}

@org.junit.Test
public void testCreatorPropertyIgnore() throws Exception {
 // test ignore flag
 String json = "{\"name1\":\"test\",\"creatorProperty\":{\"name\":\"creatorProperty\",\"_ignorable\":true}}";
 TestBean testBean = objectMapper.readValue(json, TestBean.class);
 assertNull(testBean.getCreatorProperty());
}

@org.junit.Test
public void testCreatorPropertyInjectWithoutValueDeserializer() throws Exception {
 // test injection without value deserializer
 TestJsonDeserializer jsonDeserializerMock = mock(TestJsonDeserializer.class);
 when(jsonDeserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(null);
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setAnnotationIntrospector(new AnnotationIntrospector() {
 @Override
 public void findProperties(BeanDescription beanDesc, List<BeanPropertyDefinition> propDefs) {
 for (BeanPropertyDefinition def : propDefs) {
 if (def.getName().getShortName().equals("creatorProperty")) {
 def.setValueDeserializer(jsonDeserializerMock);
 def.setValueTypeDeserializer(null);
 break;
 }
 }
 }
 });
 String json = "{\"name1\":\"test\",\"creatorProperty\":{\"name\":\"creatorProperty\"}}";
 TestBean testBean = objectMapper.readValue(json, TestBean.class);
 assertNull(testBean.getCreatorProperty());
}

@org.junit.Test
public void testCreatorPropertyInjectWithValueDeserializerNoValue() throws Exception {
 // test injection with value deserializer but no value
 TestJsonDeserializer jsonDeserializerMock = mock(TestJsonDeserializer.class);
 when(jsonDeserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(null);
 TestTypeDeserializer typeDeserializerMock = mock(TestTypeDeserializer.class);
 when(typeDeserializerMock.deserializeTypedFromAny(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(null);
 when(typeDeserializerMock.getNullValueHandler()).thenReturn(null);
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setAnnotationIntrospector(new AnnotationIntrospector() {
 @Override
 public void findProperties(BeanDescription beanDesc, List<BeanPropertyDefinition> propDefs) {
 for (BeanPropertyDefinition def : propDefs) {
 if (def.getName().getShortName().equals("creatorProperty")) {
 def.setValueDeserializer(jsonDeserializerMock);
 def.setValueTypeDeserializer(typeDeserializerMock);
 break;
 }
 }
 }
 });
 String json = "{\"name1\":\"test\",\"creatorProperty\":{\"name\":\"creatorProperty\"}}";
 TestBean testBean = objectMapper.readValue(json, TestBean.class);
 assertNull(testBean.getCreatorProperty());
}

@org.junit.Test
public void testCreatorPropertyInjectWithValueDeserializerValue() throws Exception {
 // test injection with value deserializer and value
 TestJsonDeserializer jsonDeserializerMock = mock(TestJsonDeserializer.class);
 when(jsonDeserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(new TestCreatorProperty("creatorProperty", ClassUtil.constructType(CreatorProperty.class),
 null, null, null, null, -1, null, new TestPropertyMetadata()));
 TestTypeDeserializer typeDeserializerMock = mock(TestTypeDeserializer.class);
 when(typeDeserializerMock.deserializeTypedFromAny(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(new TestCreatorProperty("creatorProperty", ClassUtil.constructType(CreatorProperty.class),
 null, null, null, null, -1, null, new TestPropertyMetadata()));
 when(typeDeserializerMock.getNullValueHandler()).thenReturn(null);
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setAnnotationIntrospector(new AnnotationIntrospector() {
 @Override
 public void findProperties(BeanDescription beanDesc, List<BeanPropertyDefinition> propDefs) {
 for (BeanPropertyDefinition def : propDefs) {
 if (def.getName().getShortName().equals("creatorProperty")) {
 def.setValueDeserializer(jsonDeserializerMock);
 def.setValueTypeDeserializer(typeDeserializerMock);
 break;
 }
 }
 }
 });
 String json = "{\"name1\":\"test\",\"creatorProperty\":{\"name\":\"creatorProperty\",\"value\":{\"name\":\"value\",\"value\":\"testValue\"}}}";
 TestBean testBean = objectMapper.readValue(json, TestBean.class);
 TestCreatorProperty creatorProperty = testBean.getCreatorProperty();
 assertEquals("testValue", creatorProperty.getValue().getValue());
}
}

class TestCreatorPropertyFactory implements Converter.ValueConverter<Object, CreatorProperty> {
 @Override
 public CreatorProperty convert(Object value) {
 return new TestCreatorProperty(new PropertyName("creatorProperty"),
 ClassUtil.constructType(CreatorProperty.class), null, null, null, null, -1, null, new TestPropertyMetadata());
 }
}