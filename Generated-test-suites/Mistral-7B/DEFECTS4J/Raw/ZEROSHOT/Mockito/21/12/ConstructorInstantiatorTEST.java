package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<Object> objectClass = Object.class;

        verify(constructorInstantiator, times(1)).noArgConstructor(objectClass);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        try {
            constructorInstantiator.newInstance(String.class);
            fail("Expected InstantiationException");
        } catch ( InstantiationException e) {
            //expected
        }

        verify(constructorInstantiator, times(1)).noArgConstructor(any(Class.class));
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstance() {
        Object outerClassInstance = mock(Object.class, withCallRealMethod());

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Object> objectClass = Object.class;

        Object instance = constructorInstantiator.newInstance(objectClass);
        verify(constructorInstantiator, times(1)).withOuterClass(objectClass);

        // check if the new instance was created with the outer class instance as a parameter
        verify(objectClass.getDeclaredConstructor(outerClassInstance.getClass())).newInstance(outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithValidOuterClassInstanceAndClassWithoutNoArgConstructor() {
        Object outerClassInstance = mock(Object.class, withCallRealMethod());
        Class<NoArgConstructorException> noArgConstructorExceptionClass = NoArgConstructorException.class;

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        constructorInstantiator.newInstance(noArgConstructorExceptionClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithValidOuterClassInstanceAndClassWithParametrizedConstructor() {
        Object outerClassInstance = mock(Object.class, withCallRealMethod());
        Class<ParametrizedConstructorException> parametrizedConstructorExceptionClass = ParametrizedConstructorException.class;

        Constructor<ParametrizedConstructorException> constructor = parametrizedConstructorExceptionClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        constructorInstantiator.newInstance(parametrizedConstructorExceptionClass);
    }

    @Test
    public void testParamsExceptionMessageIsReturned() {
        Object outerClassInstance = mock(Object.class, withCallRealMethod());
        Class<NoArgConstructorException> noArgConstructorExceptionClass = NoArgConstructorException.class;

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        try {
            constructorInstantiator.newInstance(noArgConstructorExceptionClass);
            fail("Expected InstantiationException");
        } catch ( InstantiationException e) {
            String expectedMessage = "Unable to create mock instance of '"
                    + noArgConstructorExceptionClass.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.";
            String actualMessage = e.getMessage();
            assertTrue(expectedMessage.equals(actualMessage));
        }
    }

    @Test
    public void testNoArgConstructor() {
        Class<Object> objectClass = Object.class;

        verify(objectClass, times(1)).newInstance();
    }

    static class NoArgConstructorException {
    }

    static class ParametrizedConstructorException {
        ParametrizedConstructorException(String message) {
        }
    }

    private Answer<Void> withCallRealMethod() {
        return invocation -> {
            Object[] args = invocation.getArguments();
            int length = args.length;
            for (int i = 0; i < length; i++) {
                System.out.println("Invoked method " + invocation.getMethod().getName() + " with arguments " + args[i]);
            }
            return null;
        };
    }
}