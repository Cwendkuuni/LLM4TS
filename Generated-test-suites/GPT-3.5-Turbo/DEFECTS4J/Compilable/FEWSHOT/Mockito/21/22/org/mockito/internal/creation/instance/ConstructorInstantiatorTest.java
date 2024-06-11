package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    private ConstructorInstantiator constructorInstantiator;
    
    @Before
    public void setup() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }
    
    @Test
    public void testNoArgConstructor() {
        ClassWithNoArgConstructor obj = constructorInstantiator.newInstance(ClassWithNoArgConstructor.class);
        assertTrue(obj instanceof ClassWithNoArgConstructor);
    }
    
    @Test
    public void testWithOuterClass() {
        ClassWithOuterClass obj = constructorInstantiator.newInstance(ClassWithOuterClass.class);
        assertTrue(obj instanceof ClassWithOuterClass);
    }
    
    @Test
    public void testInvalidOuterClassInstance() {
        constructorInstantiator = new ConstructorInstantiator(new Object());
        try {
            ClassWithOuterClass obj = constructorInstantiator.newInstance(ClassWithOuterClass.class);
            assertNull(obj);
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ClassWithOuterClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
    
    @Test
    public void testExceptionForNoArgConstructor() {
        constructorInstantiator = new ConstructorInstantiator(null);
        try {
            ClassWithException obj = constructorInstantiator.newInstance(ClassWithException.class);
            assertNull(obj);
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ClassWithException'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}

class ClassWithNoArgConstructor {
    public ClassWithNoArgConstructor() {
        
    }
}

class ClassWithOuterClass {
    public ClassWithOuterClass(Object outerClassInstance) {
        
    }
}

class ClassWithException {
    public ClassWithException() throws Exception {
        throw new Exception();
    }
}