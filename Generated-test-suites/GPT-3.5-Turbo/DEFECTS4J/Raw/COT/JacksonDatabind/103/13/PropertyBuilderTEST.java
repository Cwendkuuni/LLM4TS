package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void getClassAnnotationsTest() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        assertNotNull(classAnnotations);
    }

    @Test
    public void buildWriterTest() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;
        
        try {
            BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail();
        }
    }
    
    @Test
    public void findSerializationTypeTest() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail();
        }
    }
    
    @Test
    public void getDefaultBeanTest() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }
    
    @Test
    public void getPropertyDefaultValueTest() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        String name = null;
        AnnotatedMember member = null;
        JavaType type = null;
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(propertyDefaultValue);
    }
    
    @Test
    public void getDefaultValueTest() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        JavaType type = null;
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }
}
