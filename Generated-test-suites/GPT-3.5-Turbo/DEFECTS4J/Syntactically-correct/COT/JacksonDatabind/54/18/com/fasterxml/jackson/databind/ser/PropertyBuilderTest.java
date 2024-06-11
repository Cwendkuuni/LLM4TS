package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    // Test getClassAnnotations method
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNotNull(propertyBuilder.getClassAnnotations());
    }

    // Test buildWriter method
    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }
}