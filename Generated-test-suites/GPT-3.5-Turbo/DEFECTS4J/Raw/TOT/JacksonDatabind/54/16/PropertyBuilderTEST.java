package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        propertyBuilder = new PropertyBuilder(null, null);
    }

    @Test
    public void testGetPropertyDefaultValueForString() {
        // Arrange
        JavaType stringType = new JavaType(String.class);

        // Act
        Object defaultValue = propertyBuilder.getDefaultValue(stringType);

        // Assert
        Assert.assertEquals("", defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueForPrimitiveInt() {
        // Arrange
        JavaType intType = new JavaType(int.class);

        // Act
        Object defaultValue = propertyBuilder.getDefaultValue(intType);

        // Assert
        Assert.assertEquals(0, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueForWrapperInteger() {
        // Arrange
        JavaType integerType = new JavaType(Integer.class);

        // Act
        Object defaultValue = propertyBuilder.getDefaultValue(integerType);

        // Assert
        Assert.assertEquals(0, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueForContainerType() {
        // Arrange
        JavaType arrayType = new JavaType(new Object[]{});

        // Act
        Object defaultValue = propertyBuilder.getDefaultValue(arrayType);

        // Assert
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueForReferenceType() {
        // Arrange
        JavaType referenceType = new JavaType(Object.class);

        // Act
        Object defaultValue = propertyBuilder.getDefaultValue(referenceType);

        // Assert
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueForOtherType() {
        // Arrange
        JavaType objectType = new JavaType(SomeClass.class);

        // Act
        Object defaultValue = propertyBuilder.getDefaultValue(objectType);

        // Assert
        Assert.assertNull(defaultValue);
    }

    private static class SomeClass {
    }
}