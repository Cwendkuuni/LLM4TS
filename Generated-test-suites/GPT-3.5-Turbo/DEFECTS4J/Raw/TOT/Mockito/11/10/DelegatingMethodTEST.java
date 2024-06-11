import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        Assert.assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();

        Assert.assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();

        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        Assert.assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();

        Assert.assertEquals(Void.TYPE, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();

        Assert.assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();

        Assert.assertFalse(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);

        Assert.assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();

        Assert.assertEquals(1, hashCode);
    }

    private Method getTestMethod() {
        try {
            return this.getClass().getMethod("testMethod");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Test method not found", e);
        }
    }

    public void testMethod() {
        // Test method used for testing purposes
    }
}