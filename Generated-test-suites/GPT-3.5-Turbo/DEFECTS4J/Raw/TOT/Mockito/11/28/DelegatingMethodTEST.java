import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedExceptionTypes = method.getExceptionTypes();
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method expectedJavaMethod = method;
        Method actualJavaMethod = delegatingMethod.getJavaMethod();

        assertEquals(expectedJavaMethod, actualJavaMethod);
    }

    @Test
    public void testGetName() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expectedName = method.getName();
        String actualName = delegatingMethod.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedParameterTypes = method.getParameterTypes();
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> expectedReturnType = method.getReturnType();
        Class<?> actualReturnType = delegatingMethod.getReturnType();

        assertEquals(expectedReturnType, actualReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expectedIsVarArgs = method.isVarArgs();
        boolean actualIsVarArgs = delegatingMethod.isVarArgs();

        assertEquals(expectedIsVarArgs, actualIsVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expectedIsAbstract = (method.getModifiers() & Modifier.ABSTRACT) != 0;
        boolean actualIsAbstract = delegatingMethod.isAbstract();

        assertEquals(expectedIsAbstract, actualIsAbstract);
    }

    @Test
    public void testEquals() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.equals(method));
    }

    @Test
    public void testHashCode() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int expectedHashCode = 1;
        int actualHashCode = delegatingMethod.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    private Method getTestMethod() {
        try {
            return DummyClass.class.getDeclaredMethod("testMethod");
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Failed to get test method");
        }
    }

    private static class DummyClass {
        public void testMethod() {

        }
    }
}