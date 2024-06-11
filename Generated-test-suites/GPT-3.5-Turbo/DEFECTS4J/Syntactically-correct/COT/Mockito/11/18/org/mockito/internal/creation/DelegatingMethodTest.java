
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.creation.DelegatingMethod;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method testMethod = getTestMethod("getExceptionTypes");
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?>[] expected = testMethod.getExceptionTypes();
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        assertArrayEquals("Incorrect exception types", expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Method testMethod = getTestMethod("getJavaMethod");
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);
        Method expected = testMethod;
        Method actual = delegatingMethod.getJavaMethod();
        assertEquals("Incorrect Java method", expected, actual);
    }

    @Test
    public void testGetName() {
        Method testMethod = getTestMethod("getName");
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);
        String expected = testMethod.getName();
        String actual = delegatingMethod.getName();
        assertEquals("Incorrect method name", expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        Method testMethod = getTestMethod("getParameterTypes");
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?>[] expected = testMethod.getParameterTypes();
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        assertArrayEquals("Incorrect parameter types", expected, actual);
    }

    @Test
    public void testGetReturnType() {
        Method testMethod = getTestMethod("getReturnType");
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?> expected = testMethod.getReturnType();
        Class<?> actual = delegatingMethod.getReturnType();
        assertEquals("Incorrect return type", expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        Method testMethod = getTestMethod("isVarArgs");
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);
        boolean expected = testMethod.isVarArgs();
        boolean actual = delegatingMethod.isVarArgs();
        assertEquals("Incorrect varargs", expected, actual);
    }

    @Test
    public void testIsAbstract() {
        Method testMethod = getTestMethod("isAbstract");
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);
        boolean expected = (testMethod.getModifiers() & Modifier.ABSTRACT) != 0;
        boolean actual = delegatingMethod.isAbstract();
        assertEquals("Incorrect abstractness", expected, actual);
    }

    @Test
    public void testEqualsEqualsMethod() {
        Method testMethod = getTestMethod("equals");
        MockitoMethod delegatingMethod1 = new DelegatingMethod(testMethod);
        MockitoMethod delegatingMethod2 = new DelegatingMethod(testMethod);
        assertTrue("equals method should return true for same object", delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsMethod() {
        Method testMethod = getTestMethod("equals");
        MockitoMethod delegatingMethod1 = new DelegatingMethod(testMethod);
        Method method = getTestMethod("equals");
        assertTrue("equals method should return true for Method object", delegatingMethod1.equals(method));
    }

    @Test
    public void testHashCode() {
        Method testMethod = getTestMethod("hashCode");
        MockitoMethod delegatingMethod = new DelegatingMethod(testMethod);
        int expected = 1;
        int actual = delegatingMethod.hashCode();
        assertEquals("Incorrect hash code", expected, actual);
    }

    private Method getTestMethod(String methodName) {
        Method[] methods = DelegatingMethod.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
