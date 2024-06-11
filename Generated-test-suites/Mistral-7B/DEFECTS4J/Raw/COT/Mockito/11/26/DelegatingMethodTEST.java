import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertEquals(new Class[0], exceptionTypes);

        method = new Method("testMethod", new Class[] {String.class, int.class});
        method.setExceptionTypes(new Class[] {NullPointerException.class});
        delegatingMethod = new DelegatingMethod(method);
        exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[] {NullPointerException.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method("testMethod", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[] {String.class, int.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(() -> "test", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method("testMethod", new Class[] {String.class, int.class, String[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(isVarArgs);

        method = new Method("testMethod", new Class[] {String.class, int.class});
        delegatingMethod = new DelegatingMethod(method);
        isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);

        method = new Method("testMethod", new Class[0]) {
            @Override
            public boolean isAbstract() {
                return true;
            }
        };
        delegatingMethod = new DelegatingMethod(method);
        isAbstract = delegatingMethod.isAbstract();
        Assert.assertTrue(isAbstract);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(() -> {}, new Class[0]));
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsSameMethod() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        boolean equals = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertTrue(equals);
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = new Method(() -> {}, new Class[0]);
        Method method2 = new Method(() -> {}, new Class[1]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        boolean equals = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        Assert.assertEquals(hashCode, hashCode); // hashCode should be equal to itself

        Method method2 = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int hashCode2 = delegatingMethod.hashCode();
        int hashCode3 = delegatingMethod2.hashCode();
        Assert.assertNotSame(hashCode, hashCode2); // hashCode should be different for different objects
        Assert.assertNotSame(hashCode, hashCode3); // hashCode should be different for different objects
    }
}