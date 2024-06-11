package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyBuilderTest {

    // Test for getClassAnnotations method
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNotNull(propertyBuilder.getClassAnnotations());
    }

    // Test for buildWriter method
    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    // Test for findSerializationType method
    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.findSerializationType(null, false, null);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    // Test for getPropertyDefaultValue method
    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.getPropertyDefaultValue(null, null, null);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    // Test for getDefaultValue method
    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.getDefaultValue(null);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    // Test for _throwWrapped method
    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._throwWrapped(new Exception(), "testProp", "testDefaultBean");
    }
}