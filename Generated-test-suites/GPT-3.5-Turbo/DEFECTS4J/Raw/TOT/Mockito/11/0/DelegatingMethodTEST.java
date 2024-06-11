import org.junit.Test;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void getExceptionTypes_shouldReturnCorrectExceptionTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(method.getExceptionTypes(), exceptionTypes);
    }

    @Test
    public void getJavaMethod_shouldReturnCorrectJavaMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();

        assertEquals(method, javaMethod);
    }

    @Test
    public void getName_shouldReturnCorrectName() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();

        assertEquals(method.getName(), name);
    }

    @Test
    public void getParameterTypes_shouldReturnCorrectParameterTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(method.getParameterTypes(), parameterTypes);
    }

    @Test
    public void getReturnType_shouldReturnCorrectReturnType() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();

        assertEquals(method.getReturnType(), returnType);
    }

    @Test
    public void isVarArgs_shouldReturnCorrectVarArgs() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();

        assertEquals(method.isVarArgs(), isVarArgs);
    }

    @Test
    public void isAbstract_shouldReturnCorrectAbstract() {
        Method method = getAbstractTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();

        assertEquals(method.getModifiers() & Modifier.ABSTRACT, isAbstract);
    }

    @Test
    public void equals_shouldReturnTrueForEqualDelegatingMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);

        assertTrue(equals);
    }

    @Test
    public void equals_shouldReturnTrueForEqualMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean equals = delegatingMethod.equals(method);

        assertTrue(equals);
    }

    @Test
    public void equals_shouldReturnFalseForDifferentObject() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean equals = delegatingMethod.equals(new Object());

        assertFalse(equals);
    }

    @Test
    public void hashCode_shouldReturnSameHashCode() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    private Method getTestMethod() {
        try {
            return DelegatingMethodTest.class.getMethod("getTestMethod");
        } catch (NoSuchMethodException e) {
            fail("Test method not found");
            return null;
        }
    }

    private Method getAbstractTestMethod() {
        try {
            return DelegatingMethod.class.getMethod("isAbstract");
        } catch (NoSuchMethodException e) {
            fail("Test method not found");
            return null;
        }
    }
}