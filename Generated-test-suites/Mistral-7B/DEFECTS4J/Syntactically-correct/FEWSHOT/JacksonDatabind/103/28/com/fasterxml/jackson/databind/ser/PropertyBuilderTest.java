package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder _propertyBuilder;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        _propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), beanDesc);
    }

    @After
    public void tearDown() {
        _propertyBuilder = null;
    }

    @Test
    public void testFindSerializationType() {
        // Test case for findSerializationType method when annotation is present
        JavaType javaType = _propertyBuilder.getTypeFactory().constructType(String[].class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        when(annotatedMember.getType()).thenReturn(javaType);
        when(annotatedMember.getRawType()).thenReturn(String[].class);
        when(_propertyBuilder.getAnnotationIntrospector().findSerializationType(_propertyBuilder.getSerializationConfig(), annotatedMember, null)).thenReturn(javaType);

        JavaType result = _propertyBuilder.findSerializationType(annotatedMember, true, null);
        assertEquals(javaType, result);

        // Test case for findSerializationType method when annotation is not present
        AnnotatedMember annotatedMember1 = Mockito.mock(AnnotatedMember.class);
        when(annotatedMember1.getType()).thenReturn(javaType);
        when(annotatedMember1.getRawType()).thenReturn(String[].class);
        when(_propertyBuilder.getAnnotationIntrospector().findSerializationType(_propertyBuilder.getSerializationConfig(), annotatedMember1, null)).thenReturn(null);

        JavaType result1 = _propertyBuilder.findSerializationType(annotatedMember1, true, null);
        assertNull(result1);
    }

    @Test
    public void testGetDefaultBean() {
        // Test case for getDefaultBean method when bean is instantiable
        BeanDescription beanDescription = Mockito.mock(BeanDescription.class);
        when(beanDescription.instantiateBean(_propertyBuilder.getSerializationConfig().canOverrideAccessModifiers())).thenReturn(new Object());

        _propertyBuilder = new PropertyBuilder(_propertyBuilder.getSerializationConfig(), beanDescription);
        Object result = _propertyBuilder.getDefaultBean();
        assertNotNull(result);

        // Test case for getDefaultBean method when bean is not instantiable
        BeanDescription beanDescription1 = Mockito.mock(BeanDescription.class);
        when(beanDescription1.instantiateBean(_propertyBuilder.getSerializationConfig().canOverrideAccessModifiers())).thenReturn(null);

        _propertyBuilder = new PropertyBuilder(_propertyBuilder.getSerializationConfig(), beanDescription1);
        Object result1 = _propertyBuilder.getDefaultBean();
        assertNull(result1);
    }

    @Test
    public void testPropertyWriterBuilder() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition beanPropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = _propertyBuilder.getTypeFactory().constructType(String.class);
        JsonSerializer<?> jsonSerializer = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        when(annotatedMember.getValue(eq(Object.class))).thenReturn("Test Value");
        when(annotatedMember.getType()).thenReturn(declaredType);
        when(annotatedMember.getRawType()).thenReturn(String.class);
        when(beanPropertyDefinition.getAccessor()).thenReturn(annotatedMember);

        BeanPropertyWriter beanPropertyWriter = _propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false);

        assertEquals("Test Value", beanPropertyWriter.getValue());
    }
}