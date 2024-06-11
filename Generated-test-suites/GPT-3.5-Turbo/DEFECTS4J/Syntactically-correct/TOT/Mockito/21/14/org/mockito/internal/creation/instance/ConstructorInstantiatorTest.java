
package org.mockito.internal.creation.instance;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testNoArgConstructor_newInstance() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<TestClass> cls = TestClass.class;
        
        // Act
        TestClass result = instantiator.newInstance(cls);
        
        // Assert
        assertNotNull(result);
        assertEquals(cls, result.getClass());
    }
    
    @Test
    public void testNoArgConstructor_newInstance_exception() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<TestClassWithErrorConstructor> cls = TestClassWithErrorConstructor.class;
        exceptionRule.expect(InstantationException.class);
        exceptionRule.expectMessage("Unable to create mock instance of 'TestClassWithErrorConstructor'." +
                "\nPlease ensure it has parameter-less constructor.");
        
        // Act
        instantiator.newInstance(cls);
        
        // Assert (Exception is expected)
    }
    
    @Test
    public void testWithOuterClass_newInstance() {
        // Arrange
        TestOuterClass outerClassInstance = new TestOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClassWithOuterClassConstructor> cls = TestClassWithOuterClassConstructor.class;
        
        // Act
        TestClassWithOuterClassConstructor result = instantiator.newInstance(cls);
        
        // Assert
        assertNotNull(result);
        assertEquals(cls, result.getClass());
        assertEquals(outerClassInstance, result.getOuterClassInstance());
    }
    
    @Test
    public void testWithOuterClass_newInstance_exception() {
        // Arrange
        TestOuterClass outerClassInstance = new TestOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClassWithErrorOuterClassConstructor> cls = TestClassWithErrorOuterClassConstructor.class;
        exceptionRule.expect(InstantationException.class);
        exceptionRule.expectMessage("Unable to create mock instance of 'TestClassWithErrorOuterClassConstructor'." +
                "\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.");
                
        // Act
        instantiator.newInstance(cls);
        
        // Assert (Exception is expected)
    }
    
    private static class TestClass {
        // Test class with no-arg constructor
    }
    
    private static class TestClassWithErrorConstructor {
        public TestClassWithErrorConstructor(int param) {
            // Constructor with parameter
        }
    }
    
    private static class TestOuterClass {
        // Outer class for test cases
    }
    
    private static class TestClassWithOuterClassConstructor {
        private final TestOuterClass outerClassInstance;
        
        public TestClassWithOuterClassConstructor(TestOuterClass outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public TestOuterClass getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class TestClassWithErrorOuterClassConstructor {
        public TestClassWithErrorOuterClassConstructor(TestOuterClass outerClassInstance) {
            // Constructor with incorrect type for outerClassInstance parameter
        }
    }
}
