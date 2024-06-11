
package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithOuterClassNotNull() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
        assertEquals(outerClassInstance, myClass.getOuterClassInstance());
    }

    @Test
    public void testNewInstanceWithOuterClassNull() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
        assertNull(myClass.getOuterClassInstance());
    }
    
    @Test
    public void testNewInstanceWithInvalidOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            constructorInstantiator.newInstance(InvalidClass.class);
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'InvalidClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
    
    @Test
    public void testNewInstanceWithNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    private static class MyClass {
        private final Object outerClassInstance;

        public MyClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class InvalidClass {
        public InvalidClass(int parameter) {
            // constructor with parameter
        }
    }
}
