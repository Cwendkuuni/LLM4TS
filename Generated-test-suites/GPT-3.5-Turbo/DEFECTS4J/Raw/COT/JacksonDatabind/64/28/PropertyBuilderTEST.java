package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        propertyBuilder = new PropertyBuilder(null, null);
    }

    @Test
    public void testGetClassAnnotations() {
        propertyBuilder.getClassAnnotations();
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        propertyBuilder.findSerializationType(null, false, null);
    }

    @Test
    public void testGetDefaultBean() {
        propertyBuilder.getDefaultBean();
    }

    @Test
    public void testGetPropertyDefaultValue() {
        propertyBuilder.getPropertyDefaultValue(null, null, null);
    }

    @Test
    public void testGetDefaultValue() {
        propertyBuilder.getDefaultValue(null);
    }

    @Test
    public void testThrowWrapped() {
        propertyBuilder._throwWrapped(null, null, null);
    }
}