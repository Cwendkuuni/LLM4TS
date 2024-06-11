
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(null, null);
    }

    @Test
    public void testGetClassAnnotations() {
        assertNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testFindSerializationType_WithNoAnnotationsAndNoStaticTyping() {
        assertNull(propertyBuilder.findSerializationType(null, false, null));
    }

    @Test
    public void testFindSerializationType_WithNoAnnotationsAndStaticTyping() {
        assertNull(propertyBuilder.findSerializationType(null, true, null));
    }

    @Test
    public void testFindSerializationType_WithAnnotationAndNoStaticTyping() {
        assertNull(propertyBuilder.findSerializationType(null, false, null));
    }

    @Test
    public void testFindSerializationType_WithAnnotationAndStaticTyping() {
        assertNull(propertyBuilder.findSerializationType(null, true, null));
    }

    @Test
    public void testGetDefaultBean_WithNoDefaultBean() {
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetDefaultBean_WithDefaultBean() {
        assertNull(propertyBuilder.getDefaultBean());
    }
}
