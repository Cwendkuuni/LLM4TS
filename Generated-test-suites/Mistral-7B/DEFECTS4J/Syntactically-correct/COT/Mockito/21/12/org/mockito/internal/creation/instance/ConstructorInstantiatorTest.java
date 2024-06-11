package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(String.class);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testNewInstanceWithNullOuterInstanceAndConstructorPresentClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(WithOuterClass.class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithOuterInstanceAndParameterlessClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Object result = instantiator.newInstance(String.class);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testNewInstanceWithOuterInstanceAndConstructorPresentClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Object result = instantiator.newInstance(WithOuterClass.class);
        assertNotNull(result);
        assertEquals(WithOuterClass.class, result.getClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithOuterInstanceAndNoConstructorPresentClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        instantiator.newInstance(NoConstructor.class);
    }

    @Test
    public void testParamsExceptionWithParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.paramsException(String.class, new RuntimeException());
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testParamsExceptionWithConstructedClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        try {
            instantiator.paramsException(WithOuterClass.class, new RuntimeException());
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNoArgConstructorWithParameterlessClass() {
        Object result = ConstructorInstantiator.noArgConstructor(String.class);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorWithConstructedClass() {
        ConstructorInstantiator.noArgConstructor(WithOuterClass.class);
    }

    static class NoConstructor {
        private NoConstructor() {
            // Empty constructor
        }
    }

    static class WithOuterClass {
        private WithOuterClass(Object outerInstance) {
            this.outerInstance = outerInstance;
        }

        private final Object outerInstance;
    }
}