package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectNode objectNode;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 objectNode = objectMapper.createObjectNode();
 propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(),
 new BeanDescription(SomeClass.class));
 }

 @Test
 public void testBuildWriter() throws Exception {
 SomeClass someClass = new SomeClass();
 someClass.setName("nameValue");
 someClass.setAge(28);
 someClass.setList(Arrays.asList(1, 2, 3));
 SomeClass someClassInstance = new SomeClass();
 someClassInstance.setName("defaultName");
 someClassInstance.setAge(0);
 someClassInstance.setList(Collections.emptyList());

 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClass.class.getDeclaredField("name"), SomeClass.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true);

 assertEquals("nameValue", beanPropertyWriter.getValueAsString());
 assertEquals("defaultName", propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClass.class.getDeclaredField("name"), SomeClass.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, false).getValueAsString());

 assertEquals(28, propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClass.class.getDeclaredField("age"), SomeClass.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true).getValueAsInt());

 assertEquals(0, propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClass.class.getDeclaredField("age"), SomeClass.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, false).getValueAsInt());

 ObjectWriter writer = objectMapper.writerWithView(MyCustomView.class);
 BeanPropertyWriter beanPropertyWriterWithView = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClass.class.getDeclaredField("name"), SomeClass.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true, writer);
 assertEquals("nameValue", beanPropertyWriterWithView.getValueAsString());

 BeanPropertyWriter beanPropertyWriterWithContentTypeSerializer = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClass.class.getDeclaredField("list"), SomeClass.class),
 ObjectType.StandardClassType.INSTANCE, null, null,
 new SimpleTypeSerializer(TypeFactory.defaultInstance().constructValueType(List.class, Integer.class), false), null, null, true);
 assertEquals("[1, 2, 3]", beanPropertyWriterWithContentTypeSerializer.getValueAsString());

 BeanPropertyWriter beanPropertyWriterListEmpty = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClass.class.getDeclaredField("list"), SomeClass.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true);
 assertNull(beanPropertyWriterListEmpty.getValue());

 BeanPropertyWriter beanPropertyWriterNull = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClass.class.getDeclaredField("list"), SomeClass.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, false);
 assertNull(beanPropertyWriterNull.getValue());
 }

 @Test
 public void testGetClassAnnotations() {
 JsonInclude annotation = propertyBuilder.getClassAnnotations().annotation(JsonInclude.class);
 assertNotNull(annotation);
 assertEquals(JsonInclude.Include.NON_NULL, annotation.value());
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWriterWithNullBeanDescription() {
 propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
 propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getDefaultSerializerProvider(), null, null, null, null, null, null, true);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindSerializationTypeWithNullAnnotatedMember() throws Exception {
 propertyBuilder.findSerializationType(null, false, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindSerializationTypeWithNonAssignableType() throws Exception {
 SomeClassOther someClassOther = new SomeClassOther();
 propertyBuilder.findSerializationType(new AnnotatedMemberImpl(SomeClassOther.class.getDeclaredField("name"), SomeClassOther.class), false, SomeClass.class.getType());
 }

 @Test
 public void testFindSerializationTypeWithValueTypeOverride() throws Exception {
 SomeClassSomeOther someClassSomeOther = new SomeClassSomeOther();
 someClassSomeOther.setName("nameValue");
 someClassSomeOther.setValue(123);
 SomeClassSomeOther someClassSomeOtherInstance = new SomeClassSomeOther();
 SomeClassSomeOther someClassSomeOtherInstanceValue = new SomeClassSomeOther();
 someClassSomeOtherInstanceValue.setValue(0);
 BeanPropertyWriter beanPropertyWriterValueType = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClassSomeOther.class.getDeclaredField("value"), SomeClassSomeOther.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true);
 assertEquals(123, beanPropertyWriterValueType.getValueAsInt());

 JsonSerialize serializer = propertyBuilder.buildSerializer("value", SomeClassSomeOther.class, SomeClassSomeOther.class);
 ObjectWriter writerValueType = objectMapper.writerWith(serializer);
 BeanPropertyWriter beanPropertyWriterValueTypeWithSerializer = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClassSomeOther.class.getDeclaredField("value"), SomeClassSomeOther.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true, writerValueType);
 assertEquals(123, beanPropertyWriterValueTypeWithSerializer.getValueAsInt());

 SomeClassSomeOther someClassSomeOtherWithAnnotation = new SomeClassSomeOther();
 someClassSomeOtherWithAnnotation.setValue(456);
 SomeClassSomeOther someClassSomeOtherInstanceValueAnnotation = new SomeClassSomeOther();
 JsonSerialize annotationSerializer = propertyBuilder.getClassAnnotations().findSerializerByType(SomeClassSomeOther.class);
 ObjectWriter writerValueTypeAnnotation = objectMapper.writerWith(annotationSerializer);
 BeanPropertyWriter beanPropertyWriterValueTypeAnnotation = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClassSomeOther.class.getDeclaredField("value"), SomeClassSomeOther.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true, writerValueTypeAnnotation);
 assertEquals(456, beanPropertyWriterValueTypeAnnotation.getValueAsInt());
 }

 @Test
 public void testFindSerializationTypeWithContentTypeSerializer() throws Exception {
 SomeClassSomeOther someClassSomeOther = new SomeClassSomeOther();
 someClassSomeOther.setName("nameValue");
 someClassSomeOther.setList(Arrays.asList(1, 2, 3));
 SomeClassSomeOther someClassSomeOtherInstance = new SomeClassSomeOther();
 SomeClassSomeOther someClassSomeOtherInstanceValue = new SomeClassSomeOther();
 SomeClassSomeOther someClassSomeOtherListEmpty = new SomeClassSomeOther();
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClassSomeOther.class.getDeclaredField("list"), SomeClassSomeOther.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true);
 assertEquals("[1, 2, 3]", beanPropertyWriter.getValueAsString());

 BeanPropertyWriter beanPropertyWriterContentTypeSerializer = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClassSomeOther.class.getDeclaredField("list"), SomeClassSomeOther.class),
 ObjectType.StandardClassType.INSTANCE, null, null,
 new SimpleTypeSerializer(TypeFactory.defaultInstance().constructValueType(List.class, Integer.class), false), null, null, true);
 assertEquals("[1, 2, 3]", beanPropertyWriterContentTypeSerializer.getValueAsString());

 BeanPropertyWriter beanPropertyWriterListEmptyContentTypeSerializer = propertyBuilder.buildWriter(
 objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
 new BeanPropertyDefinition(SomeClassSomeOther.class.getDeclaredField("list"), SomeClassSomeOther.class),
 ObjectType.StandardClassType.INSTANCE, null, null, null, null, true,
 new SimpleTypeSerializer(TypeFactory.defaultInstance().constructValueType(List.class, Object.class), false));
 assertNull(beanPropertyWriterListEmptyContentTypeSerializer.getValue());
 }

 @Test
 public void testGetDefaultBean() throws Exception {
 SomeClass defaultBean = (SomeClass) propertyBuilder.getDefaultBean();
 assertNull(defaultBean);

 SomeClass someClassDefault = new SomeClass();
 SomeClass someClassDefaultInstance = propertyBuilder.getDefaultBean();
 assertEquals(someClassDefault, someClassDefaultInstance);
 }

 @Test
 public void testGetPropertyDefaultValue() throws Exception {
 SomeClass someClassDefault = new SomeClass();
 SomeClass someClassDefaultInstance = propertyBuilder.getDefaultBean();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("name", null, ObjectType.StandardClassType.INSTANCE);
 assertNull(propertyDefaultValue);

 propertyBuilder._defaultBean = someClassDefaultInstance;
 Object nameDefaultValue = propertyBuilder.getPropertyDefaultValue("name", null, ObjectType.StandardClassType.INSTANCE);
 assertEquals("", nameDefaultValue.toString());
 }
}

