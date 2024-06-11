package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PropertyBuilderTest {
    @Test
    public void testFindSerializationType() {
        // Create PropertyBuilder object
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        
        // Test findSerializationType method
        assertNull(propertyBuilder.findSerializationType(null, true, null));
    }

    @Test
    public void testBuildWriter() {
        // Create PropertyBuilder object
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        // Test buildWriter method
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, true);
        } catch (Exception e) {
            assertEquals("could not determine property type", e.getMessage());
        }
    }
}