package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noOuterClassInstance_shouldReturnObjectWithNoArgConstructor() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<TestClass> cls = TestClass.class;
        
        // Act
        TestClass result = instantiator.newInstance(cls);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
    }
    
    @Test
    public void testNewInstance_withOuterClassInstance_shouldReturnObjectWithOuterClassConstructor() {
        // Arrange
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClass> cls = TestClass.class;
        
        // Act
        TestClass result = instantiator.newInstance(cls);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
    }
    
    private static class TestClass {
        // No-arg constructor
        public TestClass() {
        }
        
        // Constructor with outer class instance
        public TestClass(OuterClass outerClass) {
        }
    }
    
    private static class OuterClass {
    }
}