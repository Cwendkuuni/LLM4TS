import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        // Basic functional test
        assertArrayEquals(new Class[0], exceptionTypes);

        // Edge case: Method with exceptions
        Method methodWithExceptions = new Method(Object.class.getName(), "methodName", new Class[]{String.class, Integer.class}, new Class[]{String.class, Exception.class});
        DelegatingMethod delegatingMethodWithExceptions = new DelegatingMethod(methodWithExceptions);
        Class<?>[] exceptionTypesWithExceptions = delegatingMethodWithExceptions.getExceptionTypes();
        assertArrayEquals(new Class[]{String.class, Exception.class}, exceptionTypesWithExceptions);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();

        // Basic functional test
        assertEquals(method, javaMethod);

        // Edge case: Method with different name
        Method methodDifferentName = new Method(Object.class.getName(), "anotherMethodName", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethodDifferentName = new DelegatingMethod(methodDifferentName);
        assertNotSame(method, delegatingMethodDifferentName.getJavaMethod());
    }

    @Test
    public void testgetName() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();

        // Basic functional test
        assertEquals("methodName", name);

        // Edge case: Method with empty name
        Method methodEmptyName = new Method(Object.class.getName(), "", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethodEmptyName = new DelegatingMethod(methodEmptyName);
        assertEquals("", delegatingMethodEmptyName.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{String.class, Integer.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        // Basic functional test
        assertArrayEquals(new Class[]{String.class, Integer.class}, parameterTypes);

        // Edge case: Method with no parameters
        Method methodNoParameters = new Method(Object.class.getName(), "", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethodNoParameters = new DelegatingMethod(methodNoParameters);
        assertArrayEquals(new Class[0], delegatingMethodNoParameters.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{String.class, Integer.class}, new Class[]{String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();

        // Basic functional test
        assertEquals(String.class, returnType);

        // Edge case: Method with void return type
        Method methodVoidReturnType = new Method(Object.class.getName(), "methodName", new Class[]{String.class, Integer.class}, new Class[0]);
        DelegatingMethod delegatingMethodVoidReturnType = new DelegatingMethod(methodVoidReturnType);
        assertNull(delegatingMethodVoidReturnType.getReturnType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsVarArgsWithInvalidMethod() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{String.class, Integer.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        delegatingMethod.isVarArgs(); // should throw IllegalArgumentException
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{String.class, Integer.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();

        // Basic functional test
        assertFalse(isAbstract);

        // Edge case: Abstract method
        Method methodAbstract = new Method("AbstractClass".getClass().getName(), "methodName", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethodAbstract = new DelegatingMethod(methodAbstract);
        assertTrue(delegatingMethodAbstract.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(Object.class.getName(), "methodName1", new Class[]{String.class, Integer.class}, new Class[0]);
        Method method2 = new Method(Object.class.getName(), "methodName2", new Class[]{String.class, Integer.class}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        boolean equals = delegatingMethod1.equals(delegatingMethod2);

        // Basic functional test
        assertFalse(equals);

        // Edge case: Equal DelegatingMethods
        DelegatingMethod delegatingMethodSame = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethodSame));

        // Edge case: Equal Methods
        Method methodSame = new Method(Object.class.getName(), "methodName1", new Class[]{String.class, Integer.class}, new Class[0]);
        DelegatingMethod delegatingMethodSameMethod = new DelegatingMethod(methodSame);
        assertTrue(delegatingMethod1.equals(delegatingMethodSameMethod));
    }

    @Test
    public void testHashCode() {
        Method method1 = new Method(Object.class.getName(), "methodName1", new Class[]{String.class, Integer.class}, new Class[0]);
        Method method2 = new Method(Object.class.getName(), "methodName2", new Class[]{String.class, Integer.class}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        // Basic functional test: Different objects should have different hash codes
        assertNotSame(hashCode1, hashCode2);

        // Edge case: Equal DelegatingMethods should have the same hash code
        DelegatingMethod delegatingMethodSame = new DelegatingMethod(method1);
        assertEquals(hashCode1, delegatingMethodSame.hashCode());
    }
}