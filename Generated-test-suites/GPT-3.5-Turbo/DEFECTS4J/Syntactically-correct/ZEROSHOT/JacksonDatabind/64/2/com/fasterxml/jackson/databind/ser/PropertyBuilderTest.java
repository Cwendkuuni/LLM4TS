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
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        assertNull(classAnnotations);
    }

    @Test
    public void testBuildWriter() throws Exception {
        Class<?> clazz = Class.forName("com.fasterxml.jackson.databind.ser.BeanPropertyWriter");
        assertNotNull(clazz);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Class<?> clazz = Class.forName("com.fasterxml.jackson.databind.JavaType");
        assertNotNull(clazz);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(null, null, null);
        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        Object defaultValue = propertyBuilder.getDefaultValue(null);
        assertNull(defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        try {
            propertyBuilder._throwWrapped(null, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to get property 'null' of default null instance", e.getMessage());
        }
    }
}