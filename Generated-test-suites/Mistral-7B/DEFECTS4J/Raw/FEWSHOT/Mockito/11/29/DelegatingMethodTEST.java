package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {RuntimeException.class};
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
                return new Class[] {};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
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

        assertArrayEquals(new Class[] {RuntimeException.class}, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
            }

            @Override
            public Method getJavaMethod() {
                return new Method(new Class[]{}, "testMethod", new Class[]{String.class, int.class});
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[]{String.class, int.class};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
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

        assertEquals(new Method(new Class[]{}, "testMethod", new Class[]{String.class, int.class}), delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
            }

            @Override
            public Method getJavaMethod() {
                return null;
            }

            @Override
            public String getName() {
                return "testMethod";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[]{String.class, int.class};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
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

        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
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
                return new Class[]{String.class, int.class};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
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

        assertArrayEquals(new Class[]{String.class, int.class}, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
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
                return new Class[]{String.class, int.class};
            }

            @Override
            public Class<?> getReturnType() {
                return Integer.class;
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

        assertEquals(Integer.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
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
                return new Class[]{String.class};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
            }

            @Override
            public boolean isVarArgs() {
                return true;
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

        assertTrue(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
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
                return new Class[]{String.class};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
            }

            @Override
            public boolean isVarArgs() {
                return false;
            }

            @Override
            public boolean isAbstract() {
                return true;
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

        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
            }

            @Override
            public Method getJavaMethod() {
                return new Method(new Class[]{}, "testMethod", new Class[]{String.class, int.class});
            }

            @Override
            public String getName() {
                return "testMethod";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[]{String.class, int.class};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
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

        Method method2 = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
            }

            @Override
            public Method getJavaMethod() {
                return new Method(new Class[]{}, "testMethod", new Class[]{String.class, int.class});
            }

            @Override
            public String getName() {
                return "testMethod";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[]{String.class, int.class};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
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

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = new Method(new Class[]{}, "testMethod", new Class[]{String.class, int.class});

        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertTrue(delegatingMethod1.equals(delegatingMethod3));
    }

    @Test
    public void testHashCode() {
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
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
                return new Class[]{String.class, int.class};
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
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

        assertEquals(0, delegatingMethod.hashCode());
    }

    @Test
    public void testNullInput() {
        assertNull(new DelegatingMethod(null));
    }
}