import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[0], exceptionTypes);

        Method methodWithExceptions = new Method(() -> {}, new Class[]{IllegalArgumentException.class, RuntimeException.class});
        DelegatingMethod delegatingMethodWithExceptions = new DelegatingMethod(methodWithExceptions);
        Class<?>[] exceptionTypesWithExceptions = delegatingMethodWithExceptions.getExceptionTypes();
        assertArrayEquals(new Class[]{IllegalArgumentException.class, RuntimeException.class}, exceptionTypesWithExceptions);
    }

    @Test
    public void testGetJavaMethod() {
        Method inputMethod = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(inputMethod);
        Method expectedMethod = inputMethod;
        Method actualMethod = delegatingMethod.getJavaMethod();
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetName() {
        Method inputMethod = new Method(() -> {}, new Class[0]);
        inputMethod.setName("testMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(inputMethod);
        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method inputMethod = new Method(() -> {}, new Class[]{int.class, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(inputMethod);
        Class<?>[] expectedParameterTypes = {int.class, String.class};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method inputMethod = new Method(() -> "", new Class[0]);
        inputMethod.setReturnType(int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(inputMethod);
        Class<?> expectedReturnType = int.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        assertEquals(expectedReturnType, actualReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Method inputMethod = new Method(() -> {}, new Class[]{int[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(inputMethod);
        boolean expectedResult = true;
        boolean actualResult = delegatingMethod.isVarArgs();
        assertEquals(expectedResult, actualResult);

        Method inputMethodWithoutVarArgs = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethodWithoutVarArgs = new DelegatingMethod(inputMethodWithoutVarArgs);
        boolean expectedResultWithoutVarArgs = false;
        boolean actualResultWithoutVarArgs = delegatingMethodWithoutVarArgs.isVarArgs();
        assertEquals(expectedResultWithoutVarArgs, actualResultWithoutVarArgs);
    }

    @Test(expected = AssertionError.class)
    public void testEquals_Null() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method(() -> {}, new Class[0]));
        DelegatingMethod delegatingMethod2 = null;
        delegatingMethod1.equals(delegatingMethod2);
    }

    @Test(expected = AssertionError.class)
    public void testEquals_DifferentMethods() {
        Method method1 = new Method(() -> {}, new Class[0]);
        Method method2 = new Method(() -> {}, new Class[]{int.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        delegatingMethod1.equals(delegatingMethod2);
    }

    @Test
    public void testEquals_SameMethods() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        boolean expectedResult = true;
        boolean actualResult = delegatingMethod1.equals(delegatingMethod2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testHashCode() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        int expectedHashCode = 1;
        int actualHashCode1 = delegatingMethod1.hashCode();
        int actualHashCode2 = delegatingMethod2.hashCode();
        assertEquals(expectedHashCode, actualHashCode1);
        assertEquals(expectedHashCode, actualHashCode2);
    }
}