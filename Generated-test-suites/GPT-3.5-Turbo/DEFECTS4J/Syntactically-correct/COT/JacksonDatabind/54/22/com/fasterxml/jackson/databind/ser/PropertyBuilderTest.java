package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        } catch (Exception e) {
            fail("Caught unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder.findSerializationType(null, false, null);
        } catch (Exception e) {
            fail("Caught unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultBean());
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

    @Test
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            propertyBuilder._throwWrapped(null, null, null);
        } catch (Exception e) {
            fail("Caught unexpected exception: " + e.getMessage());
        }
    }
}