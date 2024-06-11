package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PropertyBuilderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws Exception {
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        Assert.assertNotNull(writer);
    }
}