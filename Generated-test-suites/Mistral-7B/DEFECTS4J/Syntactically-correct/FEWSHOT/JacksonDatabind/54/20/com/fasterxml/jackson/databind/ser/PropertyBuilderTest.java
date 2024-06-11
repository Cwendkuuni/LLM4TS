package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        JavaType javaType = TypeFactory.uncheckedType(String[].class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getGenericType()).thenReturn(javaType);

        SerializationConfig serializationConfig = mock(SerializationConfig.class);
        when(serializationConfig.isEnabled(SerializationFeature.FORCE_DEFAULT_VALUES)).thenReturn(false);

        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        when(annotationIntrospector.refineSerializationType(eq(serializationConfig), eq(annotatedMember), eq(javaType))).thenReturn(javaType);

        PropertyBuilder.PropertyBuilderSpi propertyBuilderSpi = mock(PropertyBuilder.PropertyBuilderSpi.class);
        when(propertyBuilder.getBuilderInstance()).thenReturn(propertyBuilderSpi);

        JavaType actualType = propertyBuilder.findSerializationType(annotatedMember, false, javaType);

        assertEquals(javaType, actualType);

        // Test case for annotation override
        JsonSerialize annotation = mock(JsonSerialize.class);
        when(annotation.type()).thenReturn(TypeFactory.uncheckedType(Integer.class));
        when(annotatedMember.getAnnotation(JsonSerialize.class)).thenReturn(annotation);

        actualType = propertyBuilder.findSerializationType(annotatedMember, false, javaType);

        assertEquals(TypeFactory.uncheckedType(Integer.class), actualType);

        // Test case for static typing
        when(annotationIntrospector.refineSerializationType(eq(serializationConfig), eq(annotatedMember), eq(javaType))).thenReturn(null);
        when(serializationConfig.canOverrideAccessModifiers()).thenReturn(true);
        when(beanDesc.instantiateBean(true)).thenReturn(new Object());

        actualType = propertyBuilder.findSerializationType(annotatedMember, true, javaType);

        assertEquals(javaType.withStaticTyping(), actualType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.uncheckedType(String.class);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getGenericType()).thenReturn(declaredType);

        ObjectPropertyWriter objectPropertyWriter = mock(ObjectPropertyWriter.class);
        when(jsonSerializer.asPropertyWriter(eq(serializerProvider), eq(beanPropertyDefinition), eq(typeSerializer), eq(contentTypeSerializer), anyObject(), anyBoolean())).thenReturn(objectPropertyWriter);

        PropertyBuilder.PropertyBuilderSpi propertyBuilderSpi = mock(PropertyBuilder.PropertyBuilderSpi.class);
        when(propertyBuilder.getBuilderInstance()).thenReturn(propertyBuilderSpi);

        Object valueToSuppress = null;
        boolean suppressNulls = false;

        JsonInclude.Value inclusion = JsonInclude.Value.defaultValue();
        JsonInclude.Include inclusionType = inclusion.getValueInclusion();

        PropertyBuilder.BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, suppressNulls);

        if (inclusionType == JsonInclude.Include.NON_DEFAULT) {
            valueToSuppress = getPropertyDefaultValue(propertyBuilder, beanPropertyDefinition, declaredType);
            suppressNulls = true;
        }

        assertEquals(objectPropertyWriter, propertyWriter);
        assertEquals(valueToSuppress, propertyWriter.getValueToSuppress());
        assertEquals(suppressNulls, propertyWriter.isSuppressNulls());
    }

    private Object getPropertyDefaultValue(PropertyBuilder propertyBuilder, BeanPropertyDefinition beanPropertyDefinition, JavaType declaredType) {
        Object defaultBean = propertyBuilder.getDefaultBean();
        if (defaultBean == null) {
            Object defaultValue = getDefaultValue(declaredType);
            return defaultValue;
        }
        try {
            return beanPropertyDefinition.getMember().getValue(defaultBean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getDefaultValue(JavaType type) {
        Class<?> rawClass = type.getRawClass();
        if (rawClass.isPrimitive()) {
            return ClassUtil.defaultValue(rawClass);
        }
        if (type.isContainerType() || type.isReferenceType()) {
            return JsonInclude.Include.NON_EMPTY;
        }
        if (type.getRawClass() == String.class) {
            return "";
        }
        return null;
    }
}