
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import org.junit.Test;

public class PropertyBuilderTest {
    
    // Test getClassAnnotations() method
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNotNull(propertyBuilder.getClassAnnotations());
    }
    
    // Test buildWriter() method
    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
            // Add assertion here
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test findSerializationType() method
    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.findSerializationType(null, false, null);
            // Add assertion here
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test getDefaultBean() method
    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.getDefaultBean();
            // Add assertion here
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test getPropertyDefaultValue() method
    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.getPropertyDefaultValue(null, null, null);
            // Add assertion here
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test getDefaultValue() method
    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.getDefaultValue(null);
            // Add assertion here
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test _throwWrapped() method
    @Test
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder._throwWrapped(null, null, null);
            // Add assertion here
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
}
