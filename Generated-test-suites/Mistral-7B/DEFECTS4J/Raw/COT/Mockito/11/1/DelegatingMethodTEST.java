import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method testMethod = new MockitoMethod(new Class[] { int.class, String.class }, "testMethod", new Class[] { Integer.TYPE, String.class }, new Class[] { Exception1.class, Exception2.class }) {
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[] { Exception1.class, Exception2.class }, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method testMethod = new MockMethodWithReturnTypeAndParameters().getMockMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(testMethod, javaMethod);
    }

    @Test
    public void testGetName() {
        Method testMethod = new MockitoMethod(new Class[] {}, "testMethod", new Class[] {}, new Class[] {}) {
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method testMethod = new MockMethodWithReturnTypeAndParameters().getMockMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[] { String.class, int.class }, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method testMethod = new MockitoMethod(new Class[] {}, "testMethod", new Class[] {}, new Class[] {}) {
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertNotNull(returnType);
        Assert.assertEquals(void.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method testMethod = new MockitoMethod(new Class[] { String.class, int.class }, "testMethod", new Class[] { String[].class }, new Class[] {}) {
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method testMethod = new MockMethodWithReturnTypeAndParameters().getMockMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsWithNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[] {}));
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsWithDifferentMethods() {
        Method method1 = new Method("testMethod1", new Class[] { int.class, String.class });
        Method method2 = new Method("testMethod2", new Class[] { int.class, String.class });
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method testMethod = new MockitoMethod(new Class[] { int.class, String.class }, "testMethod", new Class[] { Integer.TYPE, String.class }, new Class[] { Exception1.class, Exception2.class }) {
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod);
        int hashCode1 = delegatingMethod1.hashCode();
        Method testMethod2 = new MockitoMethod(new Class[] { int.class, String.class }, "testMethod", new Class[] { Integer.TYPE, String.class }, new Class[] { Exception2.class, Exception1.class }) {
        };
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);
        int hashCode2 = delegatingMethod2.hashCode();
        Assert.assertNotEquals(hashCode1, hashCode2);
    }
}