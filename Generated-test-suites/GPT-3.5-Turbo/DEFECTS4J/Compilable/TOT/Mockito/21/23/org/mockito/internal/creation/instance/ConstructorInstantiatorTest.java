package org.mockito.internal.creation.instance;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor_WhenNoOuterClassInstance_ShouldReturnNewInstance() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        // Act
        Object result = instantiator.newInstance(Object.class);
        
        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Object);
    }
    
    @Test
    public void testNoArgConstructor_WhenOuterClassInstance_ShouldThrowException() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        
        // Act
        try {
            instantiator.newInstance(Object.class);
            Assert.fail("Expected InstantationException");
        } catch (InstantationException e) {
            // Assert
            Assert.assertTrue(e.getMessage().contains("outer instance has correct type"));
            Assert.assertTrue(e.getMessage().contains("target class has parameter-less constructor"));
        }
    }
    
    @Test
    public void testWithOuterClass_WhenValidOuterClassInstance_ShouldReturnNewInstance() {
        // Arrange
        Object outerClassInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        // Act
        Object result = instantiator.newInstance(Object.class);
        
        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Object);
    }
    
    @Test
    public void testWithOuterClass_WhenInvalidOuterClassInstance_ShouldThrowException() {
        // Arrange
        Object outerClassInstance = new InvalidOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        // Act
        try {
            instantiator.newInstance(Object.class);
            Assert.fail("Expected InstantationException");
        } catch (InstantationException e) {
            // Assert
            Assert.assertTrue(e.getMessage().contains("outer instance has correct type"));
            Assert.assertTrue(e.getMessage().contains("target class has parameter-less constructor"));
        }
    }
    
    @Test
    public void testWithOuterClass_WhenInvalidOuterClassInstance_ShouldThrowExceptionWithStackTrace() {
        // Arrange
        Object outerClassInstance = new InvalidOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        // Act
        try {
            instantiator.newInstance(Object.class);
            Assert.fail("Expected InstantationException");
        } catch (InstantationException e) {
            // Assert
            Assert.assertTrue(e.getMessage().contains("outer instance has correct type"));
            Assert.assertTrue(e.getMessage().contains("target class has parameter-less constructor"));
            Assert.assertNotNull(e.getCause());
            Assert.assertNotNull(e.getStackTrace());
        }
    }
    
    private static class OuterClass {
        // Outer class implementation
    }
    
    private static class InvalidOuterClass {
        // Invalid outer class implementation
    }
}

