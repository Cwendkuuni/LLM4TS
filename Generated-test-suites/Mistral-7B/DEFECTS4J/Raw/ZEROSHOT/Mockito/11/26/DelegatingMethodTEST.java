package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.MockitoMethod;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethodMatcher;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.util.reflection.Reflections.getAllDeclaredMethods;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testDelegatingMethodGetExceptionTypes() {
        Class<?> clazz = MockitoMethod.class;
        Method method = getMethod(clazz, "getExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        Class<?>[] expectedExceptionTypes = method.getExceptionTypes();

        assertThat(exceptionTypes, is(expectedExceptionTypes));
    }

    @Test
    public void testDelegatingMethodGetJavaMethod() {
        Class<?> clazz = MockitoMethod.class;
        Method method = getMethod(clazz, "getJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();

        Method expectedJavaMethod = method;

        assertThat(javaMethod, is(expectedJavaMethod));
    }

    @Test
    public void testDelegatingMethodGetName() {
        Class<?> clazz = MockitoMethod.class;
        Method method = getMethod(clazz, "getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();

        String expectedName = method.getName();

        assertThat(name, is(expectedName));
    }

    @Test
    public void testDelegatingMethodGetParameterTypes() {
        Class<?> clazz = MockitoMethod.class;
        Method method = getMethod(clazz, "getParameterTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        Class<?>[] expectedParameterTypes = method.getParameterTypes();

        assertThat(parameterTypes, is(expectedParameterTypes));
    }

    @Test
    public void testDelegatingMethodGetReturnType() {
        Class<?> clazz = MockitoMethod.class;
        Method method = getMethod(clazz, "getReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();

        Class<?> expectedReturnType = method.getReturnType();

        assertThat(returnType, is(expectedReturnType));
    }

    @Test
    public void testDelegatingMethodIsVarArgs() {
        Class<?> clazz = MockitoMethod.class;
        Method method = getMethod(clazz, "isVarArgs");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean varArgs = delegatingMethod.isVarArgs();

        boolean expectedVarArgs = method.isVarArgs();

        assertThat(varArgs, is(expectedVarArgs));
    }

    @Test
    public void testDelegatingMethodIsAbstract() {
        Class<?> clazz = MockitoMethod.class;
        Method method = getDeclaredMethod(clazz, "isAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean abstractMethod = delegatingMethod.isAbstract();

        boolean expectedAbstractMethod = method.isAbstract();

        assertThat(abstractMethod, is(expectedAbstractMethod));
    }

    @Test
    public void testDelegatingMethodEquals() {
        Class<?> clazz = MockitoMethod.class;
        Method method1 = getMethod(clazz, "getName");
        Method method2 = getMethod(clazz, "getJavaMethod");

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Object anotherObject = new Object();
        DelegatingMethod anotherDelegatingMethod = new DelegatingMethod(method1);

        Method sameMethod = getMethod(clazz, "getName");
        DelegatingMethod sameDelegatingMethod = new DelegatingMethod(sameMethod);

        MockitoMethod sameMockitoMethod = spy(method1);
        DelegatingMethod sameMockitoDelegatingMethod = new DelegatingMethod(sameMockitoMethod);

        // DelegatingMethod vs DelegatingMethod
        boolean equalsDelegatingMethodToDelegatingMethod = delegatingMethod1.equals(delegatingMethod2);
        boolean expectedEqualsDelegatingMethodToDelegatingMethod = false;
        assertThat(equalsDelegatingMethodToDelegatingMethod, is(expectedEqualsDelegatingMethod));

        // DelegatingMethod vs Method
        boolean equalsDelegatingMethodToMethod = delegatingMethod1.equals(method1);
        boolean expectedEqualsDelegatingMethodToMethod = true;
        assertThat(equalsDelegatingMethodToMethod, is(expectedEqualsDelegatingMethodToMethod));

        // DelegatingMethod vs Object
        boolean equalsDelegatingMethodToAnotherObject = delegatingMethod1.equals(anotherObject);
        boolean expectedEqualsDelegatingMethodToAnotherObject = false;
        assertThat(equalsDelegatingMethodToAnotherObject, is(expectedEqualsDelegatingMethodToAnotherObject));

        // DelegatingMethod vs same DelegatingMethod
        boolean equalsDelegatingMethodToSameDelegatingMethod = delegatingMethod1.equals(delegatingMethod1);
        boolean expectedEqualsDelegatingMethodToSameDelegatingMethod = true;
        assertThat(equalsDelegatingMethodToSameDelegatingMethod, is(expectedEqualsDelegatingMethodToSameDelegatingMethod));

        // DelegatingMethod vs same MockitoMethod
        boolean equalsDelegatingMethodToSameMockitoDelegatingMethod = delegatingMethod1.equals(sameMockitoDelegatingMethod);
        boolean expectedEqualsDelegatingMethodToSameMockitoDelegatingMethod = true;
        assertThat(equalsDelegatingMethodToSameMockitoDelegatingMethod, is(expectedEqualsDelegatingMethodToSameMockitoDelegatingMethod));

        // Method vs DelegatingMethod
        boolean equalsMethodToDelegatingMethod = method1.equals(delegatingMethod1);
        boolean expectedEqualsMethodToDelegatingMethod = false;
        assertThat(equalsMethodToDelegatingMethod, is(expectedEqualsMethodToDelegatingMethod));

        // Method vs Method
        boolean equalsMethodToMethod = method1.equals(method2);
        boolean expectedEqualsMethodToMethod = false;
        assertThat(equalsMethodToMethod, is(expectedEqualsMethodToMethod));

        // DelegatingMethod vs same DelegatingMethod with different hashCode
        DelegatingMethod sameDelegatingMethodWithDifferentHashCode = new DelegatingMethod(sameMethod) {
            @Override
            public int hashCode() {
                return 2;
            }
        };
        boolean equalsSameDelegatingMethodWithDifferentHashCodeToSameDelegatingMethod = delegatingMethod1.equals(sameDelegatingMethodWithDifferentHashCode);
        boolean expectedEqualsSameDelegatingMethodWithDifferentHashCodeToSameDelegatingMethod = true;
        assertThat(equalsSameDelegatingMethodWithDifferentHashCodeToSameDelegatingMethod, is(expectedEqualsSameDelegatingMethodWithDifferentHashCodeToSameDelegatingMethod));
    }

    @Test
    public void testDelegatingMethodHashCode() {
        Class<?> clazz = MockitoMethod.class;
        Method method = getMethod(clazz, "getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();

        int expectedHashCode = method.hashCode();

        assertThat(hashCode, is(expectedHashCode));
    }

    private Method getMethod(Class<?> clazz, String methodName) {
        return Arrays.stream(getAllDeclaredMethods(clazz)).filter(method -> method.getName().equals(methodName)).findFirst().orElse(null);
    }

    private Method getDeclaredMethod(Class<?> clazz, String methodName) {
        return clazz.getDeclaredMethod(methodName);
    }
}