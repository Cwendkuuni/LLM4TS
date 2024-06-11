package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(null, null);
    }

    @Test
    public void testGetClassAnnotations() {
        // Test when no annotations are present
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testBuildWriter_noSerializer() throws Exception {
        // Test when there is no serializer
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(writer);
        assertNull(writer.getSerializer());
    }

    @Test
    public void testBuildWriter_noUnwrapper() throws Exception {
        // Test when there is no unwrapper
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(writer);
        assertNull(writer.getUnwrapper());
    }

    @Test
    public void testGetDefaultBean() {
        // Test when default bean is null
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Test when default bean is null
        assertNull(propertyBuilder.getPropertyDefaultValue("test", null, null));
    }

    @Test
    public void testGetDefaultValue() {
        Class<?> intClass = int.class;
        Class<?> stringClass = String.class;

        // Test primitive types
        assertEquals(0, propertyBuilder.getDefaultValue(intClass));
        
        // Test String type
        assertEquals("", propertyBuilder.getDefaultValue(stringClass));
        
        // Test Object type
        assertNull(propertyBuilder.getDefaultValue(Object.class));
    }
}