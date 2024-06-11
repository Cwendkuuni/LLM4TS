package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PropertyBuilderTest {
    
    @Test
    public void testClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;
        try {
            propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        } catch (JsonMappingException e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;
        try {
            propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        } catch (JsonMappingException e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String name = null;
        AnnotatedMember member = null;
        JavaType type = null;
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertNull(result);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = null;
        Object result = propertyBuilder.getDefaultValue(type);
        Assert.assertNull(result);
    }
}