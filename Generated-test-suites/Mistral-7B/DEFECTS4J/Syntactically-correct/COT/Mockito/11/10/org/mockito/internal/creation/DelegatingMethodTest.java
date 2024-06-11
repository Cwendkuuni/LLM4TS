import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes_NoExceptionThrown() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[0]));
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod_ReturnsSameMethodInstance() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method("testMethod", new Class[0]));
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(new Method("testMethod", new Class[0]));
        Method javaMethod1 = delegatingMethod1.getJavaMethod();
        Method javaMethod2 = delegatingMethod2.getJavaMethod();
        Assert.assertSame(javaMethod1, javaMethod2);
    }

    @Test
    public void testGetName_ReturnsMethodName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[0]));
        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes_ReturnsCorrectParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod(int, String)", new Class[]{int.class, String.class}));
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[]{int.class, String.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType_ReturnsCorrectReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[0], int.class));
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertEquals(int.class, returnType);
    }

    @Test(expected = AssertionError.class)
    public void testIsAbstract_ThrowsAssertionErrorForAbstractMethods() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("abstractMethod", new Class[0]));
        delegatingMethod.isAbstract();
    }

    @Test
    public void testIsVarArgs_ReturnsCorrectResultForVarArgsMethods() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod(int, String...)", new Class[]{int.class, String[].class}));
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(isVarArgs);
    }

    @Test
    public void testEquals_ReturnsTrueForEqualObjects() {
        Method method1 = new Method("testMethod", new Class[0]);
        Method method2 = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        boolean result = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_ReturnsFalseForUnequalObjects() {
        Method method = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object unequalObject = new Object();
        boolean result = delegatingMethod.equals(unequalObject);
        Assert.assertFalse(result);
    }

    @Test
    public void testHashCode_ReturnsSameHashCodeForEqualObjects() {
        Method method1 = new Method("testMethod", new Class[0]);
        Method method2 = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_ReturnsDifferentHashCodeForUnequalObjects() {
        Method method1 = new Method("testMethod", new Class[0]);
        Method method2 = new Method("testOtherMethod", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        Assert.assertNotSame(hashCode1, hashCode2);
    }
}