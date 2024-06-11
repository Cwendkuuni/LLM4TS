package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class DelegatingMethodTest {

    private static final String CLASS_NAME = DelegatingMethod.class.getName();
    private static final String METHOD_NAME = "testMethod";

    @Test
    public void testGetExceptionTypes() {
        Method method = Mockito.spy(new MethodStub());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = Mockito.spy(new MethodStub());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();

        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = Mockito.spy(new MethodStub());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();

        assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = Mockito.spy(new MethodStub(String.class, int.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertEquals(2, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);
        assertEquals(int.class, parameterTypes[1]);
    }

    @Test
    public void testGetReturnType() {
        Method method = Mockito.spy(new MethodStub(void.class, String.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();

        assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = Mockito.spy(new MethodStub(void.class, String[].class, VariableArgs.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean varArgs = delegatingMethod.isVarArgs();

        assertTrue(varArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = Mockito.spy(new AbstractMethodStub());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean abstractMethod = delegatingMethod.isAbstract();

        assertTrue(abstractMethod);
    }

    @Test
    public void testEquals() {
        Method method1 = Mockito.spy(new MethodStub());
        Method method2 = Mockito.spy(new MethodStub());

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(null));
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod1.equals(new Object()));

        Method method3 = method1;
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertTrue(delegatingMethod1.equals(delegatingMethod3));
        assertTrue(delegatingMethod3.equals(delegatingMethod1));

        assertTrue(method1.equals(delegatingMethod1.getJavaMethod()));
        assertTrue(delegatingMethod1.getJavaMethod().equals(method1));
        assertTrue(method1.equals(delegatingMethod1.getJavaMethod()));
        assertTrue(delegatingMethod1.getJavaMethod().equals(method1));

        Method method4 = method1;
        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method4);

        assertTrue(delegatingMethod1.equals(delegatingMethod4));
        assertTrue(delegatingMethod4.equals(delegatingMethod1));

        Method method5 = new MethodStub();
        DelegatingMethod delegatingMethod5 = new DelegatingMethod(method5);

        assertFalse(delegatingMethod1.equals(delegatingMethod5));
        assertFalse(delegatingMethod5.equals(delegatingMethod1));

        Method method6 = new MethodStub();
        DelegatingMethod delegatingMethod6 = new DelegatingMethod(method6);

        assertFalse(delegatingMethod1.equals(delegatingMethod6));
        assertFalse(delegatingMethod6.equals(delegatingMethod1));

        Method method7 = Mockito.spy(method1);
        DelegatingMethod delegatingMethod7 = new DelegatingMethod(method7);

        assertTrue(delegatingMethod1.equals(delegatingMethod7));
        assertTrue(delegatingMethod7.equals(delegatingMethod1));

        Method method8 = Mockito.spy(method1);
        DelegatingMethod delegatingMethod8 = new DelegatingMethod(method8);

        assertTrue(delegatingMethod1.equals(delegatingMethod8));
        assertTrue(delegatingMethod8.equals(delegatingMethod1));
    }

    @Test
    public void testHashCode() {
        Method method = Mockito.spy(new MethodStub());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();

        assertEquals(1, hashCode);
    }

    private static class MethodStub extends MockitoMethod {
        private MethodStub() {
        }

        private MethodStub(Class<?>... parameterTypes) {
            super(parameterTypes);
        }

        private MethodStub(Class<?> returnType, Class<?>... parameterTypes) {
            super(returnType, parameterTypes);
        }

        private static Class<?> VariableArgs = new Class<?>[]{int[].class};
    }

    private static class AbstractMethodStub extends MethodStub {
        private AbstractMethodStub() {
            super(int.class);
        }
    }

    private static class MethodStubWithAnswer implements Answer {
        private Method method;

        public MethodStubWithAnswer(Method method) {
            this.method = method;
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Whitebox.setInternalState(Method.class, this.method, "mockito_originalName", "testMethod");
            return null;
        }
    }
}