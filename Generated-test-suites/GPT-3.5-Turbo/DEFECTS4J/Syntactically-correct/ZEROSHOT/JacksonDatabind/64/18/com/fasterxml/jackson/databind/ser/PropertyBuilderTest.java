package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    @Mock
    private SerializationConfig mockConfig;

    @Mock
    private BeanDescription mockBeanDesc;

    @Mock
    private AnnotationIntrospector mockAnnotationIntrospector;

    @Mock
    private Object mockDefaultBean;

    @Mock
    private SerializerProvider mockProvider;

    @Mock
    private BeanPropertyDefinition mockPropertyDef;

    @Mock
    private JavaType mockDeclaredType;

    @Mock
    private JsonSerializer<?> mockSerializer;

    @Mock
    private TypeSerializer mockTypeSerializer;

    @Mock
    private TypeSerializer mockContentTypeSerializer;

    @Mock
    private AnnotatedMember mockMember;

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        propertyBuilder = new PropertyBuilder(mockConfig, mockBeanDesc);
        propertyBuilder._defaultBean = mockDefaultBean;
        propertyBuilder._annotationIntrospector = mockAnnotationIntrospector;
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanPropertyWriter result = propertyBuilder.buildWriter(mockProvider, mockPropertyDef, mockDeclaredType, mockSerializer, mockTypeSerializer, mockContentTypeSerializer, mockMember, false);

        Assert.assertNotNull(result);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        JavaType result = propertyBuilder.findSerializationType(mockMember, false, mockDeclaredType);

        Assert.assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Object result = propertyBuilder.getPropertyDefaultValue("name", mockMember, mockDeclaredType);

        Assert.assertNull(result);
    }

    @Test
    public void testGetDefaultValue() {
        Object result = propertyBuilder.getDefaultValue(mockDeclaredType);

        Assert.assertNull(result);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations result = propertyBuilder.getClassAnnotations();

        Assert.assertNotNull(result);
    }
}