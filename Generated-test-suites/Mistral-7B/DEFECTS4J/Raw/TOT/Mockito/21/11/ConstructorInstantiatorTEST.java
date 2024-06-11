package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.InstantiationException;
import org.mockito.exceptions.base.MockitoException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParamelessClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(String.class));
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndConstructorWithParameters() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(ParamClassWithConstructor.class));
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndParamelessClass() {
        Object outerClassInstance = mock(OuterClass.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        String newInstance = constructorInstantiator.newInstance(String.class);
        assertNotNull(newInstance);
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndConstructorWithParameters() {
        Object outerClassInstance = spy(new OuterClassWithConstructor());
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        ParamClassWithConstructor newInstance = constructorInstantiator.newInstance(ParamClassWithConstructor.class);
        assertNotNull(newInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstanceAndUnsupportedClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(UnsupportedClass.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndUnsupportedClass() {
        Object outerClassInstance = mock(OuterClass.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(UnsupportedClass.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstanceAndNoArgConstructorException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> constructorInstantiator.noArgConstructor(String.class.getDeclaredConstructor(String.class)));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndNoArgConstructorException() {
        Object outerClassInstance = mock(OuterClass.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertThrows(InstantiationException.class, () -> constructorInstantiator.noArgConstructor(String.class.getDeclaredConstructor(String.class)));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstanceAndParamsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> constructorInstantiator.paramsException(String.class, new Exception()));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndParamsException() {
        Object outerClassInstance = mock(OuterClass.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertThrows(InstantiationException.class, () -> constructorInstantiator.paramsException(String.class, new Exception()));
    }

    public static class OuterClass {
    }

    public static class OuterClassWithConstructor {
    }

    public static class ParamClassWithConstructor {
        public ParamClassWithConstructor(Object outerClassInstance) {
        }
    }

    public static class UnsupportedClass {
        private UnsupportedClass() {
        }

        public UnsupportedClass(Object outerClassInstance) {
        }
    }
}