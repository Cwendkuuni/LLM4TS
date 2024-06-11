package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ArrayBuilders;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    @Mock
    private SerializationConfig _config;

    @Mock
    private BeanDescription _beanDesc;

    @Mock
    private AnnotationIntrospector _annotationIntrospector;

    @Mock
    private SerializedString _propertyName;

    @Mock
    private SerializerProvider _prov;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuildWriter() throws Exception {
        PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);

        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = builder.buildWriter(_prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(bpw);
    }

    @Test
    public void testGetPropertyDefaultValueWithDefaultBean() throws Exception {
        PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
        when(_beanDesc.instantiateBean(true)).thenReturn(this);

        Object defaultValue = builder.getPropertyDefaultValue("test", _propertyName, declaredType);

        assertNotNull(defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithoutDefaultBean() throws Exception {
        PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
        when(_beanDesc.instantiateBean(true)).thenReturn(null);

        builder.getPropertyDefaultValue("test", _propertyName, declaredType);
    }
}