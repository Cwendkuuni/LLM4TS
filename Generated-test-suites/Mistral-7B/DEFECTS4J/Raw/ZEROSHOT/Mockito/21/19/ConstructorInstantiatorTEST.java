package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.exceptions.api.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;
    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setup() {
        outerClassInstance = mock(Object.class);
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void should_instantiate_class_with_no_arg_constructor() {
        // given
        Class<String> stringClass = String.class;

        // when
        String result = constructorInstantiator.newInstance(stringClass);

        // then
        verify(stringClass).newInstance();
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test(expected = InstantiationException.class)
    public void should_throw_exception_when_class_has_no_arg_constructor_but_not_found() {
        // given
        Class<NonExistentClass> nonExistentClass = NonExistentClass.class;

        // when
        constructorInstantiator.newInstance(nonExistentClass);

        // then - exception should be thrown
    }

    @Test
    public void should_instantiate_class_with_outer_instance_as_arg() {
        // given
        Class<OuterClassWithConstructor> outerClassWithConstructor = OuterClassWithConstructor.class;
        Class<InnerClassWithConstructor> innerClassWithConstructor = InnerClassWithConstructor.class;

        // given outer class instance
        Object outerInstance = mock(OuterClassWithConstructor.class);

        // when
        when(outerClassWithConstructor.getDeclaredConstructor(outerClassInstance.getClass()))
                .thenReturn(outerClassWithConstructor.getDeclaredConstructor(outerClassInstance.getClass()));
        when(outerClassWithConstructor.getDeclaredConstructor(outerClassInstance.getClass())).isInstance(outerInstance);

        InnerClassWithConstructor result = constructorInstantiator.newInstance(innerClassWithConstructor);

        // then
        verify(outerClassWithConstructor).getDeclaredConstructor(outerClassInstance.getClass()).newInstance(outerInstance);
        assertNotNull(result);
    }

    @Test(expected = InstantiationException.class)
    public void should_throw_exception_when_class_constructor_throw_an_exception() {
        // given
        Class<ThrowingClass> throwingClass = ThrowingClass.class;

        // when
        constructorInstantiator.newInstance(throwingClass);

        // then - exception should be thrown
    }

    @Test(expected = InstantiationException.class)
    public void should_throw_exception_when_class_has_outer_instance_constructor_but_not_found() {
        // given
        Class<NonExistentOuterClass> nonExistentOuterClass = NonExistentOuterClass.class;
        Class<InnerClassWithConstructor> innerClassWithConstructor = InnerClassWithConstructor.class;

        // when
        constructorInstantiator.newInstance(innerClassWithConstructor);

        // then - exception should be thrown
    }

    @Test
    public void should_not_call_outer_class_constructor_when_class_has_no_outer_instance_constructor() {
        // given
        Class<NoOuterClass> noOuterClass = NoOuterClass.class;

        // when
        constructorInstantiator.newInstance(noOuterClass);

        // then - outer class constructor should not be called
        verifyZeroInteractions(outerClassInstance);
    }

    private static class OuterClassWithConstructor {
        public OuterClassWithConstructor() {}

        public OuterClassWithConstructor(Object outerClassInstance) {}
    }

    private static class InnerClassWithConstructor {
        public InnerClassWithConstructor() {}

        public InnerClassWithConstructor(Object outerClassInstance) {}
    }

    private static class NonExistentClass {}

    private static class NonExistentOuterClass {
        public NonExistentOuterClass(Object outerClassInstance) {}
    }

    private static class ThrowingClass {
        public ThrowingClass() throws Exception {}
    }

    private static class NoOuterClass {}
}