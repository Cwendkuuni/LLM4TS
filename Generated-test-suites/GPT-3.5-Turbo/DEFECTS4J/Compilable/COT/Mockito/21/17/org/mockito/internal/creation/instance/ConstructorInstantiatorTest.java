package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {
    
    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(TestClass.class);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
    }
    
    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        TestClass outerInstance = new TestClass();
        outerInstance.setValue(10);
        Object instance = instantiator.newInstance(TestClass.class);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
        TestClass newInstance = (TestClass) instance;
        assertEquals(10, newInstance.getValue());
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_paramException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        Object instance = instantiator.newInstance(InvalidClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructor_paramException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(InvalidClass.class);
    }
    
    class TestClass {
        private int value;
        
        public void setValue(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    class OuterClass {
    }
    
    class InvalidClass {
        public InvalidClass(int value) {
            // Invalid constructor
        }
    }
}

