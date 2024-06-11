import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        // Basic test
        Assert.assertArrayEquals(new Class[]{}, exceptionTypes); // No exception types

        // Edge case: Method with exception types
        Method methodWithExceptionTypes = new Method(Object.class.getName(), "methodWithException", new Class[] {});
        methodWithExceptionTypes.setExceptionTypes(new Class[] {IllegalArgumentException.class, NullPointerException.class});
        DelegatingMethod delegatingMethodWithException = new DelegatingMethod(methodWithExceptionTypes);
        Class<?>[] exceptionTypesWithException = delegatingMethodWithException.getExceptionTypes();

        Assert.assertArrayEquals(new Class[] {IllegalArgumentException.class, NullPointerException.class}, exceptionTypesWithException);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();

        // Basic test
        Assert.assertSame(method, javaMethod);

        // Edge case: Method with same name but different signature
        Method methodWithSameNameButDifferentSignature = new Method(Object.class.getName(), "methodName", new Class[] {int.class, String.class});
        DelegatingMethod delegatingMethodWithSameNameButDifferentSignature = new DelegatingMethod(methodWithSameNameButDifferentSignature);
        Assert.assertNotSame(method, delegatingMethodWithSameNameButDifferentSignature.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();

        // Basic test
        Assert.assertEquals("methodName", name);

        // Edge case: Method with empty name
        Method methodWithEmptyName = new Method(Object.class.getName(), "", new Class[] {});
        DelegatingMethod delegatingMethodWithEmptyName = new DelegatingMethod(methodWithEmptyName);
        Assert.assertEquals("", delegatingMethodWithEmptyName.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        // Basic test
        Assert.assertArrayEquals(new Class[] {String.class, int.class}, parameterTypes);

        // Edge case: Method with no parameters
        Method methodWithNoParameters = new Method(Object.class.getName(), "", new Class[] {});
        DelegatingMethod delegatingMethodWithNoParameters = new DelegatingMethod(methodWithNoParameters);
        Class<?>[] parameterTypesWithNoParameters = delegatingMethodWithNoParameters.getParameterTypes();

        Assert.assertArrayEquals(new Class[] {}, parameterTypesWithNoParameters);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();

        // Basic test
        Assert.assertEquals(Void.TYPE, returnType); // Void return type for this class

        // Edge case: Method with non-void return type
        Method methodWithNonVoidReturnType = new Method(Object.class.getName(), "methodWithReturnValue", new Class[] {});
        methodWithNonVoidReturnType.setReturnType(String.class);
        DelegatingMethod delegatingMethodWithNonVoidReturnType = new DelegatingMethod(methodWithNonVoidReturnType);
        Class<?> returnTypeWithNonVoidReturnType = delegatingMethodWithNonVoidReturnType.getReturnType();

        Assert.assertEquals(String.class, returnTypeWithNonVoidReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();

        // Basic test
        Assert.assertFalse(isVarArgs);

        // Edge case: Method with var-args
        Method methodWithVarArgs = new Method(Object.class.getName(), "methodWithVarArgs", new Class[] {String[].class});
        DelegatingMethod delegatingMethodWithVarArgs = new DelegatingMethod(methodWithVarArgs);
        boolean isVarArgsWithVarArgs = delegatingMethodWithVarArgs.isVarArgs();

        Assert.assertTrue(isVarArgsWithVarArgs);
    }

    @Test(expected = AssertionError.class)
    public void testEquals() {
        Method method1 = new Method(Object.class.getName(), "methodName1", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new Method(Object.class.getName(), "methodName2", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        delegatingMethod1.equals(delegatingMethod2);
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();

        // Basic test
        Assert.assertEquals(hashCode, hashCode); // Should be equal to itself

        // Edge case: Different methods with same signature should have same hashcode
        Method methodWithSameSignature = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethodWithSameSignature = new DelegatingMethod(methodWithSameSignature);

        Assert.assertEquals(hashCode, delegatingMethodWithSameSignature.hashCode());
    }
}