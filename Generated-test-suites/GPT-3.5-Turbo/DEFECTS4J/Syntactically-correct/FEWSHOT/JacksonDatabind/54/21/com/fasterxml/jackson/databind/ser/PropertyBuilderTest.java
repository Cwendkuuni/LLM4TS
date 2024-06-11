package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    @Before
    public void setUp() {
        // TODO: Implement setup
    }

    @Test
    public void testBuildWriter() {
        // TODO: Implement test case
    }

    @Test
    public void testFindSerializationType() {
        // TODO: Implement test case
    }

    @Test
    public void testGetDefaultBean() {
        // TODO: Implement test case
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // TODO: Implement test case
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultValue(null));
        assertEquals("", propertyBuilder.getDefaultValue(JavaType.constructFromCanonical("java.lang.String")));
        assertTrue(propertyBuilder.getDefaultValue(JavaType.constructFromCanonical("java.util.Map")).equals(JsonInclude.Include.NON_EMPTY));
    }

    @Test
    public void testThrowWrapped() {
        // TODO: Implement test case
    }
}