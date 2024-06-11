import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testConstructorNonNullMethod() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertNotNull(delegatingMethod);
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertNotNull(exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertNotNull(javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        Assert.assertNotNull(name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertNotNull(parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertNotNull(returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean equals = delegatingMethod.equals(method);
        Assert.assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        Method method = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        Assert.assertEquals(1, hashCode);
    }
}