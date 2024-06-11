package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    private ConstructorInstantiator constructorInstantiator;
    private Object outerClassInstance;
    
    @Before
    public void setUp() {
        outerClassInstance = new Object();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }
    
    @Test
    public void testNewInstance_withOuterClass() {
        // Test with a class that has a parameter-less constructor
        ClassWithParameterlessConstructor cls = constructorInstantiator.newInstance(ClassWithParameterlessConstructor.class);
        assertNotNull(cls);
        
        // Test with a class that has a constructor with parameters
        ClassWithConstructorWithParameters clsWithParams = constructorInstantiator.newInstance(ClassWithConstructorWithParameters.class);
        assertNotNull(clsWithParams);
        assertEquals(outerClassInstance, clsWithParams.getOuterClassInstance());
    }
    
    @Test
    public void testNewInstance_noArgConstructor() {
        ClassWithParameterlessConstructor cls = constructorInstantiator.newInstance(ClassWithParameterlessConstructor.class);
        assertNotNull(cls);
    }
    
    @Test
    public void testNewInstance_exception() {
        try {
            constructorInstantiator.newInstance(ClassWithConstructorWithoutParameters.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ClassWithConstructorWithoutParameters'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
    
    private static class ClassWithParameterlessConstructor {
        public ClassWithParameterlessConstructor() {}
    }
    
    private static class ClassWithConstructorWithParameters {
        private Object outerClassInstance;
        
        public ClassWithConstructorWithParameters(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class ClassWithConstructorWithoutParameters {
        public ClassWithConstructorWithoutParameters(int param) {}
    }
}