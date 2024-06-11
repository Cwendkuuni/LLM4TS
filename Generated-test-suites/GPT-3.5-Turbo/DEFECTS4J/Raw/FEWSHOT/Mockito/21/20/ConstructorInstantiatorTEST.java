
package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withOuterClass() {
        MyClass outerClassInstance = new MyClass();
        
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        MyClass result = instantiator.newInstance(MyClass.class);
        
        assertEquals(outerClassInstance, result.getOuterClassInstance());
    }
    
    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass result = instantiator.newInstance(MyClass.class);
        
        assertNotNull(result);
        assertNull(result.getOuterClassInstance());
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_exception() {
        MyClass outerClassInstance = new MyClass();
        
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(InvalidClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructor_exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(InvalidClass.class);
    }
    
    private static class MyClass {
        
        private Object outerClassInstance;
        
        public MyClass() {}
        
        public MyClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class InvalidClass {
        
        public InvalidClass(int arg) {}
    }
}
