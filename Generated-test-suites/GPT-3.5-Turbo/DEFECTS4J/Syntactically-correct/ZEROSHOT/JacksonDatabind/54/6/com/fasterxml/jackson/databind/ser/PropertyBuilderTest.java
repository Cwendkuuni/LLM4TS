package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private PropertyBuilder builder;
    
    @Before
    public void setUp() {
        builder = new PropertyBuilder(null, null);
    }
    
    @Test
    public void testGetClassAnnotations() {
        assertNotNull(builder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter() {
        // TODO: Implement this test case
    }

    @Test
    public void testFindSerializationType() {
        // TODO: Implement this test case
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // TODO: Implement this test case
    }

    @Test
    public void testGetDefaultValue() {
        // TODO: Implement this test case
    }

    @Test
    public void testThrowWrapped() {
        // TODO: Implement this test case
    }

}