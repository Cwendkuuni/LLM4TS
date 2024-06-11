package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    // Initialize Mocks
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Mock
    private Annotations annotations;

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
    
    @Test
    public void testBuildWriter() throws JsonMappingException {
        // Set up test data
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter expected = new BeanPropertyWriter(propDef, am, annotations, declaredType, ser, typeSer, declaredType, true, null);

        // Mocking behavior        
        Mockito.when(beanDesc.findPropertyInclusion(config.getDefaultPropertyInclusion(beanDesc.getBeanClass()))).thenReturn(JsonInclude.Value.empty());
        Mockito.when(config.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
        Mockito.when(propDef.findInclusion()).thenReturn(null);
        Mockito.when(annotationIntrospector.findNullSerializer(am)).thenReturn(null);
        Mockito.when(annotationIntrospector.findUnwrappingNameTransformer(am)).thenReturn(null);
        
        // Execute the method being tested
        BeanPropertyWriter actual = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, true);
        
        // Verify the result 
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Set up test data
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object expected = null;
        
        // Mocking behavior
        Mockito.when(beanDesc.instantiateBean(config.canOverrideAccessModifiers())).thenReturn(null);
        
        // Execute the method being tested
        Object actual = propertyBuilder.getPropertyDefaultValue("name", am, declaredType);
        
        // Verify the result
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultValue() {
        // Set up test data
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object expected = null;
        
        // Mocking behavior
        Mockito.when(declaredType.getRawClass()).thenReturn(Object.class);
        
        // Execute the method being tested
        Object actual = propertyBuilder.getDefaultValue(declaredType);
        
        // Verify the result
        Assert.assertEquals(expected, actual);
    }
}