import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedExceptions = new Class[]{IllegalArgumentException.class};
        Class<?>[] actualExceptions = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(expectedExceptions, actualExceptions);
    }

    @Test
    public void testGetJavaMethod() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method expectedMethod = new Method("testMethod", new Class[] {int.class});
        Method actualMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetName() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedParameterTypes = new Class[]{int.class};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expectedReturnType = int.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        Assert.assertEquals(expectedReturnType, actualReturnType);
    }

    @Test(expected = AssertionError.class)
    public void testIsAbstract() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        Method method1 = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        Method method2 = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExceptionTypesNullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        delegatingMethod.getExceptionTypes();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetJavaMethodNullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        delegatingMethod.getJavaMethod();
    }

    @Test(expected = NullPointerException.class)
    public void testGetNameNullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        delegatingMethod.getName();
    }

    @Test(expected = NullPointerException.class)
    public void testGetParameterTypesNullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        delegatingMethod.getParameterTypes();
    }

    @Test(expected = NullPointerException.class)
    public void testGetReturnTypeNullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        delegatingMethod.getReturnType();
    }

    @Test
    public void testIsVarArgsNullMethod() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expectedValue = true;
        boolean actualValue = delegatingMethod.isVarArgs();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsVarArgsEmptyParameterTypes() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod")).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expectedValue = false;
        boolean actualValue = delegatingMethod.isVarArgs();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsVarArgsMultipleParameterTypes() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int.class, int.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expectedValue = false;
        boolean actualValue = delegatingMethod.isVarArgs();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsVarArgsSingleParameterArrayType() throws NoSuchMethodException {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("testMethod", int[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expectedValue = true;
        boolean actualValue = delegatingMethod.isVarArgs();
        Assert.assertEquals(expectedValue, actualValue);
    }
}