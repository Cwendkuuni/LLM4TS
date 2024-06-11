
package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        
        OuterClassWithParameterlessConstructor instance = instantiator.newInstance(OuterClassWithParameterlessConstructor.class);
        
        assertNotNull(instance);
        assertEquals(outerInstance, instance.getOuterClassInstance());
    }
    
    @Test
    public void testNewInstanceWithoutOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        OuterClassWithParameterlessConstructor instance = instantiator.newInstance(OuterClassWithParameterlessConstructor.class);
        
        assertNotNull(instance);
        assertNull(instance.getOuterClassInstance());
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstanceWithInvalidOuterClassType() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        
        instantiator.newInstance(OuterClassWithNonMatchingOuterClassType.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstanceWithNoParameterlessConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        
        instantiator.newInstance(OuterClassWithNonParameterlessConstructor.class);
    }
    
    private static class OuterClassWithParameterlessConstructor {
        
        private Object outerClassInstance;
        
        public OuterClassWithParameterlessConstructor(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class OuterClassWithNonMatchingOuterClassType {
        
        private Object outerClassInstance;
        
        public OuterClassWithNonMatchingOuterClassType(Integer outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class OuterClassWithNonParameterlessConstructor {
        
        private Object outerClassInstance;
        
        public OuterClassWithNonParameterlessConstructor(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
}
