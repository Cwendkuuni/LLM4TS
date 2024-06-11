package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = new Class[0];
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return exceptionTypes;
            }

            @Override
            public Method getJavaMethod() {
                return null;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public Class<?> getReturnType() {
                return null;
            }

            @Override
            public boolean isVarArgs() {
                return false;
            }

            @Override
            public boolean isAbstract() {
                return false;
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(new Class[0], delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method("testMethod", new Class[0].getClass());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[0].getClass());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = new Class[]{String.class, int.class};
        Method method = new Method("testMethod", parameterTypes);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(parameterTypes, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = String.class;
        Method method = new Method("testMethod", new Class[0].getClass());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(returnType, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method("testMethod", new Class[]{String[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        method = new Method("testMethod", new Class[0].getClass());
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("testMethod", new Class[0].getClass());
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());

        method = new Method("abstractMethod", new Class[0].getClass());
        Modifier[] modifiers = new Modifier[]{Modifier.ABSTRACT};
        method.setModifiers(Modifier.concat(Integer.toBinaryString(method.getModifiers()), new String(modifiers)));
        delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method("testMethod1", new Class[0].getClass());
        Method method2 = new Method("testMethod2", new Class[0].getClass());
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        delegatingMethod1 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = method1;
        delegatingMethod1 = new DelegatingMethod(method3);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method1 = new Method("testMethod1", new Class[0].getClass());
        Method method2 = new Method("testMethod2", new Class[0].getClass());
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        delegatingMethod1 = new DelegatingMethod(method2);
        assertEquals(hashCode1, delegatingMethod1.hashCode());

        Method method3 = method1;
        delegatingMethod1 = new DelegatingMethod(method3);
        assertEquals(hashCode1, delegatingMethod1.hashCode());
    }
}