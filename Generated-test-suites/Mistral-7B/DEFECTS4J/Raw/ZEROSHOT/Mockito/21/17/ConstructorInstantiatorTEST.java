package org.junit.Test;

import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNullOuterInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNull(instantiator.outerClassInstance);

        assertThrows(InstantiationException.class, () -> instantiator.newInstance(String.class));
    }

    @Test
    public void testParamlessConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        String instance = instantiator.newInstance(String.class);
        assertNotNull(instance);
        assertEquals("", instance.toString());
    }

    @Test
    public void testConstructorWithArguments() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        Class<TestClassWithConstructor> testClassWithConstructor = TestClassWithConstructor.class;
        try {
            TestClassWithConstructor instance = instantiator.newInstance(testClassWithConstructor);
            assertNotNull(instance);
            assertInstanceOf(TestClassWithConstructor.class, instance);
        } catch (InstantiationException e) {
            throw new AssertionError("Unexpected error", e);
        }
    }

    @Test
    public void testNoArgConstructorFailure() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(String.class));
    }

    @Test
    public void testConstructorWithException() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        Class<ThrowsExceptionClass> throwsExceptionClass = ThrowsExceptionClass.class;
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(throwsExceptionClass));
    }

    @Test
    public void testGetDeclaredConstructorFailure() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        Class<GetConstructorExceptionClass> getConstructorExceptionClass = GetConstructorExceptionClass.class;
        assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(getConstructorExceptionClass));
    }

    @Test
    public void testNewInstanceFailure() {
        Object outerInstance = new Object();
        Constructor<TestNewInstanceFailureClass> constructor = TestNewInstanceFailureClass.class.getDeclaredConstructor(outerInstance.getClass());

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(TestNewInstanceFailureClass.class));
    }

    @Test
    public void testNoSuchMethodException() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        Class<NoSuchMethodExceptionClass> noSuchMethodExceptionClass = NoSuchMethodExceptionClass.class;
        assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(noSuchMethodExceptionClass));
    }

    @Test
    public void testIllegalAccessException() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        Class<IllegalAccessExceptionClass> illegalAccessExceptionClass = IllegalAccessExceptionClass.class;
        assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(illegalAccessExceptionClass));
    }

    @Test
    public void testInvocationTargetException() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        Class<InvocationTargetExceptionClass> invocationTargetExceptionClass = InvocationTargetExceptionClass.class;
        assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(invocationTargetExceptionClass));
    }

    @Test
    public void testParamsException() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        Class<TestParamsExceptionClass> testParamsExceptionClass = TestParamsExceptionClass.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(testParamsExceptionClass));
        assertEquals("Unable to create mock instance of 'TestParamsExceptionClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    private static class TestClassWithConstructor {
        // This class should have a constructor with one argument of type Object
    }

    private static class TestNewInstanceFailureClass {
        private TestNewInstanceFailureClass(Object outerInstance) {
            throw new RuntimeException("Test new instance failure.");
        }
    }

    private static class GetConstructorExceptionClass {
        private GetConstructorExceptionClass() {
            throw new GetConstructorException("Get constructor exception.");
        }

        private static class GetConstructorException extends Exception {}
    }

    private static class NoSuchMethodExceptionClass {
        private NoSuchMethodExceptionClass() throws NoSuchMethodException {
            // no-op
        }
    }

    private static class IllegalAccessExceptionClass {
        private IllegalAccessExceptionClass() throws IllegalAccessException {
            // no-op
        }
    }

    private static class InvocationTargetExceptionClass {
        private InvocationTargetExceptionClass() throws InvocationTargetException {
            // no-op
        }
    }

    private static class TestParamsExceptionClass {
        private TestParamsExceptionClass() {
            throw new IllegalArgumentException();
        }
    }
}