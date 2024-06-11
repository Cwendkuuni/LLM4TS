package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType javaType = TypeFactory.type(Integer[].class, Object.class);
        AnnotatedMember annotatedMember = null;

        Object result = propertyBuilder.findSerializationType(annotatedMember, false, javaType);
        Assert.assertNull(result);

        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        when(annotationIntrospector.refineSerializationType(config, annotatedMember, javaType)).thenReturn(javaType);

        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            protected AnnotationIntrospector _annotationIntrospector = annotationIntrospector;
        };

        result = propertyBuilder.findSerializationType(annotatedMember, true, javaType);
        Assert.assertEquals(javaType, result);

        when(annotationIntrospector.refineSerializationType(config, annotatedMember, javaType)).thenReturn(new TypeReference<Object>() {}.getType());

        result = propertyBuilder.findSerializationType(annotatedMember, true, javaType);
        Assert.assertNull(result);
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object result = propertyBuilder.getDefaultBean();
        Assert.assertNull(result);

        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            Object _defaultBean = new Object();
        };

        result = propertyBuilder.getDefaultBean();
        Assert.assertEquals(propertyBuilder._defaultBean, result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = TypeFactory.type(Integer.class);

        Object result = propertyBuilder.getPropertyDefaultValue("name", annotatedMember, javaType);
        Assert.assertNull(result);

        when(annotatedMember.getValue(Mockito.anyObject())).thenReturn(1);

        result = propertyBuilder.getPropertyDefaultValue("name", annotatedMember, javaType);
        Assert.assertEquals(1, result.intValue());
    }

    @Test
    public void testGetDefaultValue() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType javaType1 = TypeFactory.type(Integer.class);
        JavaType javaType2 = TypeFactory.type(String.class);
        JavaType javaType3 = TypeFactory.type(List.class, Integer.class);

        Object result = propertyBuilder.getDefaultValue(javaType1);
        Assert.assertEquals(0, result);

        result = propertyBuilder.getDefaultValue(javaType2);
        Assert.assertEquals("", result);

        result = propertyBuilder.getDefaultValue(javaType3);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }

    @Test
    public void testBuildWriter() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Annotations annotations = Mockito.mock(Annotations.class);
        JavaType declaredType = TypeFactory.type(Integer.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        when(annotatedMember.getGenericType()).thenReturn(declaredType);

        Object result = propertyBuilder.buildWriter(provider, propDef, declaredType, ser, typeSer, contentTypeSer, annotatedMember, false);
        Assert.assertNull(result);

        when(provider.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)).thenReturn(true);
        when(annotatedMember.fixAccess(true)).thenReturn(annotatedMember);
        when(typeSer.findContentSerializer(declaredType)).thenReturn(ser);

        result = propertyBuilder.buildWriter(provider, propDef, declaredType, ser, typeSer, contentTypeSer, annotatedMember, false);
        Assert.assertNotNull(result);
    }
}