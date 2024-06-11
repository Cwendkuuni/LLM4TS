package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.invokeWithArguments;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiationUnderTest = new ConstructorInstantiator(null);
        Foo foo = instantiationUnderTest.newInstance(Foo.class);
        assertNotNull(foo);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndNonParameterlessClass() {
        ConstructorInstantiator instantiationUnderTest = new ConstructorInstantiator(null);
        try {
            instantiationUnderTest.newInstance(Bar.class);
            // this assertion will fail as InstantiationException should be thrown
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndParameterlessClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiationUnderTest = new ConstructorInstantiator(outerInstance);
        Foo foo = instantiationUnderTest.newInstance(Foo.class);
        assertNotNull(foo);
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndNonParameterlessClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiationUnderTest = new ConstructorInstantiator(outerInstance);
        Bar bar = instantiationUnderTest.newInstance(Bar.class);
        assertNotNull(bar);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithValidOuterClassInstanceAndNonExistentConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiationUnderTest = new ConstructorInstantiator(outerInstance);
        instantiationUnderTest.newInstance(NonExistentConstructor.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstanceAndNonExistentConstructor() {
        ConstructorInstantiator instantiationUnderTest = new ConstructorInstantiator(null);
        instantiationUnderTest.newInstance(NonExistentConstructor.class);
    }

    @Test
    public void testParamsException() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiationUnderTest = new ConstructorInstantiator(outerInstance);
        try {
            instantiationUnderTest.withOuterClass(NonExistentConstructor.class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            InstantiationException expectedException = new InstantiationException("Unable to create mock instance of 'NonExistentConstructor'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", new Exception("Expected exception message"));
            InstantiationException actualException = (InstantiationException) e;
            assertNotNull(actualException.getMessage());
            assertNotNull(actualException.getCause());
            assertNotNull(actualException.getCause().getMessage());
            assertSame(expectedException.getMessage(), actualException.getMessage());
        }
    }

    private static class Foo {}
    private static class Bar {
        private Bar(Object outerInstance) {}
    }
    private static class NonExistentConstructor {}

    private static class NonExistentConstructorException extends Exception {}
}