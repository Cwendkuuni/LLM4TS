import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getExceptionTypes", (Class[]) null)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Class<?>[] expectedExceptionTypes = testMethod.getExceptionTypes();
        Assert.assertArrayEquals(expectedExceptionTypes, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getJavaMethod", (Class[]) null)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Method actualJavaMethod = delegatingMethod.getJavaMethod();
        Assert.assertSame(testMethod, actualJavaMethod);
    }

    @Test
    public void testGetName() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getName", (Class[]) null)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        String actualName = delegatingMethod.getName();
        String expectedName = testMethod.getName();
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getParameterTypes", (Class[]) null)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        Class<?>[] expectedParameterTypes = testMethod.getParameterTypes();
        Assert.assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getReturnType", (Class[]) null)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        Class<?> expectedReturnType = testMethod.getReturnType();
        Assert.assertSame(expectedReturnType, actualReturnType);
    }

    @Test(expected = AssertionError.class)
    public void testIsAbstractWithAbstractMethod() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("isAbstract", (Class[]) null)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertTrue(testMethod.isAbstract());
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testIsAbstractWithNonAbstractMethod() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getName", String[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method testMethod1 = new MockitoMethod(DelegatingMethod.class.getMethod("getName", String[].class)).getJavaMethod();
        Method testMethod2 = new MockitoMethod(DelegatingMethod.class.getMethod("getJavaMethod", (Class[]) null)).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        DelegatingMethod delegatingMethod3 = new DelegatingMethod(testMethod1);
        assertTrue(delegatingMethod1.equals(delegatingMethod3));

        assertTrue(testMethod1.equals(testMethod1));
        assertTrue(testMethod1.equals(delegatingMethod1.getJavaMethod()));
    }

    @Test
    public void testHashCode() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getName", String[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        int actualHashCode = delegatingMethod.hashCode();
        int expectedHashCode = System.identityHashCode(testMethod);
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testIsVarArgsWithNoVarArgsMethod() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getName", String[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsVarArgsWithVarArgsMethod() {
        Method testMethod = new MockitoMethod(DelegatingMethod.class.getMethod("getJavaMethod", Class[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertTrue(delegatingMethod.isVarArgs());
    }
}