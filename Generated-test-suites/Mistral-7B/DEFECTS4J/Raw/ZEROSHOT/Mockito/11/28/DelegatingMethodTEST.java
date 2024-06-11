package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;
import org.powermock.api.mockito.powered;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DelegatingMethod.class)
public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = mock(Method.class);
        when(method.getExceptionTypes()).thenReturn(new Class[0]);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(exceptionTypes.length, 0);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals(name, "testMethod");
    }

    @Test
    public void testGetParameterTypes() {
        Method method = mock(Method.class);
        when(method.getParameterTypes()).thenReturn(new Class[]{String.class, int.class});

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class[]{String.class, int.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(String.class);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(returnType, String.class);
    }

    @Test
    public void testIsVarArgs() {
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean varArgs = delegatingMethod.isVarArgs();
        assertTrue(varArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = mock(Method.class);
        when(method.isAbstract()).thenReturn(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();
        assertTrue(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod1.equals(null));
        assertFalse(delegatingMethod1.equals(""));

        when(method1.equals(method2)).thenReturn(true);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        when(method1.equals(method2)).thenReturn(false);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = mock(Method.class);

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    @Test
    public void testAnswerInvocationWithReturnValue() {
        Class<?> returnType = String.class;
        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(returnType);

        Stubber stubber = mock(Stubber.class);
        MethodInterceptor interceptor = mock(MethodInterceptor.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };

        when(stubber.thenAnswer(any(Answer.class))).thenReturn(interceptor);
        when(interceptor.invoke(any(Object.class), any(Object[].class))).thenCallRealMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Object result = delegatingMethod.invokeAnswer(stubber, null);
        assertEquals("answer", result);
    }

    @Test
    public void testAnswerInvocationWithArgument() {
        Class<?> returnType = String.class;
        Class<?> argumentType = int.class;

        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(returnType);
        when(method.getParameterTypes()).thenReturn(new Class[]{argumentType});

        Stubber stubber = mock(Stubber.class);
        MethodInterceptor interceptor = mock(MethodInterceptor.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                int argument = (int) args[0];
                return String.format("answer %d", argument);
            }
        };

        when(stubber.thenAnswer(any(Answer.class))).thenReturn(interceptor);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Object result = delegatingMethod.invokeAnswer(stubber, 42);
        assertEquals("answer 42", result);
    }

    @Test
    public void testAnswerInvocationWithThrowable() {
        Class<?> returnType = String.class;
        Class<?> argumentType = int.class;

        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(returnType);
        when(method.getParameterTypes()).thenReturn(new Class[]{argumentType});

        Stubber stubber = mock(Stubber.class);
        MethodInterceptor interceptor = mock(MethodInterceptor.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                int argument = (int) args[0];
                throw new RuntimeException("expected exception for argument " + argument);
            }
        };

        when(stubber.thenAnswer(any(Answer.class))).thenReturn(interceptor);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        try {
            Object result = delegatingMethod.invokeAnswer(stubber, 42);
            fail();
        } catch (RuntimeException e) {
            assertEquals("expected exception for argument 42", e.getMessage());
        }
    }

    @Test
    public void testAnswerInvocationWithCallRealMethod() {
        Class<?> returnType = String.class;
        Class<?> argumentType = int.class;

        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(returnType);
        when(method.getParameterTypes()).thenReturn(new Class[]{argumentType});

        Stubber stubber = mock(Stubber.class);
        MethodInterceptor interceptor = mock(MethodInterceptor.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                int argument = (int) args[0];
                return method.invoke(null, new Object[]{argument});
            }
        };

        when(stubber.thenAnswer(any(Answer.class))).thenReturn(interceptor);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Object result = delegatingMethod.invokeAnswer(stubber, 42);
        assertNotNull(result);
    }

    @Test
    public void testAnswerInvocationWithReturningNull() {
        Class<?> returnType = String.class;
        Class<?> argumentType = int.class;

        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(returnType);
        when(method.getParameterTypes()).thenReturn(new Class[]{argumentType});

        Stubber stubber = mock(Stubber.class);
        MethodInterceptor interceptor = mock(MethodInterceptor.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };

        when(stubber.thenAnswer(any(Answer.class))).thenReturn(interceptor);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Object result = delegatingMethod.invokeAnswer(stubber, 42);
        assertNull(result);
    }
}