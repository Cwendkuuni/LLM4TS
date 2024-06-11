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

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    private Object outerClassInstance;

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ParameterlessConstructor_Succeeds() throws Exception {
        Class<T> cls = (Class<T>) Class.forName("YourTestClassToMock");
        ConstructorInstantiator instantIator = new ConstructorInstantiator(mock(Object.class, Strictness.LENIENT));
        T result = instantIator.newInstance(cls);
        verify(cls).newInstance();
        verify(cls).equals(result.getClass());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_ParameterlessConstructor_ThrowsExceptionIfNoSuchConstructor() throws Exception {
        Class<T> cls = (Class<T>) Class.forName("YourTestClassToMockWithoutParameterlessConstructor");
        ConstructorInstantiator instantIator = new ConstructorInstantiator(mock(Object.class, Strictness.LENIENT));
        instantIator.newInstance(cls);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ConstructorWithOneArgument_Succeeds() throws Exception {
        Class<T> cls = (Class<T>) Class.forName("YourTestClassToMockWithOneArgument");
        Object argument = mock(YourArgumentType.class, Strictness.LENIENT);

        ConstructorInstantiator instantIator = new ConstructorInstantiator(mock(Object.class, Strictness.LENIENT));
        doAnswer(new Answer<T>() {
            @Override
            public T answer(InvocationOnMock invocation) throws Throwable {
                Constructor<T> constructor = (Constructor<T>) invocation.getMethod().getConstructor(YourArgumentType.class);
                return constructor.newInstance(argument);
            }
        }).when(instanceIator).withOuterClass(cls);

        T result = instantIator.newInstance(cls);
        verify(cls).getDeclaredConstructor(YourArgumentType.class);
        verify(cls).newInstance(argument);
        verify(cls).equals(result.getClass());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_ConstructorWithOneArgument_ThrowsExceptionIfNoSuchConstructor() throws Exception {
        Class<T> cls = (Class<T>) Class.forName("YourTestClassToMockWithOneArgumentButNoSuchConstructor");
        Object argument = mock(YourArgumentType.class, Strictness.LENIENT);

        ConstructorInstantiator instantIator = new ConstructorInstantiator(mock(Object.class, Strictness.LENIENT));
        instantIator.newInstance(cls);
    }

    @Test
    public void testParamsException_ThrowsExpectedException() {
        Class<Object> cls = Object.class;

        try {
            new ConstructorInstantiator(mock(Object.class, Strictness.LENIENT)).paramsException(cls, new RuntimeException("Test"));
            fail();
        } catch (InstantiationException e) {
            verify(e).getMessage().contains("Unable to create mock instance of 'Object'.");
            verify(e).getMessage().contains("Please ensure that the outer instance has correct type and that the target class has parameter-less constructor.");
        }
    }

    @Test
    public void testNoArgConstructor_ThrowsExpectedException() {
        Class<Object> cls = Object.class;

        try {
            ConstructorInstantiator.noArgConstructor(cls);
            fail();
        } catch (InstantiationException e) {
            verify(e).getMessage().contains("Unable to create mock instance of 'Object'.");
            verify(e).getMessage().contains("Please ensure it has parameter-less constructor.");
        }
    }
}