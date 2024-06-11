package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {
  
    private PropertyBuilder propertyBuilder;
    private SerializationConfig serializationConfig;
    private BeanDescription beanDescription;
    
    @Before
    public void setup() {
        serializationConfig = new SerializationConfig();
        beanDescription = new BeanDescription();
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }
  
    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();

        assertNotNull(annotations);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Annotated annotated = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();
        
        JavaType serializationType = propertyBuilder.findSerializationType(annotated, useStaticTyping, declaredType);

        assertNull(serializationType);
    }
  
    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNotNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "testProperty";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertNull(defaultValue);
    }

    @Test
    public void testBuildWriter() throws Exception {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser,
                typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(writer);
    }
}