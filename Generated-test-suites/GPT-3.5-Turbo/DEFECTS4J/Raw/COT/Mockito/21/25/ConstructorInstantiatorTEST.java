
package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    // Test for ConstructorInstantiator(Object outerClassInstance)
    @Test
    public void testConstructorInstantiator() {
        // Test with null outerClassInstance
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator);
        
        // Test with non-null outerClassInstance
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator2 = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(instantiator2);
    }

    // Test for newInstance(Class<T> cls)
    @Test
    public void testNewInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        // Test for no-arg constructor
        TestClass instance = instantiator.newInstance(TestClass.class);
        assertNotNull(instance);
    }

    // Test for withOuterClass(Class<T> cls)
    @Test
    public void testWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        
        // Test with valid outerClassInstance
        TestClass instance = instantiator.withOuterClass(TestClass.class);
        assertNotNull(instance);

        // Test with invalid outerClassInstance
        ConstructorInstantiator invalidInstantiator = new ConstructorInstantiator(new Integer(5));
        try {
            invalidInstantiator.withOuterClass(TestClass.class);
            fail("Expected InstantationException to be thrown.");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'TestClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    // Test for paramsException(Class<T> cls, Exception e)
    @Test
    public void testParamsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            // Test with valid class and exception
            instantiator.paramsException(TestClass.class, new Exception("Test exception"));
            fail("Expected InstantationException to be thrown.");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'TestClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
            assertEquals("Test exception", e.getCause().getMessage());
        }
    }

    // Test for noArgConstructor(Class<T> cls)
    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        // Test with valid class
        TestClass instance = instantiator.noArgConstructor(TestClass.class);
        assertNotNull(instance);

        // Test with invalid class
        ConstructorInstantiator invalidInstantiator = new ConstructorInstantiator(null);
        try {
            invalidInstantiator.noArgConstructor(TestClassWithArgs.class);
            fail("Expected InstantationException to be thrown.");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'TestClassWithArgs'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
    
    // Auxiliary classes used for testing
    public class TestClass {
        public TestClass() {}
    }
    
    public class TestClassWithArgs {
        public TestClassWithArgs(String arg) {}
    }
}

