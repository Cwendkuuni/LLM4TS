package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PropertyBuilderTest {
    @Test
    public void testGetDefaultBean() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getPropertyDefaultValue("", null, null));
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getDefaultValue(null));
    }
}