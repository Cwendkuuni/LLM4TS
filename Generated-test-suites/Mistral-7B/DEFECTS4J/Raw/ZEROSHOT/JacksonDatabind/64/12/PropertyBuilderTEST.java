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
 private ObjectNode node;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() throws Exception {
 config = new SerializationConfig(new AnnotationIntrospectorDefault(new StandardAnnotationIntrospector()));
 ObjectMapper mapper = new ObjectMapper(config);
 node = mapper.createObjectNode();
 }

 @Test
 public void testPropertyBuilder() throws Exception {
 // Test constructor
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(String.class, null), null);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 assertEquals(config, propertyBuilder._config);
 assertEquals(beanDesc, propertyBuilder._beanDesc);

 // Test getClassAnnotations
 Class<?> clazz = String.class;
 Annotations annotations = Mockito.mock(Annotations.class);
 when(beanDesc.getClassAnnotations()).thenReturn(annotations);
 assertEquals(annotations, propertyBuilder.getClassAnnotations());

 // Test buildWriter
 BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
 JavaType declaredType = TypeFactory.uncheckedType(String[].class);
 JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
 TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 when(am.getGenericType()).thenReturn(declaredType);
 when(am.getName()).thenReturn("testProperty");
 JavaType serializationType = declaredType;
 when(propertyBuilder.findSerializationType(am, true, declaredType)).thenReturn(serializationType);
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(new Provider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
 verify(ser, times(1)).getSerializerProvider();
 verify(am, times(1)).getValue(propertyBuilder.getDefaultBean());

 // Test getDefaultBean
 when(beanDesc.instantiateBean(config.canOverrideAccessModifiers())).thenReturn(new String[]{"defaultValue1", "defaultValue2"});
 assertEquals(new String[]{"defaultValue1", "defaultValue2"}, propertyBuilder.getDefaultBean());

 // Test getPropertyDefaultValue
 when(beanDesc.getClassAnnotations()).thenReturn(null);
 when(am.getValue(propertyBuilder.getDefaultBean())).thenReturn("defaultValue");
 assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue("testProperty", am, TypeFactory.uncheckedType(String.class)));

 // Test getDefaultValue
 when(beanDesc.getClassAnnotations()).thenReturn(null);
 when(type.isContainerType()).thenReturn(false);
 when(type.isReferenceType()).thenReturn(false);
 when(type.getRawClass()).thenReturn(Integer.class);
 assertEquals(0, propertyBuilder.getDefaultValue(TypeFactory.uncheckedType(Integer.class)));

 // Test getDefaultValue for String type
 when(type.isContainerType()).thenReturn(false);
 when(type.isReferenceType()).thenReturn(false);
 when(type.getRawClass()).thenReturn(String.class);
 assertEquals("", propertyBuilder.getDefaultValue(TypeFactory.uncheckedType(String.class)));

 // Test getDefaultValue for array type
 when(type.isContainerType()).thenReturn(true);
 when(type.getContentType()).thenReturn(TypeFactory.uncheckedType(Integer.class));
 assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(TypeFactory.uncheckedType(int[].class)));

 // Test getDefaultValue for Map type
 JavaType mapType = TypeFactory.uncheckedType(Map.class);
 when(type.isContainerType()).thenReturn(true);
 when(type.getContentType()).thenReturn(TypeFactory.uncheckedType(String.class));
 when(type.getRawClass()).thenReturn(Map.class);
 assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(mapType));

 // Test getDefaultValue for List type
 JavaType listType = TypeFactory.uncheckedType(List.class);
 when(type.isContainerType()).thenReturn(true);
 when(type.getContentType()).thenReturn(TypeFactory.uncheckedType(String.class));
 when(type.getRawClass()).thenReturn(List.class);
 assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(listType));

 // Test getDefaultValue for CustomType
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 @JsonSerialize(using = CustomSerializer.class)
 static class CustomClass {
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private String testProperty;
 }
 JavaType customType = TypeFactory.uncheckedType(CustomClass.class);
 assertEquals(JsonInclude.Include.NON_DEFAULT, propertyBuilder.getDefaultValue(customType));
 }

 class CustomSerializer extends JsonSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 // Do nothing
 }
}
}