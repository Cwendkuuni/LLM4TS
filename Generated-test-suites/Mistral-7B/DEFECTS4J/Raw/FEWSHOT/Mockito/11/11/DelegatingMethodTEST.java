package org.mockito.internal.creation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockitoMethod() {
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
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[]{Exception.class};
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[]{Exception.class}, actualExceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod", new Class[0]);
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
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method actualJavaMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(new Method("testMethod", new Class[0]), actualJavaMethod);
    }

    @Test
    public void testgetName() {
        Method method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod", new Class[0]);
            }

            @Override
            public String getName() {
                return "name";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public Class<?> getReturnType() {
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String actualName = delegatingMethod.getName();
        Assert.assertEquals("name", actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod", new Class[]{String.class, int.class});
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public Class<?> getReturnType() {
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[]{String.class, int.class}, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod", new Class[0]);
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> actualReturnType = delegatingMethod.getReturnType();
        Assert.assertEquals(String.class, actualReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod", new Class[0]);
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
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean actualIsVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(actualIsVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod", new Class[0]);
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
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean actualIsAbstract = delegatingMethod.isAbstract();
        Assert.assertTrue(actualIsAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod1", new Class[0]);
            }

            @Override
            public String getName() {
                return "testMethod1";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public Class<?> getReturnType() {
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        Method method2 = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod2", new Class[0]);
            }

            @Override
            public String getName() {
                return "testMethod1";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public Class<?> getReturnType() {
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean actualEquals = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertFalse(actualEquals);

        Method method3 = new Method("testMethod1", new Class[0]);

        delegatingMethod1 = new DelegatingMethod(method1);
        delegatingMethod2 = new DelegatingMethod(method3);

        actualEquals = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertTrue(actualEquals);
    }

    @Test
    public void testHashCode() {
        Method method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return new Method("testMethod", new Class[0]);
            }

            @Override
            public String getName() {
                return "testMethod";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public Class<?> getReturnType() {
                return Object.class;
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
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }
        };

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);

        int actualHashCode1 = delegatingMethod1.hashCode();
        Assert.assertEquals(1, actualHashCode1);

        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int actualHashCode2 = delegatingMethod2.hashCode();
        Assert.assertEquals(1, actualHashCode2);

        Assert.assertEquals(actualHashCode1, actualHashCode2);
    }
}