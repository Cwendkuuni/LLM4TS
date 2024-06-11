package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {
    
    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    
    @Before
    public void setUp() {
        config = new SerializationConfig();
        beanDesc = new BeanDescription();
        annotationIntrospector = new AnnotationIntrospector();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }
    
    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }
    
    @Test
    public void testBuildWriter() {
        SerializerProvider provider = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = false;
        
        try {
            BeanPropertyWriter writer = propertyBuilder.buildWriter(provider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testFindSerializationType() {
        Annotated a = new Annotated();
        boolean useStaticTyping = false;
        JavaType declaredType = new JavaType();
        
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        String name = "name";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();
        
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(defaultValue);
    }
    
    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType();
        
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }
}