class SomeClass {
 @JsonInclude(JsonInclude.Include.NON_NULL)
 private String name;
 @JsonInclude(JsonInclude.Include.NON_NULL)
 private int age;
 @JsonSerialize(using = SomeClassListSerializer.class)
 private List<Integer> list;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }

 public List<Integer> getList() {
 return list;
 }

 public void setList(List<Integer> list) {
 this.list = list;
 }
}

class SomeClassSomeOther {
 @JsonInclude(JsonInclude.Include.NON_NULL)
 private String name;
 @JsonSerialize
 private int value;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getValue() {
 return value;
 }

 public void setValue(int value) {
 this.value = value;
 }
}

class MyCustomView extends StdSerializer<Object> {
 @Override
 public void serializeValues(ObjectWriter writer, JsonGenerator gen, Object value) throws Exception {
 gen.writeValueAsString(value.toString().toUpperCase());
 }
}

class SomeClassListSerializer extends StdSerializer<List<Integer>> {
 @Override
 public void serializeValues(ObjectWriter writer, JsonGenerator gen, List<Integer> value) throws Exception {
 writer.writeFieldName("list");
 writer.writeStartArray();
 for (Integer intValue : value) {
 writer.writeNumber(intValue);
 }
 writer.writeEndArray();
 }
}