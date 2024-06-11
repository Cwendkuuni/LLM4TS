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

 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() {
SerializationConfig config = new SerializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(DummyClass.class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations actual = propertyBuilder.getClassAnnotations();
 Annotations expected = new StandardAnnotationMap();
 assertEquals(expected, actual);
}

 @Test
 public void testBuildWriter() throws JsonMappingException {
 JavaType declaredType = propertyBuilder._beanDesc.getType();
 BeanPropertyDefinition propDef = propertyBuilder._beanDesc.findPropertyDefinition("dummyProperty");
 JsonSerializer<Object> ser = new JsonSerializer<Object>() {};
 TypeSerializer typeSer = mapper.getTypeSerializer();
 TypeSerializer contentTypeSer = mapper.getTypeSerializer();
 AnnotatedMember am = new AnnotatedMember(null, null, null, null, null);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(propDef, am, new StandardAnnotationMap(), declaredType, ser, typeSer, null, false, null);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, true);
 assertEquals(expectedWriter, actualWriter);
}

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 Annotated a = new AnnotatedMember(null, null, JsonInclude.Include.NON_DEFAULT, null, null);
 JavaType declaredType = propertyBuilder._beanDesc.getType();
 JavaType actual = propertyBuilder.findSerializationType(a, true, declaredType);
 JavaType expected = mapper.getTypeFactory().constructType(String[].class);
 assertEquals(expected, actual);
}

 @Test
 public void testGetDefaultBean() {
 Object actual = propertyBuilder.getDefaultBean();
 Object expected = new DummyClass();
 assertSame(expected, actual);
}

 @Test
 public void testGetPropertyDefaultValue() {
 String name = "dummyProperty";
 AnnotatedMember member = new AnnotatedMember(null, null, null, null, null);
 JavaType type = propertyBuilder._beanDesc.getType();
 Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);
 Object expected = "";
 assertSame(expected, actual);
}

 @Test
 public void testGetDefaultValue() {
 JavaType type = propertyBuilder._beanDesc.getType();
 Object actual = propertyBuilder.getDefaultValue(type);
 assertNull(actual);
}
}

 class DummyClass {
 @JsonSerialize
 private int dummyInt;
 private String dummyString;
 private DummyClass[] dummyArray;
}