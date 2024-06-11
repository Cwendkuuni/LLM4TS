package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        // Generate a PropertyBuilder object
        Object obj = new PropertyBuilder(null, null);
        PropertyBuilder propertyBuilder = (PropertyBuilder) obj;

        // Invoke the getClassAnnotations() method
        Annotations annotations = propertyBuilder.getClassAnnotations();

        // Assert the result
        Assert.assertNull(annotations);
    }

    @Test
    public void testBuildWriter() {
        // Generate a PropertyBuilder object
        Object obj = new PropertyBuilder(null, null);
        PropertyBuilder propertyBuilder = (PropertyBuilder) obj;

        // Invoke the buildWriter() method
        try {
            BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
            Assert.assertNull(beanPropertyWriter);
        } catch (JsonMappingException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType() {
        // Generate a PropertyBuilder object
        Object obj = new PropertyBuilder(null, null);
        PropertyBuilder propertyBuilder = (PropertyBuilder) obj;

        // Invoke the findSerializationType() method
        try {
            JavaType javaType = propertyBuilder.findSerializationType(null, false, null);
            Assert.assertNull(javaType);
        } catch (JsonMappingException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Generate a PropertyBuilder object
        Object obj = new PropertyBuilder(null, null);
        PropertyBuilder propertyBuilder = (PropertyBuilder) obj;

        // Invoke the getPropertyDefaultValue() method
        Object result = propertyBuilder.getPropertyDefaultValue("test", null, null);

        // Assert the result
        Assert.assertNull(result);
    }

    @Test
    public void testGetDefaultValue() {
        // Generate a PropertyBuilder object
        Object obj = new PropertyBuilder(null, null);
        PropertyBuilder propertyBuilder = (PropertyBuilder) obj;

        // Invoke the getDefaultValue() method
        Object result = propertyBuilder.getDefaultValue(null);

        // Assert the result
        Assert.assertNull(result);
    }
}