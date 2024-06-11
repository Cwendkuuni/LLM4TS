package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyBuilderTest {
    @Test
    public void testBuildWriter() {
        // Test case 1
        try {
            PropertyBuilder builder = new PropertyBuilder(null, null);
            assertNull(builder.buildWriter(null, null, null, null, null, null, null, false));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2
        try {
            PropertyBuilder builder = new PropertyBuilder(null, null);
            assertNotNull(builder.buildWriter(null, null, null, null, null, null, null, true));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}