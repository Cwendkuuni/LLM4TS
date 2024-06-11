
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {
    
    private PropertyBuilder propertyBuilder;
    
    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
    }
    
    @Test
    public void testGetClassAnnotations() {
        assertNotNull(propertyBuilder.getClassAnnotations());
    }
    
    @Test
    public void testBuildWriter() {
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, true);
            // Assertion for the method functionality can be added here
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
    
    @Test
    public void testFindSerializationType() {
        try {
            propertyBuilder.findSerializationType(null, true, null);
            // Assertion for the method functionality can be added here
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(null, null, null);
        // Assertion for the method functionality can be added here
        
        assertNull(defaultValue);
    }
    
    @Test
    public void testGetDefaultValue() {
        Object defaultValue = propertyBuilder.getDefaultValue(null);
        // Assertion for the method functionality can be added here
        
        assertNull(defaultValue);
    }
    
    @Test
    public void testThrowWrapped() {
        try {
            propertyBuilder._throwWrapped(new Exception(), null, null);
            // Assertion for the method functionality can be added here
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }
}
