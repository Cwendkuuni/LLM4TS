package com.example.test.jackson;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.ReflectionUtil;
import com.fasterxml.jackson.databind.util.StandardSerializer;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDescription;
    private AnnotationIntrospector annotationIntrospector;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        config = mock(SerializationConfig.class);
        beanDescription = mock(BeanDescription.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        objectMapper = new ObjectMapper();
        ObjectMapper.Initializer initializer = new ObjectMapper.Initializer();
        initializer.setSerializerFactory(new DefaultSerializerFactory(config));
        initializer.setAnnotationIntrospector(annotationIntrospector);
        objectMapper.initialize(initializer);
    }

    @Test
    public void testPropertyBuilder() throws JsonMappingException {
        String className = "com.example.MyClass";
        Class<?> clazz = ReflectionUtil.findClass(className, this.getClass().getClassLoader());
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        BeanPropertyWriter beanPropertyWriter = new PropertyBuilder(config, beanDescription)
                .buildWriter(mock(SerializerProvider.class), null, javaType, null, null, null, null, false).nextUp();
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        String className = "com.example.MyClass";
        Class<?> clazz = ReflectionUtil.findClass(className, this.getClass().getClassLoader());
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        AnnotatedElement annotatedElement = mock(AnnotatedElement.class);
        Method method = mock(Method.class);
        when(annotationIntrospector.refineSerializationType(config, annotatedElement, javaType)).thenReturn(null);
        when(annotationIntrospector.findSerializationTyping(annotatedElement)).thenReturn(JsonSerialize.Typing.DEFAULT_TYPING);
        BeanPropertyWriter beanPropertyWriter = new PropertyBuilder(config, beanDescription)
                .findSerializationType(annotatedElement, false, javaType);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testGetDefaultBean() throws JsonMappingException {
        String className = "com.example.MyClass";
        Class<?> clazz = ReflectionUtil.findClass(className, this.getClass().getClassLoader());
        BeanDescription beanDescription = mock(BeanDescription.class);
        when(beanDescription.instantiateBean(true)).thenReturn(new Object());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetPropertyDefaultValue_nonDefault() throws JsonMappingException {
        String className = "com.example.MyClass";
        Class<?> clazz = ReflectionUtil.findClass(className, this.getClass().getClassLoader());
        BeanDescription beanDescription = mock(BeanDescription.class);
        AnnotatedElement annotatedElement = mock(AnnotatedElement.class);
        when(beanDescription.instantiateBean(true)).thenReturn(new Object());
        when(annotationIntrospector.findNullSerializer(annotatedElement)).thenReturn(null);
        when(annotationIntrospector.findUnwrappingNameTransformer(annotatedElement)).thenReturn(null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", annotatedMember, javaType);
        assertNotNull(propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_default() throws JsonMappingException {
        String className = "com.example.MyClass";
        Class<?> clazz = ReflectionUtil.findClass(className, this.getClass().getClassLoader());
        BeanDescription beanDescription = mock(BeanDescription.class);
        AnnotatedElement annotatedElement = mock(AnnotatedElement.class);
        when(beanDescription.instantiateBean(true)).thenReturn(new Object());
        when(annotationIntrospector.findNullSerializer(annotatedElement)).thenReturn(null);
        when(annotationIntrospector.findUnwrappingNameTransformer(annotatedElement)).thenReturn(null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", annotatedMember, javaType);
        assertNull(propertyDefaultValue);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException, JsonProcessingException {
        String className = "com.example.MyClass";
        Class<?> clazz = ReflectionUtil.findClass(className, this.getClass().getClassLoader());
        BeanDescription beanDescription = mock(BeanDescription.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object valueToSuppress = new Object();
        when(annotationIntrospector.findSerializationType(annotatedMember, false, javaType)).thenReturn(javaType);
        when(annotationIntrospector.findNullSerializer(annotatedMember)).thenReturn(jsonSerializer);
        when(annotationIntrospector.findUnwrappingNameTransformer(annotatedMember)).thenReturn(null);
        when(beanDescription.getClassAnnotations()).thenReturn(new Annotation[0]);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, javaType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false);
        assertNotNull(beanPropertyWriter);
    }
}