import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        Assert.assertArrayEquals(method.getExceptionTypes(), exceptionTypes);
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

        Assert.assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        Assert.assertArrayEquals(method.getParameterTypes(), parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();

        Assert.assertEquals(method.getReturnType(), returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();

        Assert.assertEquals(method.isVarArgs(), isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();

        Assert.assertEquals(Modifier.isAbstract(method.getModifiers()), isAbstract);
    }

    @Test
    public void testEquals() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean equals = delegatingMethod.equals(method);

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
            return DelegatingMethodTest.class.getDeclaredMethod("testMethod");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void testMethod() {
        // Test method
    }
}