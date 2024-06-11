import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method testMethod = new MockitoMethod(String.class.getMethod("concat", String.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Class<?>[] expectedExceptionTypes = testMethod.getExceptionTypes();

        Assert.assertArrayEquals(expectedExceptionTypes, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method testMethod = new MockitoMethod(String.class.getMethod("concat", String.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Method javaMethod = delegatingMethod.getJavaMethod();
        Method expectedJavaMethod = testMethod;

        Assert.assertSame(expectedJavaMethod, javaMethod);
    }

    @Test
    public void testGetName() {
        Method testMethod = new MockitoMethod(String.class.getMethod("concat", String.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        String name = delegatingMethod.getName();
        String expectedName = testMethod.getName();

        Assert.assertEquals(expectedName, name);
    }

    @Test
    public void testGetParameterTypes() {
        Method testMethod = new MockitoMethod(String.class.getMethod("concat", String.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Class<?>[] expectedParameterTypes = testMethod.getParameterTypes();

        Assert.assertArrayEquals(expectedParameterTypes, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method testMethod = new MockitoMethod(String.class.getMethod("concat", String.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?> returnType = delegatingMethod.getReturnType();
        Class<?> expectedReturnType = testMethod.getReturnType();

        Assert.assertSame(expectedReturnType, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method testMethodVarArgs = new MockitoMethod(String.class.getMethod("format", String.class, String[].class)).getJavaMethod();
        DelegatingMethod delegatingMethodVarArgs = new DelegatingMethod(testMethodVarArgs);

        boolean isVarArgs = delegatingMethodVarArgs.isVarArgs();
        boolean expectedIsVarArgs = testMethodVarArgs.isVarArgs();

        Assert.assertEquals(expectedIsVarArgs, isVarArgs);

        Method testMethodNoVarArgs = new MockitoMethod(String.class.getMethod("length")).getJavaMethod();
        DelegatingMethod delegatingMethodNoVarArgs = new DelegatingMethod(testMethodNoVarArgs);

        isVarArgs = delegatingMethodNoVarArgs.isVarArgs();
        boolean expectedIsVarArgsNoArgs = testMethodNoVarArgs.isVarArgs();

        Assert.assertFalse(isVarArgs);
        Assert.assertFalse(expectedIsVarArgsNoArgs);
    }

    @Test
    public void testIsAbstract() {
        Method testMethodAbstract = AbstractClassWithMethod.class.getMethod("abstractMethod");
        DelegatingMethod delegatingMethodAbstract = new DelegatingMethod(testMethodAbstract);

        boolean isAbstract = delegatingMethodAbstract.isAbstract();
        boolean expectedIsAbstract = testMethodAbstract.isAbstract();

        Assert.assertTrue(isAbstract);
        Assert.assertTrue(expectedIsAbstract);

        Method testMethodConcrete = new MockitoMethod(String.class.getMethod("length")).getJavaMethod();
        DelegatingMethod delegatingMethodConcrete = new DelegatingMethod(testMethodConcrete);

        isAbstract = delegatingMethodConcrete.isAbstract();
        boolean expectedIsAbstractConcrete = testMethodConcrete.isAbstract();

        Assert.assertFalse(isAbstract);
        Assert.assertFalse(expectedIsAbstractConcrete);
    }

    @Test
    public void testEquals() {
        Method testMethod1 = new MockitoMethod(String.class.getMethod("concat", String.class)).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod1);

        Method testMethod2 = new MockitoMethod(String.class.getMethod("concat", String.class)).getJavaMethod();
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);

        Method testMethod3 = new MockitoMethod(String.class.getMethod("length")).getJavaMethod();
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(testMethod3);

        Object testObject = new Object();

        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod1));
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
        Assert.assertTrue(delegatingMethod1.equals(testMethod1));

        Assert.assertFalse(delegatingMethod1.equals(testObject));
        Assert.assertFalse(delegatingMethod1.equals(delegatingMethod3));
    }

    @Test
    public void testHashCode() {
        Method testMethod = new MockitoMethod(String.class.getMethod("concat", String.class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        int hashCode = delegatingMethod.hashCode();
        int expectedHashCode = testMethod.hashCode();

        Assert.assertEquals(expectedHashCode, hashCode);

        Method testMethod2 = new MockitoMethod(String.class.getMethod("length")).getJavaMethod();
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);

        hashCode = delegatingMethod.hashCode();
        int expectedHashCode2 = testMethod2.hashCode();

        Assert.assertNotEquals(expectedHashCode, hashCode);
    }
}