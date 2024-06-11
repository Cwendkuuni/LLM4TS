package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = new Class[0];
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return exceptionTypes;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertNull(delegatingMethod.getExceptionTypes());

        exceptionTypes = new Class[] {Exception1.class, Exception2.class};
        method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return exceptionTypes;
            }
        };
        delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(exceptionTypes, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}) {
            @Override
            public boolean isVarArgs() {
                return false;
            }

            @Override
            public boolean isAbstract() {
                return false;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        assertNotNull(delegatingMethod.getJavaMethod());
        assertEquals(javaMethod, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}) {
            @Override
            public String getName() {
                return "testMethod2";
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = new Class[] {String.class, int.class};
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getParameterTypes() {
                return parameterTypes;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(parameterTypes, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = Integer.class;
        Method method = new MockitoMethod() {
            @Override
            public Class<?> getReturnType() {
                return returnType;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(returnType, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {String.class, int[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        method = new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class});
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(TestClass.class, "testMethod", new Class[0]);
        Method method2 = new Method(TestClass.class, "testMethod", new Class[] {});
        Method method3 = new Method(Object.class, "equals", new Class[] {Object.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertFalse(delegatingMethod1.isAbstract());
        assertFalse(delegatingMethod2.isAbstract());
        assertTrue(delegatingMethod3.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[] {});
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[] {});
        Method method3 = new Method(TestClass.class, "testMethod1", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod1.equals(delegatingMethod3));
        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(delegatingMethod3));
        assertTrue(delegatingMethod3.equals(delegatingMethod1));
    }

    @Test
    public void testHashCode() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[0]);
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertTrue(hashCode1 != hashCode2);
        assertTrue(hashCode1 == delegatingMethod1.hashCode());
    }
}

class TestClass {
    public void testMethod(String s, int... i) {
    }

    public void testMethod1() {
    }

    public void testMethod2() {
    }

    public boolean equals(Object obj) {
        return false;
    }
}

class Exception1 {
}

class Exception2 {
}