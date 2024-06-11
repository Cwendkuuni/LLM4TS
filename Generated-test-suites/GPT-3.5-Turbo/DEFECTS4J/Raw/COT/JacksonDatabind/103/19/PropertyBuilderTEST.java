
package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNotNull(builder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        BeanPropertyWriter writer = builder.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        try {
            builder.findSerializationType(null, false, null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getPropertyDefaultValue(null, null, null));
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(null, null);
        assertNull(builder.getDefaultValue(null));
    }
}

