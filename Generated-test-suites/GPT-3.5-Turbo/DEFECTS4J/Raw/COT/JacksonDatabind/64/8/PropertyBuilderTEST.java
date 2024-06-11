
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
    public void testBuildWriter() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.findSerializationType(null, false, null));
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

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._throwWrapped(new Exception(), null, null);
    }
}
