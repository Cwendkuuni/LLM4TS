
package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    private ConstructorInstantiator constructorInstantiator;
    private OuterClass outerClass;
    
    @Before
    public void setUp() {
        outerClass = new OuterClass();
        constructorInstantiator = new ConstructorInstantiator(outerClass);
    }
    
    @Test
    public void testNewInstance_withOuterClass_returnsNewInstance() {
        InnerClass innerClass = constructorInstantiator.newInstance(InnerClass.class);
        assertNotNull(innerClass);
        assertNotSame(outerClass, innerClass.getOuterClass());
    }
    
    @Test
    public void testNewInstance_noArgConstructor_returnsNewInstance() {
        NoArgConstructorClass noArgConstructorClass = constructorInstantiator.newInstance(NoArgConstructorClass.class);
        assertNotNull(noArgConstructorClass);
    }
    
    @Test
    public void testNewInstance_invalidOuterClassType_throwsException() {
        assertThrows(InstantationException.class, () -> {
            constructorInstantiator.newInstance(InvalidOuterClassType.class);
        });
    }
    
    @Test
    public void testNewInstance_noParameterlessConstructor_throwsException() {
        assertThrows(InstantationException.class, () -> {
            constructorInstantiator.newInstance(NoParameterlessConstructorClass.class);
        });
    }
    
    private static class OuterClass {
    }
    
    private static class InnerClass {
        private final OuterClass outerClass;
        
        public InnerClass(OuterClass outerClass) {
            this.outerClass = outerClass;
        }
        
        public OuterClass getOuterClass() {
            return outerClass;
        }
    }
    
    private static class NoArgConstructorClass {
    }
    
    private static class InvalidOuterClassType {
        public InvalidOuterClassType(String outerClass) {
        }
    }
    
    private static class NoParameterlessConstructorClass {
        public NoParameterlessConstructorClass(String parameter) {
        }
    }
}

