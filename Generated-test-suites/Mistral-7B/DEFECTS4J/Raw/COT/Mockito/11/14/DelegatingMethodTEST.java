import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedExceptionTypes = new Class[0];
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        Assert.assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);

        Method expectedJavaMethod = javaMethod;
        Method actualJavaMethod = delegatingMethod.getJavaMethod();

        Assert.assertSame(expectedJavaMethod, actualJavaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();

        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = new Class[]{String.class};
        Method method = new Method("testMethod", parameterTypes);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedParameterTypes = parameterTypes;
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();

        Assert.assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = String.class;
        Method method = new Method("testMethod", new Class[0], returnType);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> expectedReturnType = returnType;
        Class<?> actualReturnType = delegatingMethod.getReturnType();

        Assert.assertSame(expectedReturnType, actualReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Class<?>[] parameterTypes = new Class[]{String[].class};
        Method method = new Method("testMethod", parameterTypes);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expectedIsVarArgs = true;
        boolean actualIsVarArgs = delegatingMethod.isVarArgs();

        Assert.assertEquals(expectedIsVarArgs, actualIsVarArgs);
    }

    @Test(expected = AssertionError.class)
    public void testIsAbstract() {
        Method method = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expectedIsAbstract = false;
        boolean actualIsAbstract = delegatingMethod.isAbstract();

        // The test should fail with an AssertionError as the method is not abstract
        Assert.assertNotSame(expectedIsAbstract, actualIsAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(() -> {}, new Class[0]);
        Method method2 = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean expectedEquals = false;
        boolean actualEquals = delegatingMethod1.equals(delegatingMethod2);

        Assert.assertFalse(expectedEquals, actualEquals);

        delegatingMethod1 = new DelegatingMethod(method1);
        delegatingMethod2 = new DelegatingMethod(method1);

        expectedEquals = true;
        actualEquals = delegatingMethod1.equals(delegatingMethod2);

        Assert.assertTrue(expectedEquals, actualEquals);
    }

    @Test
    public void testHashCode() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int expectedHashCode = 1; // As per DelegatingMethod implementation
        int actualHashCode1 = delegatingMethod1.hashCode();
        int actualHashCode2 = delegatingMethod2.hashCode();

        Assert.assertEquals(expectedHashCode, actualHashCode1);
        Assert.assertEquals(expectedHashCode, actualHashCode2);
    }
}