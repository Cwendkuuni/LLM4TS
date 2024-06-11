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
        // Add test cases for buildWriter method
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        // Add test cases for findSerializationType method
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        // Add test cases for getPropertyDefaultValue method
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        // Add test cases for getDefaultValue method
    }

    @Test
    public void test_throwWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        // Add test cases for _throwWrapped method
    }

}