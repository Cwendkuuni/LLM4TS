package com.fasterxml.jackson.databind.ser;

import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter();
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        JavaType serializationType = propertyBuilder.findSerializationType();
        assertNotNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue();
        assertNotNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Object defaultValue = propertyBuilder.getDefaultValue();
        assertNotNull(defaultValue);
    }
}