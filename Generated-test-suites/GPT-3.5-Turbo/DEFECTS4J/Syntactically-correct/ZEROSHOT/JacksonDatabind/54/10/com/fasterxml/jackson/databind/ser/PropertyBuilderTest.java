package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class PropertyBuilderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Mock
    private JsonSerializer<?> serializer;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private TypeSerializer contentTypeSerializer;

    @Mock
    private AnnotatedMember annotatedMember;
    
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter() throws Exception {
        propertyBuilder.buildWriter(
                prov, propDef, declaredType, serializer, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        propertyBuilder.getDefaultValue(type);
    }

    @Test
    public void test_throwWrapped() throws Exception {
        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }
}