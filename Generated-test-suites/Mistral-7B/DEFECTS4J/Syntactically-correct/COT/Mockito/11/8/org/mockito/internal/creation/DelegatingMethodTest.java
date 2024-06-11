import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedExceptionTypes = new Class[] {NullPointerException.class};
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method expectedJavaMethod = method;
        Method actualJavaMethod = delegatingMethod.getJavaMethod();
        assertEquals(expectedJavaMethod, actualJavaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expectedName = "methodName";
        String actualName = delegatingMethod.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedParameterTypes = new Class[] {String.class, int.class};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expectedReturnType = Void.TYPE;
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        Assert.assertSame(expectedReturnType, actualReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[] {String.class, int.class, String[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expectedIsVarArgs = true;
        boolean actualIsVarArgs = delegatingMethod.isVarArgs();
        assertEquals(expectedIsVarArgs, actualIsVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expectedIsAbstract = true;
        boolean actualIsAbstract = delegatingMethod.isAbstract();
        assertEquals(expectedIsAbstract, actualIsAbstract);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNullObject() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsSameObject() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }

    @Test
    public void testHashCodeEquals() {
        Method method1 = new Method(Object.class.getName(), "methodName1", new Class[0]);
        Method method2 = new Method(Object.class.getName(), "methodName2", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int expectedHashcode = 1;
        int actualHashcode1 = delegatingMethod1.hashCode();
        int actualHashcode2 = delegatingMethod2.hashCode();
        assertEquals(expectedHashcode, actualHashcode1);
        assertEquals(expectedHashcode, actualHashcode2);
    }

    @Test
    public void testHashCodeNotEqual() {
        Method method1 = new Method(Object.class.getName(), "methodName1", new Class[0]);
        Method method2 = new Method(Object.class.getName(), "methodName2", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int expectedHashcode = 1;
        int actualHashcode1 = delegatingMethod1.hashCode();
        int actualHashcode2 = delegatingMethod2.hashCode();
        assertNotEquals(expectedHashcode, actualHashcode2);
    }
}