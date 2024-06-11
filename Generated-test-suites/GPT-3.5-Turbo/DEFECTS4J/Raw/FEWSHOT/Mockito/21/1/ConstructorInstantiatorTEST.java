package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
    public void testNewInstance_noArgConstructor() {
        assertNotNull(constructorInstantiator.newInstance(Object.class));
        assertNotNull(constructorInstantiator.newInstance(String.class));
        assertNotNull(constructorInstantiator.newInstance(Integer.class));
    }

    @Test
    public void testNewInstance_withOuterClass() {
        assertNotNull(constructorInstantiator.newInstance(OuterClass.class));
        assertNotNull(constructorInstantiator.newInstance(InnerClass.class));
    }

    @Test
    public void testNewInstance_invalidOuterClass() {
        assertNull(constructorInstantiator.newInstance(InvalidOuterClass.class));
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_exceptionNoArgConstructor() {
        constructorInstantiator.newInstance(ExceptionNoArgConstructor.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_exceptionWithOuterClass() {
        constructorInstantiator.newInstance(ExceptionWithOuterClass.class);
    }

    private class OuterClass {
        // implementation
    }

    private class InnerClass {
        // implementation
    }

    private class InvalidOuterClass {
        // implementation
    }

    private class ExceptionNoArgConstructor {
        public ExceptionNoArgConstructor() throws Exception {
            throw new Exception();
        }
    }

    private class ExceptionWithOuterClass {
        public ExceptionWithOuterClass(Object outerClassInstance) throws Exception {
            throw new Exception();
        }
    }

}