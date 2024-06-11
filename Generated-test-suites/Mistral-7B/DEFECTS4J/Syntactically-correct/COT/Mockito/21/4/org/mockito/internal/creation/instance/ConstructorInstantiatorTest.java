package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConstructorInstantiator.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        // Basic test case for newInstance method with null outerClassInstance and parameterless Class
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(String.class);
        assertNull(result);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndConstructedClass() {
        // Basic test case for newInstance method with null outerClassInstance and constructed Class
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<MyClass> cls = MyClass.class;
        try {
            Object result = instantiator.newInstance(cls);
            fail("Expected InstantiationException but got " + result.getClass().getName());
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndParameterlessClass() {
        // Basic test case for newInstance method with non-null outerClassInstance and parameterless Class
        Object outerClassInstance = mock(OuterClass.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = instantiator.newInstance(String.class);
        assertNotNull(result);
        verify(outerClassInstance, never()).equals(result);
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndConstructedClass() {
        // Basic test case for newInstance method with non-null outerClassInstance and constructed Class
        Object outerClassInstance = mock(OuterClass.class);
        Class<MyClass> cls = MyClass.class;
        when(outerClassInstance.getClass()).thenReturn(OuterClassWithParam.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = instantiator.newInstance(cls);
        assertNotNull(result);
        verify(outerClassInstance).equals(result);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassThrowsExceptionWithNullOuterClassInstance() {
        // Test for withOuterClass method throwing InstantiationException when outerClassInstance is null
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<MyClass> cls = MyClass.class;
        instantiator.withOuterClass(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassThrowsExceptionWithNullConstructor() {
        // Test for withOuterClass method throwing InstantiationException when constructor is null
        Object outerClassInstance = mock(OuterClass.class);
        Class<MyClass> cls = MyClass.class;
        when(outerClassInstance.getClass()).thenReturn(null);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.withOuterClass(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassThrowsExceptionWithIllegalAccessException() {
        // Test for withOuterClass method throwing InstantiationException when IllegalAccessException occurs
        Object outerClassInstance = mock(OuterClass.class);
        Class<MyClass> cls = MyClass.class;
        when(outerClassInstance.getClass()).thenReturn(OuterClassWithIllegalAccess.class);
        when(OuterClassWithIllegalAccess.class.getDeclaredConstructor(outerClassInstance.getClass()))
                .thenThrow(new IllegalAccessException());
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.withOuterClass(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassThrowsExceptionWithInvocationTargetException() {
        // Test for withOuterClass method throwing InstantiationException when InvocationTargetException occurs
        Object outerClassInstance = mock(OuterClass.class);
        Class<MyClass> cls = MyClass.class;
        when(outerClassInstance.getClass()).thenReturn(OuterClassWithInvocationTarget.class);
        when(OuterClassWithInvocationTarget.class.getDeclaredConstructor(outerClassInstance.getClass()))
                .thenAnswer(new Answer<MyClass>() {
                    @Override
                    public MyClass answer(InvocationOnMock invocation) throws Throwable {
                        throw new InvocationTargetException(new RuntimeException());
                    }
                });
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.withOuterClass(cls);
    }

    @Test
    public void testParamsExceptionIsThrownWhenClassHasNoParameterlessConstructor() {
        // Test for paramsException method being called when newInstance fails due to no-args constructor absence
        Class<MyClassWithNoArgsConstructor> cls = MyClassWithNoArgsConstructor.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(cls);
            fail("Expected InstantiationException but got null.");
        } catch (InstantiationException e) {
            Optional<Throwable> cause = Optional.ofNullable(e.getCause());
            if (cause.isPresent()) {
                ConstructorInstantiator.paramsException(cls, e).printStackTrace();
            } else {
                fail("Expected paramsException with cause but got null.");
            }
        }
    }

    @Test
    public void testParamsExceptionIsThrownWhenClassHasParamsConstructorThrowsException() {
        // Test for paramsException method being called when newInstance fails due to constructor exception
        Class<MyClassWithParamsConstructor> cls = MyClassWithParamsConstructor.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(cls);
            fail("Expected InstantiationException but got null.");
        } catch (InstantiationException e) {
            Optional<Throwable> cause = Optional.ofNullable(e.getCause());
            if (cause.isPresent()) {
                ConstructorInstantiator.paramsException(cls, e).printStackTrace();
            } else {
                fail("Expected paramsException with cause but got null.");
            }
        }
    }

    private static class MyClass {}

    private static class OuterClass {}

    private static class OuterClassWithParam {
        public OuterClassWithParam(Object outerClassInstance) {}
    }

    private static class OuterClassWithIllegalAccess {
        private OuterClassWithIllegalAccess() {}

        public static class InnerClass {
            public InnerClass() {}

            public InnerClass(Object outerClassInstance) {}
        }

        public InnerClass getInnerInstance() {
            return new InnerClass();
        }

        public InnerClass getInnerInstanceWithOuterClass() {
            return new InnerClass(this);
        }
    }

    private static class OuterClassWithInvocationTarget {
        public OuterClassWithInvocationTarget(Object outerClassInstance) throws Exception {
            throw new RuntimeException();
        }
    }

    private static class MyClassWithNoArgsConstructor {
        public MyClassWithNoArgsConstructor() {}
    }

    private static class MyClassWithParamsConstructor {
        public MyClassWithParamsConstructor(Object param1) {}
    }
}