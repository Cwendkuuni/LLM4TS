package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanDescription;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ArrayBuilders;

public class PropertyBuilderTest {
    
    @Mock
    private SerializationConfig _config;
    
    @Mock
    private BeanDescription _beanDesc;
    
    @Mock
    private AnnotationIntrospector _annotationIntrospector;
    
    private PropertyBuilder propertyBuilder;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        propertyBuilder = new PropertyBuilder(_config, _beanDesc);
    }
    
    @Test
    public void testBuildWriter() {
        SerializerProvider prov = new ObjectMapper().getSerializerProviderInstance();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = true;
        BeanPropertyWriter writer = null;
        try {
            writer = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        } catch (Exception e) {
            // handle exception
        }
        
        // test the writer
    }
}