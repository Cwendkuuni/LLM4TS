import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import reflectasm.CallMethod;
import reflectasm.Factory;

import java.lang.reflect.Method;
import java.util.Arrays;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = Factory.getMethod(DelegatingMethod.class, "getExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        Method testMethod = Factory.getMethod(MockitoMethod.class, "getExceptionTypes");
        Assert.assertArrayEquals(testMethod.getExceptionTypes(), exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = Factory.getMethod(DelegatingMethod.class, "getJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();

        Method testMethod = Factory.getMethod(MockitoMethod.class, "getJavaMethod");
        Assert.assertEquals(testMethod, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = Factory.getMethod(DelegatingMethod.class, "getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();

        Method testMethod = Factory.getMethod(MockitoMethod.class, "getName");
        Assert.assertEquals(testMethod.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = Factory.getMethod(DelegatingMethod.class, "getParameterTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        Method testMethod = Factory.getMethod(MockitoMethod.class, "getParameterTypes");
        Assert.assertArrayEquals(testMethod.getParameterTypes(), parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = Factory.getMethod(DelegatingMethod.class, "getReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();

        Method testMethod = Factory.getMethod(MockitoMethod.class, "getReturnType");
        Assert.assertEquals(testMethod.getReturnType(), returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = Factory.getMethod(DelegatingMethod.class, "isVarArgs");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();

        Method testMethod = Factory.getMethod(MockitoMethod.class, "isVarArgs");
        Assert.assertEquals(testMethod.isVarArgs(), isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = Factory.getMethod(DelegatingMethod.class, "isAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();

        Method testMethod = Factory.getMethod(MockitoMethod.class, "isAbstract");
        Assert.assertEquals(testMethod.isAbstract(), isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = Factory.getMethod(DelegatingMethod.class, "equals");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = Factory.getMethod(DelegatingMethod.class, "equals");
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Method method3 = Factory.getMethod(MockitoMethod.class, "equals");
        Assert.assertFalse(delegatingMethod1.equals(method3));
        Assert.assertFalse(delegatingMethod2.equals(method3));

        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod1));
        Assert.assertTrue(delegatingMethod2.equals(delegatingMethod2));
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
        Assert.assertTrue(delegatingMethod2.equals(delegatingMethod1));

        Method method4 = Factory.getMethod(DelegatingMethod.class, "getJavaMethod");
        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method4);
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod4));
        Assert.assertTrue(delegatingMethod4.equals(delegatingMethod1));
    }

    @Test
    public void testHashCode() {
        Method method = Factory.getMethod(DelegatingMethod.class, "hashCode");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method testMethod = Factory.getMethod(MockitoMethod.class, "hashCode");
        Assert.assertNotSame(testMethod.hashCode(), delegatingMethod.hashCode());
        Assert.assertNotSame(delegatingMethod.hashCode(), 0);
    }
}