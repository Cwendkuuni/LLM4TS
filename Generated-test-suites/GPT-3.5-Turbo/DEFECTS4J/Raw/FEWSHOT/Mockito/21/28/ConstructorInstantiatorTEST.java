package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ConstructorInstantiatorTest {
    
    @Test
    public void testConstructorInstantiatorWithOuterClassInstance() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        assertNotNull(constructorInstantiator);
        assertEquals(outerInstance, constructorInstantiator.outerClassInstance);
    }
    
    @Test
    public void testConstructorInstantiatorWithNullOuterClassInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertNotNull(constructorInstantiator);
        assertEquals(null, constructorInstantiator.outerClassInstance);
    }
    
    @Test
    public void testNewInstanceWithOuterClassConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        assertNotNull(constructorInstantiator);

        // Test with a valid class
        ClassWithNoArgConstructor cls = constructorInstantiator.newInstance(ClassWithNoArgConstructor.class);
        assertNotNull(cls);
        assertEquals(cls.getClass(), ClassWithNoArgConstructor.class);

        // Test with a class that requires an outer instance
        ClassWithOuterClassConstructor clsWithOuter = constructorInstantiator.newInstance(ClassWithOuterClassConstructor.class);
        assertNotNull(clsWithOuter);
        assertEquals(clsWithOuter.getClass(), ClassWithOuterClassConstructor.class);
        assertEquals(outerInstance, clsWithOuter.getOuterInstance());

        // Test with a class that does not have a parameter-less constructor
        assertThrows(InstantationException.class, () -> {
            constructorInstantiator.newInstance(ClassWithParameterizedConstructor.class);
        });
    }

    @Test
    public void testNewInstanceWithNoArgConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        assertNotNull(constructorInstantiator);

        // Test with a valid class
        ClassWithNoArgConstructor cls = constructorInstantiator.newInstance(ClassWithNoArgConstructor.class);
        assertNotNull(cls);
        assertEquals(cls.getClass(), ClassWithNoArgConstructor.class);

        // Test with a class that requires an outer instance
        assertThrows(InstantationException.class, () -> {
            constructorInstantiator.newInstance(ClassWithOuterClassConstructor.class);
        });

        // Test with a class that does not have a parameter-less constructor
        assertThrows(InstantationException.class, () -> {
            constructorInstantiator.newInstance(ClassWithParameterizedConstructor.class);
        });
    }
    
    private static class ClassWithNoArgConstructor {
        public ClassWithNoArgConstructor() {}
    }
    
    private static class ClassWithOuterClassConstructor {
        private Object outerInstance;
        
        public ClassWithOuterClassConstructor(Object outerInstance) {
            this.outerInstance = outerInstance;
        }
        
        public Object getOuterInstance() {
            return outerInstance;
        }
    }
    
    private static class ClassWithParameterizedConstructor {
        public ClassWithParameterizedConstructor(Object param) {}
    }
}