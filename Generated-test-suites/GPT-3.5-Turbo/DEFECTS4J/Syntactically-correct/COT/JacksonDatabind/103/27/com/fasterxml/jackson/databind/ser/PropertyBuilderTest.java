package com.fasterxml.jackson.databind.ser;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, true);
        Assert.assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = propertyBuilder.findSerializationType(null, true, null);
        Assert.assertNull(type);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(null, null, null);
        Assert.assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getDefaultValue(null);
        Assert.assertNull(defaultValue);
    }
}