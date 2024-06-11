package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.MockitoMethod;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.util.reflection.UsesReflection;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method("testMethod", new Class[]{String.class, Integer.TYPE}, new Class[0]);
        MockitoMethod delegatingMethod = new DelegatingMethod(method);
        MockitoMethod spyDelegatingMethod = spy(delegatingMethod);

        Class<?>[] expectedExceptionTypes = new Class[]{IllegalArgumentException.class};
        Class<?>[] actualExceptionTypes = spyDelegatingMethod.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);

        // Test with an empty array
        delegatingMethod = new DelegatingMethod(new Method("emptyMethod"));
        actualExceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[0], actualExceptionTypes);
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[]{String.class, Integer.TYPE}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();

        Assert.assertEquals(expectedName, actualName);

        method = new Method("testMethod_with_special_characters", new Class[]{String.class, Integer.TYPE}, new Class[0]);
        delegatingMethod = new DelegatingMethod(method);
        expectedName = "testMethod_with_special_characters";
        actualName = delegatingMethod.getName();

        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method("testMethod", new Class[]{String.class, Integer.TYPE}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method expectedMethod = method;
        Method actualMethod = delegatingMethod.getJavaMethod();

        Assert.assertEquals(expectedMethod, actualMethod);

        method = new Method("testMethod", new Type[] {new TypeType(String.class), new TypeType(Integer.TYPE)}, new Class[0]);
        delegatingMethod = new DelegatingMethod(method);
        expectedMethod = method;
        actualMethod = delegatingMethod.getJavaMethod();

        Assert.assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testIsVarArgs() {
        Method method1 = new Method("testMethod1", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        boolean expectedIsVarArgs1 = false;
        boolean actualIsVarArgs1 = delegatingMethod1.isVarArgs();

        Assert.assertEquals(expectedIsVarArgs1, actualIsVarArgs1);

        Method method2 = new Method("testMethod2", new Class[]{String[].class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        boolean expectedIsVarArgs2 = true;
        boolean actualIsVarArgs2 = delegatingMethod2.isVarArgs();

        Assert.assertEquals(expectedIsVarArgs2, actualIsVarArgs2);
    }

    @Test
    public void testIsAbstract() {
        Method method1 = new Method("testMethod1", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        boolean expectedIsAbstract1 = false;
        boolean actualIsAbstract1 = delegatingMethod1.isAbstract();

        Assert.assertEquals(expectedIsAbstract1, actualIsAbstract1);

        Method method2 = new Method("testMethod2", new Class[0], Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        boolean expectedIsAbstract2 = true;
        boolean actualIsAbstract2 = delegatingMethod2.isAbstract();

        Assert.assertEquals(expectedIsAbstract2, actualIsAbstract2);
    }

    @Test
    public void testEquals() {
        Method method1 = new Method("testMethod1", new Class[]{String.class, Integer.TYPE}, new Class[0]);
        Method method2 = new Method("testMethod2", new Class[]{String.class, Integer.TYPE}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        boolean expectedEquals = false;
        boolean actualEquals = delegatingMethod1.equals(delegatingMethod2);

        Assert.assertEquals(expectedEquals, actualEquals);

        method1 = method2;
        delegatingMethod1 = new DelegatingMethod(method1);
        expectedEquals = true;
        actualEquals = delegatingMethod1.equals(delegatingMethod2);

        Assert.assertEquals(expectedEquals, actualEquals);
    }

    @Test
    public void testHashCode() {
        Method method1 = new Method("testMethod1", new Class[]{String.class, Integer.TYPE}, new Class[0]);
        Method method2 = new Method("testMethod2", new Class[]{String.class, Integer.TYPE}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int expectedHashCode1 = 1;
        int actualHashCode1 = delegatingMethod1.hashCode();

        Assert.assertEquals(expectedHashCode1, actualHashCode1);

        int expectedHashCode2 = 1;
        int actualHashCode2 = delegatingMethod2.hashCode();

        Assert.assertEquals(expectedHashCode2, actualHashCode2);

        method1 = method2;
        delegatingMethod1 = new DelegatingMethod(method1);
        expectedHashCode1 = delegatingMethod1.hashCode();
        actualHashCode1 = delegatingMethod2.hashCode();

        Assert.assertNotEquals(expectedHashCode1, actualHashCode2);
    }
}