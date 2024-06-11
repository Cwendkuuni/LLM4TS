package com.fasterxml.jackson.databind.ser;

import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {
    
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNotNull(propertyBuilder.getClassAnnotations());
    }
    
    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        } catch (Exception e) {
            fail("Exception thrown during buildWriter test: " + e.getMessage());
        }
    }
    
    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            assertNull(propertyBuilder.findSerializationType(null, false, null));
        } catch (Exception e) {
            fail("Exception thrown during findSerializationType test: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getPropertyDefaultValue(null, null, null));
    }
    
    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultValue(null));
    }
    

}