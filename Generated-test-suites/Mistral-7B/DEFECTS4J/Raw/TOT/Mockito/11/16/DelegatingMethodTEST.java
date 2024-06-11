package org.mockito.samples.junit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.creation.DelegatingMethod;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Class<?> clazz = MyClass.class;
        Method method = clazz.getDeclaredMethod("myMethod", new Class[] { String.class, Integer.TYPE });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedExceptionTypes = new Class[] { MyException1.class, MyException2.class };
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetName() {
        Class<?> clazz = MyClass.class;
        Method method = clazz.getDeclaredMethod("myMethod", new Class[] { String.class, Integer.TYPE });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expectedName = "myMethod";
        String actualName = delegatingMethod.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?> clazz = MyClass.class;
        Method method = clazz.getDeclaredMethod("myMethod", new Class[] { String.class, Integer.TYPE });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedParameterTypes = new Class[] { String.class, Integer.TYPE };
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(expectedParameterTypes, actualParameterTypes);

        String inputString = "hello";
        Integer inputInteger = 42;
        Object[] inputArguments = new Object[] { inputString, inputInteger };

        Object[] expectedArguments = new Object[] { inputString, inputInteger };
        Object[] actualArguments = delegatingMethod.getJavaMethod().invoke(spy(MyClass.class), inputArguments);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void testIsVarArgs() {
        Class<?> clazz = MyClass.class;
        Method method = clazz.getDeclaredMethod("myMethod", new Class[] { String[].class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expectedIsVarArgs = true;
        boolean actualIsVarArgs = delegatingMethod.isVarArgs();

        assertEquals(expectedIsVarArgs, actualIsVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Class<?> clazz = MyAbstractClass.class;
        Method method = clazz.getDeclaredMethod("myMethod", new Class[] {});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expectedIsAbstract = true;
        boolean actualIsAbstract = delegatingMethod.isAbstract();

        assertEquals(expectedIsAbstract, actualIsAbstract);
    }

    @Test
    public void testEquals() {
        Class<?> clazz = MyClass.class;
        Method firstMethod = clazz.getDeclaredMethod("myMethod1", new Class[] { String.class });
        Method secondMethod = clazz.getDeclaredMethod("myMethod2", new Class[] { String.class });

        DelegatingMethod firstDelegatingMethod = new DelegatingMethod(firstMethod);
        DelegatingMethod secondDelegatingMethod = new DelegatingMethod(secondMethod);

        boolean expectedEquals = false;
        boolean actualEquals = firstDelegatingMethod.equals(secondDelegatingMethod);

        assertEquals(expectedEquals, actualEquals);

        Method sameMethod = clazz.getDeclaredMethod("myMethod1", new Class[] { String.class });
        DelegatingMethod sameDelegatingMethod = new DelegatingMethod(sameMethod);

        expectedEquals = true;
        actualEquals = firstDelegatingMethod.equals(sameDelegatingMethod);

        assertEquals(expectedEquals, actualEquals);
    }
}