import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;

public class DelegatingMethodTest {

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expectedReturnType = Integer.TYPE;
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        Assert.assertEquals(expectedReturnType, actualReturnType);
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedExceptionTypes = {IllegalArgumentException.class};
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetJavaMethodNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        Assert.assertNull(delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetJavaMethodEmptyName() {
        Method emptyNameMethod = new Method(TestClass.class, "", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(emptyNameMethod);
        String expectedName = "";
        String actualName = delegatingMethod.getName();
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypesDifferentTypes() {
        Method method = new Method(TestClass.class, "testMethodWithDifferentTypes", new Class[]{Integer.TYPE, String.class, Double.TYPE});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedParameterTypes = {Integer.TYPE, String.class, Double.TYPE};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetParameterTypesArrayOfTypes() {
        Method method = new Method(TestClass.class, "testMethodWithArrayOfTypes", new Class[]{Integer[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expectedParameterTypes = Integer[].class;
        Class<?> actualParameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test(expected = AssertionError.class)
    public void testIsAbstractAbstractMethod() {
        Method method = new Method(AbstractTestClass.class, "testAbstractMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testIsAbstractNonAbstractMethod() {
        Method method = new Method(NonAbstractTestClass.class, "testNonAbstractMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEqualsSameDelegateMethod() {
        Method method1 = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        Method method2 = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsDifferentDelegateMethod() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsSameMethod() {
        Method method1 = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        Method method2 = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsDifferentMethod() {
        Method method1 = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        Method method2 = new Method(TestClass.class, "testMethodWithDifferentName", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCodeSameObject() {
        Method method1 = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        Method method2 = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    @Test
    public void testHashCodeDifferentObject() {
        Method method1 = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        Method method2 = new Method(TestClass.class, "testMethodWithDifferentName", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertNotSame(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }
}