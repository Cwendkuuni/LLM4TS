package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {
	
	private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(null, null);
    }

    @Test
    public void testGetDefaultBean() {
        assertNull(propertyBuilder.getDefaultBean());
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        assertNull(propertyBuilder.getPropertyDefaultValue("name", null, null));
    }
    
    @Test
    public void testGetDefaultValue() {
        assertEquals("", propertyBuilder.getDefaultValue(null));
    }
    
    // Add more test cases to test all methods in the PropertyBuilder class
    
    @Test
    public void testFindSerializationType() {
        // Test when useStaticTyping is true
        try {
        	propertyBuilder.findSerializationType(null, true, null);
        	fail("Expected Exception");
        } catch (JsonMappingException e) {}
        
        // Test when useStaticTyping is false
        try {
        	propertyBuilder.findSerializationType(null, false, null);
        	fail("Expected Exception");
        } catch (JsonMappingException e) {}
    }
    
    @Test
    public void testGetClassAnnotations() {
    	assertNull(propertyBuilder.getClassAnnotations());
    }
    
    @Test
    public void testBuildWriter() {
    	try {
    		propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
    		fail("Expected Exception");
    	} catch (JsonMappingException e) {}
    }
    
}