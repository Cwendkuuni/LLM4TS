package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator.newInstance(String.class));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstanceAndConstructedClassWithParams() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ParametersClassWithConstructor.class);
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndParameterlessClass() {
        Object outerInstance = mock(OuterClass.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        assertNotNull(instantiator.newInstance(String.class));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithValidOuterClassInstanceAndConstructedClassWithParams() {
        Object outerInstance = mock(OuterClass.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        instantiator.newInstance(ParametersClassWithConstructor.class);
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndConstructedClassWithoutParams() {
        Object outerInstance = mock(OuterClass.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        assertNotNull(instantiator.newInstance(WithOuterClass.class));
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassThrowsExceptionWhenOuterInstanceIsNull() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.withOuterClass(ParametersClassWithConstructor.class);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassThrowsExceptionWhenNoDeclaredConstructor() {
        Object outerInstance = mock(OuterClassWithNoConstructor.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        instantiator.withOuterClass(NoConstructorClass.class);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassThrowsExceptionWhenConstructorThrowsAnException() {
        Object outerInstance = mock(OuterClass.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        try {
            instantiator.withOuterClass(ExceptionThrowingClass.class);
        } catch (Exception e) {
            throw new InstantiationException("Unexpected exception during instantiation", e);
        }
    }

    static class OuterClass {
    }

    static class ParametersClassWithConstructor {
        ParametersClassWithConstructor(Object outerClassInstance) {
        }
    }

    static class WithOuterClass {
    }

    static class NoConstructorClass {
    }

    static class ExceptionThrowingClass {
        public ExceptionThrowingClass() throws Exception {
            throw new Exception("Test exception");
        }
    }
}