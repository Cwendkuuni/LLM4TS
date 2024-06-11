package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.MockitoStubs;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_whenClassHasNoArgConstructor_thenInstanceReturned() {
        // given
        Class<MyClass> myClass = MyClass.class;

        // when
        Instantiator instantiator = new ConstructorInstantiator(mock(MyOuterClass.class));
        MyClass instance = instantiator.newInstance(myClass);

        // then
        assertNotNull(instance);
    }

    @Test
    public void testNewInstance_whenClassHasArgConstructor_thenInstanceWithOuterInstanceReturned() {
        // given
        Class<MyClass> myClass = MyClass.class;
        MyOuterClass outerInstance = mock(MyOuterClass.class);

        // when
        Instantiator instantiator = new ConstructorInstantiator(outerInstance);
        MyClass instance = instantiator.newInstance(myClass);

        // then
        assertSame(outerInstance, Whitebox.getInnerStaticField(instance, "outerInstance"));
    }

    @Test
    public void testNewInstance_whenClassNoDefaultConstructor_thenThrowsException() {
        // given
        Class<MyClass> myClass = MyClassWithNoDefaultConstructor.class;

        // when
        Instantiator instantiator = new ConstructorInstantiator(mock(MyOuterClass.class));

        // then
        InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(myClass));

        // and verify the expected message is displayed
        assertEquals("Unable to create mock instance of 'MyClassWithNoDefaultConstructor'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstance_whenClassHasArgsConstructor_withException_thenThrowsExpectedException() {
        // given
        Class<MyClass> myClass = MyClassWithCustomException.class;

        // when
        Instantiator instantiator = new ConstructorInstantiator(mock(MyOuterClass.class));

        // then
        InstantiationException expectedException = new InstantiationException("Custom exception message");
        InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(myClass));

        // and verify the expected exception is thrown
        assertEquals(expectedException.getMessage(), exception.getMessage());
    }

    @Test
    public void testConstructor_whenClassHasConstructor_thenInstanceWithOuterInstanceReturned() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvocationOnMock {
        // given
        Class<MyClass> myClass = MyClassWithConstructor.class;
        MyOuterClass outerInstance = mock(MyOuterClass.class);
        Answers answers = new Answers();
        MyMock myMock = mock(MyMock.class, answers);

        // when
        Instantiator instantiator = new ConstructorInstantiator(outerInstance);

        // then
        Constructor<MyClass> constructor = myClass.getDeclaredConstructor(MyOuterClass.class, MyMock.class);
        Whitebox.setAccessible(constructor, true);

        MyClass instance = instantiator.newInstance(myClass);

        // and verify the constructor is called with the given arguments
        verify(outerInstance).someMethod();
        verify(myMock).someMethod();
        assertSame(outerInstance, Whitebox.getInnerStaticField(instance, "outerInstance"));
    }

    @Test
    public void testConstructor_whenClassHasConstructorWithIllegalAccess_thenThrowsInstantiationException() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        // given
        Class<MyClass> myClass = MyClassWithIllegalAccess.class;
        MyOuterClass outerInstance = mock(MyOuterClass.class);

        // when
        Instantiator instantiator = new ConstructorInstantiator(outerInstance);

        // then
        InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(myClass));

        // and verify the expected message is displayed
        assertEquals("Unable to create mock instance of 'MyClassWithIllegalAccess'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testConstructor_whenClassHasPrivateConstructor_thenThrowsInstantiationException() throws InstantiationException {
        // given
        Class<MyClass> myClass = MyClassWithPrivateConstructor.class;
        MyOuterClass outerInstance = mock(MyOuterClass.class);

        // when
        Instantiator instantiator = new ConstructorInstantiator(outerInstance);

        // then
        InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(myClass));

        // and verify the expected message is displayed
        assertEquals("Unable to create mock instance of 'MyClassWithPrivateConstructor'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    private static class Answers {
        @SuppressWarnings("unchecked")
        public static <T> Answer<T> answer(final T answer) {
            return new Answer<T>() {
                @Override
                public T answer(InvocationOnMock invocation) throws Throwable {
                    return (T) answer;
                }
            };
        }
    }

    private static class MyClass {
        // empty class
    }

    private static class MyClassWithNoDefaultConstructor {
        // empty class with no default constructor
    }

    private static class MyClassWithConstructor {
        private MyOuterClass outerInstance;
        private MyMock myMock;

        public MyClass(MyOuterClass outerInstance, MyMock myMock) {
            this.outerInstance = outerInstance;
            this.myMock = myMock;
        }

        public void someMethod() {
            // some logic
        }
    }

    private static class MyClassWithCustomException extends MyClassWithConstructor {
        public MyClassWithCustomException() throws Exception {
            throw new Exception("Custom exception message");
        }
    }

    private static class MyClassWithIllegalAccess extends MyClassWithConstructor {
        private MyClassWithIllegalAccess() throws IllegalAccessException {
            throw new IllegalAccessException();
        }

        @SuppressWarnings("unused")
        private MyClassWithIllegalAccess(MyOuterClass outerInstance) {
            super(outerInstance, null);
        }
    }

    private static class MyClassWithPrivateConstructor extends MyClassWithConstructor {
        private MyClassWithPrivateConstructor() {
            // private constructor
        }

        @SuppressWarnings("unused")
        private MyClassWithPrivateConstructor(MyOuterClass outerInstance) {
            this();
            super(outerInstance, null);
        }
    }

    private static class MyMock {
        public void someMethod() {
            // some logic
        }
    }

    private static class MyOuterClass {
        public void someMethod() {
            // some logic
        }
    }
}