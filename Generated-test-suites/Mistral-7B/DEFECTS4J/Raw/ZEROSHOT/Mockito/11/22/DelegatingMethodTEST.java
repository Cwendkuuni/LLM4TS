import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockMethod("testMethod", new Class[]{String.class, int.class}, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockMethod("testMethod", new Class[]{String.class, int.class}, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new MockMethod("testMethod", new Class[]{String.class, int.class}, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockMethod("testMethod", new Class[]{String.class, int.class}, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[]{String.class, int.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new MockMethod("testMethod", new Class[]{String.class, int.class}, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertEquals(int.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MockMethod("testMethod", new Class[]{String.class, int[].class}, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(isVarArgs);

        method = new MockMethod("testMethod", new Class[]{String.class, int.class}, int.class);
        delegatingMethod = new DelegatingMethod(method);

        isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new MockMethod("testMethod", new Class[0], int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);

        method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                return null;
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }

            @Override
            public Class<?> getReturnType() {
                return null;
            }

            @Override
            public boolean isAbstract() {
                return true;
            }
        };
        delegatingMethod = new DelegatingMethod(method);

        isAbstract = delegatingMethod.isAbstract();
        Assert.assertTrue(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = new MockMethod("testMethod1", new Class[]{String.class, int.class}, int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new MockMethod("testMethod2", new Class[]{String.class, int.class}, int.class);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Method method3 = new MockMethod("testMethod1", new Class[]{String.class, int[].class}, int.class);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        Assert.assertFalse(delegatingMethod1.equals(delegatingMethod2));
        Assert.assertFalse(delegatingMethod1.equals(delegatingMethod3));
        Assert.assertFalse(delegatingMethod2.equals(delegatingMethod3));

        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod1));
        Assert.assertTrue(delegatingMethod2.equals(delegatingMethod2));
        Assert.assertTrue(delegatingMethod3.equals(delegatingMethod3));

        Assert.assertTrue(delegatingMethod1.equals(method1));
        Assert.assertTrue(delegatingMethod2.equals(method2));
        Assert.assertTrue(delegatingMethod3.equals(method3));
    }

    @Test
    public void testHashCode() {
        Method method = new MockMethod("testMethod", new Class[]{String.class, int.class}, int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }
}