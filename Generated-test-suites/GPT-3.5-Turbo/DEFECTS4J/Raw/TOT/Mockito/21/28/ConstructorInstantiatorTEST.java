package org.mockito.internal.creation.instance;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withNullOuterClassInstance_callsNoArgConstructor() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<TestClass> cls = TestClass.class;
        
        // Act
        TestClass result = instantiator.newInstance(cls);
        
        // Assert
        verify(cls, times(1)).newInstance();
    }
    
    @Test
    public void testNewInstance_withNonNullOuterClassInstance_callsWithOuterClass() {
        // Arrange
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
        Class<TestOuterClass> cls = TestOuterClass.class;
        
        // Act
        TestOuterClass result = instantiator.newInstance(cls);
        
        // Assert
        verify(cls, times(1)).getDeclaredConstructor(OuterClass.class);
        verify(cls.getDeclaredConstructor(OuterClass.class), times(1)).newInstance(outerClass);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withNullOuterClassInstanceAndNoArgConstructorThrowsException() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<TestOuterClass> cls = TestOuterClass.class;
        
        // Act
        instantiator.newInstance(cls);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withNonNullOuterClassInstanceAndNoParameterlessConstructorThrowsException() {
        // Arrange
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
        Class<TestNonParameterlessConstructor> cls = TestNonParameterlessConstructor.class;
        
        // Act
        instantiator.newInstance(cls);
    }
}

class TestClass {}

class TestOuterClass {}

class TestNonParameterlessConstructor {
    public TestNonParameterlessConstructor(int value) {}
}

class OuterClass {}