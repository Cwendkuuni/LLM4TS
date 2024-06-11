package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    private ConstructorInstantiator constructorInstantiator;
    private Object outerClassInstance;
    
    @Before
    public void setUp() {
        outerClassInstance = mock(Object.class);
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }
    
    @Test
    public void testNewInstance_withNoArgConstructor() {
        assertNotNull(constructorInstantiator.newInstance(String.class));
        assertNotNull(constructorInstantiator.newInstance(Integer.class));
        assertNotNull(constructorInstantiator.newInstance(Object.class));
    }
    
    @Test
    public void testNewInstance_withOuterClass() {
        assertNotNull(constructorInstantiator.newInstance(TestClass.class));
    }
    
    @Test
    public void testNewInstance_withInvalidOuterClassInstanceType() {
        constructorInstantiator = new ConstructorInstantiator("invalidType");
        
        assertNull(constructorInstantiator.newInstance(TestClass.class));
    }
    
    @Test
    public void testNewInstance_withInvalidConstructor() {
        assertNull(constructorInstantiator.newInstance(InvalidTestClass.class));
    }
    
    private static class TestClass {
        public TestClass(Object outerClassInstance) {
            // constructor
        }
    }
    
    private static class InvalidTestClass {
        public InvalidTestClass(String parameter) {
            // constructor
        }
    }
}