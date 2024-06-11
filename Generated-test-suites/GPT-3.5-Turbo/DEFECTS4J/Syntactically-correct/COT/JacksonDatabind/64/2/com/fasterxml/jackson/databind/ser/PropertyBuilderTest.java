
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import org.junit.Test;

public class PropertyBuilderTest {
    
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }
    
    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

}