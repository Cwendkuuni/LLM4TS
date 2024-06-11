package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testConstructor() {
        Method testMethod = new Method("testMethod", new Class[]{}, new Class[]{String.class});
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);

        Assert.assertNotNull(delegatingMethod);
        Assert.assertNotNull(delegatingMethod.getJavaMethod());
        Assert.assertEquals(testMethod, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetExceptionTypes() {
        Method testMethod = new Method("testMethod", new Class[]{}, new Class[]{}, new Class[]{Exception.class, RuntimeException.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertEquals(2, exceptionTypes.length);
        Assert.assertTrue(Exception.class.isAssignableFrom(exceptionTypes[0]));
        Assert.assertTrue(RuntimeException.class.isAssignableFrom(exceptionTypes[1]));
    }

    @Test
    public void testGetJavaMethod() {
        Method testMethod = new Method("testMethod", new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(testMethod, javaMethod);
    }

    @Test
    public void testGetName() {
        Method testMethod = new Method("testMethod", new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method testMethod = new Method("testMethod", new Class[]{String.class}, new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertEquals(1, parameterTypes.length);
        Assert.assertEquals(String.class, parameterTypes[0]);
    }

    @Test
    public void testGetReturnType() {
        Method testMethod = new Method("testMethod", new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertEquals(void.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method testMethodVariable = new Method("testMethodVariable", new Class[]{}, new Class[]{String[].class});
        Method testMethodNoVariable = new Method("testMethodNoVariable", new Class[]{}, new Class[]{});
        DelegatingMethod delegatingMethodVariable = new DelegatingMethod(testMethodVariable);
        DelegatingMethod delegatingMethodNoVariable = new DelegatingMethod(testMethodNoVariable);

        boolean isVarArgsVariable = delegatingMethodVariable.isVarArgs();
        boolean isVarArgsNoVariable = delegatingMethodNoVariable.isVarArgs();

        Assert.assertTrue(isVarArgsVariable);
        Assert.assertFalse(isVarArgsNoVariable);
    }

    @Test
    public void testIsAbstract() {
        Method testMethodAbstract = new Method("testMethodAbstract", new Class[]{}, new Class[]{});
        Method testMethodConcrete = new Method("testMethodConcrete", new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethodAbstract = new DelegatingMethod(testMethodAbstract);
        DelegatingMethod delegatingMethodConcrete = new DelegatingMethod(testMethodConcrete);

        boolean isAbstract = delegatingMethodAbstract.isAbstract();
        boolean isConcrete = delegatingMethodConcrete.isAbstract();

        Assert.assertTrue(isAbstract);
        Assert.assertFalse(isConcrete);
    }

    @Test
    public void testEquals() {
        Method testMethod1 = new Method("testMethod1", new Class[]{}, new Class[]{String.class});
        Method testMethod2 = new Method("testMethod2", new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);

        Assert.assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method testMethod1Equals = new Method("testMethod1", new Class[]{}, new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod1Equals = new DelegatingMethod(testMethod1Equals);

        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod1Equals));
        Assert.assertTrue(delegatingMethod1Equals.equals(delegatingMethod1));

        Assert.assertTrue(testMethod1.equals(testMethod1Equals));
        Assert.assertTrue(testMethod1Equals.equals(testMethod1));

        Assert.assertTrue(delegatingMethod1.equals(testMethod1));
        Assert.assertTrue(testMethod1.equals(delegatingMethod1));
    }

    @Test
    public void testHashCode() {
        Method testMethod1 = new Method("testMethod1", new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod1);
        Method testMethod2 = new Method("testMethod2", new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        Assert.assertNotSame(hashCode1, hashCode2);
    }
}