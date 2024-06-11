package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockitoMethod(() -> { throw new RuntimeException(); })
                .getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = new Class[] {RuntimeException.class};
        Assert.assertArrayEquals(expected, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockitoMethod(() -> { return "test"; })
                .getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertSame(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new MockitoMethod(() -> { return "test"; })
                .getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expected = "test";
        Assert.assertEquals(expected, delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockitoMethod((int a, int b) -> { return a + b; })
                .getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = new Class[]{int.class, int.class};
        Assert.assertArrayEquals(expected, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new MockitoMethod((int a, int b) -> { return a + b; })
                .getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expected = int.class;
        Assert.assertEquals(expected, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MockitoMethod((int... numbers) -> { return numbers.length; })
                .getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = true;
        Assert.assertEquals(expected, delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = AbstractClassWithMethod.class.getMethod("abstractMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = true;
        Assert.assertEquals(expected, delegatingMethod.isAbstract());
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsWithNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new MockitoMethod(() -> {}).getJavaMethod());
        delegatingMethod.equals(null);
    }

    @Test
    public void testHashCode() {
        Method method1 = new MockitoMethod(() -> {})
                .getJavaMethod();
        Method method2 = new MockitoMethod(() -> {})
                .getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }
}