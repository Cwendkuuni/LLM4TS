package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDescription;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Mock
    private SerializerProvider provider;

    @Mock
    private BeanPropertyDefinition propertyDefinition;

    @Mock
    private JavaType declaredType;

    @Mock
    private JsonSerializer<?> serializer;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private TypeSerializer contentSerializer;

    @Mock
    private AnnotatedMember annotatedMember;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(beanDescription.findPropertyInclusion(config.getDefaultPropertyInclusion(beanDescription.getBeanClass())))
                .thenReturn(JsonInclude.Value.empty());
        when(config.getAnnotationIntrospector()).thenReturn(annotationIntrospector);

        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        when(annotationIntrospector.findNullSerializer(annotatedMember)).thenReturn(null);
        when(annotationIntrospector.findUnwrappingNameTransformer(annotatedMember)).thenReturn(null);
        when(propertyDefinition.findInclusion()).thenReturn(null);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(provider, propertyDefinition, declaredType, serializer,
                typeSerializer, contentSerializer, annotatedMember, false);

        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        JavaType expectedType = null;
        boolean useStaticTyping = false;

        JavaType result = propertyBuilder.findSerializationType(annotatedMember, useStaticTyping, declaredType);

        assertEquals(expectedType, result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String propName = "test";
        Object defaultBean = null;
        JavaType type = declaredType;

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propName, annotatedMember, type);

        assertEquals(null, defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = declaredType;

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertEquals(null, defaultValue);
    }
}