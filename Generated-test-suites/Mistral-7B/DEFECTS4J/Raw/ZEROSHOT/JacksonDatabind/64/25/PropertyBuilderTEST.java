package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PropertyBuilderTest {

  private ObjectMapper objectMapper;
  private SerializationConfig serializationConfig;
  private BeanDescription beanDescription;
  private AnnotationIntrospector annotationIntrospector;

  @Before
  public void setup() {
    objectMapper = new ObjectMapper();
    serializationConfig = mock(SerializationConfig.class);
    beanDescription =
        new AnnotatedClass(MyClass.class).findAnnotation(JsonInclude.class).getMemberValue();
    annotationIntrospector = mock(AnnotationIntrospector.class);

    when(serializationConfig.getDefaultPropertyInclusion(MyClass.class, JsonInclude.Value.empty()))
        .thenReturn(JsonInclude.Value.empty());
    when(serializationConfig.getDefaultPropertyInclusion(MyClass.class, any(TypeReference.class)))
        .thenReturn(JsonInclude.Value.builder().include(JsonInclude.Include.NON_DEFAULT).build());
    when(annotationIntrospector.findPropertySerializer(any(), any()))
        .thenReturn(mock(JsonSerializer.class));
    when(annotationIntrospector.findNullSerializer(any()))
        .thenReturn(mock(JsonSerializer.class));
    when(annotationIntrospector.findUnwrappingNameTransformer(any()))
        .thenReturn(NameTransformer.IDENTITY);
    when(annotationIntrospector.findSerializationType(any(), anyBoolean(), any(JavaType.class)))
        .thenReturn(new TypeReference<MyClass>() {}.getType());
    when(annotationIntrospector.refineSerializationType(serializationConfig, any(), any()))
        .thenReturn(new TypeReference<MyClass>() {}.getType());
  }

  @Test
  public void testBuildWriter() throws JsonMappingException {
    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    ObjectWriter objectWriter =
        objectMapper.getSerializerProvider().getWriter("", new MyClass(), null, null, null);
    ObjectWriter.DefaultSerializerProvider delegate =
        (ObjectWriter.DefaultSerializerProvider) objectWriter;
    BeanPropertyWriter beanPropertyWriter =
        propertyBuilder.buildWriter(
            delegate,
            mock(BeanPropertyDefinition.class),
            new TypeReference<MyClass>() {}.getType(),
            mock(JsonSerializer.class),
            mock(TypeSerializer.class),
            mock(TypeSerializer.class),
            mock(AnnotatedMember.class),
            true);

    verify(annotationIntrospector, times(1)).findSerializationType(any(), anyBoolean(), any(JavaType.class));
    verify(annotationIntrospector, times(1)).refineSerializationType(serializationConfig, any(), any());
  }

  @Test
  public void testBuildWriter_NullContentTypeSerializer() throws JsonMappingException {
    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    ObjectWriter objectWriter =
        objectMapper.getSerializerProvider().getWriter("", new MyClass(), null, null, null);
    ObjectWriter.DefaultSerializerProvider delegate =
        (ObjectWriter.DefaultSerializerProvider) objectWriter;
    BeanPropertyWriter beanPropertyWriter =
        propertyBuilder.buildWriter(
            delegate,
            mock(BeanPropertyDefinition.class),
            new TypeReference<MyClass>() {}.getType(),
            mock(JsonSerializer.class),
            null,
            mock(TypeSerializer.class),
            mock(AnnotatedMember.class),
            true);

    verify(annotationIntrospector, never()).refineSerializationType(any(), any(), any());
  }

  @Test
  public void testBuildWriter_EmptyArray() throws JsonMappingException {
    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    ObjectWriter objectWriter =
        objectMapper.getSerializerProvider().getWriter("", new MyClass[0], null, null, null);
    ObjectWriter.DefaultSerializerProvider delegate =
        (ObjectWriter.DefaultSerializerProvider) objectWriter;
    BeanPropertyWriter beanPropertyWriter =
        propertyBuilder.buildWriter(
            delegate,
            mock(BeanPropertyDefinition.class),
            new TypeReference<MyClass[]>() {}.getType(),
            mock(JsonSerializer.class),
            mock(TypeSerializer.class),
            null,
            mock(AnnotatedMember.class),
            true);

    verify(annotationIntrospector, times(1)).findSerializationType(any(), anyBoolean(), any(JavaType.class));
    verify(annotationIntrospector, times(1)).refineSerializationType(serializationConfig, any(), any());
  }

  @Test
  public void testFindSerializationType() throws JsonMappingException {
    Object propertyValue = new Object() {};

    JavaType javaType = new TypeReference<MyClass>() {}.getType();

    BeanPropertyWriter beanPropertyWriter = mock(BeanPropertyWriter.class);

    when(annotationIntrospector.findSerializationType(any(), anyBoolean(), javaType))
        .thenReturn(javaType);

    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    JavaType actualType = propertyBuilder.findSerializationType(mock(AnnotatedMember.class), false, javaType);

    assertSame(javaType, actualType);
  }

  @Test
  public void testFindSerializationType_NoAnnotation() throws JsonMappingException {
    Object propertyValue = new Object() {};

    JavaType javaType = new TypeReference<MyClass>() {}.getType();

    BeanPropertyWriter beanPropertyWriter = mock(BeanPropertyWriter.class);

    when(annotationIntrospector.findSerializationType(any(), anyBoolean(), javaType))
        .thenReturn(null);

    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    JavaType actualType = propertyBuilder.findSerializationType(mock(AnnotatedMember.class), false, javaType);

    assertNull(actualType);
  }

  @Test
  public void testGetClassAnnotations() throws Exception {
    JsonInclude jsonInclude = new JsonInclude(Include.NON_EMPTY);
    Method method = MyClassWithJsonInclude.class.getMethod("getIncludedProperty");
    AnnotatedMember annotatedMember = new AnnotatedMethod(method, MyClassWithJsonInclude.class);
    when(annotationIntrospector.findClassAnnotations(MyClassWithJsonInclude.class))
        .thenReturn(Collections.singletonList(jsonInclude));

    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    Annotations actualAnnotations = propertyBuilder.getClassAnnotations();

    assertEquals(1, actualAnnotations.size());
    assertEquals(jsonInclude, actualAnnotations.get(0));
  }

  @Test
  public void testGetPropertyDefaultValue() throws Exception {
    Object defaultBean = new Object() {};
    String propName = "propertyName";

    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    Object actualValue = propertyBuilder.getPropertyDefaultValue(propName, mock(AnnotatedMember.class), new TypeReference<MyClass>() {}.getType());

    assertNull(actualValue);
  }

  @Test
  public void testGetPropertyDefaultValue_WithDefaultBean() throws Exception {
    Object defaultBean = new Object() {};
    String propName = "propertyName";

    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    when(beanDescription.instantiateBean(true)).thenReturn(defaultBean);

    Object actualValue = propertyBuilder.getPropertyDefaultValue(propName, mock(AnnotatedMember.class), new TypeReference<MyClass>() {}.getType());

    assertSame(defaultBean, actualValue);
  }

  @Test
  public void testGetPropertyDefaultValue_NoDefaultBean() throws Exception {
    String propName = "propertyName";

    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    Object actualValue = propertyBuilder.getPropertyDefaultValue(propName, mock(AnnotatedMember.class), new TypeReference<MyClass>() {}.getType());

    assertNull(actualValue);
  }

  @Test
  public void testGetDefaultValue() throws Exception {
    JavaType javaType = new TypeReference<MyClass>() {}.getType();

    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    Object actualValue = propertyBuilder.getDefaultValue(javaType);

    assertSame(JsonInclude.Include.NON_EMPTY, actualValue);
  }

  @Test
  public void testGetDefaultValue_PrimitiveType() throws Exception {
    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    Object actualValue = propertyBuilder.getDefaultValue(int.class);

    assertEquals(0, actualValue);
  }

  @Test
  public void testGetDefaultValue_StringType() throws Exception {
    PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

    Object actualValue = propertyBuilder.getDefaultValue(String.class);

    assertEquals("", actualValue);
  }

  static class MyClass {
    private String property;
  }

  static class MyClassWithJsonInclude {
    @JsonInclude(Include.NON_EMPTY)
    private String includedProperty;

    public String getIncludedProperty() {
      return includedProperty;
    }
  }
}