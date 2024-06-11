package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.Reflections.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        assertNotNull(instantIator.newInstance(String.class));
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndConstructorWithParameter() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        try {
            instantIator.newInstance(Object[].class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndParameterlessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(instantIator.newInstance(String.class));
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndConstructorWithParameter() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        Class<?> outerClassType = outerClassInstance.getClass();
        Class<Object[]> arrayClass = Object[].class;
        try {
            Object result = instantIator.newInstance(arrayClass);
            assertArrayEquals(new Object[0], result);
            Object[] resultArray = (Object[]) result;
            resultArray[0] = new Object();
            assertSame(outerClassInstance, resultArray[0]);
        } catch (InstantiationException e) {
            fail("Unexpected InstantiationException");
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstanceAndNoArgConstructorThrowsException() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        instantIator.newInstance(String.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndNoArgConstructorThrowsException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        instantIator.newInstance(String.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndClassWithNoConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        Class<Object> cls = Object.class;
        instantIator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstanceAndClassWithConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        Class<Object> cls = getClassWithPrivateConstructor(Array.class);
        instantIator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndClassWithConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        Class<Object> cls = getClassWithPrivateConstructor(Array.class);
        instantIator.newInstance(cls);
    }

    private Class<?> getClassWithPrivateConstructor(Class<?> cls) {
        if (!cls.isInstance(Array.class)) {
            try {
                return getDeclaredClass(cls, "Array");
            } catch (NoSuchMethodException e) {
                return cls;
            }
        }
        return cls;
    }

    private Class<?> getDeclaredClass(Class<?> outerClass, String className) throws NoSuchMethodException {
        Constructor<?> constructor = outerClass.getDeclaredMethod("getValue", new Class[0])
                .getAnnotation(org.mockito.InjectMocks.class).value()[0];
        return getClass(constructor.getTypeNames()[0]);
    }
}