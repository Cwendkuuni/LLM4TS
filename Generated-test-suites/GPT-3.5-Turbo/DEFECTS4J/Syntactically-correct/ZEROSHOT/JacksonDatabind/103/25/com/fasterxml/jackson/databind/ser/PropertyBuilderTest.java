package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PropertyBuilderTest {
    @Mock
    private SerializationConfig _config;
    @Mock
    private BeanDescription _beanDesc;
    @Mock
    private AnnotationIntrospector _annotationIntrospector;
    @Mock
    private SerializerProvider prov;
    @Mock
    private BeanPropertyDefinition propDef;
    @Mock
    private JavaType declaredType;
    @Mock
    private JsonSerializer<?> ser;
    @Mock
    private TypeSerializer typeSer;
    @Mock
    private TypeSerializer contentTypeSer;
    @Mock
    private AnnotatedMember am;

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        propertyBuilder = new PropertyBuilder(_config, _beanDesc);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanPropertyWriter bpw = new BeanPropertyWriter(propDef, am,
                _beanDesc.getClassAnnotations(), declaredType, ser,
                typeSer, null, true, null, null);

        when(propDef.findInclusion()).thenReturn(null);
        when(_annotationIntrospector.findSerializationType(am, false, declaredType)).thenReturn(declaredType);
        when(_annotationIntrospector.refineSerializationType(_config, am, declaredType)).thenReturn(null);
        when(declaredType.getRawClass()).thenReturn(Object.class);
        
        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer,
                contentTypeSer, am, true);

        verify(propDef).findInclusion();
        verify(_annotationIntrospector).findSerializationType(am, false, declaredType);
        verify(_annotationIntrospector).refineSerializationType(_config, am, declaredType);
        verify(declaredType).getRawClass();
    }
}