import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedExceptionTypes = new Class[0];
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals("Exception types should be equal", expectedExceptionTypes, actualExceptionTypes);

        method = new Method(String.class.getName(), "testStringMethod", new Class[]{String.class});
        delegatingMethod = new DelegatingMethod(method);
        expectedExceptionTypes = new Class[]{IllegalArgumentException.class};
        actualExceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals("Exception types should be equal", expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method expectedJavaMethod = method;
        Method actualJavaMethod = delegatingMethod.getJavaMethod();
        assertEquals("Java methods should be equal", expectedJavaMethod, actualJavaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();
        assertEquals("Names should be equal", expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class[] expectedParameterTypes = new Class[0];
        Class[] actualParameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals("Parameter types should be equal", expectedParameterTypes, actualParameterTypes);

        method = new Method(String.class.getName(), "testStringMethod", new Class[]{String.class});
        delegatingMethod = new DelegatingMethod(method);
        expectedParameterTypes = new Class[]{String.class};
        actualParameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals("Parameter types should be equal", expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expectedReturnType = Object.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        assertEquals("Return types should be equal", expectedReturnType, actualReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertFalse("Method without varargs should not be varargs", delegatingMethod1.isVarArgs());

        Method method2 = new Method(Object.class.getName(), "testMethod2", new Class[]{Object[].class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue("Method with varargs should be varargs", delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertFalse("Non-abstract method should not be abstract", delegatingMethod1.isAbstract());

        Method method2 = new Method("java.lang.Object", "equals", new Class[]{java.lang.Object.class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue("Abstract method should be abstract", delegatingMethod2.isAbstract());
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNullInput() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsSameMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertTrue("DelegatingMethods with the same Method should be equal", delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        Method method2 = new Method(Object.class.getName(), "testMethod2", new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse("DelegatingMethods with different Methods should not be equal", delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsSameObjects() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = delegatingMethod1;
        assertTrue("DelegatingMethods representing the same object should be equal", delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        int expectedHashCode = 1;
        int actualHashCode1 = delegatingMethod1.hashCode();
        int actualHashCode2 = delegatingMethod2.hashCode();
        assertEquals("Hashcodes should be equal for equal objects", expectedHashCode, actualHashCode1);
        assertEquals("Hashcodes should be equal for equal objects", expectedHashCode, actualHashCode2);
        Method differentMethod = new Method(Object.class.getName(), "testOtherMethod", new Class[0]);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(differentMethod);
        assertNotEquals("Hashcodes should be different for unequal objects", expectedHashCode, delegatingMethod3.hashCode());
    }
}