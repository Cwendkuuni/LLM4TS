import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method("testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method("testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertSame(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method("testMethod", new Class[]{int.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Class<?>[] expectedParameterTypes = new Class[]{int.class, String.class};
        Assert.assertArrayEquals(expectedParameterTypes, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method("testMethod", new Class[]{int.class, String.class}, new Class[]{boolean.class});
        DelegagingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertEquals(boolean.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method("testMethod", new Class[]{int.class, String.class}, new Class[]{int[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("testMethod", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[]{int.class, String.class}, new Class[0]));
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsSameInstance() {
        Method method = new Method("testMethod", new Class[]{int.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = delegatingMethod1;
        boolean equals = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertTrue(equals);
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = new Method("testMethod1", new Class[]{int.class, String.class}, new Class[0]);
        Method method2 = new Method("testMethod2", new Class[]{int.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        boolean equals = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        Method method = new Method("testMethod", new Class[]{int.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }
}