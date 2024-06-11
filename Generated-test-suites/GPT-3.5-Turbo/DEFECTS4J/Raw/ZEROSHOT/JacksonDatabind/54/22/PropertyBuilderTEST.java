package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(null, null);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() {
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
            fail("Exception thrown");
        }
    }

    @Test
    public void testFindSerializationType() {
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;

        try {
            propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetDefaultValueWithPrimitive() {
        JavaType type = null;
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }

    @Test
    public void testGetPropertyValueWithNonPrimitive() {
        JavaType type = null;
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }
    
}